package com.epam.wilma.sequence.service;
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

import com.epam.wilma.common.helper.CurrentDateProvider;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.sequence.RequestResponsePair;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;
import com.epam.wilma.domain.sequence.WilmaSequence;
import com.epam.wilma.domain.stubconfig.sequence.SequenceHandler;
import com.epam.wilma.router.helper.WilmaHttpRequestCloner;
import com.epam.wilma.sequence.evaluator.SequenceDescriptorEvaluator;
import com.epam.wilma.sequence.factory.SequenceFactory;
import com.epam.wilma.sequence.helper.SequenceHeaderUtil;
import com.epam.wilma.sequence.helper.SequenceIdUtil;
import com.epam.wilma.sequence.validator.HandlerKeyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * This contains the logic of sequence handling process.
 * @author Tibor_Kovacs
 *
 */
@Component
public class SequenceService {
    @Autowired
    private SequenceDescriptorEvaluator sequenceEvaluator;
    @Autowired
    private WilmaHttpRequestCloner requestCloner;
    @Autowired
    private CurrentDateProvider dateProvider;
    @Autowired
    private SequenceFactory sequenceFactory;
    @Autowired
    private SequenceHeaderUtil headerUtil;
    @Autowired
    private SequenceIdUtil sequenceIdUtil;
    @Autowired
    private HandlerKeyValidator handlerKeyValidator;

    /**
     * This method evaluate the given request with the given descriptor.
     * If the result of evaluation is true, this method will append the request to an existing sequence or
     * it will create a new sequence object with this request. After this logic this method put the sequence key into the extraheaders of request.
     * If the result of evaluation is false, this method won't do nothing.
     * @param request is the given request
     * @param sequenceDescriptor is the given SequenceDescriptor which represents a sequence type
     */
    public void checkRequest(final WilmaHttpRequest request, final SequenceDescriptor sequenceDescriptor) {
        if (sequenceEvaluator.evaluate(request, sequenceDescriptor)) {
            SequenceHandler handler = sequenceDescriptor.getHandler();
            String sequenceKey = handler.getExistingSequence(request, sequenceDescriptor.getSequences(), sequenceDescriptor.getParameters());
            if (sequenceKey == null) {
                sequenceKey = handler.generateNewSequenceKey(request, sequenceDescriptor.getParameters());
                handlerKeyValidator.validateGeneratedKey(sequenceKey, handler.getClass().getName());
                WilmaSequence sequence = sequenceFactory.createNewSequence(sequenceKey, request, sequenceDescriptor.getDefaultTimeout());
                sequenceDescriptor.putIntoSequences(sequence);
            } else {
                appendSequence(sequenceKey, request, sequenceDescriptor);
            }
            String sequenceId = sequenceIdUtil.createSequenceId(sequenceKey, sequenceDescriptor);
            appendSequenceKeyToHeader(sequenceId, request);
        }
    }

    private void appendSequence(final String sequenceKey, final WilmaHttpRequest request, final SequenceDescriptor sequenceDescriptor) {
        WilmaSequence sequence = sequenceDescriptor.getSequence(sequenceKey);
        if (sequence != null) {
            String loggerId = request.getWilmaMessageId();
            RequestResponsePair pair = new RequestResponsePair(requestCloner.cloneRequest(request));
            sequence.setTimeout(new Timestamp(dateProvider.getCurrentTimeInMillis() + sequenceDescriptor.getDefaultTimeout()));
            sequence.addPair(loggerId, pair);
        }
    }

    private void appendSequenceKeyToHeader(final String sequenceId, final WilmaHttpRequest request) {
        String oldSource = request.getSequenceId();
        String newHeaderValue = headerUtil.createSequenceHeader(oldSource, sequenceId);
        request.addSequenceId(newHeaderValue);
    }
}
