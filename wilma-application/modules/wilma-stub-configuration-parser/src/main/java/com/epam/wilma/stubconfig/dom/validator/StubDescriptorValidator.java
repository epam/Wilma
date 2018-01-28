package com.epam.wilma.stubconfig.dom.validator;
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

import javax.xml.validation.Schema;

import org.w3c.dom.Document;

/**
 * Interface for validating the stub descriptor file.
 * @author Marton_Sereg
 *
 */
public interface StubDescriptorValidator {

    /**
     * Validates a given stub descriptor document.
     * @param document the XML document to validate
     * @param schema the XSD of the document
     */
    void validate(Document document, Schema schema);

}
