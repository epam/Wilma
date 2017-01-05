package com.epam.wilma.stubconfig.initializer.template;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.domain.stubconfig.TemporaryStubResourceHolder;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateFormatter;
import com.epam.wilma.stubconfig.initializer.support.ExternalInitializer;

/**
 * Initializes template formatters with a given class.
 * @author Tunde_Kovacs
 *
 */
@Component
public class TemplateFormatterInitializer {

    @Autowired
    private StubResourcePathProvider stubResourcePathProvider;
    @Autowired
    private TemporaryStubResourceHolder resourceHolder;
    @Autowired
    private ExternalInitializer externalInitializer;

    /**
     * Retrieves the {@link TemplateFormatter} instance that will be used by wilma to format a response template.
     * Warning: <tt>DescriptorValidationFailedException</tt> is thrown if class was not found!!
     * @param className the name of the template formatter class
     * @return the new {@link TemplateFormatter} instance
     */
    public TemplateFormatter getTemplateFormatter(final String className) {
        TemplateFormatter result = null;
        Iterator<TemplateFormatter> iterator = resourceHolder.getTemplateFormatters().iterator();
        boolean doesInternalClassExist = false;
        while (iterator.hasNext() && !doesInternalClassExist) {
            TemplateFormatter next = iterator.next();
            if (next.getClass().getSimpleName().equalsIgnoreCase(className)) {
                result = next;
                doesInternalClassExist = true;
            }
        }
        if (!doesInternalClassExist) {
            result = externalInitializer.loadExternalClass(className, stubResourcePathProvider.getTemplateFormattersPathAsString(),
                    TemplateFormatter.class);
            resourceHolder.addTemplateFormatter(result);
        }
        return result;
    }
}
