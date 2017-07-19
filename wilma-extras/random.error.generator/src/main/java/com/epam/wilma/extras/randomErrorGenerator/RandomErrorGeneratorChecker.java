package com.epam.wilma.extras.randomErrorGenerator;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

/**
 * Example Wilma plugin to simulate random error situations. The following error situations are supported:
 * - E404 error will be received as response in a certain percentage of the requests, randomly
 * - E500 error will be received as response for another percentage of the requests, randomly
 * - response timeout will be cause for another percentage of the requests, randomly
 *
 * You need to have as many dialog descriptors in the stub configuration as many error situations you would like to simulate.
 *
 * @author tkohegyi
 */
public class RandomErrorGeneratorChecker implements ConditionChecker {

    private static final String PARAMETER_NAME_E404 = "E404";
    private static final String PARAMETER_NAME_E500 = "E500";
    private static final String PARAMETER_NAME_TIMEOUT = "TIMEOUT120SEC";

    @Override
    /**
     * ConditionChecker method implementation. Based on the received parameters it decides if a certain error condition should happen or not.
     */
    public boolean checkCondition(final WilmaHttpRequest request, final ParameterList parameterList) {
        boolean needError = false;

        //determine parameters
        String parameterE500Value = parameterList.get(PARAMETER_NAME_E500);
        String parameterTimeoutValue = parameterList.get(PARAMETER_NAME_TIMEOUT);


        //Handle E404 part
        double parameterE404RealValue = 0.0;
        String parameterE404Value = parameterList.get(PARAMETER_NAME_E404);
        if (parameterE404Value != null) {
            try {
                parameterE404RealValue = Double.valueOf(parameterE404Value);
            } catch (NumberFormatException e) {
                parameterE404RealValue = 0.0;
            }
        }

        return needError;
    }

}
