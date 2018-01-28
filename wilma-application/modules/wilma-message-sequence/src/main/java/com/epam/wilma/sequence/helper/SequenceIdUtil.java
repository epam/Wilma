package com.epam.wilma.sequence.helper;
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

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;

/**
 * This class provides methods to create and resolve sequence id.
 * @author Tibor_Kovacs
 */
@Component
public class SequenceIdUtil {

    private static final String ESCAPED_SEQUENCE_ID_SEPARATOR = Pattern.quote(SequenceConstants.SEQUENCE_ID_SEPARATOR.getConstant());
    @Autowired
    private SequenceDescriptorKeyUtil sequenceDescriptorKeyUtil;

    /**
     * This method gives back the sequence descriptor key from SequenceId.
     * @param sequenceId is the given sequenceId
     * @return with the sequence descriptor key
     */
    public String getDescriptorKey(final String sequenceId) {
        return sequenceId.split(ESCAPED_SEQUENCE_ID_SEPARATOR)[0];
    }

    /**
     * This method gives back the handler key from SequenceId.
     * @param sequenceId is the given sequenceId
     * @return with the handler key
     */
    public String getHandlerKey(final String sequenceId) {
        return sequenceId.split(ESCAPED_SEQUENCE_ID_SEPARATOR)[1];
    }

    /**
     * This method creates a sequence id from handlerKey and a sequence descriptor.
     * @param handlerKey is the given handler key
     * @param sequenceDescriptor is a sequence descriptor
     * @return with a sequence id
     */
    public String createSequenceId(final String handlerKey, final SequenceDescriptor sequenceDescriptor) {
        StringBuilder sb = new StringBuilder();
        String descriptorKey = sequenceDescriptorKeyUtil.createDescriptorKey(sequenceDescriptor.getGroupName(), sequenceDescriptor.getName());
        sb.append(descriptorKey).append(SequenceConstants.SEQUENCE_ID_SEPARATOR.getConstant()).append(handlerKey);
        return sb.toString();
    }
}
