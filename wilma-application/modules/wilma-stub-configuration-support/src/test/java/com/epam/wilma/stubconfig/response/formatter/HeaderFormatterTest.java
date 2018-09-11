package com.epam.wilma.stubconfig.response.formatter;
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

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Tests for {@link HeaderFormatter}.
 *
 * @author Tamas_Kohegyi
 */
public class HeaderFormatterTest {

    @Mock
    private HttpServletResponse response;
    @Mock
    private WilmaHttpRequest request;
    @Mock
    private ParameterList parameterMap;

    private HeaderFormatter underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new HeaderFormatter();
    }

    @Test
    public void testFormatterDoesNothingWhenParameterIsNull() {
        //GIVEN
        //setup
        //WHEN
        underTest.formatResponse(request, response, null, parameterMap);
        //THEN
        verifyZeroInteractions(request);
        verifyZeroInteractions(response);
    }

    @Test
    public void testFormatterDoesNothingWhenParameterIsEmpty() {
        //GIVEN
        given(parameterMap.isEmpty()).willReturn(true);
        //WHEN
        underTest.formatResponse(request, response, null, parameterMap);
        //THEN
        verifyZeroInteractions(request);
        verifyZeroInteractions(response);
    }

    @Test
    public void testFormatterAddNewHeader() {
        //GIVEN
        given(parameterMap.isEmpty()).willReturn(false);
        given(response.containsHeader("A")).willReturn(false);
        List<Parameter> parameters = new ArrayList<>();
        Parameter p = new Parameter("A", "B");
        parameters.add(p);
        given(parameterMap.getAllParameters()).willReturn(parameters);
        //WHEN
        underTest.formatResponse(request, response, null, parameterMap);
        //THEN
        verify(response).addHeader("A", "B");
    }

    @Test
    public void testFormatterUpdateHeader() {
        //GIVEN
        given(parameterMap.isEmpty()).willReturn(false);
        given(response.containsHeader("A")).willReturn(true);
        List<Parameter> parameters = new ArrayList<>();
        Parameter p = new Parameter("A", "B");
        parameters.add(p);
        given(parameterMap.getAllParameters()).willReturn(parameters);
        //WHEN
        underTest.formatResponse(request, response, null, parameterMap);
        //THEN
        verify(response).setHeader("A", "B");
    }

    @Test
    public void testFormatterDoNothingWhenParameterIsWrongNB() {
        //GIVEN
        given(parameterMap.isEmpty()).willReturn(false);
        List<Parameter> parameters = new ArrayList<>();
        Parameter p = new Parameter(null, "B");
        parameters.add(p);
        given(parameterMap.getAllParameters()).willReturn(parameters);
        //WHEN
        underTest.formatResponse(request, response, null, parameterMap);
        //THEN
        verifyZeroInteractions(request);
        verifyZeroInteractions(response);
    }

    @Test
    public void testFormatterDoNothingWhenParameterIsWrongEB() {
        //GIVEN
        given(parameterMap.isEmpty()).willReturn(false);
        List<Parameter> parameters = new ArrayList<>();
        Parameter p = new Parameter("", "B");
        parameters.add(p);
        given(parameterMap.getAllParameters()).willReturn(parameters);
        //WHEN
        underTest.formatResponse(request, response, null, parameterMap);
        //THEN
        verifyZeroInteractions(request);
        verifyZeroInteractions(response);
    }

    @Test
    public void testFormatterDoNothingWhenParameterIsWrongAE() {
        //GIVEN
        given(parameterMap.isEmpty()).willReturn(false);
        List<Parameter> parameters = new ArrayList<>();
        Parameter p = new Parameter("A", "");
        parameters.add(p);
        given(parameterMap.getAllParameters()).willReturn(parameters);
        //WHEN
        underTest.formatResponse(request, response, null, parameterMap);
        //THEN
        verifyZeroInteractions(request);
        verifyZeroInteractions(response);
    }

    @Test
    public void testFormatterDoNothingWhenParameterIsWrongAN() {
        //GIVEN
        given(parameterMap.isEmpty()).willReturn(false);
        List<Parameter> parameters = new ArrayList<>();
        Parameter p = new Parameter("A", null);
        parameters.add(p);
        given(parameterMap.getAllParameters()).willReturn(parameters);
        //WHEN
        underTest.formatResponse(request, response, null, parameterMap);
        //THEN
        verifyZeroInteractions(request);
        verifyZeroInteractions(response);
    }

}
