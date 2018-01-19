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

import com.epam.wilma.gepard.test.stub.helper.XMLAssert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class represents a TestCase, which supports HTML logs, and beforeTestCaseSet
 * and afterTestCaseSet event.
 *
 * @author Tamas Kohegyi
 */
public abstract class WilmaTestAssertDecorator extends WilmaTestLogDecorator {

    private final XMLAssert xmlAssert;

    /**
     * Constructor, use this for Wilma tests.
     */
    public WilmaTestAssertDecorator() {
        super();
        xmlAssert = new XMLAssert();
    }

    // A S S E R T S

    /**
     * Requires that the previously set expected result message can be found in the actual result.
     *
     * @param response is the response message
     */
    public void assertExpectedResultMessage(final String response) {
        String expectedResponseMessage = getExpectedResponseMessage();
        if (expectedResponseMessage == null) {
            naTestCase("Assert (assertExpectedResultMessage) called with unknown expected result message.");
        }
        String actual = response.replace("\n\t", "").replace("\r\n", "").replace("\n", "").replace("\r", "").replace(" ", "");
        String expected = expectedResponseMessage.replace("\n\t", "").replace("\r\n", "").replace("\n", "").replace("\r", "").replace(" ", "");
        String errorMessage = "The expected and the received messages are not equal.\nExpected:\n" + expected + "\n\nActual:\n" + actual;
        assertTrue(errorMessage, actual.startsWith(expected));
    }

    private String whitespaceFree(final String string) {
        return string.replaceAll("\\s", "");
    }

    /**
     * Asserts that the actual Json response equals to the expected response.
     *
     * @param actual response
     */
    protected void assertJsonContent(final String actual) {
        assertEquals(whitespaceFree(getExpectedResponseMessage()), whitespaceFree(actual));
    }

    /**
     * Asserts that the actual Xml response equals to the expected response.
     *
     * @param actual response
     * @throws Exception in case of problem
     */
    protected void checkXml(final String actual) throws Exception {
        xmlAssert.testIdentical(actual, getExpectedResponseMessage());
    }

    /**
     * Asserts that the actual response code equals to the expected response code.
     *
     * @param expected response code, the actual was set prior the call
     */
    protected void checkResponseCode(final int expected) {
        assertEquals(expected, getActualResponseCode());
    }

    /**
     * Check if the actual response content type is what we expected or not.
     *
     * @param responseContentType we expect, the actual was set prior the call
     */
    protected void checkResponseContentType(final String responseContentType) {
        assertEquals(responseContentType, getActualResponseContentType());
    }

    /**
     * Checks if the actual dialog descriptor contains the expected dialog descriptor or not.
     *
     * @param dialogDescriptor we expect, the actual was set prior the call
     */
    protected void checkResponseDialogDescriptor(final String dialogDescriptor) {
        assertTrue(getActualDialogDescriptor().contains(dialogDescriptor));
    }

}
