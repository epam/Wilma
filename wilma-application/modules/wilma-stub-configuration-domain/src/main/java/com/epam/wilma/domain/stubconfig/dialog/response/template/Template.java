package com.epam.wilma.domain.stubconfig.dialog.response.template;
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

import java.util.Arrays;

/**
 * It describes a template file and how it should be handled when it is used as response.
 * @author Tunde_Kovacs
 *
 */
public class Template {

    private final String name;
    private final TemplateType type;
    private byte[] resource;

    /**
     * Constructs a new instance of {@link Template}.
     * @param name the unique name of the template
     * @param type the type of the resource, see {@link TemplateType}
     * @param resource in case of text, xml, html, it is embedded
     * (resource contains the value, need some protection, and only the text
     * should be implemented now), other case name of the file
     * in case of external, the resource is a class name,
     * that class is loaded and its String loadTemplateResource(name) method is called.
     */
    public Template(final String name, final TemplateType type, final byte[] resource) {
        super();
        this.name = name;
        this.type = type;
        this.resource = resource;
    }

    public String getName() {
        return name;
    }

    public TemplateType getType() {
        return type;
    }

    public byte[] getResource() {
        return resource;
    }

    public void setResource(final byte[] resource) {
        this.resource = resource;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + Arrays.hashCode(resource);
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Template)) {
            return false;
        }
        Template other = (Template) obj;
        return other.name.equals(name) && other.type.equals(type) && Arrays.equals(resource, other.resource);
    }

}
