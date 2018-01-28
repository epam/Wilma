package com.epam.wilma.router.evaluation;
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
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.condition.CompositeCondition;
import com.epam.wilma.domain.stubconfig.dialog.condition.Condition;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionType;
import com.epam.wilma.domain.stubconfig.dialog.condition.SimpleCondition;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Provides unit tests for the class {@link ConditionEvaluator}.
 * @author Tunde_Kovacs
 *
 */
public class ConditionEvaluatorTest {

    @Mock
    private Condition condition;
    @Mock
    private ConditionChecker conditionChecker;
    @Mock
    private WilmaHttpRequest request;
    @Mock
    private SimpleCondition simpleCondition;
    @Mock
    private CompositeCondition compositeConditionMock;

    private CompositeCondition compositeCondition;

    private ConditionEvaluator underTest;
    private ParameterList parameterMap;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new ConditionEvaluator();
        parameterMap = new ParameterList();
        given(simpleCondition.getConditionChecker()).willReturn(conditionChecker);
        given(simpleCondition.getParameters()).willReturn(parameterMap);
    }

    @Test
    public void testEvaluateWhenConditionIsAndShouldReturnTrue() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleCondition);
        conditionList.add(simpleCondition);
        compositeCondition = new CompositeCondition(ConditionType.AND, conditionList);
        given(conditionChecker.checkCondition(request, parameterMap)).willReturn(true);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testEvaluateWhenConditionIsAndShouldReturnFalse() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleCondition);
        conditionList.add(simpleCondition);
        compositeCondition = new CompositeCondition(ConditionType.AND, conditionList);
        given(conditionChecker.checkCondition(request, parameterMap)).willReturn(false);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testEvaluateWhenConditionIsOrShouldReturnTrue() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleCondition);
        conditionList.add(simpleCondition);
        compositeCondition = new CompositeCondition(ConditionType.OR, conditionList);
        given(conditionChecker.checkCondition(request, parameterMap)).willReturn(true);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testEvaluateWhenConditionIsOrShouldReturnFalse() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleCondition);
        conditionList.add(simpleCondition);
        compositeCondition = new CompositeCondition(ConditionType.OR, conditionList);
        given(conditionChecker.checkCondition(request, parameterMap)).willReturn(false);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testEvaluateWhenConditionIsNotShouldReturnFalse() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleCondition);
        conditionList.add(simpleCondition);
        compositeCondition = new CompositeCondition(ConditionType.NOT, conditionList);
        given(conditionChecker.checkCondition(request, parameterMap)).willReturn(true);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testEvaluateWhenSimpleConditionIsNegatedShouldReturnTrue() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleCondition);
        conditionList.add(simpleCondition);
        compositeCondition = new CompositeCondition(ConditionType.NOT, conditionList);
        given(simpleCondition.isNegate()).willReturn(true);
        given(conditionChecker.checkCondition(request, parameterMap)).willReturn(true);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testEvaluateWhenSimpleConditionReturnsFalseAndIsNotShouldReturnTrue() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleCondition);
        conditionList.add(simpleCondition);
        compositeCondition = new CompositeCondition(ConditionType.NOT, conditionList);
        given(conditionChecker.checkCondition(request, parameterMap)).willReturn(false);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testEvaluateWhenConditionIsNestedOrShouldReturnTrue() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleCondition);
        conditionList.add(simpleCondition);
        CompositeCondition nestedCondition = new CompositeCondition(ConditionType.OR, new ArrayList<>(conditionList));
        conditionList.clear();
        conditionList.add(simpleCondition);
        conditionList.add(nestedCondition);
        compositeCondition = new CompositeCondition(ConditionType.OR, conditionList);
        given(conditionChecker.checkCondition(request, parameterMap)).willReturn(true);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testEvaluateWhenConditionIsNestedAndShouldReturnTrue() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleCondition);
        conditionList.add(simpleCondition);
        CompositeCondition nestedCondition = new CompositeCondition(ConditionType.AND, new ArrayList<>(conditionList));
        conditionList.clear();
        conditionList.add(simpleCondition);
        conditionList.add(nestedCondition);
        compositeCondition = new CompositeCondition(ConditionType.AND, conditionList);
        given(conditionChecker.checkCondition(request, parameterMap)).willReturn(true);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testEvaluateWhenConditionIsNotInstanceOfAnyImplementationShouldReturnFalse() {
        //GIVEN in setUp
        //WHEN
        boolean actual = underTest.evaluate(condition, request);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testEvaluateWhenCompositeConditionIsNotInstanceOfAnyImplementationShouldReturnFalse() {
        //GIVEN in setUp
        //WHEN
        boolean actual = underTest.evaluate(compositeConditionMock, request);
        //THEN
        assertEquals(actual, false);
    }
}
