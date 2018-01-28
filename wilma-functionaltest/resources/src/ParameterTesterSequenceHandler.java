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
 * This class needs to get a parameter and use that as a SequenceKey.
 * @author Tibor_Kovacs
 *
 */
public class ParameterTesterSequenceHandler implements SequenceHandler {

    @Override
    public String getExistingSequence(final WilmaHttpRequest request, final Map<String, WilmaSequence> store, final ParameterList parameters) {
        String result = null;
        for(String key : store.keySet()){
            if(key.equals(parameters.get("SampleHandlerKey"))){
                result = key;
                break;
            }
        }
        return result;
    }

    @Override
    public String generateNewSequenceKey(final WilmaHttpRequest request, final ParameterList parameters) {
        String newKey = parameters.get("SampleHandlerKey");
        return newKey;
    }
}
