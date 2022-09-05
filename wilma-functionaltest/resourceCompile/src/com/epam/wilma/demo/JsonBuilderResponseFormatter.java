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
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatter;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

public class JsonBuilderResponseFormatter implements ResponseFormatter {
    @Override
    public byte[] formatResponse(WilmaHttpRequest wilmaRequest, HttpServletResponse resp, byte[] responseResource, ParameterList parameters) throws Exception {
        //assumption - initial response is a JSON answer already
        String response = new String(responseResource);
        JSONObject o = new JSONObject(response);

        //go through the parameters and add all the names and values
        List<Parameter> params = parameters.getAllParameters();
        Iterator<Parameter> iterator = params.iterator();
        while (iterator.hasNext()) {
            Parameter parameter = iterator.next();
            String parameterName = parameter.getName();
            String parameterValue = parameter.getValue();
            o.put(parameterName, parameterValue);
        }

        //finally, we need to have proper data
        return o.toString().getBytes(StandardCharsets.UTF_8);
    }
}
