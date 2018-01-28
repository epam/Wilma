package com.epam.wilma.stubconfig.condition.checker.general.url;

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
import java.util.List;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * {@link ConditionChecker} for check the given patterns in the request's url and
 * to return with true all of the given parameters must be contained by the request's url.
 * @author Tamas_Bihari
 *
 */
public class AndUrlPatternChecker implements ConditionChecker {

    @Override
    public boolean checkCondition(final WilmaHttpRequest request, final ParameterList parameters) {
        List<Parameter> params = parameters.getAllParameters();
        Iterator<Parameter> iterator = params.iterator();
        boolean result = !params.isEmpty();
        while (iterator.hasNext() && result) {
            Parameter parameter = iterator.next();
            result = evaluateParameter(request, parameter);
        }
        return result;
    }

    private boolean evaluateParameter(final WilmaHttpRequest request, final Parameter parameter) {
        String paramValue = parameter.getValue();
        boolean result = false;
        if (request.getRequestLine().contains(paramValue)) {
            result = true;
        }
        return result;
    }

}
