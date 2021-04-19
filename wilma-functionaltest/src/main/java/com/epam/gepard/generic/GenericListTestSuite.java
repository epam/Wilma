package com.epam.gepard.generic;

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

import com.epam.gepard.AllTestRunner;
import com.epam.gepard.annotations.TestClass;
import com.epam.gepard.common.Environment;
import com.epam.gepard.common.TestClassExecutionData;
import com.epam.gepard.common.threads.BlockingInfo;
import com.epam.gepard.common.threads.TestClassExecutionThread;
import com.epam.gepard.datadriven.DataDrivenParameterArray;
import com.epam.gepard.datadriven.DataFeederLoader;
import com.epam.gepard.filter.ExpressionTestFilter;
import com.epam.gepard.generic.helper.TestClassData;
import com.epam.gepard.logger.HtmlRunReporter;
import com.epam.gepard.util.ExitCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class loads the testlist file, evaluates it ad builds up the test suite to be executed.
 */
public class GenericListTestSuite {

    private static final int TESTLIST_CLASS_NAME_FIELD = 0;
    private static final int TESTLIST_FEEDER_DESCRIPTOR_FIELD = 1;
    private static final int TESTLIST_BLOCKER_FIELD = 2;
    /**
     * Gepard level global map, to store anything you believe is important to be stored during the tests.
     * So you may use it.
     * Uniqueness of the key is your responsibility. Having a key naming convention is useful.
     */
    private static Map<String, Object> globalDataStorage = Collections.synchronizedMap(new LinkedHashMap<>());
    /**
     * Gepard level global map, to store all the test classes those are executed.
     * Do NOT touch it, otherwise you will do nasty things. It is used internally by Gepard.
     */
    private static Map<String, TestClassExecutionData> testClassMap = new LinkedHashMap<>(); //global TestClass exec info
    private int actualDataRow; //this is used during the load of the tests, do NOT use it during execution
    private String actualTestClassName; //this is used during the load of the tests, do NOT use it during execution

    private int usedTc; // = 0; //number of used Test Classes - will be used at report

    private Environment environment;

    /**
     * This class builds up the Junit Test suite (including loading data-driven tests).
     *
     * @param testListFile is the filename of the testlist file.
     * @param filter       is the filter of the test classes.
     * @param environment  holds the properties of the application
     * @throws IOException            in case testlist file cannot be accessed properly
     * @throws ClassNotFoundException in case the specified feeder class is not available.
     */
    public GenericListTestSuite(final String testListFile, final ExpressionTestFilter filter, final Environment environment) throws IOException,
            ClassNotFoundException {
        this.environment = environment;
        LineNumberReader listReader = new LineNumberReader(new InputStreamReader(new FileInputStream(testListFile)));
        String originalLine;
        while ((originalLine = listReader.readLine()) != null) {
            originalLine = originalLine.trim();
            if ("".equals(originalLine) || originalLine.startsWith("//") || originalLine.startsWith("#")) {
                continue;
            }
            String line = originalLine.replace(File.separatorChar, '.');
            // if: classname   -> 1 run is expected
            // if: classname,3 -> 3 run is expected
            // if: classname,,AAA -> AAA is used as a blocker id
            // if: classname,feederdescriptor,...-> loader class defines the number of execution and provides the tests
            String[] testDescriptor = line.split(",");
            Class<?> clazz = Class.forName(testDescriptor[TESTLIST_CLASS_NAME_FIELD]);
            //add as many classes to the stack as data driven approach requires
            if (filter.accept(clazz)) {
                int count = 1;
                DataFeederLoader dataFeeder = null;
                if ((testDescriptor.length > TESTLIST_FEEDER_DESCRIPTOR_FIELD) && (!testDescriptor[TESTLIST_FEEDER_DESCRIPTOR_FIELD].isEmpty())) {
                    //this is a data driven TC
                    dataFeeder = new DataFeederLoader(clazz.getName(), testDescriptor[TESTLIST_FEEDER_DESCRIPTOR_FIELD], environment);
                    count = dataFeeder.calculateRuns(clazz.getName(), count);
                    DataDrivenParameterArray parameterArray = dataFeeder.calculateParameterArray(clazz.getName(), null);
                    dataFeeder.reserveParameterArray(parameterArray);
                }

                //detect blocker class
                String blocker = null;
                if (testDescriptor.length > TESTLIST_BLOCKER_FIELD) {
                    //has blocker value
                    blocker = testDescriptor[TESTLIST_BLOCKER_FIELD];
                }

                //and add it to the suite
                TestClassData testClassData = new TestClassData(clazz, count, blocker);
                addTestClass(testClassData, dataFeeder, originalLine);
                usedTc++; //count the used test classes
            }
        }
        listReader.close();
    }

    public static Map<String, Object> getGlobalDataStorage() {
        return globalDataStorage;
    }

    public static void setGlobalDataStorage(final Map<String, Object> globalDataStorage) {
        GenericListTestSuite.globalDataStorage = globalDataStorage;
    }

    /**
     * Returns the {@link TestClassExecutionData} that belongs to the given id.
     *
     * @param testClassId the given id of the test class
     * @return the {@link TestClassExecutionData} that belongs to the given id.
     */
    public static TestClassExecutionData getTestClassExecutionData(final String testClassId) {
        return testClassMap.get(testClassId);
    }

    public static Set<String> getTestClassIds() {
        return testClassMap.keySet();
    }

    public static void setTestClassMap(final Map<String, TestClassExecutionData> testClassMap) {
        GenericListTestSuite.testClassMap = new LinkedHashMap<>(testClassMap);
    }

    /**
     * Add a Test Class to the suite.
     *
     * @param testClassData holds the data necessary for adding the test class
     * @param dataFeeder    is the Data Feeder class that should be used to load the data-driven values.
     * @param originalLine  is the original testlist row.
     */
    public void addTestClass(final TestClassData testClassData, final DataFeederLoader dataFeeder, final String originalLine) {
        int rowNo = 0;
        int counter = testClassData.getCount();
        Class<?> cls = testClassData.getClassOfTestClass();
        String blocker = testClassData.getBlocker();
        while (counter > 0) {
            registerMethodsInGlobalMap(cls, rowNo, dataFeeder);
            handleTestClassAnnotation(cls);

            //set test for parallel execution
            String id = cls.getName() + "/" + rowNo;
            TestClassExecutionData classData = GenericListTestSuite.getTestClassExecutionData(id); //get the class exec object
            //set blocker parameters
            String blockerString = null;
            boolean blockerSelfEnabled = false;
            if ((blocker != null) && (blocker.length() > 0)) {
                if (blocker.endsWith("*")) {
                    blockerString = blocker.substring(0, blocker.length() - 1);
                    blockerSelfEnabled = true;
                } else {
                    blockerString = blocker;
                }
                //take care about the blocker map, too
                if (!TestClassExecutionThread.containsClassBlockingInfo(blockerString)) {
                    //need a new blocker element
                    TestClassExecutionThread.putClassBlockingInfo(blockerString, new BlockingInfo());
                }
            }
            classData.setBlockerString(blockerString);
            classData.setSelfEnabledBlocker(blockerSelfEnabled);
            classData.setOriginalLine(originalLine);
            checkDataDrivenParameters(classData, dataFeeder);
            counter--;
            rowNo++;
        }
    }

    private void checkDataDrivenParameters(final TestClassExecutionData classData, final DataFeederLoader dataFeeder) {
        if (classData.getDrivenData() == null) { // this must not be data driven
            if (classData.getDrivenDataRowNo() > 0) {
                AllTestRunner.CONSOLE_LOG.info("\nERROR: Parameters are not loaded for a data driven test class."
                        + "\nPlease check and fix it!\nNow exiting...");
                AllTestRunner.exitFromGepard(ExitCode.EXIT_CODE_DATA_DRIVEN_TEST_CLASS_WITHOUT_DATA);
            }
            if (dataFeeder != null) {
                AllTestRunner.CONSOLE_LOG.info("\nERROR: DataFeederLoader is used on a non-data driven test class."
                        + "\nPlease check and fix it!\nNow exiting...");
                AllTestRunner.exitFromGepard(ExitCode.EXIT_CODE_NON_DATA_DRIVEN_TEST_CLASS_WITH_DATA);
            }
            return;
        }
        //we have parameters, now check its correctness
        String[] parameterNames = classData.getDrivenData().getParameterNames();
        if (parameterNames == null) { //we must have names for the parameters
            AllTestRunner.CONSOLE_LOG.info("\nERROR: Parameters are not loaded correctly for a data driven test class. ParameterNames are missing."
                    + "\nPlease check and fix it!\nNow exiting...");
            AllTestRunner.exitFromGepard(ExitCode.EXIT_CODE_DATA_DRIVEN_TEST_CLASS_WITHOUT_DATA_NAMES);
        }
        int columns = classData.getDrivenData().getParameters().length;
        int namesNo = parameterNames.length;
        if (columns != namesNo) { //we must have as many parameter names as parameters we have
            AllTestRunner.CONSOLE_LOG.info("\nERROR: Parameters are not loaded correctly for a data driven test class. "
                    + "Number of ParameterNames differs from the number of parameters." + "\nPlease check and fix it!\nNow exiting...");
            AllTestRunner.exitFromGepard(ExitCode.EXIT_CODE_DATA_DRIVEN_TEST_CLASS_INCORRECT_NUMBER_OF_DATA_NAMES);
        }
    }

    /**
     * This class ensures that the test class receives all data-driven (and other) information before its execution.
     *
     * @param clazz is the test class to be initialized.
     */
    protected void handleTestClassAnnotation(final Class<?> clazz) {
        // Handle annotated classes
        if (clazz.isAnnotationPresent(TestClass.class)) {
            handleTestClassInternalCounter(clazz);
            //set script id and name
            String id = clazz.getName() + "/" + actualDataRow;
            TestClassExecutionData classData = GenericListTestSuite.getTestClassExecutionData(id); //get the class exec object
            classData.setTestScriptId(clazz.getAnnotation(TestClass.class).id());
            classData.loadParameters();
            String extension = "";
            if (classData.getDrivenData() != null) {
                extension = " - " + classData.getDrivenData().getParameters()[0]; //puts the first parameter into the test name
            }
            classData.setTestScriptName(clazz.getAnnotation(TestClass.class).name() + extension);
        } else {
            //no proper annotation at Test Class, cannot continue
            AllTestRunner.CONSOLE_LOG.info("\nERROR: @TestClass annotation is missing at class: " + clazz.getCanonicalName() + " - Please fix!");
            AllTestRunner.exitFromGepard(ExitCode.EXIT_CODE_TEST_CLASS_ANNOTATION_MISSING);
        }
    }

    private void handleTestClassInternalCounter(final Class testClass) {
        //first either reset the counter or increase it, based on the given test class name
        String testClassName = testClass.getName();
        if ((actualTestClassName == null) || (!actualTestClassName.contentEquals(testClassName))) {
            actualTestClassName = testClassName;
            actualDataRow = 0;
        } else {
            actualDataRow++;
        }
    }

    /**
     * This class registers the test methods in the global map.
     *
     * @param clazz      in which class we search for the test methods.
     * @param rowNo      in case of data-driven test, when the test class is repeated, this specifies the actual repetition.
     * @param dataFeeder is the data feeder class in order to load the proper test data.
     */
    protected void registerMethodsInGlobalMap(final Class<?> clazz, final int rowNo, final DataFeederLoader dataFeeder) {
        //register this class in the global class list
        String id = clazz.getName() + "/" + rowNo;
        if (testClassMap.containsKey(id)) {
            //this is bad, this means ...
            AllTestRunner.CONSOLE_LOG.info("\nERROR: Duplicated Class found in testlist: " + clazz.getName()
                    + "\nPlease ensure that a class is listed only one time in the list!\nNow exiting...");
            AllTestRunner.exitFromGepard(ExitCode.EXIT_CODE_TEST_CLASS_DUPLICATED);
        }
        TestClassExecutionData classData = new TestClassExecutionData(id, environment);
        classData.setClassName(clazz.getName());
        classData.setDataFeederLoader(dataFeeder);
        classData.setDataRow(rowNo); //note: to load the parameters we just waiting for the paramnames
        classData.setTestClass(clazz);  //for Junit 4
        testClassMap.put(id, classData);
        new HtmlRunReporter(classData); //this prepares everything that need to be prepared for Html Logging the execution of the test class
    }

    public int getUsedTc() {
        return usedTc;
    }

    public int getTestClassCount() {
        return testClassMap.size();
    }

}
