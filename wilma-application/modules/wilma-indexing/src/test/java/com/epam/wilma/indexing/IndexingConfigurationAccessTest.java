package com.epam.wilma.indexing;

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

import com.epam.wilma.indexing.domain.PropertyDTO;
import com.epam.wilma.properties.PropertyHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for the class {@link IndexingConfigurationAccess}.
 *
 * @author Tunde_Kovacs
 */
public class IndexingConfigurationAccessTest {

    private final Integer port = 1234;

    @Mock
    private PropertyHolder propertyHolder;

    @InjectMocks
    private IndexingConfigurationAccess underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        given(propertyHolder.getInt("jms.queue.port")).willReturn(port);
    }

    @Test
    public void testLoadPropertiesShouldSetProxyHostAndPort() {
        //GIVEN in setUp
        String host = "local";
        given(propertyHolder.get("jms.queue.host")).willReturn(host);
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDTO actual = underTest.getProperties();
        assertEquals(host, actual.getBrokerHost());
        assertEquals(port, actual.getBrokerPort());
    }

    @Test
    public void testLoadPropertiesWhenHostIsMissingNull() {
        //GIVEN in setUp
        given(propertyHolder.get("jms.queue.host")).willReturn(null);
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDTO actual = underTest.getProperties();
        assertEquals("localhost", actual.getBrokerHost());
    }

    @Test
    public void testLoadPropertiesWhenHostIsMissingEmpty() {
        //GIVEN in setUp
        given(propertyHolder.get("jms.queue.host")).willReturn("");
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDTO actual = underTest.getProperties();
        assertEquals("localhost", actual.getBrokerHost());
    }

}
