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
 * Interceptor for both Requests and Responses, to alter the message content and headers.
 */
public class MessageAlterInterceptor implements RequestInterceptor, ResponseInterceptor {

    @Override
    public void onRequestReceive(final WilmaHttpRequest request, final ParameterList parameters) {
        String newBody = request.getBody();
        if (request.getHeader("AlterMessage") != null) {
            newBody = newBody.replaceAll("L", "A");
            request.setNewBody(newBody.getBytes(), newBody);
            request.addHeaderUpdate("Req-Altered-By-Wilma", "true");
        }
    }

    @Override
    public void onResponseReceive(final WilmaHttpResponse response, final ParameterList parameters) {
        String newBody = response.getBody();
        if (response.getRequestHeader("AlterMessage") != null) {
            newBody = newBody.replaceAll("B", "A");
            response.setNewBody(newBody.getBytes(), newBody);
            response.addHeaderUpdate("Resp-Altered-By-Wilma", "true");
        }
    }

}
