package com.epam.wilma.stubconfig.dom.parser.sequence.helper;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;

/**
 * Used to convert collection of {@link DialogDescriptor}s to a {@link Map}.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class DialogDescriptorMapper {

    /**
     * Creates a {@link Map} from the given list with the {@link DialogDescriptor} name as key.
     * @param dialogDescriptors the given list of {@link DialogDescriptor}s
     * @return the map
     */
    public Map<String, DialogDescriptor> groupByName(final List<DialogDescriptor> dialogDescriptors) {
        Map<String, DialogDescriptor> result = new HashMap<>();
        for (DialogDescriptor dialogDescriptor : dialogDescriptors) {
            result.put(dialogDescriptor.getAttributes().getName(), dialogDescriptor);
        }
        return result;
    }
}
