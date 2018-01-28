package com.epam.wilma.stubconfig.condition.checker.general.operator.helper;
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

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

/**
 * Factory for {@link Pattern} with regular expression.
 * @author Tamas_Bihari
 *
 */
@Component
public class RegExpPatternFactory {

    /**
     * Creates a {@link Pattern} instance using the regular expression.
     * @param regularExpression the necessary regular expression
     * @return with the new instance of {@link Pattern}
     */
    public Pattern createPattern(final String regularExpression) {
        return Pattern.compile(regularExpression);
    }
}
