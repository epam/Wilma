package com.epam.message.sequence.test;
/*==========================================================================
Copyright 2013-2016 EPAM Systems

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

import java.util.Map;
import java.util.TreeMap;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateFormatter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.domain.stubconfig.sequence.RequestResponsePair;
import com.epam.wilma.domain.stubconfig.sequence.WilmaSequence;

/**
 * Template formatter that responds with the sequence request and response body pairs.
 * @author Adam_Csaba_Kiraly
 */
public class SequenceBodyTemplateFormatter implements TemplateFormatter {

    private static final String PAIR_TEMPLATE = "\n pair%s - request body: '%s', response body: '%s'";

    @Override
    public byte[] formatTemplate(final WilmaHttpRequest wilmaRequest, final byte[] templateResource, final ParameterList params,
            final WilmaSequence sequence) throws Exception {
        String result = "wilma-sequence key: ";
        if (sequence != null) {
            result += sequence.getSequenceKey();
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
