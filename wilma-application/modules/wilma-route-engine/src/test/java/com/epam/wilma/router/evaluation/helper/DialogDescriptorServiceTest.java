package com.epam.wilma.router.evaluation.helper;
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

import com.epam.wilma.common.helper.CurrentDateProvider;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptorUsage;

/**
 * Provides unit tests for the class {@link DialogDescriptorService}.
 * @author Tunde_Kovacs
 *
 */
public class DialogDescriptorServiceTest {

    private DialogDescriptor dialogDescriptor;
    private DialogDescriptorAttributes attributes;

    @Mock
    private CurrentDateProvider currentDateProvider;

    @InjectMocks
    private DialogDescriptorService underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDecreaseWhenUsageIsHitcountShouldDecreaseHitcount() {
        //GIVEN
        attributes = new DialogDescriptorAttributes("name", DialogDescriptorUsage.HITCOUNT);
        attributes.setHitcount(2);
        dialogDescriptor = new DialogDescriptor(attributes, null, null);
        //WHEN
        underTest.decreaseHitcountWhenUsageIsHitcount(dialogDescriptor);
        //THEN
        assertEquals(dialogDescriptor.getAttributes().getHitcount(), 1);
    }

    @Test
    public void testDecreaseWhenUsageIsHitcountAndValValueIsOneShouldDisableDD() {
        //GIVEN
        attributes = new DialogDescriptorAttributes("name", DialogDescriptorUsage.HITCOUNT);
        attributes.setHitcount(1);
        dialogDescriptor = new DialogDescriptor(attributes, null, null);
        //WHEN
        underTest.decreaseHitcountWhenUsageIsHitcount(dialogDescriptor);
        //THEN
        assertEquals(dialogDescriptor.getAttributes().getUsage(), DialogDescriptorUsage.DISABLED);
    }

    @Test
    public void testDecreaseWhenUsageIsNotHitcountShouldDoNothing() {
        //GIVEN
        attributes = new DialogDescriptorAttributes("name", DialogDescriptorUsage.ALWAYS);
        dialogDescriptor = new DialogDescriptor(attributes, null, null);
        //WHEN
        underTest.decreaseHitcountWhenUsageIsHitcount(dialogDescriptor);
        //THEN
        assertEquals(dialogDescriptor.getAttributes().getUsage(), DialogDescriptorUsage.ALWAYS);
    }

    @Test
    public void testIsEnabledWhenUsageIsAlwaysShouldReturnTrue() {
        //GIVEN
        attributes = new DialogDescriptorAttributes("name", DialogDescriptorUsage.ALWAYS);
        dialogDescriptor = new DialogDescriptor(attributes, null, null);
        //WHEN
        boolean actual = underTest.isEnabled(dialogDescriptor);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testIsEnabledWhenUsageIsDisabledShouldReturnFalse() {
        //GIVEN
        attributes = new DialogDescriptorAttributes("name", DialogDescriptorUsage.DISABLED);
        dialogDescriptor = new DialogDescriptor(attributes, null, null);
        //WHEN
        boolean actual = underTest.isEnabled(dialogDescriptor);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testIsEnabledWhenUsageIsTimeoutAndCurrentTimeIsLessThanTimeoutShouldReturnTrue() {
        //GIVEN
        attributes = new DialogDescriptorAttributes("name", DialogDescriptorUsage.TIMEOUT);
        attributes.setTimeout(2000L);
        dialogDescriptor = new DialogDescriptor(attributes, null, null);
        given(currentDateProvider.getCurrentTimeInMillis()).willReturn(1000L);
        //WHEN
        boolean actual = underTest.isEnabled(dialogDescriptor);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testIsEnabledWhenUsageIsTimeoutAndCurrentTimeEqualsTimeoutShouldReturnTrue() {
        //GIVEN
        attributes = new DialogDescriptorAttributes("name", DialogDescriptorUsage.TIMEOUT);
        attributes.setTimeout(2000L);
        dialogDescriptor = new DialogDescriptor(attributes, null, null);
        given(currentDateProvider.getCurrentTimeInMillis()).willReturn(2000L);
        //WHEN
        boolean actual = underTest.isEnabled(dialogDescriptor);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testIsEnabledWhenUsageIsTimeoutAndCurrentTimeIsGreaterThanTimeoutShouldReturnFalse() {
        //GIVEN
        attributes = new DialogDescriptorAttributes("name", DialogDescriptorUsage.TIMEOUT);
        attributes.setTimeout(1000L);
        dialogDescriptor = new DialogDescriptor(attributes, null, null);
        given(currentDateProvider.getCurrentTimeInMillis()).willReturn(2000L);
        //WHEN
        boolean actual = underTest.isEnabled(dialogDescriptor);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testIsEnabledWhenUsageIsTimeoutAndCurrentTimeIsGreaterThanTimeoutShouldSetUsageToDisabled() {
        //GIVEN
        attributes = new DialogDescriptorAttributes("name", DialogDescriptorUsage.TIMEOUT);
        attributes.setTimeout(1000L);
        dialogDescriptor = new DialogDescriptor(attributes, null, null);
        given(currentDateProvider.getCurrentTimeInMillis()).willReturn(2000L);
        //WHEN
        underTest.isEnabled(dialogDescriptor);
        //THEN
        assertEquals(dialogDescriptor.getAttributes().getUsage(), DialogDescriptorUsage.DISABLED);
    }
}
