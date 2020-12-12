package com.epam.wilma.mitmProxy.transformer;
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

import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.proxy.configuration.MessageConfigurationAccess;
import com.epam.wilma.proxy.configuration.domain.MessagePropertyDTO;
import com.epam.wilma.proxy.helper.InputStreamConverter;
import com.epam.wilma.proxy.helper.WilmaRequestFactory;
import net.lightbody.bmp.proxy.http.BrowserMobHttpRequest;
import org.apache.http.Header;
import org.apache.http.RequestLine;
import org.apache.http.message.BasicHeader;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the <tt>HttpRequestTransformer</tt> class.
 *
 * @author Tunde_Kovacs
 */
public class HttpRequestTransformerTest {

    private static final String PREFIX = "prefix";

    private final Header[] headers = { new BasicHeader("Connection", "keep-alive") };
    @Mock
    private WilmaHttpRequest wilmaHttpRequest;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private BrowserMobHttpRequest browserMobHttpRequest;
    @Mock
    private InputStream clonedInputStream;
    @Mock
    private InputStreamConverter inputStreamConverter;
    @Mock
    private RequestLine requestLine;
    @Mock
    private MessageConfigurationAccess configurationAccess;
    @Mock
    private WilmaRequestFactory requestFactory;

    @InjectMocks
    private HttpRequestTransformer underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTransformRequestShouldAddHeaders() throws ApplicationException {
        //GIVEN
        setMocksForHeader();
        setMocksForMessageConfiguration();
        //WHEN
        WilmaHttpRequest actual = underTest.transformRequest(browserMobHttpRequest);
        //THEN
        verify(wilmaHttpRequest).addHeader(Mockito.anyString(), Mockito.anyString());
        assertEquals(actual.getRequestLine(), wilmaHttpRequest.getRequestLine());
        assertEquals(actual.getHeader("header"), wilmaHttpRequest.getHeader("header"));
        assertEquals(actual.getHeader("Connection"), wilmaHttpRequest.getHeader("Connection"));
    }

    @Test
    public void testTransformRequestShouldSetWilmaMessageId() throws ApplicationException {
        //GIVEN
        setMocksForHeader();
        setMocksForMessageConfiguration();
        given(browserMobHttpRequest.getWilmaMessageId()).willReturn(PREFIX);
        //WHEN
        WilmaHttpRequest actual = underTest.transformRequest(browserMobHttpRequest);
        //THEN
        verify(wilmaHttpRequest).setWilmaMessageId(PREFIX + "_" + PREFIX);
    }

    @Test
    public void testTransformRequestShouldSetBody() throws ApplicationException {
        //GIVEN
        setMocksForHeader();
        setMocksForMessageConfiguration();
        String body = setMocksForBody();
        //WHEN
        WilmaHttpRequest actual = underTest.transformRequest(browserMobHttpRequest);
        //THEN
        verify(wilmaHttpRequest).setBody(body);
        assertEquals(actual.getBody(), wilmaHttpRequest.getBody());
    }

    @Test
    public void testTransformRequestShouldSetInputStream() throws ApplicationException {
        //GIVEN
        setMocksForHeader();
        setMocksForMessageConfiguration();
        setMocksForBody();
        given(wilmaHttpRequest.getInputStream()).willReturn(clonedInputStream);
        //WHEN
        WilmaHttpRequest actual = underTest.transformRequest(browserMobHttpRequest);
        //THEN
        verify(wilmaHttpRequest).setInputStream(clonedInputStream);
        assertEquals(actual.getInputStream(), clonedInputStream);
    }

    private void setMocksForHeader() {
        given(requestFactory.createNewWilmaHttpRequest()).willReturn(wilmaHttpRequest);
        given(browserMobHttpRequest.getMethod().getRequestLine()).willReturn(requestLine);
        given(browserMobHttpRequest.getMethod().getAllHeaders()).willReturn(headers);
    }

    private String setMocksForBody() throws ApplicationException {
        String body = "body";
        given(browserMobHttpRequest.getPlayGround()).willReturn(clonedInputStream);
        given(inputStreamConverter.getStringFromStream(clonedInputStream)).willReturn(body);
        return body;
    }

    private void setMocksForMessageConfiguration() {
        String instancePrefix = PREFIX;
        MessagePropertyDTO propertiesDTO = new MessagePropertyDTO(instancePrefix);
        given(configurationAccess.getProperties()).willReturn(propertiesDTO);
    }
}