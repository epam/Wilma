package com.epam.wilma.service.configuration.stub.helper.common;
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
 * Class that holds a single condition Parameter.
 *
 * @author Tamas_Kohegyi
 */
public class ConfigurationParameter {
    private String name;
    private String value;

    /**
     * Constructor when both name and value has meaning for a parameter.
     *
     * @param name  is the name of the parameter
     * @param value is the value of the parameter
     */
    public ConfigurationParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Constructor when just the value has meaning.
     * In this case the name is self generated.
     *
     * @param value is the value of the parametr
     */
    public ConfigurationParameter(String value) {
        this.name = "generatedName" + UniqueGroupNameGenerator.getNextGeneratedId();
        this.value = value;
    }

    /**
     * Generates String value for the condition parameter.
     * Example: &lt;param name="Content-Type" value="application/fastinfoset" /&gt;
     *
     * @return with the string representation of a param element
     */
    @Override
    public String toString() {
        String stringValue = "    <param name=\"" + name + "\" value=\"" + value + "\" />";
        return stringValue;
    }
}
