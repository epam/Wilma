package com.epam.wilma.demo;
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
import org.json.JSONObject;

public class IsInvalidJSONRequestCondition implements ConditionChecker {

    @Override
    public boolean checkCondition(WilmaHttpRequest request, ParameterList parameters) {
        boolean result = true; //it is invalid by default
        //first get the body
        String body = request.getBody();
        //convert it to JSON object, if there is an exception here then it is not a valid JSON.
        try {
            new JSONObject(body);
            //if we are here, the JSON is valid
            result = false;
        } catch (Exception x) {
            //nothing to do here, default value is valid - if we want we may log that we found an issue
        }
        return result;
    }
}
