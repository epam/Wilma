package com.epam.wilma.stubconfig.initializer.template;
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
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateFormatter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

import javax.servlet.http.HttpServletResponse;

/**
 * Class created for testing purposes.
 *
 * @author Tunde_Kovacs
 */
public class TestTemplateFormatter implements TemplateFormatter {

    @Override
    public byte[] formatTemplate(final WilmaHttpRequest wilmaRequest, final HttpServletResponse resp,
                                 final byte[] templateResource, final ParameterList params) throws Exception {
        return null;
    }

}
