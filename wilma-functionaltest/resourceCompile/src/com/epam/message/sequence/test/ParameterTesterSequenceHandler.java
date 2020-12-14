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

import java.util.Iterator;
import java.util.Map;

public class ParameterTesterSequenceHandler implements SequenceHandler {

    public String getExistingSequence(WilmaHttpRequest wilmaHttpRequest, Map<String, WilmaSequence> wilmaSequenceMap, ParameterList parameterList) {
        String result = null;
        Iterator iterator = wilmaSequenceMap.keySet().iterator();

        while (iterator.hasNext()) {
            String next = (String) iterator.next();
            if (next.equals(parameterList.get("SampleHandlerKey"))) {
                result = next;
                break;
            }
        }

        return result;
    }

    public String generateNewSequenceKey(WilmaHttpRequest wilmaHttpRequest, ParameterList parameterList) {
        String result = parameterList.get("SampleHandlerKey");
        return result;
    }
}
