package com.epam.wilma.webapp.service;

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

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;
import com.epam.wilma.sequence.SequenceManager;

/**
 * Provides information about how much {@link WilmaSequence} object lives in each {@link SequenceDescriptor}.
 * @author Tibor_Kovacs
 *
 */
@Component
public class SequenceInformationCollector {

    @Autowired
    private SequenceManager sequenceManager;

    /**
     * Counts the {@link WilmaSequence} objects in each {@link SequenceDescriptor}.
     * @return a map<SequenceDescriptorKey, countofWilmaSequences>
     */
    public Map<String, Integer> collectSequences() {
        Map<String, Integer> result = new HashMap<>();
        Map<String, SequenceDescriptor> descriptors = sequenceManager.getDescriptors();
        for (String seqDescriptorKey : descriptors.keySet()) {
            int countOfSequences = descriptors.get(seqDescriptorKey).getSequences().size();
            result.put(seqDescriptorKey, countOfSequences);
        }
        return result;
    }
}
