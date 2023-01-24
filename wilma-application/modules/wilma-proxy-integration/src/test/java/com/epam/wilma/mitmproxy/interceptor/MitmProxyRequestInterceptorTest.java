package com.epam.wilma.mitmproxy.interceptor;
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

import com.epam.wilma.core.processor.request.WilmaHttpRequestProcessor;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.mitmproxy.transformer.HttpRequestTransformer;
import com.epam.wilma.mitmproxy.transformer.MitmProxyRequestUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import website.magyar.mitm.proxy.http.MitmJavaProxyHttpRequest;
import org.slf4j.Logger;

import java.io.InputStream;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

/**
 * Test class for LoggingRequestInterceptor.
 *
 * @author Marton_Sereg
 * @author Tamas_Bihari
 * @author Tunde_Kovacs
 * @author Tamas_Kohegyi
 */
public class MitmProxyRequestInterceptorTest {
    private static final String EMPTY_STRING = "";

    @Mock
    private HttpRequestTransformer httpRequestTransformer;
    @Mock
    private MitmJavaProxyHttpRequest bmRequest;
    @Mock
    private WilmaHttpRequest wilmaRequest;
    @Mock
    private Logger logger;
    @Mock
    private InputStream inputStream;
    @Mock
    private WilmaHttpRequestProcessor wilmaHttpRequestHandler;
    @Mock
    private MitmProxyRequestUpdater browserMobRequestUpdater;

    @InjectMocks
    private MitmProxyRequestInterceptor underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(underTest, "logger", logger);
    }

    @Test
    public void testProcessShouldLogErrorWhenRequestTransformerThrowsApplicationException() throws ApplicationException {
        // GIVEN
        ApplicationException e = new ApplicationException(EMPTY_STRING);
        given(httpRequestTransformer.transformRequest(bmRequest)).willThrow(e);
        // WHEN
        underTest.process(bmRequest);
        // THEN
        verify(logger).error(EMPTY_STRING, e);
    }

    @Test
    public void testProcessShouldLogErrorWhenHandleRequestThrowsApplicationException() throws ApplicationException {
        // GIVEN
        ApplicationException e = new ApplicationException(EMPTY_STRING);
        given(httpRequestTransformer.transformRequest(bmRequest)).willReturn(wilmaRequest);
        willThrow(e).given(wilmaHttpRequestHandler).processRequest(wilmaRequest);
        // WHEN
        underTest.process(bmRequest);
        // THEN
        verify(logger).error(EMPTY_STRING, e);
    }

    @Test
    public void testProcessShouldReturnProperly() throws ApplicationException {
        // GIVEN
        given(httpRequestTransformer.transformRequest(bmRequest)).willReturn(wilmaRequest);
        // WHEN
        underTest.process(bmRequest);
        // THEN
        verify(wilmaHttpRequestHandler).processRequest(wilmaRequest);
        verify(browserMobRequestUpdater).updateRequest(bmRequest, wilmaRequest);
    }
}
