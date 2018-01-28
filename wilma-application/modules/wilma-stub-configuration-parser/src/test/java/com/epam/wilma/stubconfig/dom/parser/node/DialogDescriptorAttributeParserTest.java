package com.epam.wilma.stubconfig.dom.parser.node;
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

import static org.mockito.BDDMockito.given;
import static org.testng.Assert.assertEquals;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Element;

import com.epam.wilma.common.helper.CurrentDateProvider;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptorUsage;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;

/**
 * Provides unit tests for the class {@link DialogDescriptorAttributeParser}.
 * @author Tunde_Kovacs
 *
 */
public class DialogDescriptorAttributeParserTest {

    @Mock
    private Element dialogDescriptor;
    @Mock
    private CurrentDateProvider currentDateProvider;

    @InjectMocks
    private DialogDescriptorAttributeParser underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testParseShouldReturnDialogDescriptorName() {
        //GIVEN
        getAttributeMocks();
        //WHEN
        DialogDescriptorAttributes actual = underTest.getAttributes(dialogDescriptor);
        //THEN
        assertEquals(actual.getName(), "name");

    }

    @Test
    public void testParseShouldReturnDialogDescriptorUsage() {
        //GIVEN
        getAttributeMocks();
        //WHEN
        DialogDescriptorAttributes actual = underTest.getAttributes(dialogDescriptor);
        //THEN
        assertEquals(actual.getUsage(), DialogDescriptorUsage.ALWAYS);

    }

    @Test
    public void testParseShouldReturnEmptyValidityValueWhenItIsNotGiven() {
        //GIVEN
        given(dialogDescriptor.getAttribute("name")).willReturn("name");
        given(dialogDescriptor.getAttribute("usage")).willReturn("always");
        //WHEN
        DialogDescriptorAttributes actual = underTest.getAttributes(dialogDescriptor);
        //THEN
        assertEquals(actual.getHitcount(), 0);
        assertEquals(actual.getTimeout(), 0);
    }

    @Test
    public void testParseShouldReturnEmptyValidityValueWhenItIsEmpty() {
        //GIVEN
        given(dialogDescriptor.getAttribute("name")).willReturn("name");
        given(dialogDescriptor.getAttribute("usage")).willReturn("always");
        given(dialogDescriptor.getAttribute("validityValue")).willReturn("");
        //WHEN
        DialogDescriptorAttributes actual = underTest.getAttributes(dialogDescriptor);
        //THEN
        assertEquals(actual.getHitcount(), 0);
        assertEquals(actual.getTimeout(), 0);
    }

    @Test
    public void testParseShouldReturnHitcountWhenUsageIsHitcount() {
        //GIVEN
        given(dialogDescriptor.getAttribute("name")).willReturn("name");
        given(dialogDescriptor.getAttribute("usage")).willReturn("hitcount");
        given(dialogDescriptor.getAttribute("validityValue")).willReturn("2");
        //WHEN
        DialogDescriptorAttributes actual = underTest.getAttributes(dialogDescriptor);
        //THEN
        assertEquals(actual.getHitcount(), 2);

    }

    @Test
    public void testParseShouldReturnTheMomentOfTimeoutWhenUsageIsTimeout() {
        //GIVEN
        given(dialogDescriptor.getAttribute("name")).willReturn("name");
        given(dialogDescriptor.getAttribute("usage")).willReturn("timeout");
        given(dialogDescriptor.getAttribute("validityValue")).willReturn("1");
        given(currentDateProvider.getCurrentTimeInMillis()).willReturn(1000L);
        //WHEN
        DialogDescriptorAttributes actual = underTest.getAttributes(dialogDescriptor);
        //THEN
        assertEquals(actual.getTimeout(), 61000L);
    }

    @Test(expectedExceptions = DescriptorValidationFailedException.class)
    public void testParseWhenUsageIsHitcountAndValidityValueNotSetShouldThrowException() {
        //GIVEN
        given(dialogDescriptor.getAttribute("name")).willReturn("name");
        given(dialogDescriptor.getAttribute("usage")).willReturn("hitcount");
        //WHEN
        underTest.getAttributes(dialogDescriptor);
        //THEN it should throw exception

    }

    @Test(expectedExceptions = DescriptorValidationFailedException.class)
    public void testParseWhenUsageIsTimeoutAndValidityValueNotSetShouldThrowException() {
        //GIVEN
        given(dialogDescriptor.getAttribute("name")).willReturn("name");
        given(dialogDescriptor.getAttribute("usage")).willReturn("timeout");
        //WHEN
        underTest.getAttributes(dialogDescriptor);
        //THEN it should throw exception

    }

    private void getAttributeMocks() {
        given(dialogDescriptor.getAttribute("name")).willReturn("name");
        given(dialogDescriptor.getAttribute("usage")).willReturn("always");
        given(dialogDescriptor.getAttribute("validityValue")).willReturn("0");
        given(dialogDescriptor.getAttribute("comment")).willReturn("comment");
    }
}
