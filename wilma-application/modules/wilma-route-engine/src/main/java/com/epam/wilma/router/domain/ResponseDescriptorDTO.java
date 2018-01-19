package com.epam.wilma.router.domain;
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

import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptor;

/**
 * Data transfer object for response descriptors.
 * @author Tunde_Kovacs
 *
 */
public class ResponseDescriptorDTO {

    private final ResponseDescriptor responseDescriptor;
    private final String parentName;
    private final String requestBody;

    /**
     * Constructs a response descriptor DTO.
     * @param responseDescriptor the {@link ResponseDescriptor}
     * @param parentName the parent dialog descriptor of the <tt>responseDescriptor</tt>
     * @param requestBody the body of the request that has associated a response to it
     */
    public ResponseDescriptorDTO(final ResponseDescriptor responseDescriptor, final String parentName, final String requestBody) {
        super();
        this.responseDescriptor = responseDescriptor;
        this.parentName = parentName;
        this.requestBody = requestBody;
    }

    public ResponseDescriptor getResponseDescriptor() {
        return responseDescriptor;
    }

    public String getDialogDescriptorName() {
        return parentName;
    }

    public String getRequestBody() {
        return requestBody;
    }

}
