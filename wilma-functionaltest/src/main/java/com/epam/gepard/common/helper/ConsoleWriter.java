package com.epam.gepard.common.helper;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.gepard.common.Environment;

/**
 * Provides message blocks to AllTestRunner class to print out texts to the console.
 * @author Zsolt Kiss Gere, Laszlo Toth, Tamas Godan, Tamas Kohegyi, Tibor Kovacs
 */
public class ConsoleWriter {
    private static final Logger CONSOLE_LOG = LoggerFactory.getLogger("console");
    private Environment environment;

    /**
     * Constructs a new instance of {@link Environment}.
     * @param environment holds the properties of the application
     */
    public ConsoleWriter(final Environment environment) {
        this.environment = environment;
    }

    /**
     * Prints out the starting failure message block.
     */
    public void printStartFailureBlock() {
        CONSOLE_LOG.info("");
        CONSOLE_LOG.info("Syntax: java AllTestRunner <propFileList>");
        CONSOLE_LOG.info("");
        CONSOLE_LOG.info("   <propFileList>   A list of properties files separated with colons");
        CONSOLE_LOG.info("");
    }

    /**
     * Prints out the application information message block.
     */
    public void printApplicationInfoBlock() {
        CONSOLE_LOG.info("");
        CONSOLE_LOG.info("----- Gepard - Copyright 2004-2015 EPAM Systems - GNU GPL-V3.0 License -----");
        CONSOLE_LOG.info("");
    }

    /**
     * Prints out information about some necessary parameters.
     * @param propFileList is list of given property files at starting.
     */
    public void printParameterInfoBlock(final String propFileList) {
        CONSOLE_LOG.info("Templates path:    " + environment.getProperty(Environment.GEPARD_RESULT_TEMPLATE_PATH));
        CONSOLE_LOG.info("Test Result path:  " + environment.getProperty(Environment.GEPARD_RESULT_PATH));
        CONSOLE_LOG.info("Prop file list:    " + propFileList);
        CONSOLE_LOG.info("Filter class:      " + environment.getProperty(Environment.GEPARD_FILTER_CLASS));
        CONSOLE_LOG.info("Filter expr:       " + environment.getProperty(Environment.GEPARD_FILTER_EXPRESSION));
        CONSOLE_LOG.info("Testlist file:     " + environment.getProperty(Environment.GEPARD_TESTLIST_FILE));
    }

}
