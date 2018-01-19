package com.epam.wilma.common.helper;
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

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.stereotype.Component;

/**
 * Converts a stack trace to String.
 * @author Tamas_Bihari
 *
 */
@Component
public class StackTraceToStringConverter {

    /**
     * Converts a stack trace to String.
     * @param e the stack trace what will be converted
     * @return is the generated String
     */
    public String getStackTraceAsString(final Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw); //NOSONAR - we need to have stack trace as string here
        return sw.toString();
    }
}
