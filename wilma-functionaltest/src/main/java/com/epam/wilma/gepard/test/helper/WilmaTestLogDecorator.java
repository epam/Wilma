package com.epam.wilma.gepard.test.helper;

/*==========================================================================
Copyright since 2013, EPAM Systems

This file is part of Wilma.

Wilma is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Wilma is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wilma.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

import com.epam.gepard.generic.GepardTestClass;
import com.epam.gepard.logger.HtmlRunReporter;
import com.epam.gepard.logger.LogFileWriter;
import com.epam.gepard.util.Util;
import com.epam.wilma.gepard.testclient.RequestParameters;
import com.epam.wilma.gepard.testclient.ResponseHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class represents a TestCase, which supports HTML logs, and beforeTestCaseSet
 * and afterTestCaseSet event.
 *
 * @author Tamas Kohegyi
 */
public abstract class WilmaTestLogDecorator implements GepardTestClass {

    private static int dumpFileCount;
    private final Charset utf8Charset = Charset.forName("UTF-8");

    private String originalRequestMessage;
    private String expectedResponseMessage;
    private int actualResponseCode;
    private String actualResponseContentType;
    private String actualDialogDescriptor;
    private int step; // as soon as HtmlRunReporter.step become accessible, it can be removed

    /**
     * Constructor, use this for Wilma tests.
     */
    public WilmaTestLogDecorator() {
        super();
    }

    public String getExpectedResponseMessage() {
        return expectedResponseMessage;
    }

    public int getActualResponseCode() {
        return actualResponseCode;
    }
    public String getActualResponseContentType() {
        return actualResponseContentType;
    }
    public String getActualDialogDescriptor() {
        return actualDialogDescriptor;
    }

    private String decodeUTF8(byte[] bytes) {
        //CHECKSTYLE_OFF
        return new String(bytes, utf8Charset); //sorry but this is the most effective conversation.
        //CHECKSTYLE_ON
    }

    /**
     * Loads the request message, from the filename.
     *
     * @param filename is the source of the request message
     * @throws Exception in any case of error
     */
    protected void setOriginalRequestMessageFromFile(final String filename) throws Exception {
        if ((!filename.endsWith(".xml")) && (!filename.endsWith(".fis") && (!filename.endsWith(".json")))) {
            throw new Exception("Original request message should be an xml, json or fastinfoset file!");
        }
        File file = new File(filename);
        URI uri = file.toURI();
        byte[] bytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(uri));
        originalRequestMessage = decodeUTF8(bytes);
    }

    /**
     * Set empty request message (we just call the url).
     */
    protected void setOriginalRequestMessageEmpty() {
        originalRequestMessage = "EMPTY REQUEST";
    }

    /**
     * Loads the request message, from the filename.
     *
     * @param filename is the name of the file to be used as request message
     * @throws Exception in any case of error
     */
    protected void setOriginalRequestMessageForStubConfig(final String filename) throws Exception {
        File file = new File(filename);
        URI uri = file.toURI();
        byte[] bytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(uri));
        originalRequestMessage = decodeUTF8(bytes);
    }

    /**
     * Loads the expected response message, from the filename.
     *
     * @param filename is the expected response filename
     * @throws java.io.IOException in case of error
     */
    protected void setExpectedResponseMessageFromFile(final String filename) throws IOException {
        File file = new File(filename);
        URI uri = file.toURI();
        byte[] bytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(uri));
        expectedResponseMessage = decodeUTF8(bytes);
    }

    protected void setExpectedResponseMessage(final String response) {
        expectedResponseMessage = response;
    }

    public void setActualResponseCode(final int statusCode) {
        actualResponseCode = statusCode;
    }

    public void setActualResponseContentType(final String actualResponseContentType) {
        this.actualResponseContentType = actualResponseContentType;
    }

    /**
     * Stores the actual response dialog descriptor.
     *
     * @param dialogDescriptor that should be preserved
     */
    public void setActualDialogDescriptor(final String dialogDescriptor) {
        actualDialogDescriptor = dialogDescriptor;
    }

    // FURTHER METHODS, DUMPING AND LOGGING.

    /**
     * Log Get Request Event.
     *
     * @param requestParameters is the requested parameters
     */
    public void logGetRequestEvent(RequestParameters requestParameters) {
        String text = "Sending GET request to URL: " + requestParameters.getTestServerUrl();
        if (requestParameters.isUseProxy()) {
            text += "\nUsing proxy:" + requestParameters.getWilmaHost() + ":" + requestParameters.getWilmaPort();
        } else {
            text += "\nWithout any proxy.";
        }
        logComment(text);
    }

    /**
     * Log simple Get Request Event to a specific URL.
     *
     * @param url is the target
     */
    public void logGetRequestEvent(String url) {
        String text = "Sending GET request to URL: " + url + ", without proxy.";
        logComment(text);
    }

    /**
     * Log POST request event.
     *
     * @param requestParameters that was sent in the request.
     */
    public void logPostRequestEvent(RequestParameters requestParameters) {
        HtmlRunReporter reporter = getTestClassExecutionData().getHtmlRunReporter();
        LogFileWriter logWriter = reporter.getTestMethodHtmlLog();

        String text = "Sending POST request to URL: " + requestParameters.getTestServerUrl();
        if (requestParameters.isUseProxy()) {
            text += "\nUsing proxy:" + requestParameters.getWilmaHost() + ":" + requestParameters.getWilmaPort();
        } else {
            text += "\nWithout any proxy.";
        }

        reporter.systemOutPrintLn(getStep() + ". " + text);

        String addStr = "";
        if (logWriter != null) {
            try {
                String dumpFileName = dumpSource(originalRequestMessage);
                File dumpFile = new File(dumpFileName);
                addStr = " <small>[<a href=\"" + dumpFile.getName() + "\" target=\"_new\">request source</a>] </small>";
            } catch (Exception e) {
                addStr = " <small>[Dump failed:" + e.getMessage() + "]<br/><code><small><br><pre>" + getFullStackTrace(e)
                        + "</pre></small></code></small>";
            }
        }
        if (logWriter != null) {
            logWriter.insertText("<tr><td align=\"center\">&nbsp;&nbsp;" + getStep() + ".&nbsp;&nbsp;</td><td bgcolor=\"#F0F0F0\">" + text + addStr + "</td></tr>\n");
        }
        increaseStep();
    }

    /**
     * Log resource upload event.
     *
     * @param fileName    is the name only
     * @param fileContent is the content of the file
     * @param url         is the target url
     */
    public void logResourceUploadRequestEvent(final String fileName, final String fileContent, final String url) {
        HtmlRunReporter reporter = getTestClassExecutionData().getHtmlRunReporter();
        LogFileWriter logWriter = reporter.getTestMethodHtmlLog();

        String addStr = "";
        String text = "Uploading resource: '" + fileName + "' to Wilma, using URL: " + url;

        reporter.systemOutPrintLn(text);

        if (logWriter != null) {
            try {
                String dumpFileName = dumpSource(fileContent);
                File dumpFile = new File(dumpFileName);
                addStr = " <small>[<a href=\"" + dumpFile.getName() + "\" target=\"_new\">resource content</a>] </small>";
            } catch (Exception e) {
                addStr = " <small>[Dump failed:" + e.getMessage() + "]<br/><code><small><br><pre>" + getFullStackTrace(e)
                        + "</pre></small></code></small>";
            }
        }
        if (logWriter != null) {
            logWriter.insertText("<tr><td align=\"center\">&nbsp;</td><td bgcolor=\"#F0F0F0\">" + text + addStr + "</td></tr>\n");
        }
    }

    /**
     * Log response event.
     *
     * @param responseMessage that was received
     */
    public void logResponseEvent(ResponseHolder responseMessage) {
        HtmlRunReporter reporter = getTestClassExecutionData().getHtmlRunReporter();
        LogFileWriter logWriter = reporter.getTestMethodHtmlLog();

        String addStr = "";
        String text = "Receiving response, response code: " + responseMessage.getResponseCode();
        String message = responseMessage.getResponseMessage();

        reporter.systemOutPrintLn(text);

        if (logWriter != null) {
            try {
                String dumpFileName = dumpSource(message);
                File dumpFile = new File(dumpFileName);
                addStr = " <small>[<a href=\"" + dumpFile.getName() + "\" target=\"_new\">response source</a>] </small>";
            } catch (Exception e) {
                addStr = " <small>[Dump failed:" + e.getMessage() + "]<br/><code><small><br><pre>" + getFullStackTrace(e)
                        + "</pre></small></code></small>";
            }
        }
        if (logWriter != null) {
            logWriter.insertText("<tr><td>&nbsp;</td><td bgcolor=\"#F0F0F0\">" + text + addStr + "</td></tr>\n");
        }
    }

    /**
     * Write request or response data to a file in the current test case's log directory.
     *
     * @param message the message to dump
     * @return Path of the created file
     */
    private String dumpSource(final String message) throws Exception {
        String newFilePath;
        String logPath = getTestClassExecutionData().getHtmlRunReporter().getTestMethodHtmlLog().getLogPath();
        String logPathCanonical = logPath.replace('\\', '/');
        int pos = logPathCanonical.lastIndexOf('/');
        int dumpFileID = dumpFileCount++;

        if (pos == -1) {
            newFilePath = "dump" + Integer.toString(dumpFileID) + ".html";
        } else {
            newFilePath = logPath.substring(0, pos) + "/dump" + Integer.toString(dumpFileID) + ".html";
        }

        dumpSource(newFilePath, message);

        return newFilePath;
    }

    /**
     * Write current HTML response data to file. This is not the XML representation, therefore
     * may be used only after explicit page load.
     *
     * @param fileName Target file path
     * @param message  the message to dump
     */
    private void dumpSource(final String fileName, final String message) throws Exception {
        PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8));
        out.println("<!-- Dumped on " + (new SimpleDateFormat()).format(new Date()) + " -->");

        String source = message;

        Util util = new Util();
        source = util.escapeHTML(source);
        out.println("<html> <head><meta charset=\"UTF-8\"></head> <body><pre>");
        out.println(source);
        out.println("</pre></body></html>");
        out.flush();
        out.close();
    }

    /**
     * Temp method, as soon as HtmlRunreporter.getStep method become available, this can be removed.
     *
     * @return with step value
     */
    private int getStep() {
        return step;
    }

    /**
     * Temp method, as soon as HtmlRunreporter.increaseStep method become available, this can be removed.
     */
    private void increaseStep() {
        step++;
    }

    /**
     * Gets the full stack trace of a Throwable and returns it in HTML format.
     * Temp method, as soon as Util.getFullStackTrace method become available, this can be removed.
     *
     * @param t is the throwable exception itself.
     * @return with the full stack trace, escaped for use in HTML.
     */
    protected String getFullStackTrace(final Throwable t) {
        Util u = new Util();
        return u.escapeHTML(Util.getStackTrace(t));
    }

}
