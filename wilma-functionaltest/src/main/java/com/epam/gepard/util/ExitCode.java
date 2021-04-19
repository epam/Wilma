package com.epam.gepard.util;

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

/**
 * EXIT CODEs. Set it to &gt;100 if need to mention a wrong execution at exit.
 * Under 100 is reserved for Gepard.
 *
 * @author Tamas_Kohegyi
 */
public final class ExitCode {
    public static final int EXIT_CODE_BRUTE_FORCE = -1;

    public static final int EXIT_CODE_OK = 0;

    public static final int EXIT_CODE_WRONG_NUMBER_OF_PARAMETERS_OR_HELP_REQUEST = 1;
    public static final int EXIT_CODE_BAD_SETUP = 2;
    public static final int EXIT_CODE_UNKNOWN_ERROR = 3;
    public static final int EXIT_CODE_OUTPUT_FOLDER_HANDLING_ERROR = 4;
    public static final int EXIT_CODE_WRONG_PROPERTY_FILE = 5;
    public static final int EXIT_CODE_CANNOT_CREATE_FOLDER = 6;

    public static final int EXIT_CODE_TEST_CLASS_BAD_TIMEOUT = 10;
    public static final int EXIT_CODE_TEST_CLASS_WITHOUT_TEST_METHOD = 11;
    public static final int EXIT_CODE_TEST_CLASS_ANNOTATION_MISSING = 12;
    public static final int EXIT_CODE_TEST_CLASS_INITIALIZATION_ERROR = 13;
    public static final int EXIT_CODE_TEST_CLASS_DUPLICATED = 14;
    public static final int EXIT_CODE_TEST_CLASS_HAS_BAD_CONSTRUCTOR = 20;
    public static final int EXIT_CODE_TEST_CLASS_HAS_NO_CONSTRUCTOR = 21;

    public static final int EXIT_CODE_DATA_DRIVEN_TEST_CLASS_WITHOUT_DATA = 30;
    public static final int EXIT_CODE_NON_DATA_DRIVEN_TEST_CLASS_WITH_DATA = 31;
    public static final int EXIT_CODE_DATA_DRIVEN_TEST_CLASS_WITHOUT_DATA_NAMES = 32;
    public static final int EXIT_CODE_DATA_DRIVEN_TEST_CLASS_INCORRECT_NUMBER_OF_DATA_NAMES = 33;
    public static final int EXIT_CODE_DATA_FEEDER_BAD_DESCRIPTOR = 40;
    public static final int EXIT_CODE_DATA_FEEDER_INIT_FAILED = 41;
    public static final int EXIT_CODE_DATA_FEEDER_CLASS_MISSING = 45;
    public static final int EXIT_CODE_DATA_FEEDER_CLASS_DATA_CALCULATION_ERROR = 46;
    public static final int EXIT_CODE_DATA_FEEDER_CLASS_ERROR = 47;
    public static final int EXIT_CODE_DATA_FEEDER_CLASS_LOAD_FAILED = 49;

    public static final int EXIT_CODE_THIS_SHOULD_NOT_HAPPEN_CONTACT_MAINTAINERS = 100;

    public static final int EXIT_CODE_TEMPLATE_FILE_NOT_AVAILABLE = 101;
    public static final int EXIT_CODE_TEMPLATE_CANNOT_OPEN_REPORT_LOG_FILE = 102;
    public static final int EXIT_CODE_TEMPLATE_CANNOT_CREATE_REGULAR_EXPRESSION = 103;
    public static final int EXIT_CODE_TEMPLATE_OTHER_EXCEPTION = 104;
    public static final int EXIT_CODE_TEMPLATE_BLOCK_SUBSTITUTION_ERROR = 105;
    public static final int EXIT_CODE_TEMPLATE_CANNOT_CLOSE_TEMPLATE = 106;

    private ExitCode() {
    }
}
