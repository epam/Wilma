package com.epam.wilma.webapp.security.filter;
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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;
import com.epam.wilma.webapp.security.HostValidatorService;

/**
 * Unit test for {@link HostBasedUrlAccessSecurityFilter}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class HostBasedUrlAccessSecurityFilterTest {

    @Mock
    private HostValidatorService hostValidatorService;
    @Mock
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;

    @InjectMocks
    private HostBasedUrlAccessSecurityFilter underTest;

    @Mock
    private ServletRequest servletRequest;
    @Mock
    private ServletResponse servletResponse;
    @Mock
    private FilterChain filterChain;
    @Mock
    private PrintWriter printWriter;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testdoFilterShouldPassRequestResponseToChainIfIsAdmin() throws IOException, ServletException {
        //GIVEN
        given(hostValidatorService.isRequestFromAdmin(servletRequest)).willReturn(true);
        //WHEN
        underTest.doFilter(servletRequest, servletResponse, filterChain);
        //THEN
        verify(filterChain).doFilter(servletRequest, servletResponse);
    }

    @Test
    public void testdoFilterShouldNotPassRequestResponseToChainIfNotAdmin() throws IOException, ServletException {
        //GIVEN
        given(hostValidatorService.isRequestFromAdmin(servletRequest)).willReturn(false);
        given(servletResponse.getWriter()).willReturn(printWriter);
        //WHEN
        underTest.doFilter(servletRequest, servletResponse, filterChain);
        //THEN
        verify(filterChain, never()).doFilter(servletRequest, servletResponse);
    }

    @Test
    public void testdoFilterShouldWriteErrorToResponseIfNotAdmin() throws IOException, ServletException {
        //GIVEN
        given(hostValidatorService.isRequestFromAdmin(servletRequest)).willReturn(false);
        given(servletResponse.getWriter()).willReturn(printWriter);
        //WHEN
        underTest.doFilter(servletRequest, servletResponse, filterChain);
        //THEN
        verify(printWriter).write(Mockito.anyString());
    }
}
