package com.epam.wilma.stubconfig.condition.checker.general.text;

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

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.testng.Assert.assertEquals;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.stubconfig.condition.checker.general.operator.ConditionCheckerOperator;

/**
 * Tests for {@link AndBodyChecker} class.
 * @author Tamas_Bihari
 *
 */
public class AndBodyCheckerTest {

    private static final String PARAMETER_NAME = "MSG_ID";

    private static final String MSG_ID = "msgid=\"00001\"";

    @Mock
    private WilmaHttpRequest request;
    @Mock
    private ConditionCheckerOperator operator;

    private AndBodyChecker underTest;

    private ParameterList parameterList;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new AndBodyChecker(operator);
        parameterList = new ParameterList();
    }

    @Test
    public void testCheckConditionWhenConditionIsFulfilledShouldReturnTrue() {
        //GIVEN
        parameterList.addParameter(new Parameter(PARAMETER_NAME, MSG_ID));
        given(request.getBody()).willReturn(MSG_ID);
        given(operator.checkTarget(anyString(), anyString())).willReturn(true);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testCheckConditionWhenMultipleConditionsAreFulfilledShouldReturnTrue() {
        //GIVEN
        String otherValue = "other value";
        parameterList.addParameter(new Parameter(PARAMETER_NAME, MSG_ID));
        parameterList.addParameter(new Parameter("OTHER_VALUE", otherValue));
        given(request.getBody()).willReturn(MSG_ID, otherValue);
        given(operator.checkTarget(anyString(), anyString())).willReturn(true, true);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testCheckConditionWhenOneConditionIsFulfilledShouldReturnFalse() {
        //GIVEN
        String otherValue = "other value";
        parameterList.addParameter(new Parameter(PARAMETER_NAME, MSG_ID));
        parameterList.addParameter(new Parameter("OTHER_VALUE", otherValue));
        given(request.getBody()).willReturn(MSG_ID, "");
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testCheckConditionWhenOneConditionIsNotFulfilledShouldReturnFalse() {
        //GIVEN
        String otherValue = "other value";
        parameterList.addParameter(new Parameter(PARAMETER_NAME, MSG_ID));
        parameterList.addParameter(new Parameter("OTHER_VALUE", otherValue));
        given(request.getBody()).willReturn("", otherValue);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testCheckConditionWhenNoConditionIsFulfilledShouldReturnFalse() {
        //GIVEN
        String otherValue = "other value";
        parameterList.addParameter(new Parameter(PARAMETER_NAME, MSG_ID));
        parameterList.addParameter(new Parameter("OTHER_VALUE", otherValue));
        given(request.getBody()).willReturn("something else", "something else");
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testCheckConditionWhenParameterMapIsEmptyShouldReturnFalse() {
        //GIVEN
        given(request.getBody()).willReturn(MSG_ID);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testCheckConditionWhenParameterMapHasEmptyEntryShouldReturnFalse() {
        //GIVEN
        parameterList.addParameter(new Parameter(PARAMETER_NAME, ""));
        given(request.getBody()).willReturn(MSG_ID);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertEquals(actual, false);
    }

}
