package com.epam.wilma.browserup.interceptor;
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
import com.epam.wilma.browserup.transformer.BrowserUpRequestUpdater;
import com.epam.wilma.browserup.transformer.BrowserUpHttpRequestTransformer;
import com.epam.wilma.core.processor.request.WilmaHttpRequestProcessor;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

/**
 * Test class for BrowserUpRequestInterceptor.
 *
 * @author Tamas Kohegyi
 */
public class BrowserUpRequestInterceptorTest {
    private static final String EMPTY_STRING = "";
    @Mock
    HttpRequest request;
    @Mock
    HttpMessageContents contents;
    @Mock
    HttpMessageInfo messageInfo;
    @Mock
    private BrowserUpHttpRequestTransformer browserUpHttpRequestTransformer;
    @Mock
    private WilmaHttpRequest wilmaRequest;
    @Mock
    private Logger logger;
    @Mock
    private WilmaHttpRequestProcessor wilmaHttpRequestHandler;
    @Mock
    private BrowserUpRequestUpdater browserMobRequestUpdater;

    @InjectMocks
    private BrowserUpRequestInterceptor underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "logger", logger);
    }

    //@Test ignore this now
    public void testProcessShouldLogErrorWhenRequestTransformerThrowsApplicationException() throws ApplicationException {
        // GIVEN
        ApplicationException e = new ApplicationException(EMPTY_STRING);
        given(browserUpHttpRequestTransformer.transformRequest(request, contents, messageInfo)).willThrow(e);
        // WHEN
        underTest.filterRequest(request, contents, messageInfo);
        // THEN
        verify(logger).error(EMPTY_STRING, e);
    }

    @Test
    public void testProcessShouldLogErrorWhenHandleRequestThrowsApplicationException() throws ApplicationException {
        // GIVEN
        ApplicationException e = new ApplicationException(EMPTY_STRING);
        given(browserUpHttpRequestTransformer.transformRequest(request, contents, messageInfo)).willReturn(wilmaRequest);
        willThrow(e).given(wilmaHttpRequestHandler).processRequest(wilmaRequest);
        // WHEN
        underTest.filterRequest(request, contents, messageInfo);
        // THEN
        verify(logger).error(EMPTY_STRING, e);
    }

    @Test
    public void testProcessShouldReturnProperly() throws ApplicationException {
        // GIVEN
        given(browserUpHttpRequestTransformer.transformRequest(request, contents, messageInfo)).willReturn(wilmaRequest);
        // WHEN
        underTest.filterRequest(request, contents, messageInfo);
        // THEN
        verify(wilmaHttpRequestHandler).processRequest(wilmaRequest);
        verify(browserMobRequestUpdater).updateRequest(request, contents, messageInfo, wilmaRequest);
    }
}
