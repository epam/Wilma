package com.epam.wilma.message.search.engine.initalize;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import com.epam.wilma.message.search.properties.PropertyHolder;

/**
 * Unit tests for the class {@link EngineConfigurationAccess}.
 * @author Tunde_Kovacs
 *
 */
public class EngineConfigurationAccessTest {

    private static final String MESSAGE_FOLDERS = "test-folder1,test-folder2";

    @Mock
    private PropertyHolder propertyHolder;

    @InjectMocks
    private EngineConfigurationAccess underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(propertyHolder.get("message.folders")).willReturn(MESSAGE_FOLDERS);
    }

    @Test
    public void testLoadPropertiesShouldSetMessageFolders() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDto actual = underTest.getProperties();
        assertEquals(actual.getMessageFolders(), MESSAGE_FOLDERS);
    }
}
