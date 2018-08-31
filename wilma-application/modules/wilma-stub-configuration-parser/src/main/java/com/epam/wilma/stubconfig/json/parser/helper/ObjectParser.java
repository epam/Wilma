package com.epam.wilma.stubconfig.json.parser.helper;
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

import org.json.JSONObject;

/**
 * Parser of a JSON object.
 * @author Tamas_Kohegyi
 * @param <T> Type of the parsed object
 */
public interface ObjectParser<T> {

    /**
     * Parses an object in a json object.
     * @param object the object that is parsed
     * @param root the root objectthat is parsed
     * @return the result of the parsed object. If input parameter <tt>object</tt>
     * is null, the result is null as well.
     */
    T parseObject(JSONObject object, JSONObject root);
}
