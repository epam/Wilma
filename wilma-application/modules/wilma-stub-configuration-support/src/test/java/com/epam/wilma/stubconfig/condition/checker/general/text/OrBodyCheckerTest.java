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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.stubconfig.condition.checker.general.operator.ConditionCheckerOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

/**
 * Tests for {@link OrBodyChecker}.
 *
 * @author Tamas_Bihari
 */
public class OrBodyCheckerTest {

    @Mock
    private ConditionCheckerOperator operator;
    @Mock
    private WilmaHttpRequest request;
    @Mock
    private ParameterList paramList;

    private List<Parameter> params;
    private OrBodyChecker underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new OrBodyChecker(operator);
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
    public void testCheckConditionShouldReturnFalseWhenBodyDoesNotContainParam() {
        //GIVEN
        given(request.getBody()).willReturn("b");
        given(paramList.getAllParameters()).willReturn(params);
        params.add(new Parameter("", "aa"));
        //WHEN
        boolean actual = underTest.checkCondition(request, paramList);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionShouldReturnTrueWhenBodyContainsParam() {
        //GIVEN
        given(request.getBody()).willReturn("baaacccacacacacaaasdfsfasfasdfafasdf");
        given(paramList.getAllParameters()).willReturn(params);
        given(operator.checkTarget(Mockito.anyString(), Mockito.anyString())).willReturn(true);
        params.add(new Parameter("", "aa"));
        //WHEN
        boolean actual = underTest.checkCondition(request, paramList);
        //THEN
        assertTrue(actual);
    }

    @Test
    public void testCheckConditionShouldReturnTrueWhenBodyContainsOneOfTheParams() {
        //GIVEN
        given(request.getBody()).willReturn("baaacccacacacacaaasdfsfasfasdfafasdf");
        given(paramList.getAllParameters()).willReturn(params);
        given(operator.checkTarget(Mockito.anyString(), Mockito.anyString())).willReturn(true);
        params.add(new Parameter("", "aa"));
        params.add(new Parameter("", "b"));
        params.add(new Parameter("", "cc"));
        //WHEN
        boolean actual = underTest.checkCondition(request, paramList);
        //THEN
        assertTrue(actual);
    }

}
