package com.epam.wilma.core.jms;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

/**
 * Error handler for response queue.
 * @author Adam_Csaba_Kiraly
 */
@Component
public class ResponseErrorHandler implements ErrorHandler {

    private static final String ERROR_MESSAGE = "Error occurred in response queue: ";
    private final Logger logger = LoggerFactory.getLogger(LoggerErrorHandler.class);

    @Override
    public void handleError(final Throwable t) {
        logger.warn(ERROR_MESSAGE + t.getLocalizedMessage(), t);
    }

}
