package com.epam.wilma.sequence.formatters.helper.converter;
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
 * Interface for the converters used in the sequence formatting process.
 * @author Adam_Csaba_Kiraly
 *
 */
public interface Converter {
    /**
     * Converts the given data.
     * @param data the given data
     * @param name optional (used in json conversion as root name)
     * @return the converted data
     */
    String convert(String data, String name);
}
