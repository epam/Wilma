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

import net.lightbody.bmp.proxy.http.BrowserMobHttpRequest;
import net.lightbody.bmp.proxy.http.RequestInterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.browsermob.transformer.BrowserMobRequestUpdater;
import com.epam.wilma.browsermob.transformer.HttpRequestTransformer;
import com.epam.wilma.core.processor.request.WilmaHttpRequestProcessor;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpRequest;

/**
 * Class that is able to intercept and process every request going through the proxy, by implementing the RequestInterceptor interface.
 * It logs every request that is intercepted.
 * @author Marton_Sereg
 * @author Tamas_Bihari
 *
 */
@Component
public class BrowserMobRequestInterceptor implements RequestInterceptor {

    private final Logger logger = LoggerFactory.getLogger(BrowserMobRequestInterceptor.class);

    @Autowired
    private HttpRequestTransformer httpRequestTransformer;
    @Autowired
    private BrowserMobRequestUpdater browserMobRequestUpdater;
    @Autowired
    private WilmaHttpRequestProcessor wilmaHttpRequestProcessor;

    @Override
    public void process(final BrowserMobHttpRequest request) {
        try {
            WilmaHttpRequest wilmaHttpRequest = httpRequestTransformer.transformRequest(request);
            wilmaHttpRequestProcessor.processRequest(wilmaHttpRequest);
            browserMobRequestUpdater.updateRequest(request, wilmaHttpRequest);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
