package com.epam.wilma.stubconfig.condition.checker;

import java.util.List;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Example condition checker class showing how to get the request body
 * and also how to work with the parameters.
 * @author Tunde_Kovacs
 *
 */
public class ExampleConditionChecker implements ConditionChecker {

    @Override
    public boolean checkCondition(final WilmaHttpRequest request, final ParameterList parameters) {
        boolean result = false;
        String body = request.getBody();
        List<Parameter> paramList = parameters.getAllParameters();
        for (Parameter parameter : paramList) {
            if (body.contains(parameter.getValue())) {
                result = true;
            }
        }
        return result;
    }

}
