package com.epam.wilma.stubconfig.condition.checker.general.header;
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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.stubconfig.condition.checker.general.operator.ConditionCheckerOperator;

/**
 * Tests for {@link AndHeaderChecker}.
 * @author Tamas_Bihari
 *
 */
public class AndHeaderCheckerTest {

    @Mock
    private WilmaHttpRequest request;
    @Mock
    private ParameterList paramList;
    @Mock
    private ConditionCheckerOperator operator;

    private AndHeaderChecker underTest;

    private List<Parameter> params;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new AndHeaderChecker(operator);
        params = new LinkedList<>();
    }

    @Test
    public void testCheckConditionShouldReturnFalseWhenNoParameter() {
        //GIVEN
        given(paramList.getAllParameters()).willReturn(params);
        //WHEN
        boolean actual = underTest.checkCondition(request, paramList);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionShouldReturnFalseWhenParameterEmpty() {
        //GIVEN
        given(paramList.getAllParameters()).willReturn(params);
        params.add(new Parameter("a", ""));
        //WHEN
        boolean actual = underTest.checkCondition(request, paramList);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionShouldReturnFalseWhenHeadersEmpty() {
        //GIVEN
        given(paramList.getAllParameters()).willReturn(params);
        params.add(new Parameter("a", "aa"));
        //WHEN
        boolean actual = underTest.checkCondition(request, paramList);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionShouldReturnFalseWhenHeadersDoesNotContainParameter() {
        //GIVEN
        Map<String, String> headers = new HashMap<String, String>();
        String headerValue = "bb";
        headers.put("b", headerValue);
        given(request.getHeaders()).willReturn(headers);
        given(paramList.getAllParameters()).willReturn(params);
        String parameterValue = "aa";
        params.add(new Parameter("a", parameterValue));
        given(operator.checkTarget(parameterValue, headerValue)).willReturn(false);
        //WHEN
        boolean actual = underTest.checkCondition(request, paramList);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionShouldReturnTrueWhenHeadersContainsJustTheParameter() {
        //GIVEN
        Map<String, String> headers = new HashMap<String, String>();
        String parameterValue = "aa";
        headers.put("a", parameterValue);
        given(request.getHeaders()).willReturn(headers);
        given(paramList.getAllParameters()).willReturn(params);
        params.add(new Parameter("a", parameterValue));
        given(operator.checkTarget(parameterValue, parameterValue)).willReturn(true);
        //WHEN
        boolean actual = underTest.checkCondition(request, paramList);
        //THEN
        assertTrue(actual);
    }

    @Test
    public void testCheckConditionShouldReturnTrueWhenHeadersContainsParameter() {
        //GIVEN
        Map<String, String> headers = new HashMap<String, String>();
        String parameterValue = "aa";
        headers.put("a", parameterValue);
        headers.put("b", "bb");
        given(request.getHeaders()).willReturn(headers);
        given(paramList.getAllParameters()).willReturn(params);
        params.add(new Parameter("a", parameterValue));
        given(operator.checkTarget(anyString(), anyString())).willReturn(true);
        //WHEN
        boolean actual = underTest.checkCondition(request, paramList);
        //THEN
        assertTrue(actual);
    }

    @Test
    public void testCheckConditionShouldReturnFalseWhenHeadersDoesNotContainSecondParameter() {
        //GIVEN
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("a", "aa");
        headers.put("b", "bb");
        given(request.getHeaders()).willReturn(headers);
        given(paramList.getAllParameters()).willReturn(params);
        given(operator.checkTarget(anyString(), anyString())).willReturn(false);
        params.add(new Parameter("a", "aa"));
        params.add(new Parameter("c", "aac"));
        //WHEN
        boolean actual = underTest.checkCondition(request, paramList);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionShouldReturnTrueWhenHeadersContainsAllParameter() {
        //GIVEN
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("a", "aa");
        headers.put("b", "bb");
        headers.put("c", "aac");
        given(request.getHeaders()).willReturn(headers);
        given(paramList.getAllParameters()).willReturn(params);
        given(operator.checkTarget(anyString(), anyString())).willReturn(true, false, true);
        params.add(new Parameter("a", "aa"));
        params.add(new Parameter("c", "aac"));
        //WHEN
        boolean actual = underTest.checkCondition(request, paramList);
        //THEN
        assertTrue(actual);
    }
}
