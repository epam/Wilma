package com.epam.wilma.stubconfig.dom.parser.xsd;
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

import java.net.URL;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.epam.wilma.common.helper.ResourceLoader;
import com.epam.wilma.stubconfig.dom.validator.xsd.helper.SchemaFactoryBuilder;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;

/**
 * Used to parse the stub config XSD.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class StubConfigSchemaParser {

    @Autowired
    @Qualifier("stubConfigSchemaLocation")
    private String stubConfigSchemaLocation;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private SchemaFactoryBuilder schemaFactoryBuilder;

    /**
     * Parses the XSD of the stub config.
     * @return the parsed Schema instance
     */
    public Schema parseSchema() {
        Schema result;
        try {
            URL schemaFile = resourceLoader.loadResource(stubConfigSchemaLocation);
            SchemaFactory schemaFactory = schemaFactoryBuilder.buildSchemaFactory();
            result = schemaFactory.newSchema(schemaFile);
        } catch (SAXException e) {
            throw new DescriptorValidationFailedException("Parsing of stub config XSD failed.", e);
        }
        return result;
    }

}
