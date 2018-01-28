package com.epam.wilma.stubconfig.condition.checker.general.header;

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

import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Checks request message header with the given parameters.
 * @author Tamas_Bihari
 *
 */
@Component
public class HeaderParameterChecker implements ConditionChecker {

    @Override
    public boolean checkCondition(final WilmaHttpRequest request, final ParameterList parameterList) {
        List<Parameter> params = parameterList.getAllParameters();
        Iterator<Parameter> iterator = params.iterator();
        boolean result = !params.isEmpty();
        while (iterator.hasNext() && result) {
            Parameter parameter = iterator.next();
            String header = request.getHeader(parameter.getName());
            if (header != null) {
                result = result && header.contains(parameter.getValue());
            } else {
                result = false;
            }

        }
        return result;
    }

}
