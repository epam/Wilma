package com.epam.wilma.sequence.matcher;
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

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.sequence.WilmaSequence;
import com.epam.wilma.sequence.SequenceManager;
import com.epam.wilma.sequence.helper.SequenceIdUtil;

/**
 * This class looks for the first {@link WilmaSequence} with the right sequence descriptor name by sequence key.
 * @author Tibor_Kovacs
 *
 */
@Component
public class SequenceMatcher {

    @Autowired
    private SequenceManager manager;
    @Autowired
    private SequenceIdUtil sequenceKeyResolver;

    /**
     * This method gives back the first WilmaSequence from the sequences of the given descriptor. Which key is in the seqKeys array first.
     * @param sequenceDescriptorName is the name of given sequence descriptor
     * @param sequenceIds the given sequence ids which come from request header
     * @return with a {@link WilmaSequence}
     */
    public WilmaSequence matchSequenceKeyWithDescriptor(final String sequenceDescriptorName, final String[] sequenceIds) {
        Map<String, WilmaSequence> sequences = manager.getSequences(sequenceDescriptorName);
        WilmaSequence result = null;
        for (int i = 0; i < sequenceIds.length && result == null; i++) {
            String actualKey = sequenceKeyResolver.getHandlerKey(sequenceIds[i]);
            result = sequences.get(actualKey);
        }
        return result;
    }

}
