package com.epam.wilma.stubconfig.dom.validator.xsd.helper;
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

import javax.xml.XMLConstants;
import javax.xml.validation.SchemaFactory;

import org.springframework.stereotype.Component;

/**
 * Returns a {@link SchemaFactory}.
 * @author Marton_Sereg
 *
 */
@Component
public class SchemaFactoryBuilder {

    /**
     * Gets a new {@link SchemaFactory} instance.
     * @return the new instance
     */
    public SchemaFactory buildSchemaFactory() {
        return SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    }

}
