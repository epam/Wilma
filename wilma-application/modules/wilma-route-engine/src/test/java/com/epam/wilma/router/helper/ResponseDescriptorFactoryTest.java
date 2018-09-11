package com.epam.wilma.router.helper;
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

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.stubconfig.dialog.response.MimeType;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.response.template.Template;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateType;

/**
 * Unit test for {@link ResponseDescriptorFactory}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class ResponseDescriptorFactoryTest {

    private ResponseDescriptorFactory underTest;

    @BeforeMethod
    public void setUp() throws Exception {
        underTest = new ResponseDescriptorFactory();
    }

    @Test
    public void testCreateResponseDescriptorForStubMode() {
        //GIVEN
        String expectedMessage = "The requested URL was not found on this WILMA server.";
        Template expectedTemplate = new Template("template-for-stub-mode", TemplateType.TEXT, expectedMessage.getBytes());
        //WHEN
        ResponseDescriptor responseDescriptor = underTest.createResponseDescriptorForStubMode();
        ResponseDescriptorAttributes actualAttributes = responseDescriptor.getAttributes();
        //THEN
        assertEquals(0, actualAttributes.getDelay());
        assertEquals("404", actualAttributes.getCode());
        assertEquals(MimeType.TEXT.getOfficialMimeType(), actualAttributes.getMimeType());
        assertEquals(expectedTemplate, actualAttributes.getTemplate());
        assertNull(responseDescriptor.getResponseFormatters());
    }

}
