package com.epam.wilma.gepard.test.stub.helper;
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

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLTestCase;

/**
 * Compares two xmls.
 *
 * @author Tunde_Kovacs
 */
public class XMLAssert extends XMLTestCase {

    /**
     * Checks if the the actual xml equals to the expected xml content.
     *
     * @param actual   xml content
     * @param expected xml content
     * @throws Exception in case of any problem
     */
    public void testIdentical(final String actual, final String expected) throws Exception {
        Diff myDiff = new Diff(expected, actual);
        assertTrue("The xmls are not identical " + myDiff, myDiff.identical());
    }

}
