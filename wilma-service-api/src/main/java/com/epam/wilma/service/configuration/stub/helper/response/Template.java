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

import com.epam.wilma.service.configuration.stub.helper.common.UniqueGroupNameGenerator;

/**
 * Class that holds a single response template information.
 *
 * @author Tamas_Kohegyi
 *
 */
public class Template {
    private String name;
    private TemplateType type;
    private String resource;

    /**
     * General constructor of a Template class.
     *
     * @param type can be any Teample Type
     * @param resource is the template itself, or filename to the template or a classname that generates the template
     */
    public Template(TemplateType type, String resource) {
        this.name = "template-" + UniqueGroupNameGenerator.getNextGeneratedId();
        this.type = type;
        this.resource = resource;
    }

    /**
     * Generates String value for the template.
     * @return with the config string
     */
    @Override
    public String toString() {

        String templateString = " { \"name\": \"" + name
                + "\", \"type\": \"" + type.toString().toLowerCase()
                + "\", \"resource\": \"" + new EscapeJson().escapeJson(resource)
                + "\" }";
        return templateString;
    }

    public String getName() {
        return name;
    }
}
