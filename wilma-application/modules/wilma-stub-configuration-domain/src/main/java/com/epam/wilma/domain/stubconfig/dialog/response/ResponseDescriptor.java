package com.epam.wilma.domain.stubconfig.dialog.response;
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

import java.util.Set;

import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateFormatterDescriptor;

/**
 * Describes what answer should be sent back, and in case a template is used, what method should be used to make it up-to-date.
 * @author Tunde_Kovacs
 *
 */
public class ResponseDescriptor {

    private final ResponseDescriptorAttributes attributes;
    private final Set<TemplateFormatterDescriptor> templateFormatterSet;

    /**
     * Constructs a new instance of {@link ResponseDescriptor}.
     * @param attributes includes the delay, the code, the type, the mimetype and
     * the template of the descriptor
     * @param templateFormatterDescriptorSet the set of formatter descriptors the template will be modified with
     */
    public ResponseDescriptor(final ResponseDescriptorAttributes attributes, final Set<TemplateFormatterDescriptor> templateFormatterDescriptorSet) {
        this.attributes = attributes;
        templateFormatterSet = templateFormatterDescriptorSet;
    }

    public ResponseDescriptorAttributes getAttributes() {
        return attributes;
    }

    public Set<TemplateFormatterDescriptor> getTemplateFormatters() {
        return templateFormatterSet;
    }

}
