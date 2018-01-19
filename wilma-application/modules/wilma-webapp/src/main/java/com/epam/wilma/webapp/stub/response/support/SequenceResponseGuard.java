package com.epam.wilma.webapp.stub.response.support;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.sequence.WilmaSequence;
import com.epam.wilma.webapp.configuration.WebAppConfigurationAccess;
import com.epam.wilma.webapp.configuration.domain.SequenceResponseGuardProperties;
import com.epam.wilma.webapp.stub.response.exception.ResponseTimeoutException;

/**
 * Class that handles that the template formatter receives the complete sequence with all of the responses.
 * @author Adam_Csaba_Kiraly
 */
@Component
public class SequenceResponseGuard {
    private static final String TIMEOUT_ERROR_MESSAGE = "Waiting for the responses has timed out.";

    @Autowired
    private WebAppConfigurationAccess configurationAccess;

    private SequenceResponseGuardProperties properties;

    /**
     * Waits for the given sequence's responses to arrive.
     * Throws a {@link ResponseTimeoutException} if the waiting times out.
     * @param stubbedRequest the request that got stubbed
     * @param actualSequence the given request
     * @throws InterruptedException if the waiting is interrupted
     */
    public void waitForResponses(final WilmaHttpRequest stubbedRequest, final WilmaSequence actualSequence) throws InterruptedException {
        String wilmaLoggerId = stubbedRequest.getWilmaMessageId();
        boolean allResponsesArrived = actualSequence.checkIfAllResponsesArrived(wilmaLoggerId);
        int timeWaited = 0;
        getProperties();
        int waitInterval = properties.getWaitInterval();
        int timeout = properties.getTimeout();
        while (!allResponsesArrived) {
            Thread.sleep(waitInterval);
            timeWaited += waitInterval;
            if (timeWaited > timeout) {
                throw new ResponseTimeoutException(TIMEOUT_ERROR_MESSAGE);
            }
            allResponsesArrived = actualSequence.checkIfAllResponsesArrived(wilmaLoggerId);
        }

    }

    private void getProperties() {
        if (properties == null) {
            properties = configurationAccess.getProperties().getSequenceResponseGuardProperties();
        }
    }
}
