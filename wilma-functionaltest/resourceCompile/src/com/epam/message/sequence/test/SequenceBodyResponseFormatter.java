package com.epam.message.sequence.test;
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
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.domain.sequence.RequestResponsePair;
import com.epam.wilma.domain.sequence.WilmaSequence;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class SequenceBodyResponseFormatter implements ResponseFormatter {
    private static final String PAIR_TEMPLATE = "\n pair%s - request body: '%s', response body: '%s'";

    public byte[] formatResponse(WilmaHttpRequest wilmaHttpRequest, HttpServletResponse httpServletResponse, byte[] responseResource, ParameterList parameterList) throws Exception {
        String resultString = "there is no sequence";
        WilmaSequence wilmaSequence = wilmaHttpRequest.getSequence();
        if (wilmaSequence != null) {
            resultString = "wilma-sequence key: '" + wilmaSequence.getSequenceKey() + "'";
            Map sequencePairs = wilmaSequence.getPairs();
            TreeMap treeMap = new TreeMap(sequencePairs);
            int number = 0;

            RequestResponsePair requestResponsePair;
            WilmaHttpResponse wilmaHttpResponse;
            for (Iterator iterator = treeMap.values().iterator(); iterator.hasNext();
                 resultString = resultString
                         + String.format("\n pair%s - request body: '%s', response body: '%s'", number, requestResponsePair.getRequest().getBody(), wilmaHttpResponse != null ? wilmaHttpResponse.getBody() : "null")) {
                requestResponsePair = (RequestResponsePair) iterator.next();
                ++number;
                wilmaHttpResponse = requestResponsePair.getResponse();
            }
        }

        return resultString.getBytes("UTF-8");
    }
}
