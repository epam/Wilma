package com.epam.wilma.domain.stubconfig.sequence;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.epam.wilma.domain.sequence.WilmaSequence;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionDescriptor;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * The sequence descriptor contains several {@link ConditionDescriptor}s and a list of names of {@link DialogDescriptor}s.
 * @author Tibor_Kovacs
 *
 */
public class SequenceDescriptor {
    public static final String TAG_NAME = "sequence-descriptor";
    public static final String TAG_NAME_JSON = "sequenceDescriptors";
    private final Map<String, WilmaSequence> sequences;
    private final List<ConditionDescriptor> conditionDescriptors;
    private final List<DialogDescriptor> dialogDescriptors;
    private final SequenceDescriptorAttributes attributes;

    /**
     * Constructs a new instance of {@link SequenceDescriptor}.
     * @param conditionDescriptors the list of {@link ConditionDescriptor} defined in the sequence
     * @param dialogDescriptors the list of dialog descriptors defined in the stub configuration
     * @param attributes is the collection of any attributes of SequenceDescriptor
     */
    public SequenceDescriptor(final List<ConditionDescriptor> conditionDescriptors, final List<DialogDescriptor> dialogDescriptors,
            final SequenceDescriptorAttributes attributes) {
        super();
        this.sequences = new ConcurrentHashMap<>();
        this.conditionDescriptors = conditionDescriptors;
        this.dialogDescriptors = dialogDescriptors;
        this.attributes = attributes;
    }

    /**
     * Put a new sequence object into the sequences collection.
     * @param sequence is the new sequence what we want to store
     */
    public void putIntoSequences(final WilmaSequence sequence) {
        sequences.put(sequence.getSequenceKey(), sequence);
    }

    public Collection<WilmaSequence> getSequencesInCollection() {
        return sequences.values();
    }

    public Map<String, WilmaSequence> getSequences() {
        return sequences;
    }

    /**
     * Get back a sequence by the given sequenceKey.
     * @param sequenceKey the sequenceId of searched sequence
     * @return the searched {@link WilmaSequence} object. It can be null.
     */
    public WilmaSequence getSequence(final String sequenceKey) {
        return sequences.get(sequenceKey);
    }

    public List<ConditionDescriptor> getConditionDescriptors() {
        return new ArrayList<>(conditionDescriptors);
    }

    public List<DialogDescriptor> getDialogDescriptors() {
        return new ArrayList<>(dialogDescriptors);
    }

    public SequenceHandler getHandler() {
        return attributes.getHandler();
    }

    public String getName() {
        return attributes.getName();
    }

    public String getGroupName() {
        return attributes.getGroupName();
    }

    public long getDefaultTimeout() {
        return attributes.getDefaultTimeout();
    }

    public boolean isActive() {
        return attributes.isActive();
    }

    public ParameterList getParameters() {
        return attributes.getParameters();
    }

    /**
     * This method activate or deactivate the current SequenceDescriptor.
     * @param newStatus is the new status. (true == Enabled, false == Disabled)
     */
    public void setActive(final boolean newStatus) {
        attributes.setActive(newStatus);
    }

    /**
     * This method clears the collection of WilmaSequences.
     */
    public void dropAllSequences() {
        sequences.clear();
    }
}
