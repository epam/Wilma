package com.epam.wilma.sequence.formatters.helper.converter;
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

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for {@link XmlConverter}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class XmlConverterTest {

    private static final String NOT_USED = "not used";
    private XmlConverter underTest;

    @BeforeMethod
    public void setUp() throws Exception {
        underTest = new XmlConverter();
    }

    @Test
    public void testGetJsonFormWithInvalidXmlShouldReturnEmptyJsonObject() {
        //GIVEN in setup
        //WHEN
        String json = underTest.convert("empty", NOT_USED);
        //THEN
        assertEquals(json, "{}");
    }

    @Test
    public void testGetJsonFormWithInvalidSyntaxShouldReturnEmptyString() {
        //GIVEN in setup
        //WHEN
        String json = underTest.convert("<><>", NOT_USED);
        //THEN
        assertEquals(json, "");
    }

    @Test
    public void testGetJsonFormWithValidJsonShouldReturnJson() {
        //GIVEN in setup
        //WHEN
        String json = underTest.convert("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><field1>value1</field1></root>", NOT_USED);
        //THEN
        assertEquals(json, "{\"root\":{\"field1\":\"value1\"}}");
    }

}
