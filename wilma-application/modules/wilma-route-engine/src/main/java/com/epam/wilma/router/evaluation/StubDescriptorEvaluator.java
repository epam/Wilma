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

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.StackTraceToStringConverter;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.domain.stubconfig.StubDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.condition.Condition;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionDescriptor;
import com.epam.wilma.router.domain.ResponseDescriptorDTO;
import com.epam.wilma.router.evaluation.helper.DialogDescriptorService;
import com.epam.wilma.router.helper.ResponseDescriptorDtoFactory;
import com.epam.wilma.router.helper.WilmaHttpRequestCloner;

/**
 * Evaluates the stub descriptors and returns the object necessary for the response.
 * generation.
 * @author Tunde_Kovacs
 *
 */
@Component
public class StubDescriptorEvaluator {

    private final Logger logger = LoggerFactory.getLogger(StubDescriptorEvaluator.class);

    @Autowired
    private ConditionEvaluator conditionEvaluator;
    @Autowired
    private ResponseDescriptorDtoFactory responseDescriptorDtoFactory;
    @Autowired
    private StackTraceToStringConverter stackTraceConverter;
    @Autowired
    private WilmaHttpRequestCloner requestCloner;
    @Autowired
    private DialogDescriptorService dialogDescriptorService;

    /**
     * Evaluates the stub descriptors over a request and returns the object necessary for the response
     * generation.
     * @param stubDescriptors that will be evaluated
     * @param request the {@link WilmaHttpRequest} that is matched over the conditions
     * @return the data transfer object containing the response
     */
    public ResponseDescriptorDTO findResponseDescriptor(final Map<String, StubDescriptor> stubDescriptors, final WilmaHttpRequest request) {
        ResponseDescriptorDTO responseDescriptorDTO = null;
        for (StubDescriptor stubDescriptor : stubDescriptors.values()) {
            StubDescriptorAttributes attributes = stubDescriptor.getAttributes();
            if (attributes.isActive()) {
                Iterator<DialogDescriptor> iterator = stubDescriptor.getDialogDescriptors().iterator();
                while (iterator.hasNext() && responseDescriptorDTO == null) {
                    DialogDescriptor dialogDescriptor = iterator.next();
                    responseDescriptorDTO = evaluateDialogDescriptor(request, dialogDescriptor);
                }
            }
        }
        return responseDescriptorDTO;
    }

    private ResponseDescriptorDTO evaluateDialogDescriptor(final WilmaHttpRequest request, final DialogDescriptor dialogDescriptor) {
        ResponseDescriptorDTO responseDescriptorDTO = null;
        if (dialogDescriptorService.isEnabled(dialogDescriptor)) {
            Boolean sequenceResult = request.popEvaluationResult(dialogDescriptor);
            if (Boolean.TRUE.equals(sequenceResult)) {
                responseDescriptorDTO = createResponseDescriptorDTO(dialogDescriptor, request.getBody());
            } else if (sequenceResult == null) {
                ConditionDescriptor conditionDescriptor = dialogDescriptor.getConditionDescriptor();
                Condition condition = conditionDescriptor.getCondition();
                responseDescriptorDTO = evaluateCondition(request, dialogDescriptor, condition);
            }
        }
        return responseDescriptorDTO;
    }

    private ResponseDescriptorDTO evaluateCondition(final WilmaHttpRequest request, final DialogDescriptor dialogDescriptor, final Condition condition) {
        ResponseDescriptorDTO responseDescriptorDTO = null;
        String requestBody = request.getBody();
        boolean evaluationResult = false;
        try {
            evaluationResult = conditionEvaluator.evaluate(condition, requestCloner.cloneRequest(request));
        } catch (Exception e) {
            logger.error("Error during condition evaluation in the dialog descriptor '" + dialogDescriptor.getAttributes().getName() + "'!", e);
            responseDescriptorDTO = getResponseDescriptorDTOWithError(dialogDescriptor, requestBody, e);
        }
        if (evaluationResult) {
            responseDescriptorDTO = createResponseDescriptorDTO(dialogDescriptor, requestBody);
        }
        return responseDescriptorDTO;
    }

    private ResponseDescriptorDTO createResponseDescriptorDTO(final DialogDescriptor dialogDescriptor, final String requestBody) {
        ResponseDescriptorDTO responseDescriptorDTO = responseDescriptorDtoFactory.createResponseDescriptorDTO(requestBody, dialogDescriptor);
        dialogDescriptorService.decreaseHitcountWhenUsageIsHitcount(dialogDescriptor);
        return responseDescriptorDTO;
    }

    private ResponseDescriptorDTO getResponseDescriptorDTOWithError(final DialogDescriptor dialogDescriptor, final String requestBody,
            final Exception e) {
        byte[] templateResource = stackTraceConverter.getStackTraceAsString(e).getBytes();
        return responseDescriptorDtoFactory.createResponseDescriptorDTOWithError(dialogDescriptor, requestBody, templateResource);
    }

}
