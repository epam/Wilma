package com.epam.wilma.maintainer.task.helper;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.properties.PropertyHolder;

/**
 * Test class for {@link MessageFileCounter}.
 * @author Tibor_Kovacs
 *
 */
public class MessageFileCounterTest {

    @Mock
    private PropertyHolder propertyHolder;

    @InjectMocks
    private MessageFileCounter underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(propertyHolder.get("log.folder")).willReturn("src/test/resources/test");
    }

    @Test
    public void testGetCountOfMessages() {
        //GIVEN
        //WHEN
        int result = underTest.getCountOfMessages();
        //THEN
        Assert.assertEquals(result, 2);
    }

    @Test
    public void testGetCountOfMessagesShouldReturnWithZeroWhenTheMessageFolderDoesNotExists() {
        //GIVEN
        given(propertyHolder.get("log.folder")).willReturn("src/test/resources/something");
        //WHEN
        int result = underTest.getCountOfMessages();
        //THEN
        Assert.assertEquals(result, 0);
    }
}
