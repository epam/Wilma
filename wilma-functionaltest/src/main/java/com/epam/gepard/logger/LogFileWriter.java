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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.regexp.RE;

import com.epam.gepard.AllTestRunner;
import com.epam.gepard.common.Environment;
import com.epam.gepard.util.ExitCode;

/**
 * <p><b>Description:</b> This class is created to help logging testresults in HTML or other text formats like XML or CSV.
 * It builds an output file from a template structured in blocks (with special comments)
 * as followings:</p>
 * <p><code>&lt;!--Start of Block1--&gt; <br>
 * begin of HTML code here with %variable% to substitute.....<br>
 * &lt;!--End of Block1--&gt;<br>
 * &lt;!--Start of Block2--&gt;<br>
 * continue the HTML code.....<br>
 * &lt;!--End of Block2--&gt;.....<br></code>
 * </p>
 * <p>When this class is instantiated, the template file is read from the path given in the
 * constructor. The template file is split according to the beginning and the end of the
 * block-comments.
 * The log file can be built from the blocks:
 * </p>
 * <p>E.g.:<br><code>
 * Logger logger =  Logger (logTemplatePath, logPath);<br>
 * Properties props = new Properties();<br>
 * props.put("variable", "value");<br>
 * logger.insertBlock(Block1, props);<br>
 * logger.insertBlock(Block2, null);<br><br>
 * logger.close();
 * </code>
 * </p>
 *
 * @author dora.gal, Tamas Godan
 */
public class LogFileWriter {

    /**
     * Block separator, refers to the beginning of a block in the template file.
     */
    private static final String HTML_BLOCK_START = "<!--Start of (\\w*)-->";
    /**
     * Block separator, refers to the end of a block in the template file.
     */
    private static final String HTML_BLOCK_END = "<!--End of (\\w*)--?>";

    private final String logTemplatePath;
    private final String logPath;

    /**
     * For reading from the template file.
     */
    private BufferedReader template;
    /**
     * For writing into the output file.
     */
    private PrintWriter log;
    /**
     * It holds the blocks of the template HTML.
     */
    private final Map<String, String> blocks = new HashMap<>();

    private final Environment environment;

    /**
     * Constructor for Logger.
     * It splits the containment of the template file into blocks according to the block-separators.
     * A new Logger recreates the log file.
     *
     * @param logTemplatePath The path where the template is located.
     * @param logPath         The path where the output file should be put.
     * @param environment holds the properties of the application
     */
    public LogFileWriter(final String logTemplatePath, final String logPath, final Environment environment) {
        // storing path data
        this.logTemplatePath = formatPathName(logTemplatePath);
        this.logPath = formatPathName(logPath);
        this.environment = environment;

        openTemplateFileForReading(logTemplatePath);
        openLogFileForWriting(logPath);
        cutTemplateIntoBlocks();
    }

    private void cutTemplateIntoBlocks() {
        RE begin = null;
        RE end = null;
        try {
            begin = new RE(HTML_BLOCK_START);
            end = new RE(HTML_BLOCK_END);
        } catch (Exception e) {
            AllTestRunner.exitFromGepardWithCriticalException("\nCould not create regular expressions for extraction of template blocks", e, true,
                    ExitCode.EXIT_CODE_TEMPLATE_CANNOT_CREATE_REGULAR_EXPRESSION);
        }
        try {
            separatesBlocks(begin, end);
        } catch (IOException e) {
            AllTestRunner.exitFromGepardWithCriticalException("\nAn exception occurred during template processing:", e, true,
                    ExitCode.EXIT_CODE_TEMPLATE_OTHER_EXCEPTION);
        }
    }

    private void separatesBlocks(final RE begin, final RE end) throws IOException {
        String templateLine = template.readLine();
        StringBuilder blockContent = new StringBuilder();
        while (templateLine != null) {
            templateLine = insertBlock(begin, end, templateLine, blockContent);
            templateLine = template.readLine();
        }
    }

    private String insertBlock(final RE begin, final RE end, final String templateLineSource, final StringBuilder blockContent) throws IOException {
        String templateLine = templateLineSource;
        String blockName;
        if (begin.match(templateLine)) { //new block found
            blockContent.setLength(0); //re-setting the buffer
            blockName = begin.getParen(1); //getting the name of the block from the RE
            templateLine = template.readLine();
            while (!end.match(templateLine)) { //collecting blockContent
                blockContent.append(templateLine).append(System.getProperty("line.separator"));
                templateLine = template.readLine();
            }
            blocks.put(blockName, blockContent.toString());
        }
        return templateLine;
    }

    private void openLogFileForWriting(final String logPath) {
        try {
            File logFile = new File(logPath);
            //noinspection ResultOfMethodCallIgnored
            logFile.createNewFile();
            OutputStream out = new FileOutputStream(logFile);
            log = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        } catch (Exception e) {
            AllTestRunner.exitFromGepardWithCriticalException("\nCould not open log file " + logPath, e, true,
                    ExitCode.EXIT_CODE_TEMPLATE_CANNOT_OPEN_REPORT_LOG_FILE);
        }
    }

    private void openTemplateFileForReading(final String logTemplatePath) {
        try {
            BufferedReader br;
            //first try to access to templates on file system
            try {
                br = new BufferedReader(new FileReader(logTemplatePath));
            } catch (FileNotFoundException npe) {
                //not in filesystem, try to load it from jar
                Reader paramReader = new InputStreamReader(getClass().getResourceAsStream(logTemplatePath));
                br = new BufferedReader(paramReader);
            }
            template = br;
        } catch (Exception e) {
            AllTestRunner.CONSOLE_LOG.info("\nCould not open template file " + logTemplatePath);
            AllTestRunner.exitFromGepard(ExitCode.EXIT_CODE_TEMPLATE_FILE_NOT_AVAILABLE);
        }
    }

    /**
     * Re-formats a path so that it contains only forward slashes,
     * and also removes double slashes.
     *
     * @param pathName is the original path name to be formatted
     * @return with the formatted path name
     */
    public String formatPathName(final String pathName) {
        String inPathName = pathName.replace('\\', '/');
        int index = 0;
        int pos;
        while ((pos = inPathName.indexOf("//", index)) != -1) {
            inPathName = inPathName.substring(0, pos) + "/" + inPathName.substring(pos + 2);
            index = pos;
        }
        return inPathName;
    }

    /**
     * Gets the reference to the template file.
     * @return BufferReader Refers to the HTML template.
     */
    public BufferedReader getTemplate() {
        return template;
    }

    /**
     * Sets the template.
     * @param template The template to set
     */
    public void setTemplate(final BufferedReader template) {
        this.template = template;
    }

    /**
     * Gets the reference of the output log file.
     *
     * @return PrintWriter Refers to the HTML log file.
     */
    public PrintWriter getLog() {
        return log;
    }

    /**
     * Sets the log.
     *
     * @param log The log to set
     */
    public void setLog(final PrintWriter log) {
        this.log = log;
    }

    /**
     * Gets the path of the output log file.
     * @return String Log file name.
     */
    public String getLogPath() {
        return logPath;
    }

    /**
     * Gets the path of the input template file.
     * @return String Template file name.
     */
    public String getTemplatePath() {
        return logTemplatePath;
    }

    /**
     * This method inserts a block from the template file to the block file.
     *
     * @param blockName The name of the block to insert.
     * @param props     Contains the variable names and values which must be exchanged
     *                  in the block.
     */
    public void insertBlock(final String blockName, final Properties props) {
        String blockContent = blocks.get(blockName);
        Properties finalProps = copyPropertiesToFinalProperties(props);
        try {
            blockContent = substituteBlocks(blockContent, finalProps);
        } catch (Exception e) {
            AllTestRunner.exitFromGepardWithCriticalException("Exception occurred during substitution of block " + blockName + " in template "
                    + logTemplatePath, e, true, ExitCode.EXIT_CODE_TEMPLATE_BLOCK_SUBSTITUTION_ERROR);
        }
        getLog().print(blockContent);
        getLog().flush();
    }

    private String substituteBlocks(final String blockContent, final Properties finalProps) {
        String result = blockContent;
        String key;
        String value;
        Iterator<Object> itr = finalProps.keySet().iterator();
        RE re;
        while (itr.hasNext()) {
            key = itr.next().toString();
            value = finalProps.getProperty(key);
            re = new RE("%" + key + "%");
            result = re.subst(result, value);
        }
        return result;
    }

    private Properties copyPropertiesToFinalProperties(final Properties props) {
        Properties result = new Properties(environment.getProperties());
        if (props != null) {
            String key;
            String value;
            Iterator<Object> itr = props.keySet().iterator();
            while (itr.hasNext()) {
                key = itr.next().toString();
                value = props.getProperty(key);
                result.setProperty(key, value);
            }
        }
        return result;
    }

    /**
     * This method inserts text to the output file.
     *
     * @param text The text to insert.
     */
    public void insertText(final String text) {
        getLog().print(text + System.getProperty("line.separator"));
        getLog().flush();
    }

    /**
     * Closes the HTML template file and the HTMl log file.
     */
    public void close() {
        try {
            template.close();
            log.close();
        } catch (Exception e) {
            AllTestRunner.exitFromGepardWithCriticalException("\nCould not close template or log file", e, true,
                    ExitCode.EXIT_CODE_TEMPLATE_CANNOT_CLOSE_TEMPLATE);
        }
    }
}
