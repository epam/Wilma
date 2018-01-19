package com.epam.wilma.stubconfig.dom.validator.xsd;
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
import javax.xml.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.epam.wilma.stubconfig.dom.validator.StubDescriptorValidator;
import com.epam.wilma.stubconfig.dom.validator.xsd.helper.DOMSourceFactory;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;

/**
 * Validates the stub descriptor with the corresponding XSD schema file.
 * @author Marton_Sereg
 *
 */
@Component
public class XsdStubDescriptorValidator implements StubDescriptorValidator {

    @Autowired
    private DOMSourceFactory domSourceFactory;

    @Override
    public void validate(final Document document, final Schema schema) {
        try {
            Validator validator = schema.newValidator();
            validator.validate(domSourceFactory.newDOMSource(document));
        } catch (Exception e) {
            throw new DescriptorValidationFailedException("Validation of stub descriptor failed - XSD validation failed.", e);
        }

    }
}
