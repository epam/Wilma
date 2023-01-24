package com.epam.wilma.sequence.formatters.helper.resolver;

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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for {@link XmlXpathResolver}.
 *
 * @author Balazs_Berkes
 */
public class XmlXpathResolverTest {

    @InjectMocks
    private XmlXpathResolver underTest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        XPathProvider xPathProvider = new XPathProvider();
        ReflectionTestUtils.setField(underTest, "xPathProvider", xPathProvider);
    }

    @Test
    public void testGetValue() {
        //GIVEN in request
        //WHEN
        String value = underTest.getValue("local-name(/*[1])", request());
        //THEN
        assertEquals("BOB", value);
    }

    @Test
    public void testGetValueWithInvalid() {
        Assertions.assertThrows(XmlXpathResolver.InvalidXPathExpressionException.class, () -> {
            //GIVEN in request
            //WHEN
            underTest.getValue("local-name(//***\\\\/*[1])", request());
            //THEN exception is thrown
        });
    }

    private String request() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<ns7:BOB xmlns:ns16=\"urn:bob\"\n"
                + "                         xmlns:ns17=\"urn:bob\"\n" + "                         xmlns:ns14=\"urn:bob\"\n"
                + "                         xmlns:ns15=\"urn:bob\"\n" + "                         xmlns:ns9=\"urn:bob\"\n"
                + "                         xmlns:ns5=\"urn:bob\"\n" + "                         xmlns:ns12=\"urn:bob\"\n"
                + "                         xmlns:ns6=\"urn:bob\"\n" + "                         xmlns:ns13=\"urn:bob\"\n"
                + "                         xmlns:ns7=\"urn:bob\"\n" + "                         xmlns:ns10=\"urn:bob\"\n"
                + "                         xmlns:ns8=\"urn:bob\"\n" + "                         xmlns:ns11=\"urn:bob\"\n"
                + "                         xmlns:ns2=\"urn:bob\"\n" + "                         xmlns:ns1=\"urn:bob\"\n"
                + "                         xmlns:ns4=\"urn:bob\"\n" + "                         xmlns:ns3=\"urn:bob\">\n" + "</ns7:BOB>";
    }
}
