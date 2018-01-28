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

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.router.LocalhostRequestRouter;
import com.epam.wilma.router.helper.LocalhostRequestChecker;

/**
 * Unit test for {@link LocalhostRequestProcessor}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class LocalhostRequestProcessorTest {

    @Mock
    private LocalhostRequestChecker localhostRequestChecker;
    @Mock
    private LocalhostRequestRouter localhostRequestRouter;

    @InjectMocks
    private LocalhostRequestProcessor underTest;

    @Mock
    private WilmaHttpRequest request;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIfEnabledShouldNotRerouteNonLocalhostRequest() throws ApplicationException {
        //GIVEN
        underTest.setEnabled(true);
        given(localhostRequestChecker.checkIfRequestTargetsLocalhost(Mockito.any(WilmaHttpRequest.class))).willReturn(false);
        //WHEN
        underTest.process(request);
        //THEN
        verify(localhostRequestRouter, never()).reroute(request);
        verify(request, never()).setRerouted(true);
    }

    @Test
    public void testIfEnabledShouldRerouteLocalhostRequest() throws ApplicationException {
        //GIVEN
        underTest.setEnabled(true);
        given(localhostRequestChecker.checkIfRequestTargetsLocalhost(Mockito.any(WilmaHttpRequest.class))).willReturn(true);
        //WHEN
        underTest.process(request);
        //THEN
        verify(localhostRequestRouter).reroute(request);
        verify(request).setRerouted(true);
    }

}
