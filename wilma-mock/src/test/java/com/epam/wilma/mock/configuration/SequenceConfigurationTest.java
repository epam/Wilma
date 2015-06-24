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

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
 * Unit test for {@link SequenceConfiguration}.
 *
 * @author Tamas_Pinter
 *
 */
public class SequenceConfigurationTest {

    private static final String HOST = "host";
    private static final Integer PORT = 1;
    private static final String SEQUENCES_STATUS_URL = "http://host:1/config/public/livesequences";
    private static final String JSON_STRING = "{\"JSON\": \"string\"}";

    private static final JSONObject EMPTY_JSON_OBJECT = new JSONObject();

    @Mock
    private WilmaHttpClient client;

    private SequenceConfiguration sequenceConfiguration;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);

        WilmaMockConfig config = createMockConfig();
        sequenceConfiguration = new SequenceConfiguration(config, client);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConfigIsMissing() {
        new SequenceConfiguration(null);
    }

    @Test
    public void shouldReturnEmptyJSONObjectIfClientReturnsOptionalAbsent() {
        when(client.sendGetterRequest(SEQUENCES_STATUS_URL)).thenReturn(Optional.<String>absent());

        JSONObject result = sequenceConfiguration.getSequencesLiveInformation();

        assertTrue(EMPTY_JSON_OBJECT.similar(result));
        assertEquals(EMPTY_JSON_OBJECT.length(), result.length());
        verify(client, never()).sendSetterRequest(anyString());
    }

    @Test
    public void shouldReturnJSONObjectWithProperValue() {
        when(client.sendGetterRequest(SEQUENCES_STATUS_URL)).thenReturn(Optional.of(JSON_STRING));

        JSONObject result = sequenceConfiguration.getSequencesLiveInformation();

        assertTrue(new JSONObject(JSON_STRING).similar(result), "The two JSON objects should be similar.");
        verify(client, never()).sendSetterRequest(anyString());
    }

    private WilmaMockConfig createMockConfig() {
        return WilmaMockConfig.getBuilder()
                .withHost(HOST)
                .withPort(PORT)
                .build();
    }

}
