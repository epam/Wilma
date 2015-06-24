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

import static com.epam.wilma.mock.domain.StubConfigOrder.DOWN;
import static com.epam.wilma.mock.domain.StubConfigOrder.UP;
import static com.epam.wilma.mock.domain.StubConfigStatus.DISABLED;
import static com.epam.wilma.mock.domain.StubConfigStatus.ENABLED;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;

import org.json.JSONObject;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.epam.wilma.mock.domain.StubConfigOrder;
import com.epam.wilma.mock.domain.StubConfigStatus;
import com.epam.wilma.mock.domain.WilmaMockConfig;
import com.epam.wilma.mock.http.WilmaHttpClient;
import com.google.common.base.Optional;

/**
 * Unit test for {@link StubConfiguration}.
 *
 * @author Tamas_Pinter
 *
 */
public class StubConfigurationTest {

    private static final String HOST = "host";
    private static final Integer PORT = 1;
    private static final String STUB_STATUS_URL = "http://host:1/config/public/stubdescriptor";
    private static final String DROP_STUB_URL = "http://host:1/config/admin/stub/drop?groupname=testGroup1234";
    private static final String SAVE_STUB_URL = "http://host:1/config/admin/stub/save";
    private static final String GROUP_NAME = "testGroup1234";
    private static final String JSON_STRING = "{\"JSON\": \"string\"}";

    private static final JSONObject EMPTY_JSON_OBJECT = new JSONObject();

    @Mock
    private WilmaHttpClient client;

    private StubConfiguration stubConfiguration;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);

        WilmaMockConfig config = createMockConfig();
        stubConfiguration = new StubConfiguration(config, client);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConfigIsMissing() {
        new StubConfiguration(null);
    }

    @Test
    public void shouldReturnEmptyJSONObjectIfClientReturnsOptionalAbsent() {
        when(client.sendGetterRequest(STUB_STATUS_URL)).thenReturn(Optional.<String>absent());

        JSONObject result = stubConfiguration.getStubConfigInformation();

        assertTrue(EMPTY_JSON_OBJECT.similar(result));
        assertEquals(EMPTY_JSON_OBJECT.length(), result.length());
        verify(client, never()).sendSetterRequest(anyString());
    }

    @Test
    public void shouldReturnJSONObjectWithProperValue() {
        when(client.sendGetterRequest(STUB_STATUS_URL)).thenReturn(Optional.of(JSON_STRING));

        JSONObject result = stubConfiguration.getStubConfigInformation();

        assertTrue(new JSONObject(JSON_STRING).similar(result), "The two JSON objects should be similar.");
        verify(client, never()).sendSetterRequest(anyString());
    }

    @DataProvider(name = "stubStatusConfiguration")
    public static Object[][] stubStatusConfiguration() {
        return new Object[][] {{ENABLED, "true"}, {DISABLED, "false"}};
    }

    @Test(dataProvider = "stubStatusConfiguration")
    public void shouldCallProperUrlForStubConfigurationChangeStatusRequest(StubConfigStatus status, String nextStatusValue) {
        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);

        stubConfiguration.setStubConfigStatus(GROUP_NAME, status);

        verify(client).sendSetterRequest(url.capture());

        assertTrue(url.getValue().startsWith("http://host:1/config/admin/stub/changestatus?"));
        assertTrue(url.getValue().contains("groupname=testGroup1234"));
        assertTrue(url.getValue().contains("nextstatus=" + nextStatusValue));

        verify(client, never()).sendGetterRequest(anyString());
    }

    @DataProvider(name = "stubOrderConfiguration")
    public static Object[][] stubOrderConfiguration() {
        return new Object[][] {{DOWN, "-1"}, {UP, "1"}};
    }

    @Test(dataProvider = "stubOrderConfiguration")
    public void shouldCallProperUrlForStubConfigurationChangeOrderRequest(StubConfigOrder order, String direction) {
        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);

        stubConfiguration.setStubConfigOrder(GROUP_NAME, order);

        verify(client).sendSetterRequest(url.capture());

        assertTrue(url.getValue().startsWith("http://host:1/config/admin/stub/changeorder?"));
        assertTrue(url.getValue().contains("groupname=testGroup1234"));
        assertTrue(url.getValue().contains("direction=" + direction));

        verify(client, never()).sendGetterRequest(anyString());
    }

    @Test
    public void shouldUseProperDropConfigUrl() {
        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);

        stubConfiguration.dropStubConfig(GROUP_NAME);

        verify(client).sendSetterRequest(url.capture());
        assertEquals(DROP_STUB_URL, url.getValue());
    }

    @Test
    public void shouldUseProperSaveConfigUrl() {
        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);

        stubConfiguration.persistActualStubConfig();

        verify(client).sendSetterRequest(url.capture());
        assertEquals(SAVE_STUB_URL, url.getValue());
    }

    private WilmaMockConfig createMockConfig() {
        return WilmaMockConfig.getBuilder()
                .withHost(HOST)
                .withPort(PORT)
                .build();
    }

}
