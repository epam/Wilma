package com.epam.wilma.domain.stubconfig.dialog;
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

import com.epam.wilma.domain.evaluation.Evaluable;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptor;

/**
 * Decribes a request-response pair( with what response the stub should answer in case of a specific request).
 * Each descriptor contains a condition descriptor and a response descriptor.
 * If a condition descriptor returns true, a new response descriptor is created.
 * @author Tunde_Kovacs
 *
 */
public class DialogDescriptor implements Evaluable {

    private final DialogDescriptorAttributes attributes;
    private final ConditionDescriptor conditionDescriptor;
    private final ResponseDescriptor responseDescriptor;

    /**
     * Constructs a new instance with the given attributes.
     * @param attributes includes the name, the usage, the validity value
     * and the comment attributes
     * @param conditionDescriptor describes the condition the incoming request will
     * be matched for
     * @param responseDescriptor describes the response that will associated to the request
     */
    public DialogDescriptor(final DialogDescriptorAttributes attributes, final ConditionDescriptor conditionDescriptor,
            final ResponseDescriptor responseDescriptor) {
        super();
        this.attributes = attributes;
        this.conditionDescriptor = conditionDescriptor;
        this.responseDescriptor = responseDescriptor;
    }

    public DialogDescriptorAttributes getAttributes() {
        return attributes;
    }

    public ConditionDescriptor getConditionDescriptor() {
        return conditionDescriptor;
    }

    public ResponseDescriptor getResponseDescriptor() {
        return responseDescriptor;
    }

    /**
     * Decides if the dialog descriptor is enabled i.e. should be used
     * for condition evaluation or not.
     * @return true if it is enabled, false otherwise
     */
    public boolean isEnabled() {
        return getAttributes().getUsage() != DialogDescriptorUsage.DISABLED;
    }

}
