package com.epam.wilma.sequence.helper;
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

/**
 * This class provides methods to work with value of sequence extra header.
 * @author Tibor_Kovacs
 *
 */
@Component
public class SequenceHeaderUtil {

    /**
     * This method creates the extra header for WilmaHttpRequest.
     * @param headerSource is the old value.
     * @param sequenceId is the given sequence id
     * @return with a String which be able to put into extra headers. It contains all the old keys plus the new one.
     */
    public String createSequenceHeader(final String headerSource, final String sequenceId) {
        String result = "";
        if (headerSource == null) {
            result = sequenceId;
        } else {
            StringBuilder sb = new StringBuilder(headerSource);
            sb.append(SequenceConstants.SEQUENCE_KEY_SEPARATOR.getConstant()).append(sequenceId);
            result = sb.toString();
        }
        return result;
    }

    /**
     * This method does the splitting of an given String.
     * @param headerSource is the source
     * @return with the String array of sequence keys
     */
    public String[] resolveSequenceHeader(final String headerSource) {
        String[] result = new String[0];
        if (headerSource != null) {
            result = headerSource.split(SequenceConstants.SEQUENCE_KEY_SEPARATOR.getConstant());
        }
        return result;
    }
}
