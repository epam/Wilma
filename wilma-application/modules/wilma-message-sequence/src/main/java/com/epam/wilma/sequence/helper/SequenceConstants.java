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

/**
 * Provides constants for the wilma-message-sequence module.
 * @author Tibor_Kovacs
 */
public enum SequenceConstants {

    SEQUENCE_KEY_SEPARATOR(";"),
    SEQUENCE_ID_SEPARATOR("|"),
    DESCRIPTOR_KEY_PART_SEPARATOR("_");

    private String constant;

    private SequenceConstants(final String value) {
        constant = value;
    }

    public String getConstant() {
        return constant;
    }
}
