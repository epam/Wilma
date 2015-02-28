package com.epam.wilma.browsermob.interceptor;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import com.epam.wilma.browsermob.transformer.BrowserMobResponseUpdater;
import net.lightbody.bmp.proxy.http.BrowserMobHttpResponse;
import net.lightbody.bmp.proxy.http.ResponseInterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.browsermob.transformer.HttpResponseTransformer;
import com.epam.wilma.core.processor.response.WilmaHttpResponseProcessor;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpResponse;

/**
 * Intercepts and processes every response going through the proxy,
 * by implementing the <tt>ResponseInterceptor</tt> interface.
 * It logs every response that is intercepted.
 */
@Component
public class BrowserMobResponseInterceptor implements ResponseInterceptor {

    private final Logger logger = LoggerFactory.getLogger(BrowserMobResponseInterceptor.class);
    @Autowired
    private HttpResponseTransformer responseTransformer;
    @Autowired
    private BrowserMobResponseUpdater browserMobResponseUpdater;
    @Autowired
    private WilmaHttpResponseProcessor responseProcessor;

    @Override
    public void process(final BrowserMobHttpResponse response) {
        try {
            WilmaHttpResponse wilmaResponse = responseTransformer.transformResponse(response);
            responseProcessor.processResponse(wilmaResponse);
            browserMobResponseUpdater.updateResponse(response, wilmaResponse);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
