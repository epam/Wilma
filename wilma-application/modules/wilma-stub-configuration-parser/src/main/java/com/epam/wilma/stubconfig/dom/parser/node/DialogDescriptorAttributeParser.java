package com.epam.wilma.stubconfig.dom.parser.node;
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
import org.w3c.dom.Element;

import com.epam.wilma.common.helper.CurrentDateProvider;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptorUsage;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;

/**
 * Parses the attributes of a dialog descriptor element.
 * @author Tunde_Kovacs
 *
 */
@Component
public class DialogDescriptorAttributeParser {

    private static final long MILLISECOND_MULTIPLIER = 60000;
    @Autowired
    private CurrentDateProvider currentDateProvider;

    /**
     * Parses the attributes of a dialog descriptor element.
     * @param el the dialog descriptor that's attributes are parsed
     * @return a new {@link DialogDescriptorAttributes} instance
     */
    public DialogDescriptorAttributes getAttributes(final Element el) {
        String name = el.getAttribute("name");
        DialogDescriptorUsage usage = getUsage(el.getAttribute("usage"));
        long validityValue = getValidityValue(el.getAttribute("validityValue"), name, usage);
        String comment = el.getAttribute("comment");
        DialogDescriptorAttributes attributes = createAttributes(name, usage, validityValue, comment);
        return attributes;
    }

    private DialogDescriptorAttributes createAttributes(final String name, final DialogDescriptorUsage usage, final long validityValue,
            final String comment) {
        DialogDescriptorAttributes attributes = new DialogDescriptorAttributes(name, usage);
        setValidityValue(attributes, validityValue);
        attributes.setComment(comment);
        return attributes;
    }

    private long getValidityValue(final String validityString, final String name, final DialogDescriptorUsage usage) {
        long validityValue = getValidityValue(validityString, usage);
        validateValidityValueVersusUsage(name, usage, validityValue);
        return validityValue;
    }

    private void setValidityValue(final DialogDescriptorAttributes attributes, final long validityValue) {
        DialogDescriptorUsage usage = attributes.getUsage();
        if (usage == DialogDescriptorUsage.HITCOUNT) {
            attributes.setHitcount(validityValue);
        } else if (usage == DialogDescriptorUsage.TIMEOUT) {
            attributes.setTimeout(validityValue);
        }
    }

    private long getValidityValue(final String validityString, final DialogDescriptorUsage usage) {
        long validityValue = -1;
        if (validityString != null && !validityString.isEmpty()) {
            if (usage == DialogDescriptorUsage.TIMEOUT) {
                long currentTimeInMillis = currentDateProvider.getCurrentTimeInMillis();
                validityValue = currentTimeInMillis + Integer.valueOf(validityString) * MILLISECOND_MULTIPLIER;
            } else {
                validityValue = Integer.valueOf(validityString);
            }
        }
        return validityValue;
    }

    private void validateValidityValueVersusUsage(final String name, final DialogDescriptorUsage usage, final long validityValue) {
        if (validityValue == -1 && (usage == DialogDescriptorUsage.HITCOUNT || usage == DialogDescriptorUsage.TIMEOUT)) {
            throw new DescriptorValidationFailedException("The '" + name + "' dialog descriptor's usage is " + usage
                    + ", please provide the 'validityValue' attribute as well!");
        }
    }

    private DialogDescriptorUsage getUsage(final String usage) {
        return DialogDescriptorUsage.valueOf(usage.toUpperCase());
    }
}
