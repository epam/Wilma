package com.epam.wilma.safeguard.configuration;
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

import com.epam.wilma.properties.PropertyHolder;
import com.epam.wilma.safeguard.configuration.domain.PropertyDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for the class {@link SafeguardConfigurationAccess}.
 *
 * @author Tunde_Kovacs
 */
public class SafeguardConfigurationAccessTest {

    private static final long LIMIT = 100L;
    private static final String PORT = "1099";

    private static final String CRON_EXPRESSION = "CRON_EXPRESSION";

    @InjectMocks
    private SafeguardConfigurationAccess underTest;

    @Mock
    private PropertyHolder propertyHolder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        given(propertyHolder.getLong("safeguard.responseFIdecoder.OFFlimit")).willReturn(LIMIT);
        given(propertyHolder.getLong("safeguard.responseFIdecoder.ONlimit")).willReturn(LIMIT);
        given(propertyHolder.getLong("safeguard.responseMessageWriter.OFFlimit")).willReturn(LIMIT);
        given(propertyHolder.getLong("safeguard.responseMessageWriter.ONlimit")).willReturn(LIMIT);
        given(propertyHolder.get("com.sun.management.jmxremote.port")).willReturn(PORT);
        given(propertyHolder.get("safeguard.guardperiod")).willReturn(CRON_EXPRESSION);
    }

    @Test
    public void testLoadPropertiesShouldSetSafeGuardLimits() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDTO actual = underTest.getProperties();
        assertEquals(Long.valueOf(LIMIT), actual.getSafeguardLimits().getFiOffLimit());
        assertEquals(Long.valueOf(LIMIT), actual.getSafeguardLimits().getFiOnLimit());
        assertEquals(Long.valueOf(LIMIT), actual.getSafeguardLimits().getMwOffLimit());
        assertEquals(Long.valueOf(LIMIT), actual.getSafeguardLimits().getMwOnLimit());
        assertEquals(PORT, actual.getSafeguardLimits().getJmxPort());
    }

    @Test
    public void testLoadPropertiesShouldSetSchedulingCronExpression() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDTO actual = underTest.getProperties();
        assertEquals(CRON_EXPRESSION, actual.getCronExpression());
    }

}
