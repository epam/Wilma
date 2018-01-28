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

import com.epam.wilma.domain.stubconfig.dialog.response.MimeType;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.response.template.Template;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateType;

/**
 * Factory for creating new instances of {@link ResponseDescriptor}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class ResponseDescriptorFactory {

    private static final String DEFAULT_ERROR_MESSAGE = "The requested URL was not found on this WILMA server.";

    /**
     * Creates a new {@link ResponseDescriptor} for stub mode.
     * @return the new instance
     */
    public ResponseDescriptor createResponseDescriptorForStubMode() {
        Template template = new Template("template-for-stub-mode", TemplateType.TEXT, DEFAULT_ERROR_MESSAGE.getBytes());
        ResponseDescriptorAttributes responseAttributes = new ResponseDescriptorAttributes.Builder().delay(0).code("404")
                .mimeType(MimeType.TEXT.getOfficialMimeType()).template(template).build();
        ResponseDescriptor responseDescriptor = new ResponseDescriptor(responseAttributes, null);
        return responseDescriptor;
    }
}
