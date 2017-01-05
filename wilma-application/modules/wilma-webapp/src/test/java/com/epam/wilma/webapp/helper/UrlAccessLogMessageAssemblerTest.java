package com.epam.wilma.webapp.helper;
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
import static org.mockito.Mockito.verify;
import static org.testng.AssertJUnit.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for {@link UrlAccessLogMessageAssembler}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class UrlAccessLogMessageAssemblerTest {

    @Mock
    private IpAddressResolver ipAddressResolver;

    @InjectMocks
    private UrlAccessLogMessageAssembler underTest;

    @Mock
    private HttpServletRequest request;

    private String message;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAssembleMessageShouldGetRemoteAddressOfRequest() {
        //GIVEN request
        //WHEN
        underTest.assembleMessage(request, message);
        //THEN
        verify(request).getRemoteAddr();
    }

    @Test
    public void testAssembleMessageShouldResolveIpOfRequestToHostName() {
        //GIVEN
        given(request.getRemoteAddr()).willReturn("ipAddress");
        //WHEN
        underTest.assembleMessage(request, message);
        //THEN
        verify(ipAddressResolver).resolveToHostName("ipAddress");
    }

    @Test
    public void testAssembleMessageShoulReturnAnAssembledMessage() {
        //GIVEN
        String expectedResult = "URL: 'null', accessed from: 'null', message: 'null'";
        //WHEN
        String actualResult = underTest.assembleMessage(request, message);
        //THEN
        assertEquals(expectedResult, actualResult);
    }
}
