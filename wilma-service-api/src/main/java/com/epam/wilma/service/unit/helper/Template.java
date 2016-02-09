package com.epam.wilma.service.unit.helper;
/*==========================================================================
 Copyright 2013-2016 EPAM Systems

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
 * Class that holds a single response template information.
 *
 * @author Tamas_Kohegyi
 *
 */
public class Template {
    private String name;
    private String type;
    private String resource;

    /**
     * General constructor of a Template class.
     *
     * @param type can be text|xmlfile|htmlfile|textfile|external
     * @param resource is the template itself, or filename to the template or a classname that generates the template
     */
    public Template(String type, String resource) {
        this.name = "template-" + IdGenerator.getNextGeneratedId();
        this.type = type;
        this.resource = resource;
    }
    /**
     * Generates String value for the template.
     * @return with the config string
     */
    public String toString() {

        String templateString = "<template name=\"" + name + "\" type=\"" + type + "\" resource=\"" + resource + "\">\n";
        return templateString;
    }

    public String getName() {
        return name;
    }
}
