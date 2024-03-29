package com.epam.wilma.core.toggle.mode;
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

import com.epam.wilma.core.processor.entity.LocalhostRequestProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link LocalhostRequestProcessorToggle}.
 *
 * @author Adam_Csaba_Kiraly
 */
public class LocalhostRequestProcessorToggleTest {
    @Mock
    private LocalhostRequestProcessor localhostRequestProcessor;

    @InjectMocks
    private LocalhostRequestProcessorToggle underTest;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSwitchOffShouldDisableLocalhostRequestPreprocessor() {
        //GIVEN
        //WHEN
        underTest.switchOff();
        //THEN
        verify(localhostRequestProcessor).setEnabled(false);
    }

    @Test
    public void testSwitchOnShouldEnableLocalhostRequestPreprocessor() {
        //GIVEN
        //WHEN
        underTest.switchOn();
        //THEN
        verify(localhostRequestProcessor).setEnabled(true);
    }

    @Test
    public void testIsOnShouldReturnTrueIfLocalhostRequestProcessorIsEnabled() {
        //GIVEN
        given(localhostRequestProcessor.isEnabled()).willReturn(true);
        //WHEN
        boolean result = underTest.isOn();
        //THEN
        assertTrue(result);
    }

    @Test
    public void testIsOnShouldReturnFalseIfLocalhostRequestProcessorIsDisabled() {
        //GIVEN
        given(localhostRequestProcessor.isEnabled()).willReturn(false);
        //WHEN
        boolean result = underTest.isOn();
        //THEN
        assertFalse(result);
    }

}
