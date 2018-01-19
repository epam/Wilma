package com.epam.wilma.extras.randomErrorGenerator;
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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

import java.util.Random;

/**
 * Example Wilma plugin to simulate random error situations. The following error situations are supported:
 * - E404 error will be received as response in a certain percentage of the requests, randomly
 * - E500 error will be received as response for another percentage of the requests, randomly
 * - response timeout will be cause for another percentage of the requests, randomly
 *
 * You need to have as many dialog descriptors in the stub configuration as many error situations you would like to simulate, otherwise you may use a single response only.
 * See the provided stubConfig.xml to see how the class should be used within the configuration, together with the specific responses.
 *
 * @author tkohegyi
 */
public class RandomErrorGeneratorChecker implements ConditionChecker {

    private static final String PARAMETER_NAME_E404 = "E404";
    private static final String PARAMETER_NAME_E500 = "E500";
    private static final String PARAMETER_NAME_TIMEOUT = "TIMEOUT120SEC";

    @Override
    public boolean checkCondition(final WilmaHttpRequest request, final ParameterList parameterList) {
        return getNeedError(PARAMETER_NAME_E404, parameterList) || getNeedError(PARAMETER_NAME_E500, parameterList) || getNeedError(PARAMETER_NAME_TIMEOUT, parameterList);
    }

    private boolean getNeedError(final String parameterName, final ParameterList parameterList) {
        Random randomGenerator = new Random();
        int parameterRealValue = 0;
        String parameterValue = parameterList.get(parameterName);
        if (parameterValue != null) {
            try {
                parameterRealValue = Integer.valueOf(parameterValue);
            } catch (NumberFormatException e) {
                parameterRealValue = 0;
            }
        }
        return parameterRealValue > 0 && randomGenerator.nextInt(parameterRealValue) == 0;
    }

}
