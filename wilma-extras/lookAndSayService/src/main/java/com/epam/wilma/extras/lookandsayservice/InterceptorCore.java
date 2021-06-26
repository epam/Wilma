package com.epam.wilma.extras.lookandsayservice;
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

/**
 * The main class of this service - do the Look-And-Say action.
 *
 * @author tkohegyi
 */
public class InterceptorCore {

    /**
     * The main method of this service - generates the next number.
     *
     * @param input is the input string
     * @return with the result of the iteration
     */
    protected String handleIteration(final String input) {
        String response = "";
        boolean inSequence = false;
        char activeChar = ' '; //not necessary, compiler does not recognize, that it is always assigned properly
        int activeCharLength = 0; //not necessary, compiler does not recognize, that it is always assigned properly
        for (int i = 0; i < input.length(); i++) {
            if (!inSequence) {
                activeChar = input.charAt(i);
                activeCharLength = 1;
                inSequence = true;
            } else {
                if (activeChar == input.charAt(i)) {
                    activeCharLength++;
                } else {
                    response = response + activeCharLength + activeChar;
                    activeChar = input.charAt(i);
                    activeCharLength = 1;
                }
            }
        }
        if (input.length() > 0) {
            response = response + activeCharLength + activeChar;
        }
        return response;
    }

}
