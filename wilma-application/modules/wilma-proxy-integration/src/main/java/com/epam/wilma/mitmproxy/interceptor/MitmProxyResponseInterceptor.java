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

import com.epam.wilma.core.processor.response.WilmaHttpResponseProcessor;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.mitmproxy.transformer.HttpResponseTransformer;
import com.epam.wilma.mitmproxy.transformer.MitmProxyResponseUpdater;
import website.magyar.mitm.proxy.ResponseInterceptor;
import website.magyar.mitm.proxy.http.MitmJavaProxyHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Intercepts and processes every response going through the proxy,
 * by implementing the <tt>ResponseInterceptor</tt> interface.
 * It logs every response that is intercepted.
 */
@Component
public class MitmProxyResponseInterceptor implements ResponseInterceptor {

    private final Logger logger = LoggerFactory.getLogger(MitmProxyResponseInterceptor.class);
    @Autowired
    private HttpResponseTransformer responseTransformer;
    @Autowired
    private MitmProxyResponseUpdater mitmProxyResponseUpdater;
    @Autowired
    private WilmaHttpResponseProcessor responseProcessor;

    @Override
    public void process(final MitmJavaProxyHttpResponse response) {
        try {
            WilmaHttpResponse wilmaResponse = responseTransformer.transformResponse(response);
            responseProcessor.processResponse(wilmaResponse);
            mitmProxyResponseUpdater.updateResponse(response, wilmaResponse);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
