package com.epam.wilma.stubconfig.condition.checker.general.operator;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.stubconfig.condition.checker.general.operator.helper.RegExpPatternFactory;
import com.epam.wilma.domain.stubconfig.exception.RegularExpressionEvaluationException;

/**
 * Class for checking existence of a regular expression match in the given {@link String}.
 * @author Tamas_Bihari
 *
 */
@Component
public class RegExpConditionCheckerOperator implements ConditionCheckerOperator {

    @Autowired
    private RegExpPatternFactory patternFactory;

    @Override
    public boolean checkTarget(final String regExp, final String target) {
        boolean result = false;
        if (!"".equals(regExp)) {
            try {
                Pattern pattern = patternFactory.createPattern(regExp);
                Matcher matcher = pattern.matcher(target);
                if (matcher.find()) {
                    result = true;
                }
            } catch (Exception e) {
                throw new RegularExpressionEvaluationException("Regular expression evaluation failed with the expression: " + regExp, e);
            }
        }
        return result;
    }
}
