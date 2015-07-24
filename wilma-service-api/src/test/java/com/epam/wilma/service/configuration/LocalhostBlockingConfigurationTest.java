package com.epam.wilma.service.configuration;

/*==========================================================================
 Copyright 2015 EPAM Systems

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

import com.epam.wilma.service.domain.LocalhostControlStatus;
import com.epam.wilma.service.domain.WilmaServiceConfig;
import com.epam.wilma.service.http.WilmaHttpClient;
import com.google.common.base.Optional;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.epam.wilma.service.domain.LocalhostControlStatus.OFF;
import static com.epam.wilma.service.domain.LocalhostControlStatus.ON;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Unit test for {@link LocalhostBlockingConfiguration}.
 *
 * @author Tamas_Pinter
 *
 */
public class LocalhostBlockingConfigurationTest {

    private static final String HOST = "host";
    private static final Integer PORT = 1;
    private static final String LOCALHOST_BLOCKING_STATUS_URL = "http://host:1/config/public/localhost/status";
    private static final String LOCALHOST_BLOCKING_SETTER_URL_ON = "http://host:1/config/admin/localhost/on";
    private static final String LOCALHOST_BLOCKING_SETTER_URL_OFF = "http://host:1/config/admin/localhost/off";
    private static final String JSON_STRING = "{\"localhostMode\":true}";

    @Mock
    private WilmaHttpClient client;

    private LocalhostBlockingConfiguration localhostBlockingConfiguration;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);

        WilmaServiceConfig config = createMockConfig();
        localhostBlockingConfiguration = new LocalhostBlockingConfiguration(config, client);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConfigIsMissing() {
        new LocalhostBlockingConfiguration(null);
    }

    @Test
    public void shouldReturnNullIfClientReturnsOptionalAbsent() {
        when(client.sendGetterRequest(LOCALHOST_BLOCKING_STATUS_URL)).thenReturn(Optional.<String>absent());

        LocalhostControlStatus result = localhostBlockingConfiguration.getLocalhostBlockingStatus();

        assertNull(result);
        verify(client, never()).sendSetterRequest(anyString());
    }

    @Test
    public void shouldReturnWithProperValue() {
        when(client.sendGetterRequest(LOCALHOST_BLOCKING_STATUS_URL)).thenReturn(Optional.of(JSON_STRING));

        LocalhostControlStatus result = localhostBlockingConfiguration.getLocalhostBlockingStatus();

        assertTrue(result == LocalhostControlStatus.ON);
        verify(client, never()).sendSetterRequest(anyString());
    }

    @Test
    public void shouldReturnWithProperBooleanValueForSetterRequest() {
        when(client.sendSetterRequest(LOCALHOST_BLOCKING_SETTER_URL_ON)).thenReturn(true);
        when(client.sendSetterRequest(LOCALHOST_BLOCKING_SETTER_URL_OFF)).thenReturn(false);

        assertTrue(localhostBlockingConfiguration.setLocalhostBlockingStatus(ON));
        assertFalse(localhostBlockingConfiguration.setLocalhostBlockingStatus(OFF));
        verify(client, never()).sendGetterRequest(anyString());
    }

    private WilmaServiceConfig createMockConfig() {
        return WilmaServiceConfig.getBuilder()
                .withHost(HOST)
                .withPort(PORT)
                .build();
    }

}
