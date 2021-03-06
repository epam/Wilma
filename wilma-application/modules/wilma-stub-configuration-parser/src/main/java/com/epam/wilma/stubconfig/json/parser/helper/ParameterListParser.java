package com.epam.wilma.stubconfig.json.parser.helper;
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

import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

/**
 * Parses a simple condition.
 *
 * @author Tamas_Kohegyi
 */
@Component
public class ParameterListParser implements ObjectParser<ParameterList> {

    @Override
    public ParameterList parseObject(final JSONObject objectWithParameterList, final JSONObject root) {
        ParameterList parameterList = new ParameterList();
        if (objectWithParameterList.has("parameters")) {
            JSONArray parameters = objectWithParameterList.getJSONArray("parameters");
            for (int i = 0; i < parameters.length(); i++) {
                JSONObject object = parameters.getJSONObject(i);
                String name = object.getString("name");
                String value = object.getString("value");
                parameterList.addParameter(new Parameter(name, value));
            }
        }
        return parameterList;
    }
}
