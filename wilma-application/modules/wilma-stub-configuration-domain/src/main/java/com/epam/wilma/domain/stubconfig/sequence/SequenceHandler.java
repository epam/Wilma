package com.epam.wilma.domain.stubconfig.sequence;
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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.sequence.WilmaSequence;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Handle a group of sequences based on a given logic. A group means those sequence objects which comes from the same sequence type.
 * @author Tibor_Kovacs
 *
 */
public interface SequenceHandler {

    /**
     * This method tries to find a {@link WilmaSequence} which belongs to the given request. If the request doesn't belong to any sequence, it will return with null.
     * @param request will be handled
     * @param store contains the collection of existing sequences.
     * @param parameters the parameter list the sequence handler needs
     * @return with the key of searched sequence. It could be null!
     */
    String getExistingSequence(final WilmaHttpRequest request, final Map<String, WilmaSequence> store, final ParameterList parameters);

    /**
     * Create a new key which is unique in a type of sequences.
     * @param request will be handled
     * @param parameters the parameter list the sequence handler needs
     * @return with a new key.
     */
    String generateNewSequenceKey(final WilmaHttpRequest request, final ParameterList parameters);
}
