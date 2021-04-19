package com.epam.gepard.common;
/*==========================================================================
 Copyright 2004-2015 EPAM Systems

 This file is part of Gepard.

 Gepard is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Gepard is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Gepard.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

import java.util.Properties;

/**
 * Load property files for Gepard.
 *
 * @author Laszlo_Kishalmi
 */
class AntProperties extends Properties {

    /**
     * Creates a new instance of AntProperties.
     */
    public AntProperties() {
        super(System.getProperties());
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        Object retValue;
        if (!containsKey(key)) {
            String extValue = resolveReferences((String) value);
            retValue = super.put(key, extValue);
        } else {
            retValue = get(key);
        }
        return retValue;
    }

    String resolveReferences(final String str) {
        StringBuilder sb = new StringBuilder(str);
        int start = sb.indexOf("${");
        while (start  >= 0) {
            int end = sb.indexOf("}", start);
            String key = sb.substring(start + 2, end);
            String value = getProperty(key);
            if (value != null) {
                sb.replace(start, end + 1, value);
                start = sb.indexOf("${");
            } else {
                start = sb.indexOf("${", end);
            }
        }
        return sb.toString();
    }

}
