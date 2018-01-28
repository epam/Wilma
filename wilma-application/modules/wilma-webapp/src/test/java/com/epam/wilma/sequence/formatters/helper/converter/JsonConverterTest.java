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
import static org.testng.Assert.assertTrue;

import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for {@link JsonConverter}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class JsonConverterTest {

    private JsonConverter underTest;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        underTest = new JsonConverter();
    }

    @Test
    public void testGetXmlFormWithInvalidJsonShouldReturnEmptyString() {
        //GIVEN in setup
        //WHEN
        String xml = underTest.convert("content", "rootElementName");
        //THEN
        assertEquals(xml, "");
    }

    @Test
    public void testGetXmlFormWithValidJsonShouldReturnXml() {
        //GIVEN in setup
        //WHEN
        String xml = underTest.convert("{\"field1\":\"value1\",\"field2\":\"value2\"}", "rootElementName");
        //THEN
        //two answer is accepted
        String expectedResult1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rootElementName><field2>value2</field2><field1>value1</field1></rootElementName>";
        String expectedResult2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rootElementName><field1>value1</field1><field2>value2</field2></rootElementName>";
        assertTrue(xml.equals(expectedResult1) || xml.equals(expectedResult2), "Convert from JSON to XML failed, got answer:" + xml);
    }
}
