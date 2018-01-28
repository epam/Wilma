package com.epam.wilma.router;

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

import com.epam.wilma.common.helper.OperationMode;
import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.router.command.StubDescriptorModificationCommand;
import com.epam.wilma.router.configuration.RouteEngineConfigurationAccess;
import com.epam.wilma.router.configuration.domain.PropertyDTO;
import com.epam.wilma.router.domain.ResponseDescriptorDTO;
import com.epam.wilma.router.evaluation.StubDescriptorEvaluator;
import com.epam.wilma.router.evaluation.StubModeEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Contains route logic of request messages.
 *
 * @author Tunde_Kovacs
 * @author Tamas_Bihari
 */
@Component
public class RoutingService {

    private final Map<String, ResponseDescriptorDTO> responseDescriptorMap = new HashMap<>();
    private final Object guard = new Object();
    private Map<String, StubDescriptor> stubDescriptors = new LinkedHashMap<>();
    private OperationMode operationMode;
    @Autowired
    private StubDescriptorEvaluator stubDescriptorEvaluator;
    @Autowired
    private StubModeEvaluator stubModeEvaluator;
    @Autowired
    private RouteEngineConfigurationAccess configurationAccess;

    /**
     * Redirects requests based on their content. If a request needs to be redirected to the stub,
     * it will be added to a map that will provide response information for the stub.
     *
     * @param request the request message that is checked
     * @return true if message should be redirected to the webapp. False otherwise.
     */
    public boolean redirectRequestToStub(final WilmaHttpRequest request) {
        boolean redirect;
        ResponseDescriptorDTO responseDescriptorDTO = stubDescriptorEvaluator.findResponseDescriptor(stubDescriptors, request);
        if (responseDescriptorDTO == null) {
            responseDescriptorDTO = stubModeEvaluator.getResponseDescriptorForStubMode(request, operationMode);
        }
        redirect = responseDescriptorDTO != null;
        if (redirect) {
            //need to add this extra header, helping the stub to identify the response
            request.addHeaderUpdate(WilmaHttpEntity.WILMA_LOGGER_ID, request.getWilmaMessageId());
            saveInResponseDescriptorMap(request, responseDescriptorDTO);
        }
        return redirect;
    }

    /**
     * Reads a value matched to a key from the response descriptor map and
     * if the value is found it deletes it from the map in order to free the map from it.
     *
     * @param key search parameter
     * @return a {@link ResponseDescriptorDTO} matching the given <tt>key</tt>
     */
    public ResponseDescriptorDTO getResponseDescriptorDTOAndRemove(final String key) {
        ResponseDescriptorDTO responseDescriptor = responseDescriptorMap.get(key);
        if (responseDescriptor != null) {
            responseDescriptorMap.remove(key);
        }
        return responseDescriptor;
    }

    /**
     * Sets the new operation mode.
     *
     * @param operationMode the new operation mode coming from a UI config
     */
    public void setOperationMode(final OperationMode operationMode) {
        this.operationMode = operationMode;
    }

    public Map<String, StubDescriptor> getStubDescriptors() {
        return stubDescriptors;
    }

    public boolean isStubModeOn() {
        return operationMode == OperationMode.STUB;
    }

    private void getOperationMode() {
        if (operationMode == null) {
            PropertyDTO properties = configurationAccess.getProperties();
            operationMode = properties.getOperationMode();
        }
    }

    private void saveInResponseDescriptorMap(final WilmaHttpRequest request, final ResponseDescriptorDTO responseDescriptorDTO) {
        responseDescriptorMap.put(request.getWilmaMessageId(), responseDescriptorDTO);
    }

    /**
     * This method execute the given command. The given command is any operation which works with the stubDescriptors collection.
     *
     * @param command is the given operation
     * @throws ClassNotFoundException if problem happens
     */
    public void performModification(final StubDescriptorModificationCommand command) throws ClassNotFoundException {
        synchronized (guard) {
            stubDescriptors = command.modify(stubDescriptors);
        }
        getOperationMode();
    }
}
