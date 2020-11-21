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

import com.browserup.bup.filters.ResponseFilter;
import com.browserup.bup.util.HttpMessageContents;
import com.browserup.bup.util.HttpMessageInfo;
import com.epam.wilma.browserup.transformer.BrowserUpHttpResponseTransformer;
import com.epam.wilma.browserup.transformer.BrowserUpResponseUpdater;
import com.epam.wilma.core.processor.response.WilmaHttpResponseProcessor;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import org.littleshoot.proxy.extras.PreservedInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BrowserUpResponseInterceptor implements ResponseFilter {
    private final Logger logger = LoggerFactory.getLogger(BrowserUpResponseInterceptor.class);
    @Autowired
    private BrowserUpHttpResponseTransformer responseTransformer;
    @Autowired
    private BrowserUpResponseUpdater browserUpResponseUpdater;
    @Autowired
    private WilmaHttpResponseProcessor responseProcessor;

    @Override
    public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo, PreservedInformation preservedInformation) {
        try {
            WilmaHttpResponse wilmaResponse = responseTransformer.transformResponse(response, contents, messageInfo, preservedInformation);
            responseProcessor.processResponse(wilmaResponse);
            browserUpResponseUpdater.updateResponse(response, contents, messageInfo, wilmaResponse);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
        }
    }


}
