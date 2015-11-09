package com.epam.wilma.webapp.stub.response;

/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import com.epam.wilma.common.helper.StackTraceToStringConverter;
import com.epam.wilma.core.MapBasedResponseDescriptorAccess;
import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateFormatter;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateFormatterDescriptor;
import com.epam.wilma.domain.stubconfig.sequence.WilmaSequence;
import com.epam.wilma.router.domain.ResponseDescriptorDTO;
import com.epam.wilma.sequence.helper.SequenceHeaderUtil;
import com.epam.wilma.sequence.matcher.SequenceMatcher;
import com.epam.wilma.webapp.stub.response.support.HttpServletRequestTransformer;
import com.epam.wilma.webapp.stub.response.support.SequenceResponseGuard;
import com.epam.wilma.webapp.stub.response.support.StubResponseHeaderConfigurer;
import com.epam.wilma.webapp.stub.servlet.helper.WaitProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * Generates the appropriate response for a request.
 * Gets the response descriptor data transfer object from the key-value store using the request's WilmaLoggerId and generates the response.
 * @author Tamas_Bihari
 *
 */
@Component
public class StubResponseGenerator {

    private final Logger logger = LoggerFactory.getLogger(StubResponseGenerator.class);
    @Autowired
    private MapBasedResponseDescriptorAccess responseDescriptorAccess;
    @Autowired
    private StackTraceToStringConverter stackTraceConverter;
    @Autowired
    private WaitProvider waitProvider;
    @Autowired
    private StubResponseHeaderConfigurer headerConfigurer;
    @Autowired
    private HttpServletRequestTransformer requestTransformer;
    @Autowired
    private SequenceHeaderUtil headerCreator;
    @Autowired
    private SequenceMatcher matcher;
    @Autowired
    private SequenceResponseGuard sequenceResponseGuard;

    /**
     * Gets the response descriptor data transfer object from the key-value store using the request's WilmaLoggerId and generates the response.
     * @param req is the request for the response generation
     * @param resp is the response
     * @return with the generated response as byte array
     */
    public byte[] generateResponse(final HttpServletRequest req, final HttpServletResponse resp) {
        String wilmaLoggerId = req.getHeader(WilmaHttpRequest.WILMA_LOGGER_ID);
        byte[] result = null;
        if (wilmaLoggerId != null) {
            ResponseDescriptorDTO responseDescriptorDTO = responseDescriptorAccess.getResponseDescriptor(wilmaLoggerId);
            Set<TemplateFormatterDescriptor> templateFormatterDescriptors = responseDescriptorDTO.getResponseDescriptor().getTemplateFormatters();
            //generate pure WilmaHttpRequest
            WilmaHttpRequest wilmaRequest = requestTransformer.transformToWilmaHttpRequest(wilmaLoggerId, req, responseDescriptorDTO);
            //add wilma information to response header
            headerConfigurer.addWilmaInfoToResponseHeader(req, resp, responseDescriptorDTO.getDialogDescriptorName());
            //set headers generate response body
            result = generate(resp, responseDescriptorDTO, templateFormatterDescriptors, wilmaRequest);
        }
        return result;
    }

    private byte[] generate(final HttpServletResponse resp, final ResponseDescriptorDTO responseDescriptorDTO,
            final Set<TemplateFormatterDescriptor> templateFormatterDescriptors, final WilmaHttpRequest wilmaRequest) {
        byte[] result;
        ResponseDescriptor responseDescriptor = responseDescriptorDTO.getResponseDescriptor();
        try {
            result = responseDescriptor.getAttributes().getTemplate().getResource();
            String sequenceKeysParam = wilmaRequest.getHeader(WilmaHttpEntity.WILMA_SEQUENCE_ID);
            String[] sequenceIds = headerCreator.resolveSequenceHeader(sequenceKeysParam);
            WilmaSequence actualSequence = matcher.matchSequenceKeyWithDescriptor(responseDescriptor.getAttributes().getSequenceDescriptorKey(),
                    sequenceIds);
            if (actualSequence != null) {
                sequenceResponseGuard.waitForResponses(wilmaRequest, actualSequence);
            }
            if (templateFormatterDescriptors != null && !templateFormatterDescriptors.isEmpty()) {
                for (TemplateFormatterDescriptor templateFormatterDescriptor : templateFormatterDescriptors) {
                    TemplateFormatter templateFormatter = templateFormatterDescriptor.getTemplateFormatter();
                    result = templateFormatter.formatTemplate(wilmaRequest, result, templateFormatterDescriptor.getParams(), actualSequence);
                }
            }
            //set response status and content type
            headerConfigurer.setResponseContentTypeAndStatus(resp, responseDescriptorDTO);
            //delay response if necessary
            delayResponse(responseDescriptor.getAttributes().getDelay());
        } catch (Exception e) {
            headerConfigurer.setErrorResponseContentTypeAndStatus(resp);
            result = getErrorMessageWithStackTrace(e);
        }
        return result;
    }

    private byte[] getErrorMessageWithStackTrace(final Exception e) {
        return stackTraceConverter.getStackTraceAsString(e).getBytes();
    }

    private void delayResponse(final int delay) {
        try {
            waitProvider.waitMilliSeconds(delay);
        } catch (InterruptedException e) {
            logger.error("Could not return response. Exception while thread.sleep", e);
        }
    }

}
