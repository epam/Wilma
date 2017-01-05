package com.epam.wilma.stubconfig.condition.checker.general.header;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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
 * Tests for {@link OrHeaderChecker}.
 * @author Tamas_Bihari
 *
 */
public class OrHeaderCheckerTest {

    @Mock
    private WilmaHttpRequest request;
    @Mock
    private ParameterList paramList;
    @Mock
    private ConditionCheckerOperator operator;

    private List<Parameter> params;
    private OrHeaderChecker underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new OrHeaderChecker(operator);
        params = new LinkedList<>();
    }

    @Test
    public void testCheckConditionShouldReturnFalseWhenParamsIsEmpty() {
        //GIVEN
        given(paramList.getAllParameters()).willReturn(params);
        //WHEN
        boolean actual = underTest.checkCondition(request, paramList);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionShouldReturnFalseWhenOneParamIsEmptyString() {
        //GIVEN
        given(paramList.getAllParameters()).willReturn(params);
        params.add(new Parameter("", ""));
        //WHEN
        boolean actual = underTest.checkCondition(request, paramList);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionShouldReturnFalseWhenHeaderDoesNotContainParam() {
        //GIVEN
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("b", "bb");
        headers.put("c", "cc");
        given(request.getHeaders()).willReturn(headers);
        given(paramList.getAllParameters()).willReturn(params);
        params.add(new Parameter("", "aa"));
        params.add(new Parameter("", "dd"));
        //WHEN
        boolean actual = underTest.checkCondition(request, paramList);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionShouldReturnFalseWhenHeaderContainsJustTheDesiredParam() {
        //GIVEN
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("a", "aa");
        given(request.getHeaders()).willReturn(headers);
        given(paramList.getAllParameters()).willReturn(params);
        given(operator.checkTarget(anyString(), anyString())).willReturn(true);
        params.add(new Parameter("", "aa"));
        //WHEN
        boolean actual = underTest.checkCondition(request, paramList);
        //THEN
        assertTrue(actual);
    }

    @Test
    public void testCheckConditionShouldReturnTrueWhenHeaderContainsOneOfTheParams() {
        //GIVEN
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("a", "aa");
        headers.put("c", "cc");
        headers.put("b", "bb");
        given(request.getHeaders()).willReturn(headers);
        given(paramList.getAllParameters()).willReturn(params);
        params.add(new Parameter("", "aa"));
        params.add(new Parameter("", "dd"));
        given(operator.checkTarget(anyString(), anyString())).willReturn(true);
        //WHEN
        boolean actual = underTest.checkCondition(request, paramList);
        //THEN
        assertTrue(actual);
    }
}
