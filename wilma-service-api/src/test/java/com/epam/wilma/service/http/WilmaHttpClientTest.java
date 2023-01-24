package com.epam.wilma.service.http;

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

import com.google.common.base.Optional;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link WilmaHttpClient}.
 *
 * @author Tamas_Pinter
 */
public class WilmaHttpClientTest {

    private static final String TEST_URL = "testurl";
    private static final String RESPONSE = "response";
    private static final File TEST_FILE = mock(File.class);

    private WilmaHttpClient wilmaHttpClient;

    @BeforeEach
    public void init() {
        wilmaHttpClient = new WilmaHttpClient();

        when(TEST_FILE.isFile()).thenReturn(true);
        when(TEST_FILE.canRead()).thenReturn(true);
    }

    @Test
    public void shouldReturnOptionalAbsentForGetterMethodIfHttpClientThrowsHttpException() throws Exception {
        HttpClient httpClient = mock(HttpClient.class);
        when(httpClient.executeMethod(any(GetMethod.class))).thenThrow(new HttpException());

        wilmaHttpClient.setHttpClient(httpClient);
        Optional<String> result = wilmaHttpClient.sendGetterRequest(TEST_URL);

        assertEquals(result, Optional.<String>absent());
    }

    @Test
    public void shouldReturnOptionalAbsentForGetterMethodIfHttpClientThrowsIOException() throws Exception {
        HttpClient httpClient = mock(HttpClient.class);
        when(httpClient.executeMethod(any(GetMethod.class))).thenThrow(new IOException());

        wilmaHttpClient.setHttpClient(httpClient);
        Optional<String> result = wilmaHttpClient.sendGetterRequest(TEST_URL);

        assertEquals(result, Optional.<String>absent());
    }

    @Test
    public void shouldReturnOptionalOfResponseForGetterMethod() throws Exception {
        HttpClient httpClient = new MockHttpClient(200, RESPONSE);
        wilmaHttpClient.setHttpClient(httpClient);

        Optional<String> result = wilmaHttpClient.sendGetterRequest(TEST_URL);

        assertEquals(result, Optional.<String>of(RESPONSE));
    }

    @Test
    public void shouldReturnFalseForSetterMethodIfHttpClientThrowsHttpException() throws Exception {
        HttpClient httpClient = mock(HttpClient.class);
        when(httpClient.executeMethod(any(GetMethod.class))).thenThrow(new HttpException());

        wilmaHttpClient.setHttpClient(httpClient);
        boolean result = wilmaHttpClient.sendSetterRequest(TEST_URL);

        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseForSetterMethodIfHttpClientThrowsIOException() throws Exception {
        HttpClient httpClient = mock(HttpClient.class);
        when(httpClient.executeMethod(any(GetMethod.class))).thenThrow(new IOException());

        wilmaHttpClient.setHttpClient(httpClient);
        boolean result = wilmaHttpClient.sendSetterRequest(TEST_URL);

        assertFalse(result);
    }

    @Test
    public void shouldReturnTrueForSetterMethod() throws Exception {
        HttpClient httpClient = new MockHttpClient(200, RESPONSE);
        wilmaHttpClient.setHttpClient(httpClient);

        boolean result = wilmaHttpClient.sendSetterRequest(TEST_URL);

        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseForFileUploadMethodIfHttpClientThrowsHttpException() throws Exception {
        HttpClient httpClient = mock(HttpClient.class);
        when(httpClient.executeMethod(any(PostMethod.class))).thenThrow(new HttpException());

        wilmaHttpClient.setHttpClient(httpClient);
        boolean result = wilmaHttpClient.uploadFile(TEST_URL, TEST_FILE);

        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseForFileUploadMethodIfHttpClientThrowsIOException() throws Exception {
        HttpClient httpClient = mock(HttpClient.class);
        when(httpClient.executeMethod(any(PostMethod.class))).thenThrow(new IOException());

        wilmaHttpClient.setHttpClient(httpClient);
        boolean result = wilmaHttpClient.uploadFile(TEST_URL, TEST_FILE);

        assertFalse(result);
    }

    @Test
    public void shouldReturnTrueForFileUploadMethod() {
        HttpClient httpClient = new MockHttpClient(200, RESPONSE);
        wilmaHttpClient.setHttpClient(httpClient);

        boolean result = wilmaHttpClient.uploadFile(TEST_URL, TEST_FILE);

        assertTrue(result);
    }

    /**
     * Mock HttpClient that is able to control the response status and response
     * body.
     *
     * @author Tamas_Pinter
     */
    public class MockHttpClient extends HttpClient {
        private int expectedResponseStatus;
        private String expectedResponseBody;

        public MockHttpClient(int responseStatus, String responseBody) {
            this.expectedResponseStatus = responseStatus;
            this.expectedResponseBody = responseBody;
        }

        @Override
        public int executeMethod(HttpMethod method) {
            try {
                Field responseBody = HttpMethodBase.class.getDeclaredField("responseBody");
                responseBody.setAccessible(true);
                responseBody.set(method, expectedResponseBody.getBytes());
            } catch (Exception e) {
                throw new RuntimeException("Exception setting response stream", e);
            }
            return expectedResponseStatus;
        }
    }

}
