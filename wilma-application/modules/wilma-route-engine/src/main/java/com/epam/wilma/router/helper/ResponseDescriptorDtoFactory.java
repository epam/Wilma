package com.epam.wilma.router.helper;
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

import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.response.MimeType;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.response.template.Template;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateType;
import com.epam.wilma.router.domain.ResponseDescriptorDTO;

/**
 * Factory for creating new instances of {@link ResponseDescriptorDTO}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class ResponseDescriptorDtoFactory {

    private static final String ERROR_MESSAGE_404_TEXT = "The requested URL was not found on this WILMA server.";
    private static final String ERROR_MESSAGE_404_HTML = "<html><head>\n<title>404 Not Found</title>\n</head><body>\n<h1>Not Found</h1>\n"
            + "<p>The requested URL was not found on this WILMA server.</p>\n</body></html>";
    private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String ERROR_CODE = "500";
    private static final String ERROR_RESPONSE_TEMPLATE = "errorResponse";
    private static final String ACCEPT = "Accept";

    /**
     * Creates a new instance of {@link ResponseDescriptorDTO}.
     * @param requestBody the request body that will be passed to the new instance
     * @param dialogDescriptor the name of the {@link DialogDescriptor} containing the {@link ResponseDescriptor}.
     * @return the new instance
     */
    public ResponseDescriptorDTO createResponseDescriptorDTO(final String requestBody, final DialogDescriptor dialogDescriptor) {
        ResponseDescriptor responseDescriptor = dialogDescriptor.getResponseDescriptor();
        return new ResponseDescriptorDTO(responseDescriptor, dialogDescriptor.getAttributes().getName(), requestBody);
    }

    /**
     * Creates a {@link ResponseDescriptorDTO} that carries an error message.
     * @param dialogDescriptor the name of the {@link DialogDescriptor} containing the {@link ResponseDescriptor}.
     * @param requestBody the request body that will be passed to the new instance
     * @param templateResource the exception that will be set to the {@link ResponseDescriptor}'s template
     * @return the new instance
     */
    public ResponseDescriptorDTO createResponseDescriptorDTOWithError(final DialogDescriptor dialogDescriptor, final String requestBody,
            final byte[] templateResource) {
        ResponseDescriptorDTO responseDescriptorDTO;
        Template template = new Template(ERROR_RESPONSE_TEMPLATE, TemplateType.TEXT, templateResource);
        ResponseDescriptorAttributes attributes = new ResponseDescriptorAttributes.Builder().delay(0).code(ERROR_CODE)
                .mimeType(MimeType.TEXT.getOfficialMimeType()).template(template).build();
        ResponseDescriptor responseDescriptor = new ResponseDescriptor(attributes, null);
        responseDescriptorDTO = createResponseDescriptorDTO(requestBody, dialogDescriptor, responseDescriptor);
        return responseDescriptorDTO;
    }

    private ResponseDescriptorDTO createResponseDescriptorDTO(final String requestBody, final DialogDescriptor dialogDescriptor,
            final ResponseDescriptor responseDescriptor) {
        return new ResponseDescriptorDTO(responseDescriptor, dialogDescriptor.getAttributes().getName(), requestBody);
    }

    /**
     * Modifies a response based on the request's accept header.
     * @param request the http request that's header is checked
     * @param responseDescriptorDTO it contains the response descriptor
     */
    public void modifyResponseDescriptorDTOForStubMode(final WilmaHttpRequest request, final ResponseDescriptorDTO responseDescriptorDTO) {
        String acceptHeader = request.getHeader(ACCEPT);
        if (acceptHeader != null) {
            byte[] resource = responseDescriptorDTO.getResponseDescriptor().getAttributes().getTemplate().getResource();
            if (acceptHeader.equals(MimeType.TEXT.getOfficialMimeType())) {
                resource = ERROR_MESSAGE_404_TEXT.getBytes();
            } else if (acceptHeader.equals(MimeType.HTML.getOfficialMimeType())) {
                resource = ERROR_MESSAGE_404_HTML.getBytes();
            } else if (acceptHeader.equals(MimeType.XML.getOfficialMimeType())) {
                resource = (XML_DECLARATION + ERROR_MESSAGE_404_HTML).getBytes();
            }
            responseDescriptorDTO.getResponseDescriptor().getAttributes().getTemplate().setResource(resource);
        }
    }
}
