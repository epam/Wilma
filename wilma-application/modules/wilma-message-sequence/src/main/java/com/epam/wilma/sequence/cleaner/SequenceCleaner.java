package com.epam.wilma.sequence.cleaner;
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

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.CurrentDateProvider;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;
import com.epam.wilma.domain.stubconfig.sequence.WilmaSequence;

/**
 * This class provides the cleaning functionality to {@link SequenceManager}.
 * @author Tibor_Kovacs
 *
 */
@Component
public class SequenceCleaner {

    @Autowired
    private CurrentDateProvider dateProvider;

    /**
     * This method removes all the expired WilmaSequences.
     * @param descriptors is the given collection of SequenceDescriptors
     */
    public void cleanTheExpiredSequences(final Map<String, SequenceDescriptor> descriptors) {
        for (SequenceDescriptor descriptor : descriptors.values()) {
            cleanSequences(descriptor);
        }
    }

    private void cleanSequences(final SequenceDescriptor sequenceDescriptor) {
        Iterator<WilmaSequence> iterator = sequenceDescriptor.getSequencesInCollection().iterator();
        while (iterator.hasNext()) {
            WilmaSequence actualSeq = iterator.next();
            if (actualSeq.isExpired(new Timestamp(dateProvider.getCurrentTimeInMillis()))) {
                iterator.remove();
            }
        }
    }
}