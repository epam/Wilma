package com.epam.wilma.core.processor.entity;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.router.Router;

/**
 * Provides unit tests for the class {@link RouterProcessor}.
 * @author Tunde_Kovacs
 *
 */
public class RouterProcessorTest {

    private static final String BYPASS_VALUE = "WilmaBypass=true";

    @Mock
    private WilmaHttpRequest request;
    @Mock
    private Router router;

    @InjectMocks
    private RouterProcessor underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessShouldCallRouter() throws ApplicationException {
        //GIVEN in setUp
        Map<String, String> headers = new HashMap<>();
        headers.put("1", "1");
        given(request.getHeaders()).willReturn(headers);
        //WHEN
        underTest.process(request);
        //THEN
        verify(router).reroute(request);
    }

    @Test
    public void testProcessShouldWhenByPassExistsInHeaderNameShouldNotRedirectRequest() throws ApplicationException {
        //GIVEN in setUp
        Map<String, String> headers = new HashMap<>();
        headers.put(BYPASS_VALUE, "");
        given(request.getHeaders()).willReturn(headers);
        //WHEN
        underTest.process(request);
        //THEN
        verify(router, never()).reroute(request);
    }

    @Test
    public void testProcessShouldWhenByPassExistsInHeaderValueShouldNotRedirectRequest() throws ApplicationException {
        //GIVEN in setUp
        Map<String, String> headers = new HashMap<>();
        headers.put("1", BYPASS_VALUE);
        given(request.getHeaders()).willReturn(headers);
        //WHEN
        underTest.process(request);
        //THEN
        verify(router, never()).reroute(request);
    }

    @Test
    public void testProcessShouldWhenByPassExistsInHeaderNameAndValueShouldNotRedirectRequest() throws ApplicationException {
        //GIVEN in setUp
        Map<String, String> headers = new HashMap<>();
        headers.put("1", "1");
        headers.put(BYPASS_VALUE, BYPASS_VALUE);
        given(request.getHeaders()).willReturn(headers);
        //WHEN
        underTest.process(request);
        //THEN
        verify(router, never()).reroute(request);
    }
}
