package com.epam.wilma.core.processor.request;

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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.wilma.core.processor.WilmaEntityProcessorInterface;
import com.epam.wilma.core.processor.entity.ProcessorBase;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.exception.TooManyRequestsException;
import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.http.WilmaHttpRequest;

/**
 * Class for calling processors with {@link WilmaHttpRequest} in given order.
 * @author Tamas_Bihari
 *
 */
public class WilmaHttpRequestProcessor {

    private static final String ABORT_REQUEST_MESSAGE = "Aborting request";
    private final Logger logger = LoggerFactory.getLogger(WilmaHttpRequestProcessor.class);
    private List<ProcessorBase> processors;

    /**
     * Calls processors with the given {@link WilmaHttpEntity} in the given order.
     * @param entity is the handled request
     * @throws ApplicationException when an exception occurs in a processor
     */
    public void processRequest(final WilmaHttpEntity entity) throws ApplicationException {
        WilmaHttpRequest request = (WilmaHttpRequest) entity;
        try {
            for (ProcessorBase processor : processors) {
                if (processor.isEnabled()) {
                    processor.process(request);
                }
            }
        } catch (TooManyRequestsException e) {
            logger.debug(ABORT_REQUEST_MESSAGE, e);
            request.setAborted(true);
        }
    }

    /**
     * Disabales a processor in the processor chain if the chain contained the processor.
     * @param processor the processor that should be removed
     */
    public void disableProcessor(final ProcessorBase processor) {
        if (processors.contains(processor)) {
            processor.setEnabled(false);
        }
    }

    /**
     * Disabales a processor in the processor chain if the chain contained the processor.
     * @param processor the processor that should be removed
     */
    public void enableProcessor(final ProcessorBase processor) {
        if (processors.contains(processor)) {
            processor.setEnabled(true);
        }
    }

    /**
     * Gets if a specific processor is enabled or not.
     * @param processor the processor that should be investigated.
     * @return true if enabled, otherwise false.
     */
    public boolean isProcessorEnabled(final ProcessorBase processor) {
        boolean enabled = false;
        if (processors.contains(processor)) {
            enabled = processor.isEnabled();
        }
        return enabled;
    }

    /**
     * Adds a processor to the processor chain.
     * @param processor the processor to add
     */
    public void addProcessor(final ProcessorBase processor) {
        if (!processors.contains(processor)) {
            processors.add(processor);
        }
    }

    /**
     * Returns true if the chain of processors contain the given processor.
     * @param processor processor to check
     * @return true if contains
     */
    public boolean containsProcessor(final WilmaEntityProcessorInterface processor) {
        return processors.contains(processor);
    }

    public void setProcessors(final List<ProcessorBase> processors) {
        this.processors = processors;
    }

}
