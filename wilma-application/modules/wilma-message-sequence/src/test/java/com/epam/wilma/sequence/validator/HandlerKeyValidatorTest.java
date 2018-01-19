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

import org.testng.annotations.Test;

import com.epam.wilma.sequence.handler.exception.SequenceHandlerKeyValidationException;

/**
 * Provides unit tests for the class {@link HandlerKeyValidator}.
 * @author Tibor_Kovacs
 *
 */
public class HandlerKeyValidatorTest {
    private static final String HANDLER_NAME = "Teszt";
    private HandlerKeyValidator underTest = new HandlerKeyValidator();

    @Test(expectedExceptions = SequenceHandlerKeyValidationException.class)
    public void testCheckRequestShouldThrowValidationExceptionWhenHandlerKeyContainsForbiddenCharacterVerticalbar() {
        //GIVEN
        String generatedKey = "test|Key2";
        //WHEN
        underTest.validateGeneratedKey(generatedKey, HANDLER_NAME);
        //THEN except an EXCEPTION
    }

    @Test(expectedExceptions = SequenceHandlerKeyValidationException.class)
    public void testCheckRequestShouldThrowValidationExceptionWhenHandlerKeyContainsForbiddenCharacterSemicolon() {
        //GIVEN
        String generatedKey = "testKey;2";
        //WHEN
        underTest.validateGeneratedKey(generatedKey, HANDLER_NAME);
        //THEN except an EXCEPTION
    }

    @Test(expectedExceptions = SequenceHandlerKeyValidationException.class)
    public void testCheckRequestShouldThrowValidationExceptionWhenHandlerKeyIsNull() {
        //GIVEN
        //WHEN
        underTest.validateGeneratedKey(null, HANDLER_NAME);
        //THEN except an EXCEPTION
    }
}
