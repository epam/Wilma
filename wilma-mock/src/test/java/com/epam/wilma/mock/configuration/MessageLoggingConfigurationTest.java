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

import static com.epam.wilma.mock.domain.MessageLoggingControl.OFF;
import static com.epam.wilma.mock.domain.MessageLoggingControl.ON;
import static junit.framework.Assert.assertEquals;
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
 * Unit test for {@link MessageLoggingConfiguration}.
 *
 * @author Tamas_Pinter
 *
 */
public class MessageLoggingConfigurationTest {
    
    private static final String HOST = "host";
    private static final Integer PORT = 1;
    private static final String LOGGING_STATUS_URL = "http://host:1/config/public/logging/status";
    private static final String LOGGING_STATUS_SETTER_URL_ON = "http://host:1/config/admin/logging/on";
    private static final String LOGGING_STATUS_SETTER_URL_OFF = "http://host:1/config/admin/logging/off";
    private static final String JSON_STRING = "{\"JSON\": \"string\"}";

    private static final JSONObject EMPTY_JSON_OBJECT = new JSONObject();

    @Mock
    private WilmaHttpClient client;

    private MessageLoggingConfiguration messageLoggingConfiguration;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);

        WilmaMockConfig config = createMockConfig();
        messageLoggingConfiguration = new MessageLoggingConfiguration(config, client);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConfigIsMissing() {
        new MessageLoggingConfiguration(null);
    }
    
    @Test
    public void shouldReturnEmptyJSONObjectIfClientReturnsOptionalAbsent() {
        when(client.sendGetterRequest(LOGGING_STATUS_URL)).thenReturn(Optional.<String>absent());

        JSONObject result = messageLoggingConfiguration.getMessageLoggingStatus();

        assertTrue(EMPTY_JSON_OBJECT.similar(result));
        assertEquals(EMPTY_JSON_OBJECT.length(), result.length());
        verify(client, never()).sendSetterRequest(anyString());
    }

    @Test
    public void shouldReturnJSONObjectWithProperValue() {
        when(client.sendGetterRequest(LOGGING_STATUS_URL)).thenReturn(Optional.of(JSON_STRING));

        JSONObject result = messageLoggingConfiguration.getMessageLoggingStatus();

        assertTrue(new JSONObject(JSON_STRING).similar(result), "The two JSON objects should be similar.");
        verify(client, never()).sendSetterRequest(anyString());
    }

    @Test
    public void shouldReturnWithProperBooleanValueForSetterRequest() {
        when(client.sendSetterRequest(LOGGING_STATUS_SETTER_URL_ON)).thenReturn(true);
        when(client.sendSetterRequest(LOGGING_STATUS_SETTER_URL_OFF)).thenReturn(false);
        
        assertTrue(messageLoggingConfiguration.setMessageLoggingStatus(ON));
        assertFalse(messageLoggingConfiguration.setMessageLoggingStatus(OFF));
        verify(client, never()).sendGetterRequest(anyString());
    }

    private WilmaMockConfig createMockConfig() {
        return WilmaMockConfig.getBuilder()
                .withHost(HOST)
                .withPort(PORT)
                .build();
    }
}
