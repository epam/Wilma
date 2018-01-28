package com.epam.wilma.sequence;

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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptorHolder;
import com.epam.wilma.domain.sequence.WilmaSequence;
import com.epam.wilma.router.helper.WilmaHttpResponseCloner;
import com.epam.wilma.sequence.cleaner.SequenceCleaner;
import com.epam.wilma.sequence.handler.exception.SequenceHandlerKeyValidationException;
import com.epam.wilma.sequence.helper.SequenceDescriptorKeyUtil;
import com.epam.wilma.sequence.helper.SequenceHeaderUtil;
import com.epam.wilma.sequence.helper.SequenceIdUtil;
import com.epam.wilma.sequence.service.SequenceService;

/**
 * This class stores the {@link SequenceDescriptor} objects. And provides methods to set the status of descriptors and modify the collection of descriptors.
 * @author Tibor_Kovacs
 *
 */
@Component
public class SequenceManager implements SequenceDescriptorHolder {
    private final Logger logger = LoggerFactory.getLogger(SequenceManager.class);
    private final Map<String, SequenceDescriptor> descriptors = new ConcurrentHashMap<>();

    @Autowired
    private WilmaHttpResponseCloner responseCloner;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private SequenceCleaner cleaner;
    @Autowired
    private SequenceHeaderUtil headerUtil;
    @Autowired
    private SequenceDescriptorKeyUtil sequenceDescriptorKeyUtil;
    @Autowired
    private SequenceIdUtil sequenceIdUtil;

    /**
     * This method calls the SequenceLogic to check the Request with all the descriptors.
     * @param request is the checked request
     */
    public void handleRequest(final WilmaHttpRequest request) {
        for (SequenceDescriptor descriptor : descriptors.values()) {
            if (descriptor.isActive()) {
                try {
                    sequenceService.checkRequest(request, descriptor);
                } catch (SequenceHandlerKeyValidationException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    /**
     * This method checks all sequences of all sequence descriptors and deletes the expired sequences.
     */
    public void cleanUpDescriptors() {
        cleaner.cleanTheExpiredSequences(descriptors);
    }

    /**
     * This method adds a sequence descriptor into the descriptors collection.
     * @param descriptorKey is the new key of sequence descriptor
     * @param descriptor is the new SequenceDescriptor
     */
    public void addSequenceDescriptor(final String descriptorKey, final SequenceDescriptor descriptor) {
        descriptors.put(descriptorKey, descriptor);
    }

    /**
     * This method removes all the SequenceDescriptors which has come from that xml configuration which is identified by the given groupName.
     * @param groupName is the groupname attribute of a xml stub configuration
     */
    public void removeSequenceDescriptors(final String groupName) {
        Iterator<SequenceDescriptor> descItr = descriptors.values().iterator();
        SequenceDescriptor actualDescriptor = null;
        while (descItr.hasNext()) {
            actualDescriptor = descItr.next();
            if (groupName.equals(actualDescriptor.getGroupName())) {
                descItr.remove();
            }
        }
    }

    /**
     * This method sets the Enabled/Disabled status of all the SequenceDescriptors which has come from that xml configuration which is identified by the given groupName.
     * @param groupName is the groupname attribute of a xml stub configuration
     * @param newStatus is the new status (Enabled/Disabled)
     */
    public void setStatusOfDescriptors(final String groupName, final boolean newStatus) {
        for (SequenceDescriptor descriptor : descriptors.values()) {
            if (groupName.equals(descriptor.getGroupName())) {
                descriptor.setActive(newStatus);
                if (!newStatus) {
                    descriptor.dropAllSequences();
                }
            }
        }
    }

    /**
     * Drops all of the sequences of every {@link SequenceDescriptor}.
     */
    public void dropAllSequences() {
        for (SequenceDescriptor sequenceDescriptor : descriptors.values()) {
            sequenceDescriptor.dropAllSequences();
        }
    }

    /**
     * This method tries to save the given response to a sequence.
     * @param response is the given response
     */
    public void tryToSaveResponseIntoSequence(final WilmaHttpResponse response) {
        String headerParameter = response.getSequenceId();
        if (headerParameter != null) {
            String[] sequenceKeys = headerUtil.resolveSequenceHeader(headerParameter);
            for (String key : sequenceKeys) {
                addResponseToSequence(key, response);
            }
        }
    }

    private void addResponseToSequence(final String sequenceId, final WilmaHttpResponse response) {
        String descriptorName = sequenceIdUtil.getDescriptorKey(sequenceId);
        SequenceDescriptor descriptor = descriptors.get(descriptorName);
        String handlerKey = sequenceIdUtil.getHandlerKey(sequenceId);
        if (descriptor != null) {
            WilmaSequence sequence = descriptor.getSequence(handlerKey);
            if (sequence != null) {
                sequence.addResponseToPair(responseCloner.cloneResponse(response));
            }
        }
    }

    /**
     * This method gives back the {@link WilmaSequence}s of the given sequence descriptor. But the result Map is read only!
     * @param sequenceDescriptorName is the name of a specific sequence descriptor
     * @return the collection of {@link WilmaSequence}s
     */
    public Map<String, WilmaSequence> getSequences(final String sequenceDescriptorName) {
        SequenceDescriptor descriptor = descriptors.get(sequenceDescriptorName);
        Map<String, WilmaSequence> result = Collections.emptyMap();
        if (descriptor != null) {
            result = descriptor.getSequences();
        }
        return result;
    }

    @Override
    public void addAllSequenceDescriptors(final StubDescriptor stubDescriptor) {
        for (SequenceDescriptor sequenceDescriptor : stubDescriptor.getSequenceDescriptors()) {
            String descriptorKey = sequenceDescriptorKeyUtil.createDescriptorKey(sequenceDescriptor.getGroupName(), sequenceDescriptor.getName());
            addSequenceDescriptor(descriptorKey, sequenceDescriptor);
        }
    }

    public Map<String, SequenceDescriptor> getDescriptors() {
        return new HashMap<>(descriptors);
    }
}
