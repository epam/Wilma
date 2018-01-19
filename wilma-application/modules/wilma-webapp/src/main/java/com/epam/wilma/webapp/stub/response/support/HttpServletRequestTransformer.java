package com.epam.wilma.webapp.stub.response.support;
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
import com.epam.wilma.router.domain.ResponseDescriptorDTO;
import com.epam.wilma.router.helper.WilmaHttpRequestCloner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Transforms a {@link HttpServletRequest} to the appropriate object.
 * @author Tamas_Bihari
 *
 */
@Component
public class HttpServletRequestTransformer {

    @Autowired
    private WilmaHttpRequestCloner requestCloner;

    /**
     * Transforms a {@link HttpServletRequest} to {@link WilmaHttpRequest} using a {@link ResponseDescriptorDTO} to get
     * previously uncompressed request's components.
     * param wilmaLoggerId is the internal id of the message
     * @param wilmaLoggerId is the internal ID of the message
     * @param request is the {@link HttpServletRequest}, what will be transformed
     * @param responseDescriptorDTO contains the previously uncompressed request body
     * @return with the transformation result as {@link WilmaHttpRequest}
     */
    public WilmaHttpRequest transformToWilmaHttpRequest(final String wilmaLoggerId, final HttpServletRequest request, final ResponseDescriptorDTO responseDescriptorDTO) {
        WilmaHttpRequest wilmaRequest = new WilmaHttpRequest();
        copyServletHeadersIntoWilmaRequest(request, wilmaRequest);
        copyUnCompressedBodyIntoWilmaRequest(responseDescriptorDTO, wilmaRequest);
        wilmaRequest.setWilmaMessageId(wilmaLoggerId);
        return requestCloner.cloneRequest(wilmaRequest);
    }

    private void copyUnCompressedBodyIntoWilmaRequest(final ResponseDescriptorDTO responseDescriptorDTO, final WilmaHttpRequest wilmaRequest) {
        wilmaRequest.setBody(responseDescriptorDTO.getRequestBody());
    }

    private void copyServletHeadersIntoWilmaRequest(final HttpServletRequest request, final WilmaHttpRequest wilmaRequest) {
        Enumeration<?> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = (String) headerNames.nextElement();
            String header = request.getHeader(headerName);
            wilmaRequest.addHeader(headerName, header);
        }
    }
}
