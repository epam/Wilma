package com.epam.wilma.stubconfig.condition.checker.json;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.jayway.jsonpath.JsonPath;

/**
 * Checks whether a selected property of the captured Json request equals to the
 * expected value.
 * <ul>
 * <li>Expected parameter key {@link JsonPathChecker#EXPECTED_KEY}.</li>
 * <li>JSON path parameter key {@link JsonPathChecker#JSONPATH_KEY}.</li>
 * </ul>
 *
 * @author Balazs_Berkes
 */
@Component
public class JsonPathChecker implements ConditionChecker {

    private static final String EXPECTED_KEY = "expected";
    private static final String JSONPATH_KEY = "jsonPath";
    private final Logger logger = LoggerFactory.getLogger(JsonPathChecker.class);

    @Override
    public boolean checkCondition(final WilmaHttpRequest request, final ParameterList parameters) {
        String expected = parameters.get(EXPECTED_KEY);
        String path = parameters.get(JSONPATH_KEY);
        boolean equals;
        try {
            String actualValue = JsonPath.read(request.getBody(), path);
            equals = evaluate(expected, actualValue);
        } catch (Exception e) {
            logger.debug("Request body isn't of JSON format, message:" + request.getWilmaMessageLoggerId(), e);
            equals = false;
        }
        return equals;
    }

    /**
     * Performs a check whether the condition checker should catch the request.
     * @param expected provided from stub config with key {@link JsonPathChecker#EXPECTED_KEY}
     * @param actualValue the result of the JSON path query given in stub config {@link JsonPathChecker#JSONPATH_KEY}.
     * @return {@code true} if the checker should check the request otherwise false.
     */
    protected boolean evaluate(final String expected, final String actualValue) {
        return expected.equals(actualValue);
    }
}
