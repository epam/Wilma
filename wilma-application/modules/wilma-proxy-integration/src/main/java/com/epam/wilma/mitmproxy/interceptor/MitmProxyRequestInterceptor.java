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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import website.magyar.mitm.proxy.RequestInterceptor;
import website.magyar.mitm.proxy.http.MitmJavaProxyHttpRequest;

/**
 * Class that is able to intercept and process every request going through the proxy, by implementing the RequestInterceptor interface.
 * It logs every request that is intercepted.
 *
 * @author Marton_Sereg
 * @author Tamas_Bihari
 */
@Component
public class MitmProxyRequestInterceptor implements RequestInterceptor {

    private final Logger logger = LoggerFactory.getLogger(MitmProxyRequestInterceptor.class);

    @Autowired
    private HttpRequestTransformer httpRequestTransformer;
    @Autowired
    private MitmProxyRequestUpdater mitmProxyRequestUpdater;
    @Autowired
    private WilmaHttpRequestProcessor wilmaHttpRequestProcessor;

    @Override
    public void process(final MitmJavaProxyHttpRequest request) {
        try {
            WilmaHttpRequest wilmaHttpRequest = httpRequestTransformer.transformRequest(request);
            wilmaHttpRequestProcessor.processRequest(wilmaHttpRequest);
            mitmProxyRequestUpdater.updateRequest(request, wilmaHttpRequest);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
