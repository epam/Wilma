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

import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Unit test for {@link JsonPathRegExChecker}.
 *
 * @author Balazs_Berkes
 */
public class JsonPathRegExCheckerTest {

    private static final String EXPECTED_KEY = "expected";
    private static final String JSONPATH_KEY = "jsonPath";

    private WilmaHttpRequest request;
    private ParameterList parameters;

    private JsonPathRegExChecker underTest;

    @BeforeClass
    public void setup() {
        underTest = new JsonPathRegExChecker();
    }

    @Test
    public void testCheckConditionShouldPassWhenPropertyMatches() {
        givenExpectations("Wilma.*", "$.name");
        givenWilmaRequest("{\"name\":\"Wilma Test\",\"age\":\"20\"}");

        boolean matches = underTest.checkCondition(request, parameters);

        assertTrue(matches);
    }

    private void givenWilmaRequest(String body) {
        request = new WilmaHttpRequest();
        request.setBody(body);
    }

    private void givenExpectations(String expected, String path) {
        parameters = new ParameterList();
        parameters.addParameter(new Parameter(EXPECTED_KEY, expected));
        parameters.addParameter(new Parameter(JSONPATH_KEY, path));
    }
}
