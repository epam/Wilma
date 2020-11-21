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

import com.browserup.bup.filters.RequestFilter;
import com.browserup.bup.util.HttpMessageContents;
import com.browserup.bup.util.HttpMessageInfo;
import com.epam.wilma.browserup.transformer.BrowserUpHttpRequestTransformer;
import com.epam.wilma.browserup.transformer.BrowserUpRequestUpdater;
import com.epam.wilma.core.processor.request.WilmaHttpRequestProcessor;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import org.littleshoot.proxy.extras.PreservedInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BrowserUpRequestInterceptor implements RequestFilter {
    private final Logger logger = LoggerFactory.getLogger(BrowserUpRequestInterceptor.class);

    @Autowired
    private BrowserUpHttpRequestTransformer browserUpHttpRequestTransformer;
    @Autowired
    private BrowserUpRequestUpdater browserUpRequestUpdater;
    @Autowired
    private WilmaHttpRequestProcessor wilmaHttpRequestProcessor;

    @Override
    public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo, PreservedInformation preservedInformation) {
        try {
            WilmaHttpRequest wilmaHttpRequest = browserUpHttpRequestTransformer.transformRequest(request, contents, messageInfo, preservedInformation);
            wilmaHttpRequestProcessor.processRequest(wilmaHttpRequest);
            browserUpRequestUpdater.updateRequest(request, contents, messageInfo, wilmaHttpRequest);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
        }
        return null; //never "short-circuit" the response
    }
}
