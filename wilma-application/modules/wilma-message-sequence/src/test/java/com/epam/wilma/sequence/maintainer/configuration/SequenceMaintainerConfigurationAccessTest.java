package com.epam.wilma.sequence.maintainer.configuration;
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
import com.epam.wilma.sequence.maintainer.configuration.domain.SequenceProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for the class {@link SequenceMaintainerConfigurationAccess}.
 *
 * @author Tibor_Kovacs
 */
public class SequenceMaintainerConfigurationAccessTest {
    private static final String EXPRESSION = "STRING_EXPRESSION";

    @Mock
    private SequenceProperties properties;
    @Mock
    private PropertyHolder propertyHolder;

    @InjectMocks
    private SequenceMaintainerConfigurationAccess underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        given(propertyHolder.get("sequence.cleanup.cron")).willReturn(EXPRESSION);
        given(properties.getCronExpression()).willReturn(EXPRESSION);
        ReflectionTestUtils.setField(underTest, "properties", null);
    }

    @Test
    public void testLoadPropertiesShouldSetCronExpressionWhenThePropertyIsNotLoaded() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        SequenceProperties actual = underTest.getProperties();
        assertEquals(EXPRESSION, actual.getCronExpression());
    }

    @Test
    public void testLoadPropertiesShouldSetCronExpressionWhenThePropertyIsLoaded() {
        //GIVEN in setUp
        ReflectionTestUtils.setField(underTest, "properties", properties);
        //WHEN
        underTest.loadProperties();
        //THEN
        SequenceProperties actual = underTest.getProperties();
        assertEquals(EXPRESSION, actual.getCronExpression());
    }

}
