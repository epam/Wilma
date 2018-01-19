package com.epam.wilma.core.toggle.sequence;
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
import static org.mockito.Mockito.verify;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.helper.BlockLocalhostUsage;
import com.epam.wilma.common.helper.OperationMode;
import com.epam.wilma.common.helper.SequenceHandlingState;
import com.epam.wilma.core.configuration.CoreConfigurationAccess;
import com.epam.wilma.core.configuration.domain.PropertyDto;
import com.epam.wilma.core.processor.entity.SequenceRequestHandlingProcessor;
import com.epam.wilma.sequence.SequenceManager;

/**
 * Unit test for {@link SequenceHandlingToggle}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class SequenceHandlingToggleTest {

    private PropertyDto.Builder builder;

    @Mock
    private CoreConfigurationAccess configurationAccess;
    @Mock
    private SequenceRequestHandlingProcessor sequenceRequestHandlingProcessor;
    @Mock
    private SequenceManager sequenceManager;

    @InjectMocks
    private SequenceHandlingToggle underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        builder = new PropertyDto.Builder();
        builder.sequenceHandlingState(SequenceHandlingState.OFF).blockLocalhostUsage(BlockLocalhostUsage.OFF).interceptorMode("off")
                .messageLogging("off").operationMode(OperationMode.WILMA);
    }

    @Test
    public void testSwitchOnShouldEnableSequenceHandling() {
        //GIVEN
        //WHEN
        underTest.switchOn();
        //THEN
        verify(sequenceRequestHandlingProcessor).setEnabled(true);
    }

    @Test
    public void testSwitchOffShouldDisableSequenceHandling() {
        //GIVEN
        //WHEN
        underTest.switchOff();
        //THEN
        verify(sequenceRequestHandlingProcessor).setEnabled(false);
    }

    @Test
    public void testSwitchOffShouldDropTheSequences() {
        //GIVEN
        //WHEN
        underTest.switchOff();
        //THEN
        verify(sequenceManager).dropAllSequences();
    }

    @Test
    public void testIsOnShouldReturnFalseWhenSequenceHandlingIsDisabled() {
        //GIVEN
        given(sequenceRequestHandlingProcessor.isEnabled()).willReturn(false);
        //WHEN
        boolean result = underTest.isOn();
        //THEN
        assertFalse(result);
    }

    @Test
    public void testIsOnShouldReturnTrueWhenSequenceHandlingIsEnabled() {
        //GIVEN
        given(sequenceRequestHandlingProcessor.isEnabled()).willReturn(true);
        //WHEN
        boolean result = underTest.isOn();
        //THEN
        assertTrue(result);
    }

    @Test
    public void testOnApplicationEventShouldDisableSequenceHandlingWhenConfigPropertyIsOff() {
        //GIVEN
        PropertyDto properties = builder.sequenceHandlingState(SequenceHandlingState.OFF).build();
        given(configurationAccess.getProperties()).willReturn(properties);
        //WHEN
        underTest.onApplicationEvent(null);
        //THEN
        verify(sequenceRequestHandlingProcessor).setEnabled(false);
    }

    @Test
    public void testOnApplicationEventShouldEnableSequenceHandlingWhenConfigPropertyIsOn() {
        //GIVEN
        given(configurationAccess.getProperties()).willReturn(builder.sequenceHandlingState(SequenceHandlingState.ON).build());
        //WHEN
        underTest.onApplicationEvent(null);
        //THEN
        verify(sequenceRequestHandlingProcessor).setEnabled(true);
    }

}
