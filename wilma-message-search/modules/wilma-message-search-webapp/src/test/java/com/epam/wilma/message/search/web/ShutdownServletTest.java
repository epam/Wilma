package com.epam.wilma.message.search.web;

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

import com.epam.wilma.message.search.web.service.WebAppStopper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link ShutdownServlet}.
 *
 * @author Adam_Csaba_Kiraly
 */
public class ShutdownServletTest {

    private static final String SHUTDOWN_MESSAGE = "Shutting down Wilma Message Search.";
    @Mock
    private WebAppStopper webAppStopper;
    @Mock
    private Logger logger;

    private ShutdownServlet underTest;

    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        PrintWriter printWriter = new PrintWriter(System.out);
        underTest = new ShutdownServlet(webAppStopper);
        ReflectionTestUtils.setField(underTest, "logger", logger);
        given(resp.getWriter()).willReturn(printWriter);
    }

    @Test
    public void testDoGetShouldLogShutdownMessage() throws ServletException, IOException {
        //GIVEN in setup
        //WHEN
        underTest.doGet(req, resp);
        //THEN
        verify(logger).info(SHUTDOWN_MESSAGE);
    }

    @Test
    public void testDoGetShouldWriteResponse() throws ServletException, IOException {
        //GIVEN in setup
        //WHEN
        underTest.doGet(req, resp);
        //THEN
        verify(resp).getWriter();
    }

    @Test
    public void testDoGetShouldStopWebApp() throws ServletException, IOException {
        //GIVEN in setup
        //WHEN
        underTest.doGet(req, resp);
        //THEN
        verify(webAppStopper).stopAsync();
    }

    @Test
    public void testDoPostShouldDoTheSameAsDoGet() throws ServletException, IOException {
        //GIVEN in setup
        //WHEN
        underTest.doPost(req, resp);
        //THEN
        verify(logger).info(SHUTDOWN_MESSAGE);
        verify(webAppStopper).stopAsync();
    }

}
