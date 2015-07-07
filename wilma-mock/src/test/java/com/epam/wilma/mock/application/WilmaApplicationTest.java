package com.epam.wilma.mock.application;

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

import com.epam.wilma.mock.domain.WilmaMockConfig;
import com.epam.wilma.mock.http.WilmaHttpClient;
import com.google.common.base.Optional;
import org.json.JSONObject;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;

/**
 * Unit test for {@link WilmaApplication}.
 *
 * @author Tamas_Pinter, Tamas_Kohegyi
 *
 */
public class WilmaApplicationTest {

    private static final String HOST = "host";
    private static final Integer PORT = 1;
    private static final String ACTUAL_LOAD_INFO_URL = "http://host:1/config/public/actualload";
    private static final String VERSION_INFO_URL = "http://host:1/config/public/version";
    private static final String SHUTDOWN_URL = "http://host:1/config/admin/shutdown";
    private static final String JSON_STRING = "{\"JSON\": \"string\"}";

    private static final JSONObject EMPTY_JSON_OBJECT = new JSONObject();

    @Mock
    private WilmaHttpClient client;

    private WilmaApplication wilmaApplication;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);

        WilmaMockConfig config = createMockConfig();
        wilmaApplication = new WilmaApplication(config, client);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConfigIsMissing() {
        new WilmaApplication(null);
    }

    @Test
    public void shouldReturnEmptyJSONObjectIfClientReturnsOptionalAbsentForLoadInformation() {
        when(client.sendGetterRequest(ACTUAL_LOAD_INFO_URL)).thenReturn(Optional.<String>absent());

        JSONObject result = wilmaApplication.getActualLoadInformation();

        assertTrue(EMPTY_JSON_OBJECT.similar(result));
        assertEquals(EMPTY_JSON_OBJECT.length(), result.length());
        verify(client, never()).sendSetterRequest(anyString());
    }

    @Test
    public void shouldReturnJSONObjectWithProperValueForLoadInformation() {
        when(client.sendGetterRequest(ACTUAL_LOAD_INFO_URL)).thenReturn(Optional.of(JSON_STRING));

        JSONObject result = wilmaApplication.getActualLoadInformation();

        assertTrue(new JSONObject(JSON_STRING).similar(result), "The two JSON object should be similar.");
        verify(client, never()).sendSetterRequest(anyString());
    }

    @Test
    public void shouldReturnEmptyJSONObjectIfClientReturnsOptionalAbsentForVersionInformation() {
        when(client.sendGetterRequest(VERSION_INFO_URL)).thenReturn(Optional.<String>absent());

        JSONObject result = wilmaApplication.getVersionInformation();

        assertTrue(EMPTY_JSON_OBJECT.similar(result));
        assertEquals(EMPTY_JSON_OBJECT.length(), result.length());
        verify(client, never()).sendSetterRequest(anyString());
    }

    @Test
    public void shouldReturnJSONObjectWithProperValueForVersionInformation() {
        when(client.sendGetterRequest(VERSION_INFO_URL)).thenReturn(Optional.of(JSON_STRING));

        JSONObject result = wilmaApplication.getVersionInformation();

        assertTrue(new JSONObject(JSON_STRING).similar(result), "The two JSON object should be similar.");
        verify(client, never()).sendSetterRequest(anyString());
    }

    @Test
    public void shouldReturnWithProperBooleanValueForShutdownRequest() {
        when(client.sendSetterRequest(SHUTDOWN_URL)).thenReturn(true);

        assertTrue(wilmaApplication.shutdownApplication());
        verify(client, never()).sendGetterRequest(anyString());
    }

    private WilmaMockConfig createMockConfig() {
        return WilmaMockConfig.getBuilder()
                .withHost(HOST)
                .withPort(PORT)
                .build();
    }

}
