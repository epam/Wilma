package com.epam.wilma.webapp.config.servlet.operation.mode;
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
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.core.toggle.mode.ProxyModeToggle;
import com.epam.wilma.router.RoutingService;

/**
 * Provides unit tests for the class {@link OperationModeStatusServlet}.
 * @author Tunde_Kovacs
 *
 */
public class OperationModeStatusServletTest {

    @Mock
    private ProxyModeToggle proxyModeToggle;
    @Mock
    private RoutingService routingService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private OperationModeStatusServlet underTest;

    @BeforeMethod
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "proxyModeToggle", proxyModeToggle);
        Whitebox.setInternalState(underTest, "routingService", routingService);
        given(response.getWriter()).willReturn(printWriter);
    }

    @Test
    public void testDoGetShouldWriteStatusToResponse() throws ServletException, IOException {
        //GIVEN
        given(proxyModeToggle.isProxyModeOn()).willReturn(true);
        given(routingService.isStubModeOn()).willReturn(false);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(printWriter).write("{\"proxyMode\":true,\"stubMode\":false,\"wilmaMode\":false}");
    }

    @Test
    public void testDoGetShouldWriteSetContentTypeToJson() throws ServletException, IOException {
        //GIVEN
        given(proxyModeToggle.isProxyModeOn()).willReturn(true);
        given(routingService.isStubModeOn()).willReturn(false);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(response).setContentType("application/json");
    }

    @Test
    public void testDoPostShouldWriteStatusToResponse() throws ServletException, IOException {
        //GIVEN
        given(proxyModeToggle.isProxyModeOn()).willReturn(true);
        given(routingService.isStubModeOn()).willReturn(false);
        //WHEN
        underTest.doPost(request, response);
        //THEN
        verify(printWriter).write("{\"proxyMode\":true,\"stubMode\":false,\"wilmaMode\":false}");
    }
}
