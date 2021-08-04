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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.condition.CompositeCondition;
import com.epam.wilma.domain.stubconfig.dialog.condition.Condition;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionType;
import com.epam.wilma.domain.stubconfig.dialog.condition.SimpleCondition;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 * Provides unit tests for the class {@link ConditionEvaluator}.
 *
 * @author Tunde_Kovacs, Tamas_Kohegyi
 */
public class ConditionEvaluatorTest {

    @Mock
    private Condition condition;
    @Mock
    private ConditionChecker conditionCheckerA;
    @Mock
    private ConditionChecker conditionCheckerB;
    @Mock
    private WilmaHttpRequest request;
    @Mock
    private SimpleCondition simpleConditionA;
    @Mock
    private SimpleCondition simpleConditionB;
    @Mock
    private CompositeCondition compositeConditionMock;

    private CompositeCondition compositeCondition;

    private ConditionEvaluator underTest;
    private ParameterList parameterMap;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new ConditionEvaluator();
        parameterMap = new ParameterList();
        given(simpleConditionA.getConditionChecker()).willReturn(conditionCheckerA);
        given(simpleConditionA.getParameters()).willReturn(parameterMap);
        given(simpleConditionB.getConditionChecker()).willReturn(conditionCheckerB);
        given(simpleConditionB.getParameters()).willReturn(parameterMap);
    }

    @Test
    public void testEvaluateWhenConditionIsAndShouldReturnTrueTT() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleConditionA);
        conditionList.add(simpleConditionB);
        compositeCondition = new CompositeCondition(ConditionType.AND, conditionList);
        given(conditionCheckerA.checkCondition(request, parameterMap)).willReturn(true);
        given(conditionCheckerB.checkCondition(request, parameterMap)).willReturn(true);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testEvaluateWhenConditionIsAndShouldReturnFalseFF() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleConditionA);
        conditionList.add(simpleConditionB);
        compositeCondition = new CompositeCondition(ConditionType.AND, conditionList);
        given(conditionCheckerA.checkCondition(request, parameterMap)).willReturn(false);
        given(conditionCheckerB.checkCondition(request, parameterMap)).willReturn(false);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testEvaluateWhenConditionIsAndShouldReturnFalseTF() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleConditionA);
        conditionList.add(simpleConditionB);
        compositeCondition = new CompositeCondition(ConditionType.AND, conditionList);
        given(conditionCheckerA.checkCondition(request, parameterMap)).willReturn(true);
        given(conditionCheckerB.checkCondition(request, parameterMap)).willReturn(false);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testEvaluateWhenConditionIsAndShouldReturnFalseFT() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleConditionA);
        conditionList.add(simpleConditionB);
        compositeCondition = new CompositeCondition(ConditionType.AND, conditionList);
        given(conditionCheckerA.checkCondition(request, parameterMap)).willReturn(false);
        given(conditionCheckerB.checkCondition(request, parameterMap)).willReturn(true);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testEvaluateWhenConditionIsOrShouldReturnTrueTT() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleConditionA);
        conditionList.add(simpleConditionB);
        compositeCondition = new CompositeCondition(ConditionType.OR, conditionList);
        given(conditionCheckerA.checkCondition(request, parameterMap)).willReturn(true);
        given(conditionCheckerB.checkCondition(request, parameterMap)).willReturn(true);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testEvaluateWhenConditionIsOrShouldReturnTrueTF() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleConditionA);
        conditionList.add(simpleConditionB);
        compositeCondition = new CompositeCondition(ConditionType.OR, conditionList);
        given(conditionCheckerA.checkCondition(request, parameterMap)).willReturn(true);
        given(conditionCheckerB.checkCondition(request, parameterMap)).willReturn(false);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testEvaluateWhenConditionIsOrShouldReturnTrueFT() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleConditionA);
        conditionList.add(simpleConditionB);
        compositeCondition = new CompositeCondition(ConditionType.OR, conditionList);
        given(conditionCheckerA.checkCondition(request, parameterMap)).willReturn(false);
        given(conditionCheckerB.checkCondition(request, parameterMap)).willReturn(true);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testEvaluateWhenConditionIsOrShouldReturnFalseFF() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleConditionA);
        conditionList.add(simpleConditionB);
        compositeCondition = new CompositeCondition(ConditionType.OR, conditionList);
        given(conditionCheckerA.checkCondition(request, parameterMap)).willReturn(false);
        given(conditionCheckerB.checkCondition(request, parameterMap)).willReturn(false);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testEvaluateWhenConditionIsNotShouldReturnFalse() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleConditionA);
        compositeCondition = new CompositeCondition(ConditionType.NOT, conditionList);
        given(conditionCheckerA.checkCondition(request, parameterMap)).willReturn(true);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testEvaluateWhenConditionIsNotShouldReturnTrue() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleConditionA);
        compositeCondition = new CompositeCondition(ConditionType.NOT, conditionList);
        given(conditionCheckerA.checkCondition(request, parameterMap)).willReturn(false);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testEvaluateWhenSimpleConditionIsNegatedShouldReturnTrue() {
        //GIVEN
        given(simpleConditionA.isNegate()).willReturn(true);
        given(conditionCheckerA.checkCondition(request, parameterMap)).willReturn(true);
        //WHEN
        boolean actual = underTest.evaluate(simpleConditionA, request);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testEvaluateWhenSimpleConditionIsNegatedShouldReturnFalse() {
        //GIVEN
        given(simpleConditionA.isNegate()).willReturn(true);
        given(conditionCheckerA.checkCondition(request, parameterMap)).willReturn(false);
        //WHEN
        boolean actual = underTest.evaluate(simpleConditionA, request);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testEvaluateWhenConditionIsNestedOrShouldReturnTrueTT_T() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleConditionA);
        conditionList.add(simpleConditionA);
        CompositeCondition nestedCondition = new CompositeCondition(ConditionType.OR, new ArrayList<>(conditionList));
        conditionList.clear();
        conditionList.add(simpleConditionA);
        conditionList.add(nestedCondition);
        compositeCondition = new CompositeCondition(ConditionType.OR, conditionList);
        given(conditionCheckerA.checkCondition(request, parameterMap)).willReturn(true);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testEvaluateWhenConditionIsNestedOrShouldReturnFalseFF_F() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleConditionA);
        conditionList.add(simpleConditionA);
        CompositeCondition nestedCondition = new CompositeCondition(ConditionType.OR, new ArrayList<>(conditionList));
        conditionList.clear();
        conditionList.add(simpleConditionB);
        conditionList.add(nestedCondition);
        compositeCondition = new CompositeCondition(ConditionType.OR, conditionList);
        given(conditionCheckerA.checkCondition(request, parameterMap)).willReturn(false);
        given(conditionCheckerB.checkCondition(request, parameterMap)).willReturn(false);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testEvaluateWhenConditionIsNestedOrShouldReturnTrueTF_F() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleConditionA);
        conditionList.add(simpleConditionB);
        CompositeCondition nestedCondition = new CompositeCondition(ConditionType.OR, new ArrayList<>(conditionList));
        conditionList.clear();
        conditionList.add(simpleConditionB);
        conditionList.add(nestedCondition);
        compositeCondition = new CompositeCondition(ConditionType.OR, conditionList);
        given(conditionCheckerA.checkCondition(request, parameterMap)).willReturn(true);
        given(conditionCheckerB.checkCondition(request, parameterMap)).willReturn(false);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testEvaluateWhenConditionIsNestedAndShouldReturnTrueTT_T() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleConditionA);
        conditionList.add(simpleConditionA);
        CompositeCondition nestedCondition = new CompositeCondition(ConditionType.AND, new ArrayList<>(conditionList));
        conditionList.clear();
        conditionList.add(simpleConditionB);
        conditionList.add(nestedCondition);
        compositeCondition = new CompositeCondition(ConditionType.AND, conditionList);
        given(conditionCheckerA.checkCondition(request, parameterMap)).willReturn(true);
        given(conditionCheckerB.checkCondition(request, parameterMap)).willReturn(true);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testEvaluateWhenConditionIsNestedAndShouldReturnFalseTF_T() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleConditionA);
        conditionList.add(simpleConditionB);
        CompositeCondition nestedCondition = new CompositeCondition(ConditionType.AND, new ArrayList<>(conditionList));
        conditionList.clear();
        conditionList.add(simpleConditionA);
        conditionList.add(nestedCondition);
        compositeCondition = new CompositeCondition(ConditionType.AND, conditionList);
        given(conditionCheckerA.checkCondition(request, parameterMap)).willReturn(true);
        given(conditionCheckerB.checkCondition(request, parameterMap)).willReturn(false);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testEvaluateWhenConditionIsNestedAndShouldReturnFalseTT_F() {
        //GIVEN
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(simpleConditionA);
        conditionList.add(simpleConditionA);
        CompositeCondition nestedCondition = new CompositeCondition(ConditionType.AND, new ArrayList<>(conditionList));
        conditionList.clear();
        conditionList.add(simpleConditionB);
        conditionList.add(nestedCondition);
        compositeCondition = new CompositeCondition(ConditionType.AND, conditionList);
        given(conditionCheckerA.checkCondition(request, parameterMap)).willReturn(true);
        given(conditionCheckerB.checkCondition(request, parameterMap)).willReturn(false);
        //WHEN
        boolean actual = underTest.evaluate(compositeCondition, request);
        //THEN
        assertEquals(actual, false);
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
