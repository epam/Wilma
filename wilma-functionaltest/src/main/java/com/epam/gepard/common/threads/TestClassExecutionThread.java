package com.epam.gepard.common.threads;

/*==========================================================================
 Copyright 2004-2015 EPAM Systems

 This file is part of Gepard.

 Gepard is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Gepard is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Gepard.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import com.epam.gepard.logger.HtmlRunReporter;
import org.junit.runner.Computer;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.gepard.common.GepardConstants;
import com.epam.gepard.common.TestClassExecutionData;
import com.epam.gepard.generic.GenericListTestSuite;
import com.epam.gepard.logger.XmlRunReporter;

/**
 * This threads are used to execute the test cases in parallel.
 */
public class TestClassExecutionThread extends Thread {

    public static final InheritableThreadLocal<TestClassExecutionData> CLASS_DATA_IN_CONTEXT = new InheritableThreadLocal<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(TestClassExecutionThread.class);
    private static final Semaphore AVAILABLE = new Semaphore(1, false);

    /**
     * Key is the blocker, value is the lock of the blocker.
     */
    private static Map<String, BlockingInfo> testClassBlockingMap = new LinkedHashMap<>(); //blocking TClass map

    //TC executor
    private final JUnitCore core = new JUnitCore();
    private boolean enabled; // = false; //weather TC execution enabled for this thread or not
    private TestClassExecutionData classData; // = null; //points to the actual tc, under exec
    private String xmlResultPath;

    /**
     * Constructs a new instance of {@link TestClassExecutionThread}.
     * @param xmlResultPath the path to use for xml report generation
     */
    public TestClassExecutionThread(final String xmlResultPath) {
        this.xmlResultPath = xmlResultPath;
    }

    @Override
    public void run() {
        String me = this.getName();
        core.addListener(new XmlRunReporter(new File(xmlResultPath)));
        core.addListener(new XmlRunReporter(new File(xmlResultPath), true));
        //forever loop we have
        while (true) {
            tryToExecuteATest(me);
        }
    }

    private void tryToExecuteATest(final String me) {
        //first, detect whether there is anything to do
        boolean grabbed = tryToGrab();
        if (grabbed) {
            pickAClassToBeExecuted(me);
        } else {
            wait(me);
        }
    }

    private boolean tryToGrab() {
        boolean grabbed = false;
        if (isThereAnyThingTodo() && isEnabled()) {
            //there is something to do, so grab the semaphore
            grabbed = AVAILABLE.tryAcquire();
        }
        return grabbed;
    }

    private void pickAClassToBeExecuted(final String me) {
        //semaphore is grabbed
        //+ there is something to do, so pick a class to be executed
        LOGGER.debug("Thread: " + me + " grabbed the semaphore.");
        TestClassExecutionData o = pickClass();
        if (o != null) {
            executeCandidate(me, o);
        } else {
            releaseAndWait(me);
        }
    }

    private void executeCandidate(final String me, final TestClassExecutionData o) {
        //we grabbed the semaphore and has a candidate to be executed
        String info = lock(o); //lock the class
        LOGGER.debug("Thread: " + me + " released the semaphore, and " + info);
        AVAILABLE.release(); //release the semaphore
        execClass(o); //execute the class
        setLockExecuted(me, o); //done, so need to be set as done
    }

    private void releaseAndWait(final String me) {
        //hmm, nothing to pick, maybe the last one just picked or blocked.
        //release the semaphore, wait a bit and reiterate
        LOGGER.debug("Thread: " + me + " released the semaphore (no Class to execute).");
        AVAILABLE.release(); //release the semaphore
        //now wait for the next iteration
        wait(me);
    }

    private void wait(final String me) {
        try {
            sleep(GepardConstants.ONE_SECOND_LENGTH.getConstant()); //sleep for a sec, then restart the loop
        } catch (InterruptedException e) {
            //this was not expected, but if happens, then time to exit
            LOGGER.debug("Thread: " + me + " is exiting, as got InterruptedException!");
        }
    }

    private boolean isThereAnyThingTodo() {
        boolean result = false;
        Iterator<String> keyIterator = GenericListTestSuite.getTestClassIds().iterator();
        while (keyIterator.hasNext() && !result) {
            String s = keyIterator.next();
            TestClassExecutionData d = GenericListTestSuite.getTestClassExecutionData(s);
            result = d.getLock() >= 0;
        }
        return result;
    }

    private TestClassExecutionData pickClass() {
        TestClassExecutionData result = null;
        Iterator<String> iterator = GenericListTestSuite.getTestClassIds().iterator();
        while (iterator.hasNext() && result == null) {
            String s = iterator.next();
            TestClassExecutionData d = GenericListTestSuite.getTestClassExecutionData(s);
            result = findUnblockedClass(d);
        }
        return result; //no class to be proposed for exec
    }

    private TestClassExecutionData findUnblockedClass(final TestClassExecutionData d) {
        TestClassExecutionData result = null;
        boolean notBlocked = true;
        if (d.getBlockerString() != null) {
            //need to check blocker map, too
            notBlocked = checkBlocked(d);
        }
        //not blocked, so check the locked status
        if (notBlocked && d.getLock() == 0) {
            result = d; //if not locked, then this is the class we propose to be locked
        }
        return result;
    }

    private boolean checkBlocked(final TestClassExecutionData d) {
        boolean result = true;
        BlockingInfo blockerInfo = TestClassExecutionThread.testClassBlockingMap.get(d.getBlockerString());
        if (blockerInfo.isBlockerInUse()) {
            //continue the search if self-parallelism is not allowed
            if (!d.isSelfEnabledBlocker()) {
                result = false;
            }
            //hmm. if this class is not the same as blocked it, we should continue the search
            if (notTheSameAsBlocked(d, blockerInfo)) {
                result = false;
            }
            //self-parallelism is allowed, and the blocker is this class, so this class can be executed now
        }
        return result;
    }

    private boolean notTheSameAsBlocked(final TestClassExecutionData d, final BlockingInfo blockerInfo) {
        return d.getClassName().compareTo(blockerInfo.getActualClass()) != 0;
    }

    private String lock(final TestClassExecutionData o) {
        //first detect if second lock is necessary
        String blockerString = o.getBlockerString();
        boolean needsecondLock = blockerString != null;
        String additionalInfo = "";
        if (needsecondLock) {
            BlockingInfo blockingInfo = TestClassExecutionThread.testClassBlockingMap.get(blockerString);
            blockingInfo.setBlockerInUse(true); //now locks the blocker it in the map
            blockingInfo.setActualClass(o.getClassName());
            blockingInfo.setOverload(blockingInfo.getOverload() + 1);
            additionalInfo = ", with Blocker: " + blockerString + " Class:" + blockingInfo.getActualClass() + " Overload:"
                    + blockingInfo.getOverload();
        }
        o.lock();
        return "got Class to execute:" + o.getID() + additionalInfo;
    }

    private void setLockExecuted(final String me, final TestClassExecutionData o) {
        //first detect if second lock is necessary
        String blockerString = o.getBlockerString();
        boolean needsecondLock = blockerString != null;
        String additionalInfo = "";
        if (needsecondLock) {
            BlockingInfo blockingInfo = TestClassExecutionThread.testClassBlockingMap.get(blockerString);
            blockingInfo.setOverload(blockingInfo.getOverload() - 1);
            additionalInfo = ", and remove Blocker: " + blockerString + " and Class:" + blockingInfo.getActualClass() + ", now Overload:"
                    + blockingInfo.getOverload();
            if (blockingInfo.getOverload() == 0) {
                //this was this last blocker class so now release it
                blockingInfo.setBlockerInUse(false); //now unlocks the blocker it in the map
                blockingInfo.setActualClass(null);
            }
        }
        o.setLockExecuted();
        LOGGER.debug("Thread: " + me + " sets class as executed:" + o.getID() + additionalInfo);
    }

    private void execClass(final TestClassExecutionData o) {
        classData = o;
        CLASS_DATA_IN_CONTEXT.set(o);
        try {
            HtmlRunReporter reporter = o.getHtmlRunReporter();
            reporter.hiddenBeforeTestClassExecution();
            core.addListener(reporter);

            Result result = core.run(Computer.serial(), o.getTestClass());
            for (Failure failure : result.getFailures()) {
                LOGGER.debug(failure.toString());
            }
            classData.setCountOfRuns(result.getRunCount());
            core.removeListener(reporter);
            reporter.hiddenAfterTestClassExecution();
        } catch (Throwable e) {
            //this is gas
            LOGGER.debug("Thread: got EX during JUnitCore execution.", e);
        }
        CLASS_DATA_IN_CONTEXT.set(null);
        classData = null;
    }

    /**
     * Delegates the method call to the inner map.
     * @param key the key of the BlockingInfo
     * @return true if it's contained false otherwise
     */
    public static boolean containsClassBlockingInfo(final String key) {
        return TestClassExecutionThread.testClassBlockingMap.containsKey(key);
    }

    /**
     * Delegates the method call to the inner map.
     * @param key the key to store
     * @param blockingInfo the value to store
     */
    public static void putClassBlockingInfo(final String key, final BlockingInfo blockingInfo) {
        TestClassExecutionThread.testClassBlockingMap.put(key, blockingInfo);
    }

    /**
     * Set enabled property of this thread. If it is not enabled, that do idle actions only.
     *
     * @param enabled is the parameter
     */
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Getter method of the thread enabled property.
     *
     * @return with the enabled property.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Get the actual test class data which is under test by this thread.
     * If the thread does not work with any test, it is null.
     *
     * @return with the Test Class execution information of the running test case.
     */
    public TestClassExecutionData getActiveTest() {
        return classData;
    }

}
