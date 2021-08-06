package com.epam.wilma.proxy.configuration;

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
import com.epam.wilma.proxy.configuration.domain.ProxyPropertyDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 * Provides unit tests for the class {@link ProxyConfigurationAccess}.
 *
 * @author Tunde_Kovacs
 */
public class ProxyConfigurationAccessTest {

    private static final int REQUEST_TIMEOUT = 30000;

    private static final int PROXY_PORT = 1234;

    @Mock
    private PropertyHolder propertyHolder;

    @InjectMocks
    private ProxyConfigurationAccess underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(propertyHolder.getInt("proxy.request.timeout")).willReturn(REQUEST_TIMEOUT);
        given(propertyHolder.getInt("proxy.port")).willReturn(PROXY_PORT);
        given(propertyHolder.get("proxy.response.volatile")).willReturn(null);
    }

    @Test
    public void testLoadPropertiesShouldSetProxyPort() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THENs
        ProxyPropertyDTO actual = underTest.getProperties();
        assertEquals(Integer.valueOf(PROXY_PORT), actual.getProxyPort());
    }

    @Test
    public void testLoadPropertiesShouldSetRequestTimeout() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THENs
        ProxyPropertyDTO actual = underTest.getProperties();
        assertEquals(Integer.valueOf(REQUEST_TIMEOUT), actual.getRequestTimeout());
    }

    @Test
    public void testLoadPropertiesShouldSetDefaultResponseUpdateVolatileAsFalse() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THENs
        ProxyPropertyDTO actual = underTest.getProperties();
        assertEquals(Boolean.FALSE, actual.getAllowResponseUpdate());
    }

    @Test
    public void testLoadPropertiesShouldSetResponseUpdateVolatileAsFalse() {
        //GIVEN in setUp
        given(propertyHolder.get("proxy.response.volatile")).willReturn("false");
        //WHEN
        underTest.loadProperties();
        //THENs
        ProxyPropertyDTO actual = underTest.getProperties();
        assertEquals(Boolean.FALSE, actual.getAllowResponseUpdate());
    }

    @Test

    public void testLoadPropertiesShouldSetResponseUpdateVolatileAsTrue() {
        //GIVEN in setUp
        given(propertyHolder.get("proxy.response.volatile")).willReturn("true");
        //WHEN
        underTest.loadProperties();
        //THENs
        ProxyPropertyDTO actual = underTest.getProperties();
        assertEquals(Boolean.TRUE, actual.getAllowResponseUpdate());
    }
}
