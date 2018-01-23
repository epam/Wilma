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

import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.helper.OperationMode;
import com.epam.wilma.core.toggle.mode.ProxyModeToggle;
import com.epam.wilma.router.RoutingService;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;

/**
 * Provides unit tests for the class {@link WilmaModeOnServlet}.
 * @author Tunde_Kovacs
 *
 */
public class WilmaModeOnServletTest {

    @Mock
    private ProxyModeToggle proxyModeToggle;
    @Mock
    private RoutingService routingService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;

    @InjectMocks
    private WilmaModeOnServlet underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "urlAccessLogMessageAssembler", urlAccessLogMessageAssembler);
        Whitebox.setInternalState(underTest, "proxyModeToggle", proxyModeToggle);
        Whitebox.setInternalState(underTest, "routingService", routingService);
    }

    @Test
    public void testDoGetShouldSwitchOffProxyMode() throws ServletException, IOException {
        //GIVEN in setUp
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(proxyModeToggle).switchProxyModeOff();
    }

    @Test
    public void testDoGetShouldSetOpModeAndToggleInRoutingService() throws ServletException, IOException {
        //GIVEN
        OperationMode operationMode = OperationMode.WILMA;
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(routingService).setOperationMode(operationMode);
    }

    @Test
    public void testDoGetShouldAssembleMessage() throws ServletException, IOException {
        //GIVEN in setUp
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(urlAccessLogMessageAssembler).assembleMessage(Mockito.any(HttpServletRequest.class), Mockito.anyString());
    }

    @Test
    public void testDoPostShouldSwitchOffProxyMode() throws ServletException, IOException {
        //GIVEN in setUp
        //WHEN
        underTest.doPost(request, response);
        //THEN
        verify(proxyModeToggle).switchProxyModeOff();
    }
}
