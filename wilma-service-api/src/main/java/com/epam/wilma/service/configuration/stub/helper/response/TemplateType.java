package com.epam.wilma.service.configuration.stub.helper.response;
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

import java.util.EnumSet;

/**
 * Possible values of template type attribute in the configuration.
 *
 * @author Tamas_Kohegyi
 */
public enum TemplateType {
    XML, TEXT, HTML, JSON, XMLFILE, TEXTFILE, HTMLFILE, JSONFILE, EXTERNAL;

    private static EnumSet<TemplateType> setOfTypesThoseNeedEscape
        = EnumSet.of(TemplateType.XML, TemplateType.TEXT, TemplateType.HTML, TemplateType.JSON);

    /**
     * Determines if the template resource value should be escaped (because it is part of the stub configuration), or not.
     *
     * @param type is the type of the template resource
     * @return true when the resource content need to be escaped
     */
    public static boolean needEscape(TemplateType type) {
        return setOfTypesThoseNeedEscape.contains(type);
    }
}
