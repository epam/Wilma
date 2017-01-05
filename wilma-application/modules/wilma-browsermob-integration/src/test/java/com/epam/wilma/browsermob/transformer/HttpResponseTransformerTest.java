package com.epam.wilma.browsermob.transformer;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

import com.epam.wilma.browsermob.configuration.MessageConfigurationAccess;
import com.epam.wilma.browsermob.configuration.domain.MessagePropertyDTO;
import com.epam.wilma.browsermob.transformer.helper.WilmaResponseFactory;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import net.lightbody.bmp.proxy.http.BrowserMobHttpResponse;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the <tt>HttpResponseTransformer</tt> class.
 * @author Tunde_Kovacs
 *
 */
public class HttpResponseTransformerTest {

    private static final String RESPONSE_BODY = "response";
    private static final String CONTENT_TYPE = "application/xml";
    private static final String PREFIX = "prefix";
    private Header[] responseHeaders;
    private Header[] requestHeaders;

    @Mock
    private WilmaHttpResponse response;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private BrowserMobHttpResponse browserMobHttpResponse;
    @Mock
    private MessageConfigurationAccess configurationAccess;
    @Mock
    private WilmaResponseFactory responseFactory;

    @InjectMocks
    private HttpResponseTransformer underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(responseFactory.createNewWilmaHttpResponse(false)).willReturn(response);
        requestHeaders = new Header[1];
        responseHeaders = new Header[1];
        requestHeaders[0] = new BasicHeader("reqName", "reqValue");
        responseHeaders[0] = new BasicHeader("respName", "respValue");
    }

    @Test
    public void testTransformShouldSetRequestHeader() {
        //GIVEN
        setMocksForMessageContent();
        setMocksForMessageConfiguration();
        //WHEN
        underTest.transformResponse(browserMobHttpResponse);
        //THEN
        verify(response).addRequestHeader("reqName", "reqValue");
    }

    @Test
    public void testTransformShouldSetResponseHeader() {
        setMocksForMessageContent();
        setMocksForMessageConfiguration();
        //WHEN
        underTest.transformResponse(browserMobHttpResponse);
        //THEN
        verify(response).addHeader("respName", "respValue");
    }

    @Test
    public void testTransformShouldSetResponseWilmaMessageId() {
        setMocksForMessageContent();
        setMocksForMessageConfiguration();
        given(browserMobHttpResponse.getEntry().getWilmaEntryId()).willReturn(PREFIX);
        //WHEN
        underTest.transformResponse(browserMobHttpResponse);
        //THEN
        verify(response).setWilmaMessageId(PREFIX + "_" + PREFIX);
    }

    @Test
    public void testTransformShouldNotSetResponseHeaderWhenRawResponseIsNull() {
        setMocksForMessageContent();
        given(browserMobHttpResponse.getRawResponse()).willReturn(null);
        setMocksForMessageConfiguration();
        //WHEN
        underTest.transformResponse(browserMobHttpResponse);
        //THEN
        verify(response, never()).addHeader("respName", "respValue");
    }

    @Test
    public void testTransformShouldSetBody() {
        setMocksForMessageContent();
        setMocksForMessageConfiguration();
        //WHEN
        underTest.transformResponse(browserMobHttpResponse);
        //THEN
        verify(response).setBody(RESPONSE_BODY);
    }

    @Test
    public void testTransformShouldSetContentType() {
        setMocksForMessageContent();
        setMocksForMessageConfiguration();
        given(browserMobHttpResponse.getContentType()).willReturn(CONTENT_TYPE);
        //WHEN
        underTest.transformResponse(browserMobHttpResponse);
        //THEN
        verify(response).setContentType(CONTENT_TYPE);
    }

    @Test
    public void testTransformShouldSetStatusCode() {
        setMocksForMessageContent();
        setMocksForMessageConfiguration();
        //WHEN
        underTest.transformResponse(browserMobHttpResponse);
        //THEN
        verify(response).setStatusCode(200);
    }

    private void setMocksForMessageConfiguration() {
        String instancePrefix = PREFIX;
        MessagePropertyDTO propertiesDTO = new MessagePropertyDTO(instancePrefix);
        given(configurationAccess.getProperties()).willReturn(propertiesDTO);
    }

    private void setMocksForMessageContent() {
        given(browserMobHttpResponse.getRawResponse().getAllHeaders()).willReturn(responseHeaders);
        given(browserMobHttpResponse.getRequestHeaders()).willReturn(requestHeaders);
        given(browserMobHttpResponse.getBody()).willReturn(RESPONSE_BODY);
        given(browserMobHttpResponse.getStatus()).willReturn(200);
    }

}
