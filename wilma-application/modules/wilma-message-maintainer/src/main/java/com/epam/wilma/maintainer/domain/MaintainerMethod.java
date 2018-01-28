package com.epam.wilma.maintainer.domain;
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
 * This enum stores the maintainer methods and maps the maintainer method types given in properties file to these enum values.
 * @author Marton_Sereg
 *
 */
public enum MaintainerMethod {
    TIMELIMIT("timelimit"),
    FILELIMIT("filelimit"),
    INVALID("");

    private String propertyName;

    private MaintainerMethod(final String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * Returns the maintainer method enum value based on the maintainer method property value.
     * @param propertyName the property value
     * @return the mapped enum value
     */
    public static MaintainerMethod getMethod(final String propertyName) {
        MaintainerMethod result = INVALID;
        if (propertyName != null) {
            boolean limitNotFound = false;
            MaintainerMethod[] limits = MaintainerMethod.values();
            for (int i = 0; i < limits.length && !limitNotFound; i++) {
                if (propertyName.equals(limits[i].propertyName)) {
                    result = limits[i];
                    limitNotFound = true;
                }
            }
        }
        return result;
    }
}
