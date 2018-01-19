package com.epam.wilma.sequence.validator;
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

import org.springframework.stereotype.Component;
import com.epam.wilma.sequence.handler.exception.SequenceHandlerKeyValidationException;
import com.epam.wilma.sequence.helper.SequenceConstants;

/**
 * This class validates keys of handlers.
 * @author Tibor_Kovacs
 */
@Component
public class HandlerKeyValidator {

    /**
     * This method looks for the forbidden characters in the given handler key and if it finds at least 1 forbidden character it throws an SequenceHandlerKeyValidationException.
     * @param handlerKey is the given handler key
     * @param handlerClassName is the name of the handler class
     */
    public void validateGeneratedKey(final String handlerKey, final String handlerClassName) {
        if (handlerKey == null || (handlerKey.contains(SequenceConstants.SEQUENCE_KEY_SEPARATOR.getConstant())
                || handlerKey.contains(SequenceConstants.SEQUENCE_ID_SEPARATOR.getConstant()))) {
            throw new SequenceHandlerKeyValidationException(handlerKey + " is not valid, handler(" + handlerClassName + "). Forbidden charaters: "
                    + SequenceConstants.SEQUENCE_KEY_SEPARATOR.getConstant() + " " + SequenceConstants.SEQUENCE_ID_SEPARATOR.getConstant());
        }
    }
}
