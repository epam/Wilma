package com.epam.wilma.browserup.transformer;
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

import com.browserup.bup.util.HttpMessageContents;
import com.browserup.bup.util.HttpMessageInfo;
import com.epam.wilma.proxy.configuration.MessageConfigurationAccess;
import com.epam.wilma.proxy.configuration.domain.MessagePropertyDTO;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.proxy.helper.WilmaResponseFactory;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.littleshoot.proxy.extras.PreservedInformation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.epam.wilma.browserup.transformer.BrowserUpHttpRequestTransformer.PROVIDED_WILMA_MSG_ID;
import static com.epam.wilma.browserup.transformer.BrowserUpHttpRequestTransformer.PROVIDED_WILMA_ORIGINAL_URI;
import static com.epam.wilma.browserup.transformer.BrowserUpHttpRequestTransformer.PROVIDED_WILMA_REMOTE_ADDRESS;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the <tt>BrowserUpHttpResponseTransformer</tt> class.
 *
 * @author Tunde_Kovacs
 */
public class BrowserUpHttpResponseTransformerTest {

    private static final String RESPONSE_BODY = "response";
    private static final String REQUEST_LINE = "request_line";
    private static final String REQUEST_PROXY_LINE = "request_proxy_line";
    private static final String CONTENT_TYPE = "application/xml";
    private static final String PREFIX = "prefix";

    private HttpHeaders httpRequestHeaders;
    private HttpHeaders httpResponseHeaders;

    @Mock
    private WilmaHttpResponse result;
    @Mock
    private HttpRequest request;
    @Mock
    private HttpResponse response;
    @Mock
    private HttpResponseStatus httpResponseStatus;
    @Mock
    private HttpMessageContents contents;
    @Mock
    private HttpMessageInfo messageInfo;
    @Mock
    private PreservedInformation preservedInformation;
    @Mock
    private MessageConfigurationAccess configurationAccess;
    @Mock
    private WilmaResponseFactory responseFactory;

    @InjectMocks
    private BrowserUpHttpResponseTransformer underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(responseFactory.createNewWilmaHttpResponse(anyBoolean())).willReturn(result);
        httpRequestHeaders = new DefaultHttpHeaders();
        httpRequestHeaders.add("A", "B");
        httpResponseHeaders = new DefaultHttpHeaders();
        httpResponseHeaders.add("C", "D");
        given(response.headers()).willReturn(httpResponseHeaders);
        given(request.headers()).willReturn(httpRequestHeaders);
        preservedInformation = new PreservedInformation();
        preservedInformation.informationMap.put(PROVIDED_WILMA_MSG_ID, PROVIDED_WILMA_MSG_ID);
        preservedInformation.informationMap.put(PROVIDED_WILMA_ORIGINAL_URI, PROVIDED_WILMA_ORIGINAL_URI);
        preservedInformation.informationMap.put(PROVIDED_WILMA_REMOTE_ADDRESS, PROVIDED_WILMA_REMOTE_ADDRESS);
    }

    @Test
    public void testTransformShouldSetRequestHeader() {
        //GIVEN
        setMocksForMessageContent();
        setMocksForMessageConfiguration();
        //WHEN
        underTest.transformResponse(response, contents, messageInfo, preservedInformation);
        //THEN
        verify(result).addRequestHeader("A", "B");
    }

    @Test
    public void testTransformShouldSetResponseHeader() {
        setMocksForMessageContent();
        setMocksForMessageConfiguration();
        //WHEN
        underTest.transformResponse(response, contents, messageInfo, preservedInformation);
        //THEN
        verify(result).addHeader("C", "D");
    }

    @Test
    public void testTransformShouldSetResponseWilmaMessageId() {
        setMocksForMessageContent();
        setMocksForMessageConfiguration();
        //WHEN
        underTest.transformResponse(response, contents, messageInfo, preservedInformation);
        //THEN
        verify(result).setWilmaMessageId(PREFIX + "_" + preservedInformation.informationMap.get(PROVIDED_WILMA_MSG_ID));
    }

    @Test
    public void testTransformShouldSetBody() {
        setMocksForMessageContent();
        setMocksForMessageConfiguration();
        //WHEN
        underTest.transformResponse(response, contents, messageInfo, preservedInformation);
        //THEN
        verify(result).setBody(RESPONSE_BODY);
    }

    @Test
    public void testTransformShouldSetContentType() {
        setMocksForMessageContent();
        setMocksForMessageConfiguration();
        given(contents.getContentType()).willReturn(CONTENT_TYPE);
        //WHEN
        underTest.transformResponse(response, contents, messageInfo, preservedInformation);
        //THEN
        verify(result).setContentType(CONTENT_TYPE);
    }

    @Test
    public void testTransformShouldSetStatusCode() {
        setMocksForMessageContent();
        setMocksForMessageConfiguration();
        //WHEN
        underTest.transformResponse(response, contents, messageInfo, preservedInformation);
        //THEN
        verify(result).setStatusCode(200);
    }

    @Test
    public void testTransformShouldSetRequestLineAndProxyRequestLine() {
        setMocksForMessageContent();
        setMocksForMessageConfiguration();
        given(request.uri()).willReturn(REQUEST_LINE);
        //WHEN
        underTest.transformResponse(response, contents, messageInfo, preservedInformation);
        //THEN
        verify(result).setRequestLine(REQUEST_LINE);
        verify(result).setProxyRequestLine(PROVIDED_WILMA_ORIGINAL_URI);
    }


    private void setMocksForMessageConfiguration() {
        String instancePrefix = PREFIX;
        MessagePropertyDTO propertiesDTO = new MessagePropertyDTO(instancePrefix);
        given(configurationAccess.getProperties()).willReturn(propertiesDTO);
    }

    private void setMocksForMessageContent() {
        given(response.headers()).willReturn(httpResponseHeaders);
        given(messageInfo.getOriginalRequest()).willReturn(request);
        given(request.headers()).willReturn(httpRequestHeaders);
        given(contents.getBinaryContents()).willReturn(RESPONSE_BODY.getBytes());
        given(response.status()).willReturn(httpResponseStatus);
        given(httpResponseStatus.code()).willReturn(200);
    }

}
