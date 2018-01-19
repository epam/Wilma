package com.epam.wilma.router.evaluation;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.OperationMode;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.router.domain.ResponseDescriptorDTO;
import com.epam.wilma.router.helper.DialogDescriptorFactory;
import com.epam.wilma.router.helper.ResponseDescriptorDtoFactory;

/**
 * This class ensures response descriptor of stub mode.
 * @author Tibor_Kovacs
 *
 */
@Component
public class StubModeEvaluator {

    private static final String DIALOG_DESCRIPTOR_NAME_FOR_STUB_MODE = "dialog-descriptor-for-stub-mode";

    private DialogDescriptor dialogDescriptorForStub;
    @Autowired
    private DialogDescriptorFactory dialogDescriptorFactory;
    @Autowired
    private ResponseDescriptorDtoFactory responseDescriptorDtoFactory;

    /**
     * This method creates a response descriptor for stub mode if the operation mode is STUB. If it was, then it modifies the response based on the request's
     * accept header.
     * @param request the request that carries the accept header
     * contains the response
     * @param operationMode the operation mode of the application
     * @return with the responseDescriptor for default dialogDescriptor for stub mode.
     */
    public ResponseDescriptorDTO getResponseDescriptorForStubMode(final WilmaHttpRequest request, final OperationMode operationMode) {
        ResponseDescriptorDTO responseDescriptorDTOForStub = null;
        if (operationMode == OperationMode.STUB) {
            if (dialogDescriptorForStub == null) {
                dialogDescriptorForStub = dialogDescriptorFactory.createDialogDescriptorForStubMode(DIALOG_DESCRIPTOR_NAME_FOR_STUB_MODE);
            }
            responseDescriptorDTOForStub = responseDescriptorDtoFactory.createResponseDescriptorDTO(request.getBody(), dialogDescriptorForStub);
            responseDescriptorDtoFactory.modifyResponseDescriptorDTOForStubMode(request, responseDescriptorDTOForStub);
        }
        return responseDescriptorDTOForStub;
    }
}
