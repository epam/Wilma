package com.epam.wilma.browsermob.interceptor;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

import java.io.InputStream;

import net.lightbody.bmp.proxy.http.BrowserMobHttpRequest;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.browsermob.transformer.BrowserMobRequestUpdater;
import com.epam.wilma.browsermob.transformer.HttpRequestTransformer;
import com.epam.wilma.core.processor.request.WilmaHttpRequestProcessor;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpRequest;

/**
 * Test class for LoggingRequestInterceptor.
 * @author Marton_Sereg
 * @author Tamas_Bihari
 * @author Tunde_Kovacs
 */
public class BrowserMobRequestInterceptorTest {
    private static final String EMPTY_STRING = "";

    @Mock
    private HttpRequestTransformer httpRequestTransformer;
    @Mock
    private BrowserMobHttpRequest bmRequest;
    @Mock
    private WilmaHttpRequest wilmaRequest;
    @Mock
    private Logger logger;
    @Mock
    private InputStream inputStream;
    @Mock
    private WilmaHttpRequestProcessor wilmaHttpRequestHandler;
    @Mock
    private BrowserMobRequestUpdater browserMobRequestUpdater;

    @InjectMocks
    private BrowserMobRequestInterceptor underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "logger", logger);
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
