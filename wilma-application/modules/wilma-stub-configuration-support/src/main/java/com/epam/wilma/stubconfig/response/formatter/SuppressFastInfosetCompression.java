package com.epam.wilma.stubconfig.response.formatter;
/*==========================================================================
Copyright 2013-2018 EPAM Systems

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
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

import javax.servlet.http.HttpServletResponse;

/**
 * A built-in template formatter class.
 * This class ensures that during generation of the response,
 * the final response will not be encoded with fastinfoset even if the request allows it.
 *
 * @author Tamas_Kohegyi on 2018-01-15.
 */
public class SuppressFastInfosetCompression implements ResponseFormatter {

    static final String FASTINFOSET = "fastinfoset";
    static final String ACCEPT_VALUE_FASTINFOSET = "application/fastinfoset";
    static final String ACCEPT_HEADER_KEY = "Accept";
    static final String HEADER_KEY_SUPPRESS_ENCODING = "Wilma-Suppress-Encoding";

    @Override
    public byte[] formatResponse(WilmaHttpRequest wilmaRequest, HttpServletResponse resp,
                                 byte[] responseResource, ParameterList params) {
        //force turn off gzip compression usage for this answer
        String acceptHeader = wilmaRequest.getHeader(ACCEPT_HEADER_KEY);
        if (acceptHeader != null && acceptHeader.contains(ACCEPT_VALUE_FASTINFOSET)) {
            String existingHeaderContent = resp.getHeader(HEADER_KEY_SUPPRESS_ENCODING);
            String newHeaderContent = existingHeaderContent == null ? FASTINFOSET : existingHeaderContent + "," + FASTINFOSET;
            resp.addHeader(HEADER_KEY_SUPPRESS_ENCODING, newHeaderContent);
        }
        //we don't touch the body part
        return responseResource;
    }
}
