package com.epam.wilma.engine.properties.validation;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import ognl.Ognl;
import ognl.OgnlException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.properties.InvalidPropertyException;
import com.epam.wilma.properties.PropertyHolder;
import com.epam.wilma.properties.validation.SafeguardLimitValidator;

/**
 * Validates configuration properties.
 * @author Tunde_Kovacs
 *
 */
@Component
public class PropertyValidator {

    private Properties properties;

    @Autowired
    private PropertyHolder propertyHolder;
    @Autowired
    private SafeguardLimitValidator safeguardLimitValidator;

    /**
     * Validates configuration properties.
     */
    public void validateProperties() {
        validate();
        //special safeguard limit validation that could not be made with Ognl
        safeguardLimitValidator.validate();

    }

    public void setProperties(final Properties properties) {
        this.properties = properties;

    }

    private void validate() {
        Set<Object> keySet = properties.keySet();
        Map<String, String> invalidProperties = new HashMap<>();
        for (Object key : keySet) {
            String validationExpression = properties.getProperty(key.toString());
            String propertyValue = propertyHolder.get(key.toString());
            boolean valid = false;
            try {
                valid = (boolean) Ognl.getValue(validationExpression, propertyValue);
            } catch (NumberFormatException e) {
                throw new InvalidPropertyException("The property '" + key + "'=" + propertyValue + " failed validation! Wilma is stopping now!", e);
            } catch (OgnlException e) {
                throw new InvalidPropertyException("The property '" + key + "' failed validation! Invalid expression '" + validationExpression + "'",
                        e);
            }
            if (!valid) {
                invalidProperties.put(key.toString(), validationExpression);
            }
        }
        if (!invalidProperties.isEmpty()) {
            throw new InvalidPropertyException("The validation of properties: " + invalidProperties.toString() + " failed! Wilma is stopping now!");
        }
    }

}
