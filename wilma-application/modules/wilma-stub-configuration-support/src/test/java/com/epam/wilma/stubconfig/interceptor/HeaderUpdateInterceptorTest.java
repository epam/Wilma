package com.epam.wilma.stubconfig.interceptor;
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
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Tests for {@link HeaderUpdateInterceptor}.
 *
 * @author Tamas_Kohegyi
 */
public class HeaderUpdateInterceptorTest {

    @Mock
    private WilmaHttpRequest request;
    @Mock
    private WilmaHttpResponse response;
    @Mock
    private ParameterList parameterMap;

    private HeaderUpdateInterceptor underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new HeaderUpdateInterceptor();
    }

    @Test
    public void testInterceptorDoesNothingWhenParameterIsNull() {
        //GIVEN
        //setup
        //WHEN
        underTest.onRequestReceive(request, null);
        underTest.onResponseReceive(response, null);
        //THEN
        verifyZeroInteractions(request);
        verifyZeroInteractions(response);
    }

    @Test
    public void testInterceptorDoesNothingWhenParameterIsEmpty() {
        //GIVEN
        given(parameterMap.isEmpty()).willReturn(true);
        //WHEN
        underTest.onRequestReceive(request, parameterMap);
        underTest.onResponseReceive(response, parameterMap);
        //THEN
        verifyZeroInteractions(request);
        verifyZeroInteractions(response);
    }

    @Test
    public void testRequestInterceptorMarksHeadersToBeRemoved() {
        //GIVEN
        given(parameterMap.isEmpty()).willReturn(false);
        List<Parameter> parameters = new ArrayList<>();
        Parameter p = new Parameter(HeaderUpdateInterceptor.REMOVE_HEADER, "B");
        parameters.add(p);
        given(parameterMap.getAllParameters()).willReturn(parameters);
        //WHEN
        underTest.onRequestReceive(request, parameterMap);
        //THEN
        verify(request).addHeaderRemove("B");
    }

    @Test
    public void testResponseInterceptorMarksHeadersToBeRemoved() {
        //GIVEN
        List<Parameter> parameters = new ArrayList<>();
        Parameter p = new Parameter(HeaderUpdateInterceptor.REMOVE_HEADER, "B");
        parameters.add(p);
        given(parameterMap.getAllParameters()).willReturn(parameters);
        //WHEN
        underTest.onResponseReceive(response, parameterMap);
        //THEN
        verify(response).addHeaderRemove("B");
    }

    @Test
    public void testRequestInterceptorMarksHeadersToBeUpdated() {
        //GIVEN
        given(parameterMap.isEmpty()).willReturn(false);
        List<Parameter> parameters = new ArrayList<>();
        Parameter p = new Parameter("A", "B");
        parameters.add(p);
        given(parameterMap.getAllParameters()).willReturn(parameters);
        //WHEN
        underTest.onRequestReceive(request, parameterMap);
        //THEN
        verify(request).addHeaderUpdate("A", "B");
    }

    @Test
    public void testResponseInterceptorMarksHeadersToBeUpdated() {
        //GIVEN
        List<Parameter> parameters = new ArrayList<>();
        Parameter p = new Parameter("A", "B");
        parameters.add(p);
        given(parameterMap.getAllParameters()).willReturn(parameters);
        //WHEN
        underTest.onResponseReceive(response, parameterMap);
        //THEN
        verify(response).addHeaderUpdate("A", "B");
    }

}
