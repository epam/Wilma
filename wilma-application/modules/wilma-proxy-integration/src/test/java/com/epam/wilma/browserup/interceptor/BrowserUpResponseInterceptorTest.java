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
import com.epam.wilma.browserup.transformer.BrowserUpHttpResponseTransformer;
import com.epam.wilma.browserup.transformer.BrowserUpResponseUpdater;
import com.epam.wilma.core.processor.response.WilmaHttpResponseProcessor;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import org.littleshoot.proxy.extras.PreservedInformation;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Test class for BrowserUpResponseInterceptor.
 *
 * @author Tamas Kohegyi
 */
public class BrowserUpResponseInterceptorTest {

    @InjectMocks
    private BrowserUpResponseInterceptor underTest;
    @Mock
    private HttpResponse response;
    @Mock
    private HttpMessageContents contents;
    @Mock
    private HttpMessageInfo messageInfo;
    @Mock
    private PreservedInformation preservedInformation;
    @Mock
    private WilmaHttpResponse wilmaResponse;
    @Mock
    private BrowserUpHttpResponseTransformer responseTransformer;
    @Mock
    private WilmaHttpResponseProcessor responseHandler;
    @Mock
    private BrowserUpResponseUpdater browserMobResponseUpdater;
    @Mock
    private Logger logger;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "logger", logger);
    }

    @Test
    public void testProcessShouldCallLogger() throws ApplicationException {
        // GIVEN
        given(responseTransformer.transformResponse(response, contents, messageInfo, preservedInformation)).willReturn(wilmaResponse);
        // WHEN
        underTest.filterResponse(response, contents, messageInfo, preservedInformation);
        // THEN
        verify(responseHandler).processResponse(wilmaResponse);
        verify(browserMobResponseUpdater).updateResponse(response, contents, messageInfo, wilmaResponse);
    }

    @Test
    public void testProcessWhenApplicationExceptionIsThrownShouldLogError() throws ApplicationException {
        // GIVEN
        ApplicationException e = new ApplicationException("exception");
        given(responseTransformer.transformResponse(response, contents, messageInfo, preservedInformation)).willReturn(wilmaResponse);
        BDDMockito.willThrow(e).given(responseHandler).processResponse(wilmaResponse);
        // WHEN
        underTest.filterResponse(response, contents, messageInfo, preservedInformation);
        // THEN
        verify(logger).error("exception", e);
    }

}
