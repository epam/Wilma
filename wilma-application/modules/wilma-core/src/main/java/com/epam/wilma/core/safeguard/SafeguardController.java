package com.epam.wilma.core.safeguard;
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

import com.epam.wilma.logger.request.jms.JmsRequestLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.core.processor.response.jms.ResponseQueueListener;

/**
 * Enables or disables fastInfoset and message writing safeguard on {@link JmsRequestLogger} and {@link ResponseQueueListener}.
 * @author Marton_Sereg
 *
 */
@Component
public class SafeguardController {

    @Autowired
    private ResponseQueueListener responseQueueListener;

    @Autowired
    private JmsRequestLogger jmsRequestLogger;

    /**
     * Turns FastInfoset decompression on or off.
     * @param fIDecompressionEnabled true if decompression is enabled, false otherwise
     */
    public void setFIDecompressionEnabled(final boolean fIDecompressionEnabled) {
        responseQueueListener.setFiDecompressionEnabled(fIDecompressionEnabled);
    }

    /**
     * Turns message writing on or off.
     * @param messageWritingEnabled true if message writing is enabled, false otherwise
     */
    public void setMessageWritingEnabled(final boolean messageWritingEnabled) {
        responseQueueListener.setMessageLoggingEnabled(messageWritingEnabled);
        jmsRequestLogger.setMessageLoggingEnabled(messageWritingEnabled);
    }

}
