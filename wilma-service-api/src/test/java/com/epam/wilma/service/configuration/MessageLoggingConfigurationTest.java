package com.epam.wilma.service.configuration;

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

import com.epam.wilma.service.domain.MessageLoggingControlStatus;
import com.epam.wilma.service.domain.WilmaServiceConfig;
import com.epam.wilma.service.http.WilmaHttpClient;
import com.google.common.base.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.epam.wilma.service.domain.MessageLoggingControlStatus.OFF;
import static com.epam.wilma.service.domain.MessageLoggingControlStatus.ON;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link MessageLoggingConfiguration}.
 *
 * @author Tamas_Pinter
 */
public class MessageLoggingConfigurationTest {

    private static final String HOST = "host";
    private static final Integer PORT = 1;
    private static final String LOGGING_STATUS_URL = "http://host:1/config/public/logging/status";
    private static final String LOGGING_STATUS_SETTER_URL_ON = "http://host:1/config/admin/logging/on";
    private static final String LOGGING_STATUS_SETTER_URL_OFF = "http://host:1/config/admin/logging/off";
    private static final String JSON_STRING = "{\"requestLogging\":true,\"responseLogging\":true}";

    @Mock
    private WilmaHttpClient client;

    private MessageLoggingConfiguration messageLoggingConfiguration;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        WilmaServiceConfig config = createMockConfig();
        messageLoggingConfiguration = new MessageLoggingConfiguration(config, client);
    }

    @Test
    public void shouldThrowExceptionWhenConfigIsMissing() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new MessageLoggingConfiguration(null);
        });
    }

    @Test
    public void shouldReturnNullIfClientReturnsOptionalAbsent() {
        when(client.sendGetterRequest(LOGGING_STATUS_URL)).thenReturn(Optional.<String>absent());

        MessageLoggingControlStatus result = messageLoggingConfiguration.getMessageLoggingStatus();

        Assertions.assertNull(result);
        verify(client, never()).sendSetterRequest(anyString());
    }

    @Test
    public void shouldReturnJSONObjectWithProperValue() {
        when(client.sendGetterRequest(LOGGING_STATUS_URL)).thenReturn(Optional.of(JSON_STRING));

        MessageLoggingControlStatus result = messageLoggingConfiguration.getMessageLoggingStatus();

        Assertions.assertTrue(result == MessageLoggingControlStatus.ON);
        verify(client, never()).sendSetterRequest(anyString());
    }

    @Test
    public void shouldReturnWithProperBooleanValueForSetterRequest() {
        when(client.sendSetterRequest(LOGGING_STATUS_SETTER_URL_ON)).thenReturn(true);
        when(client.sendSetterRequest(LOGGING_STATUS_SETTER_URL_OFF)).thenReturn(false);

        Assertions.assertTrue(messageLoggingConfiguration.setMessageLoggingStatus(ON));
        Assertions.assertFalse(messageLoggingConfiguration.setMessageLoggingStatus(OFF));
        verify(client, never()).sendGetterRequest(anyString());
    }

    private WilmaServiceConfig createMockConfig() {
        return WilmaServiceConfig.getBuilder()
                .withHost(HOST)
                .withPort(PORT)
                .build();
    }
}
