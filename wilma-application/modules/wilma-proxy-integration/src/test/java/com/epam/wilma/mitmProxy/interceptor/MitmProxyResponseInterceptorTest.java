package com.epam.wilma.mitmProxy.interceptor;
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

import com.epam.wilma.core.processor.response.WilmaHttpResponseProcessor;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.mitmProxy.transformer.HttpResponseTransformer;
import com.epam.wilma.mitmProxy.transformer.MitmProxyResponseUpdater;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.rockhill.mitm.proxy.http.MitmJavaProxyHttpResponse;
import org.slf4j.Logger;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Test class for LoggingResponseInterceptor.
 *
 * @author Marton_Sereg, Tamas_Kohegyi
 */
public class MitmProxyResponseInterceptorTest {

    @InjectMocks
    private MitmProxyResponseInterceptor underTest;
    @Mock
    private MitmJavaProxyHttpResponse response;
    @Mock
    private WilmaHttpResponse wilmaResponse;
    @Mock
    private HttpResponseTransformer responseTransformer;
    @Mock
    private WilmaHttpResponseProcessor responseHandler;
    @Mock
    private MitmProxyResponseUpdater browserMobResponseUpdater;
    @Mock
    private Logger logger;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "logger", logger);
    }

    @Test
    public void testProcessShouldCallLogger() throws ApplicationException {
        // GIVEN
        given(responseTransformer.transformResponse(response)).willReturn(wilmaResponse);
        // WHEN
        underTest.process(response);
        // THEN
        verify(responseHandler).processResponse(wilmaResponse);
        verify(browserMobResponseUpdater).updateResponse(response, wilmaResponse);
    }

    @Test
    public void testProcessWhenApplicationExceptionIsThrownShouldLogError() throws ApplicationException {
        // GIVEN
        ApplicationException e = new ApplicationException("exception");
        given(responseTransformer.transformResponse(response)).willReturn(wilmaResponse);
        BDDMockito.willThrow(e).given(responseHandler).processResponse(wilmaResponse);
        // WHEN
        underTest.process(response);
        // THEN
        verify(logger).error("exception", e);
    }

}
