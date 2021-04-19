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

/**
 * General Error class to be used when a test case fails,
 * and no other specific error/exception is suitable for use.
 *
 * Created by Tamas_Kohegyi on 2015-03-15.
 */
public class TestFailedError extends Error {

    /**
     * Default constructor set.
     */
    public TestFailedError() {
    }

    /**
     * Default constructor set.
     * @param message that should be used as info about the exception.
     */
    public TestFailedError(String message) {
        super(message);
    }

    /**
     * Default constructor set.
     * @param cause is the reason of the exception, as caused by ...
     */
    public TestFailedError(Throwable cause) {
        super(cause);
    }

    /**
     * Default constructor set.
     * @param message that should be used as info about the exception.
     * @param cause is the reason of the exception, as caused by ...
     */
    public TestFailedError(String message, Throwable cause) {
        super(message, cause);
    }
}
