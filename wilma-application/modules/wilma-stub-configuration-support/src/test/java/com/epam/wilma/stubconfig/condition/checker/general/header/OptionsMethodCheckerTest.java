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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Tests for {@link OptionsMethodChecker}.
 *
 * @author Tamas_Kohegyi
 */
public class OptionsMethodCheckerTest {

    @Mock
    private WilmaHttpRequest request;
    @Mock
    private ParameterList paramList;

    private List<Parameter> params;
    private OptionsMethodChecker underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new OptionsMethodChecker();
        params = new LinkedList<>();
    }

    @Test
    public void testCheckConditionShouldReturnTrueWhenRequestWhatWeExpect() {
        //GIVEN
        given(paramList.getAllParameters()).willReturn(params);
        given(request.getRequestLine()).willReturn("OPTIONS BLAH");
        //WHEN
        boolean actual = underTest.checkCondition(request, paramList);
        //THEN
        assertTrue(actual);
    }

    @Test
    public void testCheckConditionShouldReturnFalseWhenRequestWhatWeDoNotExpect() {
        //GIVEN
        given(paramList.getAllParameters()).willReturn(params);
        given(request.getRequestLine()).willReturn("PUSH BLAH");
        //WHEN
        boolean actual = underTest.checkCondition(request, paramList);
        //THEN
        assertFalse(actual);
    }

}
