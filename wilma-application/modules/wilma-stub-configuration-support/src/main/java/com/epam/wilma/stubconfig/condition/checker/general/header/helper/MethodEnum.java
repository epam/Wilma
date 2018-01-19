package com.epam.wilma.stubconfig.condition.checker.general.header.helper;
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
 * Enum of Methods supported/evaluated.
 *
 * @author Tamas_Kohegyi
 */
public enum MethodEnum {
    GET("GET "),
    POST("POST "),
    PUT("PUT "),
    DELETE("DELETE "),
    OPTIONS("OPTIONS "),
    HEAD("HEAD ");

    private final String methodString;

    MethodEnum(final String methodString) {
        this.methodString = methodString;
    }

    @Override
    public String toString() {
        return methodString;
    }
}
