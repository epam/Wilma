package com.epam.wilma.domain.stubconfig.parameter;
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
 * Class for storing stub configuration parameter element's name and value.
 * @author Tamas_Bihari
 *
 */
public class Parameter {
    private final String name;
    private final String value;

    /**
     * Constructs a {@link Parameter} instance with necessary fields.
     * @param parameterName is the name of the parameter
     * @param parameterValue is the value of the parameter
     */
    public Parameter(final String parameterName, final String parameterValue) {
        name = parameterName;
        value = parameterValue;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Parameter [" + name + ":" + value + "]";
    }

}
