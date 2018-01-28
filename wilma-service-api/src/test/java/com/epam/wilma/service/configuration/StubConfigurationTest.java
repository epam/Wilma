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

import com.epam.wilma.service.domain.StubConfigOrder;
import com.epam.wilma.service.domain.StubConfigStatus;
import com.epam.wilma.service.domain.WilmaServiceConfig;
import com.epam.wilma.service.http.WilmaHttpClient;
import com.google.common.base.Optional;
import org.json.JSONObject;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.epam.wilma.service.domain.StubConfigOrder.DOWN;
import static com.epam.wilma.service.domain.StubConfigOrder.UP;
import static com.epam.wilma.service.domain.StubConfigStatus.DISABLED;
import static com.epam.wilma.service.domain.StubConfigStatus.ENABLED;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

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
    private static final String DROP_STUB_URL1 = "http://host:1/config/admin/stub/drop?groupname=testGroup1";
    private static final String DROP_STUB_URL2 = "http://host:1/config/admin/stub/drop?groupname=testGroup2";
    private static final String SAVE_STUB_URL = "http://host:1/config/admin/stub/save";
    private static final String GROUP_NAME = "testGroup1";
    private static final String JSON_STRING = "{\"JSON\": \"string\"}";

    private static final JSONObject EMPTY_JSON_OBJECT = new JSONObject();

    @Mock
    private WilmaHttpClient client;

    private StubConfiguration stubConfiguration;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);

        WilmaServiceConfig config = createMockConfig();
        stubConfiguration = new StubConfiguration(config, client);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConfigIsMissing() {
        new StubConfiguration(null);
    }

    @Test
    public void shouldReturnNullJSONObjectIfClientReturnsOptionalAbsent() {
        when(client.sendGetterRequest(STUB_STATUS_URL)).thenReturn(Optional.<String>absent());

        JSONObject result = stubConfiguration.getStubConfigInformation();

        assertNull(result);
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
        assertTrue(url.getValue().contains("groupname=testGroup1"));
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
        assertTrue(url.getValue().contains("groupname=testGroup1"));
        assertTrue(url.getValue().contains("direction=" + direction));

        verify(client, never()).sendGetterRequest(anyString());
    }

    @Test
    public void shouldUseProperDropConfigUrl() {
        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);

        stubConfiguration.dropStubConfig(GROUP_NAME);

        verify(client).sendSetterRequest(url.capture());
        assertEquals(url.getValue(), DROP_STUB_URL1);
    }

    @Test
    public void shouldUseProperSaveConfigUrl() {
        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);

        stubConfiguration.persistActualStubConfig();

        verify(client).sendSetterRequest(url.capture());
        assertEquals(url.getValue(), SAVE_STUB_URL);
    }

    @Test
    public void shouldReturnTrueForDropAllStubConfigIfTheGivenJSONIsCorrect() {
        when(client.sendGetterRequest(STUB_STATUS_URL)).thenReturn(Optional.<String>of(validNotEmptyJson()));
        when(client.sendSetterRequest(DROP_STUB_URL1)).thenReturn(true);
        when(client.sendSetterRequest(DROP_STUB_URL2)).thenReturn(true);

        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);

        boolean result = stubConfiguration.dropAllStubConfig();

        assertTrue(result);
        verify(client, times(2)).sendSetterRequest(url.capture());
        assertEquals(url.getAllValues().get(0), DROP_STUB_URL1);
        assertEquals(url.getAllValues().get(1), DROP_STUB_URL2);
    }

    @Test
    public void shouldReturnTrueForDropAllStubConfigIfTheGivenJSONIsCorrectEvenIfEmpty() {
        when(client.sendGetterRequest(STUB_STATUS_URL)).thenReturn(Optional.<String>of(validEmptyJson()));

        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);

        boolean result = stubConfiguration.dropAllStubConfig();

        assertTrue(result);
        verify(client, times(0)).sendSetterRequest(url.capture());
    }

    @Test
    public void shouldReturnFalseForDropAllStubConfigIfTheConfigIsMissing() {
        when(client.sendGetterRequest(STUB_STATUS_URL)).thenReturn(Optional.<String>absent());

        assertFalse(stubConfiguration.dropAllStubConfig());
    }

    @DataProvider(name = "testdata")
    public Object[][] testdata() {
        return new Object[][] {{invalidEmptyJson()}, {jsonWithoutConfigsArray()}, {jsonWithoutGroupName()}};
    }

    @Test(dataProvider = "testdata")
    public void shouldReturnFalseForDropAllStubConfigIfTheGivenJSONIsIncorrect(String jsonString) {
        when(client.sendGetterRequest(STUB_STATUS_URL)).thenReturn(Optional.<String>of(jsonString));

        assertFalse(stubConfiguration.dropAllStubConfig());
    }

    private String validEmptyJson() {
        return "{\"configs\":[]}";
    }

    private String validNotEmptyJson() {
        return "{\n"
                + "   \"configs\":[\n"
                + "      {\n"
                + "         \"groupname\":\"testGroup1\"\n"
                + "      },\n"
                + "      {\n"
                + "         \"groupname\":\"testGroup2\"\n"
                + "      }"
                + "   ]\n"
                + "}";

    }

    private String invalidEmptyJson() {
        return "{}";
    }

    private String jsonWithoutConfigsArray() {
        return "{\n"
                + "   \"json\":[\n"
                + "      {\n"
                + "         \"incorrect\":\"json\"\n"
                + "      }"
                + "   ]\n"
                + "}";
    }

    private String jsonWithoutGroupName() {
        return "{\n"
                + "   \"configs\":[\n"
                + "      {\n"
                + "         \"incorrect\":\"json\"\n"
                + "      }"
                + "   ]\n"
                + "}";
    }

    private WilmaServiceConfig createMockConfig() {
        return WilmaServiceConfig.getBuilder()
                .withHost(HOST)
                .withPort(PORT)
                .build();
    }

}
