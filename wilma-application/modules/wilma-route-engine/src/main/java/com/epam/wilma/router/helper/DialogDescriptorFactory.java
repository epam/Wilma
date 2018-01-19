package com.epam.wilma.router.helper;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptorUsage;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptor;

/**
 * Factory for creating new dialog descriptors.
 * @author Tunde_Kovacs
 *
 */
@Component
public class DialogDescriptorFactory {

    @Autowired
    private ResponseDescriptorFactory responseDescriptorFactory;
    @Autowired
    private ConditionDescriptorFactory conditionDescriptorFactory;

    /**
     * Creates a new dialog descriptor for stub mode response.
     * @param dialogDescriptorName the name of the new dialog descriptor
     * @return the new dialog descriptor with a dummy response. The real response will be generated when a request matches
     * this dialog descriptor, such that the content type of the response will be determined based on the corresponding request header.
     */
    public DialogDescriptor createDialogDescriptorForStubMode(final String dialogDescriptorName) {
        DialogDescriptorAttributes attributes = new DialogDescriptorAttributes(dialogDescriptorName, DialogDescriptorUsage.ALWAYS);
        ConditionDescriptor conditionDescriptor = conditionDescriptorFactory.createConditionDescriptorForStubMode();
        ResponseDescriptor responseDescriptor = responseDescriptorFactory.createResponseDescriptorForStubMode();
        return new DialogDescriptor(attributes, conditionDescriptor, responseDescriptor);
    }

}
