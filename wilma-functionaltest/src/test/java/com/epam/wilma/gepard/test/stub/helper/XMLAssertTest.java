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

import org.junit.Before;
import org.junit.Test;
import org.xmlunit.XMLUnitException;

public class XMLAssertTest {

    private XMLAssert underTest;

    @Before
    public void setUp() {
        underTest = new XMLAssert();
    }

    @Test
    public void testIdenticalOK() {
        String actual = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<ExampleRequest>\n"
                + "   <stuff exampleID=\"456\"></stuff>\n"
                + "</ExampleRequest>";
        String expected = String.copyValueOf(actual.toCharArray());
        underTest.testIdentical(actual, expected);
    }

    @Test(expected = XMLUnitException.class)
    public void testIdenticalXmlError() {
        String actual = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<ExampleRequest>\n"
                + "   <stuff exampleID=\"456\"></stuff>\n"
                + "</ExampleRequest"; //wrong closure of the xml
        String expected = String.copyValueOf(actual.toCharArray());
        underTest.testIdentical(actual, expected);
    }

    @Test(expected = Error.class)
    public void testIdenticalFail1() {
        String actual = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<ExampleRequest>\n" //here is the diff
                + "   <stuff exampleID=\"456\"></stuff>\n"
                + "</ExampleRequest>";
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<ExampleResponse>\n"
                + "   <stuff exampleID=\"456\"></stuff>\n"
                + "</ExampleResponse>";
        underTest.testIdentical(actual, expected);
    }

    @Test(expected = Error.class)
    public void testIdenticalFail2() {
        String actual = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<ExampleRequest>\n"
                + "   <stuff exampleID=\"456\"></stuff>\n"
                + "</ExampleRequest>";
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<ExampleRequest>\n"
                + "   <stuff exampleID=\"123\"></stuff>\n" //here is the diff
                + "</ExampleRequest>";
        underTest.testIdentical(actual, expected);
    }

}