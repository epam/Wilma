package com.epam.wilma.sequence.factory;
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

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.CurrentDateProvider;
import com.epam.wilma.common.helper.WilmaConstants;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.sequence.RequestResponsePair;
import com.epam.wilma.domain.stubconfig.sequence.WilmaSequence;
import com.epam.wilma.domain.stubconfig.sequence.WilmaSequencePairs;

/**
 * This class creates new {@link WilmaSequence} objects.
 * @author Tibor_Kovacs
 *
 */
@Component
public class SequenceFactory {

    @Autowired
    private CurrentDateProvider dateProvider;

    /**
     * This method creates new WilmaSequence object from the given parameters.
     * @param sequenceKey is the key of the new WilmaSequence
     * @param request is the first request of the new WilmaSequence
     * @param timeout is time in milliseconds when the sequence will be expired
     * @return with the new WilmaSequence object.
     */
    public WilmaSequence createNewSequence(final String sequenceKey, final WilmaHttpRequest request, final long timeout) {
        WilmaSequencePairs messageStore = new WilmaSequencePairs();
        RequestResponsePair firstPair = new RequestResponsePair(request);
        String loggerId = request.getExtraHeader(WilmaConstants.WILMA_LOGGER_ID.getConstant());
        messageStore.addStore(loggerId, firstPair);
        WilmaSequence sequence = new WilmaSequence(sequenceKey, new Timestamp(dateProvider.getCurrentTimeInMillis() + timeout), messageStore);
        return sequence;
    }
}
