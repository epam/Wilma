package com.epam.wilma.sequence.evaluator;
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

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.condition.Condition;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionDescriptor;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;
import com.epam.wilma.router.evaluation.ConditionEvaluator;
import com.epam.wilma.router.evaluation.helper.DialogDescriptorService;
import com.epam.wilma.router.helper.WilmaHttpRequestCloner;
import com.epam.wilma.sequence.helper.SequenceDescriptorKeyUtil;

/**
 * This class decides if a Request belongs to a SequenceDescriptor or not.
 * @author Tibor_Kovacs
 *
 */
@Component
public class SequenceDescriptorEvaluator {
    private final Logger logger = LoggerFactory.getLogger(SequenceDescriptorEvaluator.class);

    @Autowired
    private ConditionEvaluator conditionEvaluator;
    @Autowired
    private WilmaHttpRequestCloner requestCloner;
    @Autowired
    private DialogDescriptorService dialogDescriptorService;
    @Autowired
    private SequenceDescriptorKeyUtil sequenceDescriptorKeyUtil;

    /**
     * This method checks whether the given SequenceDescriptor has a rule which is matched with the given Request or not.
     * @param request is the checked request
     * @param sequenceDescriptor contains conditions and dialog descriptors.
     * @return with true if there is any condition which has matched with the given Request, otherwise return with false
     */
    public boolean evaluate(final WilmaHttpRequest request, final SequenceDescriptor sequenceDescriptor) {
        boolean result = false;
        result = checkConditions(request, sequenceDescriptor, result);
        if (!result) {
            result = checkDialogDescriptors(request, sequenceDescriptor, result);
        }
        return result;
    }

    private boolean checkConditions(final WilmaHttpRequest request, final SequenceDescriptor sequenceDescriptor, final boolean state) {
        boolean result = state;
        Iterator<ConditionDescriptor> cdIterator = sequenceDescriptor.getConditionDescriptors().iterator();
        while (cdIterator.hasNext() && !result) {
            ConditionDescriptor conditionDescriptor = cdIterator.next();
            Condition condition = conditionDescriptor.getCondition();
            try {
                result = conditionEvaluator.evaluate(condition, requestCloner.cloneRequest(request));
            } catch (Exception e) {
                String descriptorKey = sequenceDescriptorKeyUtil.createDescriptorKey(sequenceDescriptor.getGroupName(), sequenceDescriptor.getName());
                logger.error("Error during condition evaluation in the " + descriptorKey + " sequence!", e);
            }
        }
        return result;
    }

    private boolean checkDialogDescriptors(final WilmaHttpRequest request, final SequenceDescriptor sequenceDescriptor, final boolean state) {
        boolean result = state;
        Iterator<DialogDescriptor> ddIterator = sequenceDescriptor.getDialogDescriptors().iterator();
        while (ddIterator.hasNext() && !result) {
            DialogDescriptor dialogDescriptor = ddIterator.next();
            if (dialogDescriptorService.isEnabled(dialogDescriptor)) {
                ConditionDescriptor conditionDescriptor = dialogDescriptor.getConditionDescriptor();
                Condition condition = conditionDescriptor.getCondition();
                result = conditionEvaluator.evaluate(condition, requestCloner.cloneRequest(request));
                request.pushEvaluationResult(dialogDescriptor, result);
            }
        }
        return result;
    }

}
