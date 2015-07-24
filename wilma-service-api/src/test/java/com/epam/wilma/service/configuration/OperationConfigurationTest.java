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

import com.epam.wilma.service.domain.OperationMode;
import com.epam.wilma.service.domain.WilmaServiceConfig;
import com.epam.wilma.service.http.WilmaHttpClient;
import com.google.common.base.Optional;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.epam.wilma.service.domain.OperationMode.PROXY;
import static com.epam.wilma.service.domain.OperationMode.STUB;
import static com.epam.wilma.service.domain.OperationMode.WILMA;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Unit test for {@link OperationConfiguration}.
 *
 * @author Tamas_Pinter
 *
 */
public class OperationConfigurationTest {

    private static final String HOST = "host";
    private static final Integer PORT = 1;
    private static final String OPERATION_MODE_STATUS_URL = "http://host:1/config/public/switch/status";
    private static final String OPERATION_MODE_SETTER_URL_WILMA = "http://host:1/config/admin/switch/wilma";
    private static final String OPERATION_MODE_SETTER_URL_STUB = "http://host:1/config/admin/switch/stub";
    private static final String OPERATION_MODE_SETTER_URL_PROXY = "http://host:1/config/admin/switch/proxy";
    private static final String JSON_STRING_WILMA_MODE = "{\"proxyMode\":false,\"stubMode\":false,\"wilmaMode\":true}";
    private static final String JSON_STRING_STUB_MODE = "{\"proxyMode\":false,\"stubMode\":true,\"wilmaMode\":false}";
    private static final String JSON_STRING_PROXY_MODE = "{\"proxyMode\":true,\"stubMode\":false,\"wilmaMode\":false}";

    @Mock
    private WilmaHttpClient client;

    private OperationConfiguration operationConfiguration;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);

        WilmaServiceConfig config = createMockConfig();
        operationConfiguration = new OperationConfiguration(config, client);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConfigIsMissing() {
        new OperationConfiguration(null);
    }

    @Test
    public void shouldReturnNullIfClientReturnsOptionalAbsent() {
        when(client.sendGetterRequest(OPERATION_MODE_STATUS_URL)).thenReturn(Optional.<String>absent());

        OperationMode result = operationConfiguration.getOperationMode();

        assertNull(result);
        verify(client, never()).sendSetterRequest(anyString());
    }

    @Test
    public void shouldReturnWithProperValueWilma() {
        when(client.sendGetterRequest(OPERATION_MODE_STATUS_URL)).thenReturn(Optional.of(JSON_STRING_WILMA_MODE));

        OperationMode result = operationConfiguration.getOperationMode();

        assertTrue(result == OperationMode.WILMA, "The operation mode should be WILMA.");
        verify(client, never()).sendSetterRequest(anyString());
    }

    @Test
    public void shouldReturnWithProperValueStub() {
        when(client.sendGetterRequest(OPERATION_MODE_STATUS_URL)).thenReturn(Optional.of(JSON_STRING_STUB_MODE));

        OperationMode result = operationConfiguration.getOperationMode();

        assertTrue(result == OperationMode.STUB, "The operation mode should be STUB.");
        verify(client, never()).sendSetterRequest(anyString());
    }

    @Test
    public void shouldReturnWithProperValueProxy() {
        when(client.sendGetterRequest(OPERATION_MODE_STATUS_URL)).thenReturn(Optional.of(JSON_STRING_PROXY_MODE));

        OperationMode result = operationConfiguration.getOperationMode();

        assertTrue(result == OperationMode.PROXY, "The operation mode should be PROXY.");
        verify(client, never()).sendSetterRequest(anyString());
    }

    @Test
    public void shouldReturnWithProperBooleanValueForSetterRequest() {
        when(client.sendSetterRequest(OPERATION_MODE_SETTER_URL_WILMA)).thenReturn(true);
        when(client.sendSetterRequest(OPERATION_MODE_SETTER_URL_STUB)).thenReturn(false);
        when(client.sendSetterRequest(OPERATION_MODE_SETTER_URL_PROXY)).thenReturn(true);

        assertTrue(operationConfiguration.setOperationMode(WILMA));
        assertTrue(operationConfiguration.setOperationMode(PROXY));
        assertFalse(operationConfiguration.setOperationMode(STUB));
        verify(client, never()).sendGetterRequest(anyString());
    }

    private WilmaServiceConfig createMockConfig() {
        return WilmaServiceConfig.getBuilder()
                .withHost(HOST)
                .withPort(PORT)
                .build();
    }

}
