package com.epam.wilma.webapp.service;

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

import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;
import com.epam.wilma.sequence.SequenceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides information about how much WilmaSequence object lives.
 *
 * @author Tibor_Kovacs
 */
@Component
public class SequenceInformationCollector {

    static final String GROUPS_KEY = "groupedbydescriptors";
    static final String SUM_KEY = "countofsequences";
    @Autowired
    private SequenceManager sequenceManager;

    /**
     * Creates a map with the count of all sequences and a map containing the count of each sequence descriptor's sequences.
     *
     * @return a map which contains the sum of WilmaSequence with SUM_KEY and
     * an other map with sum of specific {@link SequenceDescriptor}'s sequences.
     */
    public Map<String, Object> collectInformation() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Integer> countOfGroups = new HashMap<>();
        Map<String, SequenceDescriptor> descriptors = sequenceManager.getDescriptors();
        int sumSequenceCount = 0;
        for (String seqDescriptorKey : descriptors.keySet()) {
            int countOfSequences = descriptors.get(seqDescriptorKey).getSequences().size();
            sumSequenceCount += countOfSequences;
            countOfGroups.put(seqDescriptorKey, countOfSequences);
        }
        result.put(SUM_KEY, sumSequenceCount);
        result.put(GROUPS_KEY, countOfGroups);
        return result;
    }
}
