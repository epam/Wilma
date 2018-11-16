package com.epam.wilma.stubconfig.dom.parser.node.helper;
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

/**
 * This enum stores the stub configuration XML condition tag's possible names of child elements.
 * @author Tamas_Bihari
 *
 */
public enum ConditionTagNames {
    TAG_NAME_COND_SET_INVOKER("condition-set-invoker"),
    TAG_NAME_CONDITION("condition"),
    TAG_NAME_NOT("not"),
    TAG_NAME_OR("or"),
    TAG_NAME_AND("and"),
    TAG_NAME_INVALID("");

    private String xmlTagName;

    private ConditionTagNames(final String xmlTagName) {
        this.xmlTagName = xmlTagName;
    }

    /**
     * Returns the condition tag's child name enum value based on the XML tag name.
     * @param xmlTagName is the XML tag name
     * @return the mapped enum value
     */
    public static ConditionTagNames getTagName(final String xmlTagName) {
        ConditionTagNames result = ConditionTagNames.TAG_NAME_INVALID;
        if (xmlTagName != null) {
            boolean limitNotFound = false;
            ConditionTagNames[] limits = ConditionTagNames.values();
            for (int i = 0; i < limits.length && !limitNotFound; i++) {
                if (xmlTagName.equals(limits[i].xmlTagName)) {
                    result = limits[i];
                    limitNotFound = true;
                }
            }
        }
        return result;
    }
}
