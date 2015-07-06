package com.epam.wilma.mock.configuration;

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

import static com.epam.wilma.mock.domain.LocalhostControl.OFF;
import static com.epam.wilma.mock.domain.LocalhostControl.ON;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.json.JSONObject;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.mock.domain.WilmaMockConfig;
import com.epam.wilma.mock.http.WilmaHttpClient;
import com.google.common.base.Optional;

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
    private static final String JSON_STRING = "{\"JSON\": \"string\"}";

    private static final JSONObject EMPTY_JSON_OBJECT = new JSONObject();

    @Mock
    private WilmaHttpClient client;

    private LocalhostBlockingConfiguration localhostBlockingConfiguration;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);

        WilmaMockConfig config = createMockConfig();
        localhostBlockingConfiguration = new LocalhostBlockingConfiguration(config, client);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConfigIsMissing() {
        new LocalhostBlockingConfiguration(null);
    }

    @Test
    public void shouldReturnEmptyJSONObjectIfClientReturnsOptionalAbsent() {
        when(client.sendGetterRequest(LOCALHOST_BLOCKING_STATUS_URL)).thenReturn(Optional.<String>absent());

        JSONObject result = localhostBlockingConfiguration.getLocalhostBlockingStatus();

        assertTrue(EMPTY_JSON_OBJECT.similar(result));
        assertEquals(EMPTY_JSON_OBJECT.length(), result.length());
        verify(client, never()).sendSetterRequest(anyString());
    }

    @Test
    public void shouldReturnJSONObjectWithProperValue() {
        when(client.sendGetterRequest(LOCALHOST_BLOCKING_STATUS_URL)).thenReturn(Optional.of(JSON_STRING));

        JSONObject result = localhostBlockingConfiguration.getLocalhostBlockingStatus();

        assertTrue(new JSONObject(JSON_STRING).similar(result), "The two JSON objects should be similar.");
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

    private WilmaMockConfig createMockConfig() {
        return WilmaMockConfig.getBuilder()
                .withHost(HOST)
                .withPort(PORT)
                .build();
    }

}
