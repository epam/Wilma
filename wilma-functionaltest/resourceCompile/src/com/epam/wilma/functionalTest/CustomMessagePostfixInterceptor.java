package com.epam.wilma.functionalTest;
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
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.domain.stubconfig.interceptor.RequestInterceptor;
import com.epam.wilma.domain.stubconfig.interceptor.ResponseInterceptor;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Interceptor for both Requests and Responses, to set the custom postfix parameter of the message.
 */
public class CustomMessagePostfixInterceptor implements RequestInterceptor, ResponseInterceptor {

    @Override
    public void onRequestReceive(final WilmaHttpRequest request, final ParameterList parameters) {
        String requestCustomPostfix = "APOST";
        request.setWilmaMessageCustomPostfix(requestCustomPostfix);
    }

    @Override
    public void onResponseReceive(final WilmaHttpResponse response, final ParameterList parameters) {
        String responseCustomPostfix = "BPOST";
        response.setWilmaMessageCustomPostfix(responseCustomPostfix);
    }

}
