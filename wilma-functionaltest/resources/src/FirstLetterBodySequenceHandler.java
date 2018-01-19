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
import com.epam.wilma.domain.sequence.WilmaSequence;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.domain.stubconfig.sequence.SequenceHandler;

import java.util.Map;

/**
 * {@link SequenceHandler} where sequences are determined by the request body's first letter.
 * It also marks each sequence's request after the first one with an extraheader value.
 * @author Adam_Csaba_Kiraly
 *
 */
public class FirstLetterBodySequenceHandler implements SequenceHandler {

    @Override
    public String getExistingSequence(final WilmaHttpRequest request, final Map<String, WilmaSequence> store, final ParameterList parameters) {
        String result = null;
        String key = generateNewSequenceKey(request, parameters);
        WilmaSequence sequence = store.get(key);
        if (sequence != null) {
            result = key;
            request.addHeaderUpdate("sq-end", "end");
        }
        return result;
    }

    @Override
    public String generateNewSequenceKey(final WilmaHttpRequest request, final ParameterList parameters) {
        String requestBody = request.getBody();
        return requestBody.length() > 0 ? String.valueOf(requestBody.toCharArray()[0]) : "";
    }

}
