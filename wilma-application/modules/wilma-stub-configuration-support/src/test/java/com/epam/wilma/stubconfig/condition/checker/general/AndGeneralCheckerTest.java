package com.epam.wilma.stubconfig.condition.checker.general;
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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Provides unit tests for the class {@link AndGeneralChecker}.
 * @author Tunde_Kovacs
 *
 */
public class AndGeneralCheckerTest {

    @Mock
    private ConditionChecker headerChecker;
    @Mock
    private ConditionChecker bodyChecker;
    @Mock
    private WilmaHttpRequest request;
    @Mock
    private ParameterList parameterMap;
    @Mock
    private ConditionChecker urlChecker;

    private AndGeneralChecker underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new AndGeneralChecker(headerChecker, bodyChecker, urlChecker);
    }

    @Test
    public void testCheckConditionShouldCallHeaderAndBodyPatternCheckersAndReturnWithTrue() {
        //GIVEN
        BDDMockito.given(headerChecker.checkCondition(request, parameterMap)).willReturn(true);
        BDDMockito.given(bodyChecker.checkCondition(request, parameterMap)).willReturn(true);
        BDDMockito.given(urlChecker.checkCondition(request, parameterMap)).willReturn(true);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterMap);
        //THEN
        assertTrue(actual);
    }

    @Test
    public void testCheckConditionShouldCallHeaderAndBodyPatternCheckersAndReturnWithTrueWhenPatternNotInHeader() {
        //GIVEN
        BDDMockito.given(headerChecker.checkCondition(request, parameterMap)).willReturn(false);
        BDDMockito.given(bodyChecker.checkCondition(request, parameterMap)).willReturn(true);
        BDDMockito.given(urlChecker.checkCondition(request, parameterMap)).willReturn(false);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterMap);
        //THEN
        assertTrue(actual);
    }

    @Test
    public void testCheckConditionShouldCallHeaderAndBodyPatternCheckersAndReturnWithTrueWhenPatternNotInBody() {
        //GIVEN
        BDDMockito.given(headerChecker.checkCondition(request, parameterMap)).willReturn(true);
        BDDMockito.given(bodyChecker.checkCondition(request, parameterMap)).willReturn(false);
        BDDMockito.given(urlChecker.checkCondition(request, parameterMap)).willReturn(false);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterMap);
        //THEN
        assertTrue(actual);
    }

    @Test
    public void testCheckConditionShouldCallHeaderAndBodyPatternCheckersAndReturnWithFalse() {
        //GIVEN
        BDDMockito.given(headerChecker.checkCondition(request, parameterMap)).willReturn(false);
        BDDMockito.given(bodyChecker.checkCondition(request, parameterMap)).willReturn(false);
        BDDMockito.given(urlChecker.checkCondition(request, parameterMap)).willReturn(false);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterMap);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionShouldCallUrlPatternCheckerAndReturnWithTrue() {
        //GIVEN
        BDDMockito.given(headerChecker.checkCondition(request, parameterMap)).willReturn(false);
        BDDMockito.given(bodyChecker.checkCondition(request, parameterMap)).willReturn(false);
        BDDMockito.given(urlChecker.checkCondition(request, parameterMap)).willReturn(true);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterMap);
        //THEN
        assertTrue(actual);
    }
}
