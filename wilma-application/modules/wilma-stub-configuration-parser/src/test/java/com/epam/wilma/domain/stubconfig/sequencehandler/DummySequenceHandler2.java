package com.epam.wilma.domain.stubconfig.sequencehandler;
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

import java.util.Map;

import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.domain.sequence.WilmaSequence;
import com.epam.wilma.domain.stubconfig.sequence.SequenceHandler;

/**
 * Doesn't do anything useful, only used for unit testing.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class DummySequenceHandler2 implements SequenceHandler {

    @Override
    public String getExistingSequence(final WilmaHttpRequest request, final Map<String, WilmaSequence> store, final ParameterList parameters) {
        return null;
    }

    @Override
    public String generateNewSequenceKey(final WilmaHttpRequest request, final ParameterList parameters) {
        return null;
    }

}
