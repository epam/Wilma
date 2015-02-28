package com.epam.wilma.domain.stubconfig.dialog.response.template;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.domain.stubconfig.sequence.WilmaSequence;

/**
 * Interface for stub template's formatter mechanism. All of the template formatters need to implement this interface.
 * @author Tamas_Bihari
 *
 */
public interface TemplateFormatter {

    /**
     * Formats a template with the given parameters.
     * @param wilmaRequest is a specific object, which contains the request headers and the uncompressed request body.
     * @param templateResource is the specified template which will be modified by the formatter
     * @param params is the necessary parameters of the template formatter class
     * @param sequence is that object which contains the history of the actual request
     * @throws Exception if error occurs, it will be caught by the stub mechanism
     * @return with the formatted template resource
     */
    byte[] formatTemplate(WilmaHttpRequest wilmaRequest, byte[] templateResource, final ParameterList params, final WilmaSequence sequence) throws Exception;
}
