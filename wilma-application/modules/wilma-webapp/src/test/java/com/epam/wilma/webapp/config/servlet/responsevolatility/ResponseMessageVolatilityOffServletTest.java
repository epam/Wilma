package com.epam.wilma.webapp.config.servlet.responsevolatility;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;
import net.lightbody.bmp.proxy.ProxyServer;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.verify;

/**
 * Test class for {@link ResponseMessageVolatilityOffServlet}.
 *
 * @author Tamas_Kohegyi
 */
public class ResponseMessageVolatilityOffServletTest {

    @InjectMocks
    private ResponseMessageVolatilityOffServlet underTest;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public final void testDoGetShouldAssembleMessage() throws ServletException, IOException {
        // GIVEN in setup
        // WHEN
        underTest.doGet(request, response);
        // THEN
        verify(urlAccessLogMessageAssembler).assembleMessage(Mockito.any(HttpServletRequest.class), Mockito.anyString());
    }

    @Test
    public final void testDoGetShouldTurnResponseVolatilityOff() throws ServletException, IOException {
        // GIVEN in setup
        ProxyServer.setResponseVolatile(true);
        // WHEN
        underTest.doGet(request, response);
        // THEN
        Assert.assertTrue(!ProxyServer.getResponseVolatile());
    }

}
