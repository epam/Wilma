package com.epam.wilma.stubconfig.response.formatter;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

/**
 * This class adds the necessary header(s) to the stub response.
 * header name/value pairs should be defined as parameters of this template formatter class.
 *
 * @author Tamas_Kohegyi on 2016-02-22.
 */
public class HeaderFormatter implements ResponseFormatter {

    private final Logger logger = LoggerFactory.getLogger(HeaderFormatter.class);

    @Override
    public byte[] formatResponse(WilmaHttpRequest wilmaRequest, HttpServletResponse resp, byte[] responseResource, ParameterList params) {
        //we add/update headers based on params
        for (Parameter p : params.getAllParameters()) {
            String name = p.getName();
            String value = p.getValue();
            if (name != null && name.length() > 0 && value != null && value.length() > 0) {
                if (resp.containsHeader(name)) {
                    resp.setHeader(name, value);
                } else {
                    resp.addHeader(name, value);
                }
            } else {
                logger.warn("Template Formatter issue: specified parameters cannot be used in header: " + p.toString());
            }
        }
        //we don't touch the body part
        return responseResource;
    }
}
