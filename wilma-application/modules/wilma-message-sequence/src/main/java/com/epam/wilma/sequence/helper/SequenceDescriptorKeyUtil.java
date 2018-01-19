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

import org.springframework.stereotype.Component;

/**
 * This class creates SequenceDescriptorKey.
 * @author Tibor_Kovacs
 */
@Component
public class SequenceDescriptorKeyUtil {

    /**
     * This method creates the sequence descriptor key from the given parameters.
     * @param groupname is the groupname attribute of SequenceDescriptor
     * @param descriptorName is the name attribute of SequenceDescriptor
     * @return with a sequencedescriptor key.
     */
    public String createDescriptorKey(final String groupname, final String descriptorName) {
        StringBuilder sb = new StringBuilder();
        sb.append(groupname).append(SequenceConstants.DESCRIPTOR_KEY_PART_SEPARATOR.getConstant()).append(descriptorName);
        return sb.toString();
    }

}
