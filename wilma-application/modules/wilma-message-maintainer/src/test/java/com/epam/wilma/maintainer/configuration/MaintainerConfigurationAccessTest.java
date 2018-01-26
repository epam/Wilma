package com.epam.wilma.maintainer.configuration;

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

import com.epam.wilma.maintainer.configuration.domain.MaintainerProperties;
import com.epam.wilma.properties.PropertyHolder;

/**
 * Unit tests for the class {@link MaintainerConfigurationAccess}.
 * @author Tunde_Kovacs
 *
 */
public class MaintainerConfigurationAccessTest {
    private static final String MAINTAINER_METHOD = "fileLimit";
    private static final String EXPRESSION = "STRING_EXPRESSION";
    private static final Integer FILE_LIMIT = 0;

    @InjectMocks
    private MaintainerConfigurationAccess underTest;

    @Mock
    private PropertyHolder propertyHolder;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(propertyHolder.get("log.maintainer.cron")).willReturn(EXPRESSION);
        given(propertyHolder.get("log.maintainer.method")).willReturn(MAINTAINER_METHOD);
        given(propertyHolder.getInt("log.maintainer.file.limit")).willReturn(FILE_LIMIT);
        given(propertyHolder.get("log.maintainer.time.limit")).willReturn(EXPRESSION);
    }

    @Test
    public void testLoadPropertiesShouldSetLogFileMaintainerFields() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        MaintainerProperties actual = underTest.getProperties();
        assertEquals(actual.getCronExpression(), EXPRESSION);
        assertEquals(actual.getMaintainerMethod(), MAINTAINER_METHOD);
    }

    @Test
    public void testLoadPropertiesShouldSetFileLimit() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        MaintainerProperties actual = underTest.getProperties();
        assertEquals(actual.getFileLimit(), FILE_LIMIT);
    }

    @Test
    public void testLoadPropertiesShouldSetTimeLimit() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        MaintainerProperties actual = underTest.getProperties();
        assertEquals(actual.getTimeLimit(), EXPRESSION);
    }

}
