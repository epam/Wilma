package com.epam.wilma.stubconfig.initializer.template;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateGenerator;
import com.epam.wilma.stubconfig.initializer.support.ExternalInitializer;

/**
 * Initializes stub template generator with a given class.
 * @author Tamas_Bihari
 *
 */
@Component
public class TemplateGeneratorInitializer {

    @Autowired
    private StubResourcePathProvider stubResourcePathProvider;
    @Autowired
    private ExternalInitializer externalInitializer;

    /**
     * Retrieves the {@link TemplateGenerator} instance that will be used by Wilma generate template for request.
     * Warning: <tt>DescriptorValidationFailedException</tt> is thrown if class was not found!!
     * @param className the name of the template generator class
     * @return the new {@link TemplateGenerator} instance
     */
    public TemplateGenerator getTemplateGenerator(final String className) {
        return externalInitializer.loadExternalClass(className, stubResourcePathProvider.getTemplatesPathAsString(), TemplateGenerator.class);
    }
}
