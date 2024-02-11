package com.epam.wilma.service.application;

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

import com.epam.wilma.service.domain.WilmaLoadInformation;
import com.epam.wilma.service.domain.WilmaServiceConfig;
import com.epam.wilma.service.http.WilmaHttpClient;
import com.google.common.base.Optional;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link WilmaApplication}.
 *
 * @author Tamas_Pinter, Tamas_Kohegyi
 */
public class WilmaApplicationTest {

    private static final String HOST = "host";
    private static final Integer PORT = 1;
    private static final String ACTUAL_LOAD_INFO_URL = "http://host:1/config/public/actualload";
    private static final String VERSION_INFO_URL = "http://host:1/config/public/version";
    private static final String SERVICE_URL = "http://host:1/config/public/service?";
    private static final String SHUTDOWN_URL = "http://host:1/config/admin/shutdown";
    private static final String LOAD_INFO_JSON_STRING = "{\"deletedFilesCount\":0,\"countOfMessages\":1,\"responseQueueSize\":2,\"loggerQueueSize\":3}";
    private static final String VERSION_INFO_JSON_STRING = "{\"wilmaVersion\":\"Version\"}";

    @Mock
    private WilmaHttpClient client;

    private WilmaApplication wilmaApplication;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        WilmaServiceConfig config = createMockConfig();
        wilmaApplication = new WilmaApplication(config, client);
    }

    @Test
    public void shouldThrowExceptionWhenConfigIsMissing() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new WilmaApplication(null);
        });
    }

    @Test
    public void shouldReturnNullIfClientReturnsOptionalAbsentForLoadInformation() {
        when(client.sendGetterRequest(ACTUAL_LOAD_INFO_URL)).thenReturn(Optional.<String>absent());

        WilmaLoadInformation result = wilmaApplication.getActualLoadInformation();

        Assertions.assertNull(result, "WilmaLoadInformation is expected to be null.");
        verify(client, never()).sendSetterRequest(anyString());
    }

    @Test
    public void shouldReturnWithProperValueForLoadInformation() {
        when(client.sendGetterRequest(ACTUAL_LOAD_INFO_URL)).thenReturn(Optional.of(LOAD_INFO_JSON_STRING));

        WilmaLoadInformation result = wilmaApplication.getActualLoadInformation();

        verify(client, never()).sendSetterRequest(anyString());
        Assertions.assertTrue(result.getDeletedFilesCount() == 0, "Wilma Load Information content is wrong.");
        Assertions.assertTrue(result.getCountOfMessages() == 1, "Wilma Load Information content is wrong.");
        Assertions.assertTrue(result.getResponseQueueSize() == 2, "Wilma Load Information content is wrong.");
        Assertions.assertTrue(result.getLoggerQueueSize() == 3, "Wilma Load Information content is wrong.");
    }

    @Test
    public void shouldReturnNullIfClientReturnsOptionalAbsentForVersionInformation() {
        when(client.sendGetterRequest(VERSION_INFO_URL)).thenReturn(Optional.<String>absent());

        String result = wilmaApplication.getVersionInformation();

        Assertions.assertNull(result, "WilmaVersionInformation is expected to be null.");
        verify(client, never()).sendSetterRequest(anyString());
    }

    @Test
    public void shouldReturnWithProperValueForVersionInformation() {
        when(client.sendGetterRequest(VERSION_INFO_URL)).thenReturn(Optional.of(VERSION_INFO_JSON_STRING));

        String result = wilmaApplication.getVersionInformation();

        verify(client, never()).sendSetterRequest(anyString());
        Assertions.assertTrue(VERSION_INFO_JSON_STRING.contains(result), "Wilma Version Information content is wrong.");
    }

    @Test
    public void shouldReturnWithProperBooleanValueForShutdownRequest() {
        when(client.sendSetterRequest(SHUTDOWN_URL)).thenReturn(true);

        Assertions.assertTrue(wilmaApplication.shutdownApplication());
        verify(client, never()).sendGetterRequest(anyString());
    }

    @Test
    public void testCallToSpecialServices() {
        String queryString = "blah";
        when(client.sendGetterRequest(SERVICE_URL + queryString)).thenReturn(Optional.of("{\"unknownRequest\":\"blah\"}"));
        JSONObject o = wilmaApplication.callGetService(queryString);
        verify(client, never()).sendSetterRequest(anyString());
        Assertions.assertTrue(o != null, "Wilma Special Service Call is wrong.");
    }

    private WilmaServiceConfig createMockConfig() {
        return WilmaServiceConfig.getBuilder()
                .withHost(HOST)
                .withPort(PORT)
                .build();
    }

}
