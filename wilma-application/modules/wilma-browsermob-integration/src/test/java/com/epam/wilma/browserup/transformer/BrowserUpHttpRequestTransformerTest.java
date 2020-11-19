package com.epam.wilma.browserup.transformer;
/*==========================================================================
Copyright since 2020, EPAM Systems

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
import com.epam.wilma.browsermob.configuration.MessageConfigurationAccess;
import com.epam.wilma.browsermob.configuration.domain.MessagePropertyDTO;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.proxy.helper.WilmaRequestFactory;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

/**
 * Provides unit tests for the <tt>BrowserUpHttpRequestTransformer</tt> class.
 *
 * @author Tamas_Kohegyi
 */
public class BrowserUpHttpRequestTransformerTest {

    private static final String PREFIX = "prefix";

    private HttpHeaders httpHeaders;

    @Mock
    private WilmaHttpRequest wilmaHttpRequest;
    @Mock
    private HttpRequest request;
    @Mock
    private HttpMessageContents contents;
    @Mock
    private HttpMessageInfo messageInfo;
    @Mock
    private MessageConfigurationAccess configurationAccess;
    @Mock
    private WilmaRequestFactory requestFactory;

    @InjectMocks
    private BrowserUpHttpRequestTransformer underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        setDefaultMocks();
    }

    @Test
    public void testTransformRequestShouldAddHeaders() throws ApplicationException {
        //GIVEN
        //WHEN
        WilmaHttpRequest actual = underTest.transformRequest(request, contents, messageInfo);
        //THEN
        verify(wilmaHttpRequest).addHeader(Mockito.anyString(), Mockito.anyString());
        assertEquals(actual.getRequestLine(), wilmaHttpRequest.getRequestLine());
        assertEquals(actual.getHeader("A"), wilmaHttpRequest.getHeader("A"));
    }

    @Test
    public void testTransformRequestShouldSetWilmaMessageId() throws ApplicationException {
        //GIVEN
        //WHEN
        WilmaHttpRequest actual = underTest.transformRequest(request, contents, messageInfo);
        //THEN
        verify(wilmaHttpRequest).setWilmaMessageId(anyString());
    }

    @Test
    public void testTransformRequestShouldSetBody() throws ApplicationException {
        //GIVEN
        String body = setMocksForBody();
        //WHEN
        WilmaHttpRequest actual = underTest.transformRequest(request, contents, messageInfo);
        //THEN
        verify(wilmaHttpRequest).setBody(body);
        assertEquals(actual.getBody(), wilmaHttpRequest.getBody());
    }

    private void setDefaultMocks() {
        given(request.uri()).willReturn("requestLine");
        httpHeaders = new DefaultHttpHeaders();
        httpHeaders.add("A", "B");
        given(request.headers()).willReturn(httpHeaders);
        given(requestFactory.createNewWilmaHttpRequest()).willReturn(wilmaHttpRequest);
        String instancePrefix = PREFIX;
        MessagePropertyDTO propertiesDTO = new MessagePropertyDTO(instancePrefix);
        given(configurationAccess.getProperties()).willReturn(propertiesDTO);
        setMocksForBody();
    }

    private String setMocksForBody() {
        String body = "body";
        given(contents.getBinaryContents()).willReturn(body.getBytes());
        return body;
    }

}
