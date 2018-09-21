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
import java.util.Map;
import java.util.TreeMap;

/**
 * Response formatter that responds with the sequence request and response body pairs.
 * @author Adam_Csaba_Kiraly
 */
public class SequenceBodyResponseFormatter implements ResponseFormatter {

    private static final String PAIR_TEMPLATE = "\n pair%s - request body: '%s', response body: '%s'";

    @Override
    public byte[] formatResponse(final WilmaHttpRequest wilmaRequest, final HttpServletResponse resp,
                                 final byte[] templateResource, final ParameterList params) throws Exception {
        String result = "there is no sequence";
        WilmaSequence sequence = wilmaRequest.getSequence();
        if (sequence != null) {
            result = "wilma-sequence key: '" + sequence.getSequenceKey() + "'";
            Map<String, RequestResponsePair> messages = sequence.getPairs();
            Map<String, RequestResponsePair> sortedMessages = new TreeMap<>(messages);
            int i = 0;
            for (RequestResponsePair pair : sortedMessages.values()) {
                i++;
                WilmaHttpResponse response = pair.getResponse();
                result += String.format(PAIR_TEMPLATE, i, pair.getRequest().getBody(), response != null ? response.getBody() : "null");
            }
        }
        return result.getBytes("UTF-8");
    }

}
