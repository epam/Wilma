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

import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.stubconfig.dialog.response.MimeType;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptorAttributes;
import com.epam.wilma.router.domain.ResponseDescriptorDTO;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Writes different kind of information into the response header.
 * @author Tamas_Bihari
 *
 */
@Component
public class StubResponseHeaderConfigurer {

    /**
     * Gets the WilmaSequence(if exists) from the request headers
     * and write the necessary information into the response header.
     * @param req is the request
     * @param resp is the response in what the new information will be added
     * @param dialogDescriptorName is the name of the actually used DialogDescriptor
     */
    public void addWilmaInfoToResponseHeader(final HttpServletRequest req, final HttpServletResponse resp, final String dialogDescriptorName) {
        String wilmaSequence = req.getHeader(WilmaHttpEntity.WILMA_SEQUENCE_ID);

        if (wilmaSequence != null) {
            resp.addHeader(WilmaHttpEntity.WILMA_SEQUENCE_ID, wilmaSequence + "," + dialogDescriptorName);
        } else {
            resp.addHeader(WilmaHttpEntity.WILMA_SEQUENCE_ID, dialogDescriptorName);
        }
    }

    /**
     * Sets the response content type and status using information from {@link ResponseDescriptorDTO}.
     * @param resp is the response, which will be sent
     * @param responseDescriptorDTO contains the necessary information about response content type
     */
    public void setResponseContentTypeAndStatus(final HttpServletResponse resp, final ResponseDescriptorDTO responseDescriptorDTO) {
        ResponseDescriptorAttributes attributes = responseDescriptorDTO.getResponseDescriptor().getAttributes();
        resp.setContentType(attributes.getMimeType());
        resp.setStatus(Integer.valueOf(attributes.getCode()));
    }

    /**
     * Sets the appropriate status code and content type for stub error response.
     * @param resp will be sent as response
     */
    public void setErrorResponseContentTypeAndStatus(final HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        resp.setContentType(MimeType.TEXT.getOfficialMimeType());
    }
}
