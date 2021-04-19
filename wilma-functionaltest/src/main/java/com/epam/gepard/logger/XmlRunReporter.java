package com.epam.gepard.logger;

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
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.epam.gepard.common.GepardConstants;
import com.epam.gepard.common.NATestCaseException;
import com.epam.gepard.util.FileUtil;

/**
 * This reporter generates XML JUnit reports like the Ant JUnit task.
 * This report format is the one which the most build result processing
 * tool understand.
 *
 * @author Laszlo Kishalmi
 */
public final class XmlRunReporter extends RunListener {

    private static final AtomicInteger SUITE_INDEX = new AtomicInteger();
    private final DateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private Document doc;
    private final Map<String, TestInfo> testCases = new ConcurrentHashMap<>();
    private Element root;
    private final AtomicInteger errors = new AtomicInteger();
    private final AtomicInteger failures = new AtomicInteger();
    private final AtomicInteger ignores = new AtomicInteger();
    private final AtomicInteger tests = new AtomicInteger();
    private final File logPath;
    private boolean flushed;
    private long startTime;
    private final boolean handleIgnore;

    /**
     * Set-up the XML logger. This will be used as JUNIT results, and with the results,
     * tools those understand JUnit xml reports can be informed about the test results.
     * @param logPath is the path of the xml file.
     * @param handleIgnore if ignore handling is necessary or not.
     */
    public XmlRunReporter(final File logPath, final boolean handleIgnore) {
        this.logPath = logPath;
        this.handleIgnore = handleIgnore;
    }

    /**
     * Set-up the XML logger. This will be used as JUNIT results, and with the results,
     * tools those understand JUnit xml reports can be informed about the test results.
     * @param logPath is the path of the xml file.
     */
    public XmlRunReporter(final File logPath) {
        this(logPath, false);
    }

    @Override
    public void testFinished(final Description description) throws Exception {
        long finish = System.currentTimeMillis();
        String name = description.getDisplayName();
        TestInfo info = testCases.get(name);
        double time = (finish - info.startTime) / GepardConstants.ONE_SECOND_LENGTH.getConstant();
        info.element.setAttribute("time", String.valueOf(time));
        root.appendChild(info.element);
        testCases.remove(name);
    }

    @Override
    public void testRunFinished(final Result result) throws Exception {
        if (!flushed) {
            root.setAttribute("failures", String.valueOf(failures.get()));
            root.setAttribute("errors", String.valueOf(errors.get()));
            root.setAttribute("tests", String.valueOf(tests.get()));
            if (handleIgnore) {
                root.setAttribute("ignores", String.valueOf(ignores.get()));
            }
            root.setAttribute("time", String.valueOf((System.currentTimeMillis() - startTime) / GepardConstants.ONE_SECOND_LENGTH.getConstant()));
            String tsName = root.getAttribute("name");

            root.appendChild(doc.createElement("system-out"));
            root.appendChild(doc.createElement("system-err"));

            // Write the XML log into the log-dir or if it not specified use the current directory
            File dir = logPath != null ? logPath : new File(System.getProperty("user.dir"));
            String prefix = handleIgnore ? "ITEST-" : "TEST-";
            FileUtil fileUtil = new FileUtil();
            fileUtil.writeToFile(doc, new File(dir, prefix + calcName(tsName).replace(' ', '_') + "_" + SUITE_INDEX.getAndIncrement() + ".xml"));
            flushed = true;
        }
    }

    @Override
    public void testRunStarted(final Description description) throws Exception {
        flushed = false;
        startTime = System.currentTimeMillis();

        doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        root = doc.createElement("testsuite");
        root.setAttribute("name", description.getDisplayName());
        doc.appendChild(root);

        // Create properties section of the report
        Element properties = doc.createElement("properties");
        root.appendChild(properties);
        for (String key : System.getProperties().stringPropertyNames()) {
            Element property = doc.createElement("property");
            property.setAttribute("name", key);
            property.setAttribute("value", System.getProperty(key));
            properties.appendChild(property);
        }

        // Add hostname and timestamp attribute
        String hostName = InetAddress.getLocalHost().getCanonicalHostName();
        root.setAttribute("hostname", hostName);

        root.setAttribute("timestamp", timestamp.format(new Date()));

        errors.set(0);
        failures.set(0);
        ignores.set(0);
        tests.set(0);
    }

    @Override
    public void testStarted(final Description description) throws Exception {
        String name = description.getDisplayName();
        TestInfo info = testCases.put(name, new TestInfo(doc, name));
        if (info != null) {
            info.toString();
        }
        tests.incrementAndGet();
    }

    @Override
    public void testFailure(final Failure failure) throws Exception {
        String name = failure.getDescription().getDisplayName();
        TestInfo info = testCases.get(name);
        Element fe;
        if (!(failure.getException() instanceof AssertionError)) {
            fe = doc.createElement("error");
            fe.setAttribute("message", failure.getMessage());
            fe.setAttribute("type", failure.getException().getClass().getName());
            errors.incrementAndGet();
        } else {
            AssertionError ae = (AssertionError) failure.getException();
            if (handleIgnore && (ae.getCause() instanceof NATestCaseException)) {
                fe = doc.createElement("ignore");
                fe.setAttribute("message", ae.getCause().getMessage());
                fe.setAttribute("type", ae.getCause().getClass().getName());
                ignores.incrementAndGet();
            } else {
                fe = doc.createElement("failure");
                fe.setAttribute("message", failure.getMessage());
                fe.setAttribute("type", failure.getException().getClass().getName());
                failures.incrementAndGet();
            }
        }
        fe.appendChild(doc.createTextNode(failure.getTrace()));
        if (info != null) {
            info.element.appendChild(fe);
        }
    }

    @Override
    public void testIgnored(final Description description) throws Exception {
    }

    private static String calcName(final String name) {
        int openPar = name.indexOf('(');
        int closePar = name.indexOf(')', openPar + 1);
        return (openPar > 0) && (closePar > openPar) ? name.substring(0, openPar) : name;
    }

    private static String calcParam(final String name) {
        int openPar = name.indexOf('(');
        int closePar = name.indexOf(')', openPar + 1);
        return (openPar > 0) && (closePar > openPar) ? name.substring(openPar + 1, closePar) : null;
    }

    private static class TestInfo {

        private final long startTime = System.currentTimeMillis();
        private final Element element;

        public TestInfo(final Document doc, final String name) {
            element = doc.createElement("testcase");
            String tcName = calcName(name);
            String tcClass = calcParam(name);

            element.setAttribute("name", tcName);
            if (tcClass != null) {
                element.setAttribute("classname", tcClass);
            }
        }
    }
}
