package com.epam.wilma.stubconfig.dom.parser.node.helper;
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

import java.util.List;

import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.domain.stubconfig.exception.ConditionEvaluationFailedException;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;

/**
 * Syntactically validates a custom xquery body checker class' xquery.
 * @author Tunde_Kovacs
 *
 */
@Component
public class CustomXQueryCheckerValidator {

    private static final String CUSTOM_BODY_CHECKER = "CustomXQueryBodyChecker";
    private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><tag></tag>";

    /**
     * Syntactically validates a custom xquery body checker class' xquery.
     * @param conditionChecker the class that is validated
     * @param parameterList the parameter containing the xquery
     */
    public void validate(final ConditionChecker conditionChecker, final ParameterList parameterList) {
        List<Parameter> params = parameterList.getAllParameters();
        if (getSimpleName(conditionChecker).equals(CUSTOM_BODY_CHECKER)) {
            WilmaHttpRequest request = new WilmaHttpRequest();
            request.setBody(XML_DECLARATION);
            request.addHeader("Content-Type", "application/xml");
            if (params.size() == 1) {
                try {
                    conditionChecker.checkCondition(request, parameterList);
                } catch (ConditionEvaluationFailedException e) {
                    throw new DescriptorValidationFailedException("Xquery '" + params.iterator().next().getValue()
                            + "' is syntactically incorrect! For details, check the wilmalog!", e);
                }
            }
        }

    }

    String getSimpleName(final ConditionChecker conditionChecker) {
        return conditionChecker.getClass().getSimpleName();
    }

}
