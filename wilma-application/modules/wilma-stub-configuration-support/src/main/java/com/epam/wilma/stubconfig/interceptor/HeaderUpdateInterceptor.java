package com.epam.wilma.stubconfig.interceptor;
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
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

import java.util.List;

/**
 * Interceptor for both Requests and Responses, to remove headers listed in parameters.
 * Beware that the response header removal works only if Response Message Volatility is enabled.
 *
 * @author Tamas_Kohegyi
 */
public class HeaderUpdateInterceptor implements RequestInterceptor, ResponseInterceptor {

    public static final String REMOVE_HEADER = "REMOVE";

    @Override
    public void onRequestReceive(final WilmaHttpRequest request, final ParameterList parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            List<Parameter> parameterList = parameters.getAllParameters();
            for (Parameter parameter : parameterList) {
                String name = parameter.getName();
                String value = parameter.getValue();
                if (REMOVE_HEADER.equals(name.toUpperCase())) {
                    // remove header
                    request.addHeaderRemove(value);
                } else {
                    request.addHeaderUpdate(name, value);
                }
            }
        }
    }

    @Override
    public void onResponseReceive(final WilmaHttpResponse response, final ParameterList parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            List<Parameter> parameterList = parameters.getAllParameters();
            for (Parameter parameter : parameterList) {
                String name = parameter.getName();
                String value = parameter.getValue();
                if (REMOVE_HEADER.equals(name.toUpperCase())) {
                    // remove header
                    response.addHeaderRemove(value);
                } else {
                    response.addHeaderUpdate(name, value);
                }
            }
        }
    }

}
