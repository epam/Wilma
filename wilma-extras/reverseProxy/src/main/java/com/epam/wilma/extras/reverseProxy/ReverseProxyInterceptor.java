package com.epam.wilma.extras.reverseProxy;
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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.interceptor.RequestInterceptor;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Interceptor that implements request interceptor interfaces.
 *
 * @author tkohegyi
 */
public class ReverseProxyInterceptor extends ReverseProxyService implements RequestInterceptor {

    private final Logger logger = LoggerFactory.getLogger(ReverseProxyInterceptor.class);

    /**
     * This is the Request Interceptor implementation.
     *
     * @param wilmaHttpRequest is the incoming request
     * @param parameters       is the parameter list of the specific interceptor
     */
    @Override
    public void onRequestReceive(WilmaHttpRequest wilmaHttpRequest, ParameterList parameters) {
        Map<String, ReverseProxyInformation> localProxyMap;
        synchronized (GUARD) {
            localProxyMap = new HashMap<>(REVERSE_PROXY_INFORMATION_MAP);
        }
        for (String i : localProxyMap.keySet()) {
            ReverseProxyInformation reverseProxyInformation = localProxyMap.get(i);
            if (reverseProxyInformation.isValid()) {
                String originalTarget = reverseProxyInformation.getOriginalTarget();
                String realTarget = reverseProxyInformation.getRealTarget();
                String uriString = wilmaHttpRequest.getUri().toString();
                try {
                    //replace the original URL part with the new url part, if necessary
                    if (uriString.startsWith(originalTarget)) {
                        uriString = realTarget + uriString.substring(originalTarget.length());
                        wilmaHttpRequest.setUri(new java.net.URI(uriString));
                    }
                } catch (URISyntaxException e) {
                    logger.warn("ReverseProxy was unable to redirect request to the right direction: " + uriString);
                }
            }
        }
    }

}
