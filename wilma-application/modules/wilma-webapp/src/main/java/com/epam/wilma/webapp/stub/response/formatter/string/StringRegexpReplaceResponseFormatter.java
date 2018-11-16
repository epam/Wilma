package com.epam.wilma.webapp.stub.response.formatter.string;

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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.PatternSyntaxException;

import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatter;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.webapp.domain.exception.ResponseFormattingFailedException;

import javax.servlet.http.HttpServletResponse;

/**
 * Searches for a given regexp to be replaced in a response template. If there is more than one parameter, then
 * it will look for all of them in the template. When a parameter name is matched, it replaces the name with its value.
 * @author Tunde_Kovacs
 *
 */
@Component
public class StringRegexpReplaceResponseFormatter implements ResponseFormatter {

    @Override
    public byte[] formatResponse(final WilmaHttpRequest wilmaRequest, final HttpServletResponse resp,
                                 final byte[] templateResource, final ParameterList params) throws Exception {
        byte[] result = templateResource;
        String template = null;
        if (!params.getAllParameters().isEmpty()) {
            template = getTemplate(templateResource, params);
            result = template.getBytes(StandardCharsets.UTF_8);
        }
        return result;
    }

    private String getTemplate(final byte[] templateResource, final ParameterList params) throws IOException {
        String template = IOUtils.toString(templateResource, "utf-8");
        for (Parameter param : params.getAllParameters()) {
            String name = param.getName();
            String value = param.getValue();
            try {
                template = template.replaceAll(name, value);
            } catch (PatternSyntaxException e) {
                throw new ResponseFormattingFailedException("Pattern syntax :'" + name + "' incorrect!", e);
            }
        }
        return template;
    }

}
