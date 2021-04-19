package com.epam.gepard.common;

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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.epam.gepard.AllTestRunner;

/**
 * <p>Title: Environment Class.</p>
 * <p>Description:
 * This class is for holding the properties with regard to the project.
 * The properties are to be set in a property file, in the following way:
 * <p>
 * //Comments beginning with with "//"  <br>
 * property.name1=property.value1       <br>
 * property.name2=property.value2       <br>
 * ....
 * </p>
 * Also supports setting a property, but it is only stored in the memory (ie. the property file
 * will not reflect the changes. Useful for properties which are only needed in the lifetime of this class).
 *
 * @author dora.gal, Tamas Godan, Adam_Csaba_Kiraly, tkohegyi
 */
public final class Environment {

    public static final String GEPARD_RESULT_PATH = "gepard.result.path";
    public static final String GEPARD_HTML_RESULT_PATH = "gepard.html.result.path";
    public static final String GEPARD_XML_RESULT_PATH = "gepard.xml.result.path";
    public static final String GEPARD_CSV_RESULT_PATH = "gepard.csv.result.path";
    public static final String GEPARD_RESULT_TEMPLATE_PATH = "gepard.result.template.path";

    public static final String GEPARD_FILTER_CLASS = "gepard.filter.class";
    public static final String GEPARD_FILTER_EXPRESSION = "gepard.filter.expression";

    public static final String GEPARD_TESTLIST_FILE = "gepard.testlist.file";
    public static final String GEPARD_TESTLIST_FAILURE_PATH = "gepard.testlist-failure.path";
    public static final String GEPARD_TESTLIST_FAILURE_FILE = "gepard.testlist-failure.file";
    public static final String GEPARD_TEST_RESOURCE_PATH = "gepard.test.resource.path";
    public static final String GEPARD_THREADS = "gepard.threads";

    public static final String GEPARD_DATA_DRIVEN_PATH_BASE = "gepard.datadriven.pathbase";
    public static final String GEPARD_DATA_DRIVEN_COLUMN_SPLITTER = "gepard.datadriven.columnsplitter";
    public static final String GEPARD_DATA_DRIVEN_FEEDER_CLASS = "gepard.datadriven.feeder.class";

    public static final String GEPARD_LOAD_AND_EXIT = "gepard.loadandexit";
    public static final String GEPARD_PUBLIC_PATH = "gepard.public.path";
    public static final String GEPARD_PUBLIC_RESULT = "gepard.public.result";
    public static final String GEPARD_PUBLIC_ENABLED = "gepard.public.enabled";

    public static final String GEPARD_REMOTE_ENABLED = "gepard.remote.enabled";
    public static final String GEPARD_REMOTE_PORT = "gepard.remote.port";
    public static final String GEPARD_REMOTE_FULL_CONTROL = "gepard.remote.fullcontrol";

    public static final String JIRA_SITE_URL = "jira.site.url";
    public static final String JIRA_SITE_USERNAME = "jira.site.username";
    public static final String JIRA_SITE_PASSWORD = "jira.site.password";

    public static final String SYSTEM_UNDER_TEST_VERSION = "system-under-test.version";
    public static final String TEST_ENVIRONMENT_ID = "TEID";

    private static final String DELIMITER = ",";

    private static String testEnvironmentID;
    private final Properties properties = new AntProperties();

    /**
     * This method sets up the global variables needed for the application.
     * The variables are read from the property file, and are held in the
     * static property object.
     *
     * @param propFiles - The name of the property file with the whole path.
     * @return with true if property file was loaded properly, otherwise with false
     */
    public boolean setUp(final String propFiles) {
        boolean isLoadedProperly = true;
        if ("".equals(propFiles.trim())) {
            AllTestRunner.CONSOLE_LOG.info("No property files has been loaded into Environment class.");
            isLoadedProperly = false;
        } else { //properties need to be loaded - it can be a comma separated list of property files
            String[] strParts = propFiles.split(DELIMITER);
            String propFile;
            for (String strPart : strParts) {
                propFile = strPart;
                try {
                    InputStream inp = new FileInputStream(propFile);
                    properties.load(inp);
                    inp.close();
                } catch (IOException e) {
                    AllTestRunner.CONSOLE_LOG.info("Cannot load property file: " + propFile);
                    isLoadedProperly = false;
                }
            }
        }
        testEnvironmentID = getProperty(Environment.TEST_ENVIRONMENT_ID);
        return isLoadedProperly;
    }

    /**
     * Returns all the properties in a Properties object.
     *
     * @return a Property object that holds the properties.
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Get the value of the specified property.
     *
     * @param name - refers to the name of property needed to return.
     * @return the value of the property represented as a String.
     */
    public String getProperty(final String name) {
        return properties.getProperty(name);
    }

    /**
     * Get a boolean property. If there is no such property or the value is not "true", it will be "false".
     *
     * @param name is the name of the boolean property we are interested in.
     * @return with the value of the boolean property.
     */
    public boolean getBooleanProperty(final String name) {
        boolean result = false;
        String b = properties.getProperty(name);
        if (b != null) {
            result = b.startsWith("true");
        }
        return result;
    }

    /**
     * Get the value of the specified property.
     *
     * @param name - refers to the name of property needed to return.
     * @param def  - a default value if the property does not exist in
     *             this environment.
     * @return the value of the property represented as a String.
     */
    public String getProperty(final String name, final String def) {
        return properties.getProperty(name, def);
    }

    /**
     * Sets the value of a property temporarily.
     *
     * @param name  Name of the property
     * @param value Value of the property
     */
    public void setProperty(final String name, final String value) {
        properties.setProperty(name, value);
    }

    /**
     * Gets the specified Test Environment. Used for loading Test Environmnet specific data access.
     * @return with the ID of the Test Environment, TEID.
     */
    public String getTestEnvironmentID() {
        return testEnvironmentID;
    }

    /**
     * Get Test Environment specific property.
     * Test Environment specific properties starts with "env.<TSID>....",
     * and if such value don't found, try to lead the default value from "env.default..." property key.
     *
     * @param propertyKey is the property key
     * @return the property value
     */
    public String getTestEnvironmentProperty(String propertyKey) {
        String localPropertyKey = propertyKey.replaceAll("^", "env." + testEnvironmentID + ".");

        String result = getProperty(localPropertyKey);
        if (result == null) {
            result = getProperty("env.default." + propertyKey);
        }

        return result;
    }

    /**
     * Set environment property value.
     * @param property String
     * @param value String
     */
    public void setTestEnvironmentProperty(String property, String value) {
        String localPropertyKey = property.replaceAll("^", "env." + testEnvironmentID + ".");
        setProperty(localPropertyKey, value);
    }
}
