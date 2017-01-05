/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

import com.epam.wilma.domain.stubconfig.interceptor.ResponseInterceptor;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.domain.http.WilmaHttpResponse;

/**
 * Dummy Interceptor for Responses, to mark that the message has went through this interceptor. Created for test purpose only.
 */
public class DummyRespInterceptor implements ResponseInterceptor {

    private static final String HEADER_FIELD_RESP = "WILMA_TESTINTERCEPTOR_RESP";

    @Override
    public void onResponseReceive(final WilmaHttpResponse response, final ParameterList parameters) {
        response.addHeaderUpdate(HEADER_FIELD_RESP, "yes");
    }

}
