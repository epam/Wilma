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

import com.epam.wilma.domain.stubconfig.interceptor.RequestInterceptor;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.domain.http.WilmaHttpRequest;

/**
 * Dummy Interceptor for Requests, to mark that the message has went through this interceptor. Created for test purpose only.
 */
public class DummyReqInterceptor implements RequestInterceptor {

    private static final String HEADER_FIELD_REQ = "WILMA_TESTINTERCEPTOR_REQ";

    @Override
    public void onRequestReceive(final WilmaHttpRequest request, final ParameterList parameters) {
        request.addHeaderUpdate(HEADER_FIELD_REQ, "yes");
    }

}
