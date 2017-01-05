package com.epam.wilma.stubconfig.condition.checker.general.url;
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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Test class for {@link OrUrlPatternChecker}.
 * @author Tamas_Bihari
 *
 */
public class OrUrlPatternCheckerTest {

    @Mock
    private WilmaHttpRequest request;

    private ParameterList paramList;
    private OrUrlPatternChecker underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new OrUrlPatternChecker();
        paramList = new ParameterList();
    }

    @Test
    public void testCheckConditionShouldReturnFalseWhenParameterListIsEmpty() {
        //GIVEN
        //WHEN
        boolean result = underTest.checkCondition(request, paramList);
        //THEN
        assertFalse(result);
    }

    @Test
    public void testCheckConditionShouldReturnFalseWhenParameterListContainsOneWrongElement() {
        //GIVEN
        paramList.addParameter(new Parameter("key1", "aba"));
        given(request.getRequestLine()).willReturn("cba");
        //WHEN
        boolean result = underTest.checkCondition(request, paramList);
        //THEN
        assertFalse(result);
    }

    @Test
    public void testCheckConditionShouldReturnTrueWhenParameterListContainsMoreElementButAnyOfThemContaindByTheURL() {
        //GIVEN
        paramList.addParameter(new Parameter("key1", "aba"));
        paramList.addParameter(new Parameter("key2", "ba"));
        paramList.addParameter(new Parameter("key3", "a"));
        given(request.getRequestLine()).willReturn("cba");
        //WHEN
        boolean result = underTest.checkCondition(request, paramList);
        //THEN
        assertTrue(result);
    }

    @Test
    public void testCheckConditionShouldReturnTrueWhenParameterListContainsOneMatcherElement() {
        //GIVEN
        paramList.addParameter(new Parameter("key1", "aba"));
        given(request.getRequestLine()).willReturn("aba");
        //WHEN
        boolean result = underTest.checkCondition(request, paramList);
        //THEN
        assertTrue(result);
    }

    @Test
    public void testCheckConditionShouldReturnTrueWhenParameterListContainsWrongElements() {
        //GIVEN
        paramList.addParameter(new Parameter("key1", "fff"));
        paramList.addParameter(new Parameter("key2", "ff"));
        paramList.addParameter(new Parameter("key3", "fffsa"));
        given(request.getRequestLine()).willReturn("aba");
        //WHEN
        boolean result = underTest.checkCondition(request, paramList);
        //THEN
        assertFalse(result);
    }

}
