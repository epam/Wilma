package com.epam.wilma.webapp.config.servlet;

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
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.helper.WilmaService;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;

/**
 * Unit test for {@link ShutdownServlet}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class ShutdownServletTest {

    private static final String SHUTDOWN_MESSAGE = "Shutting down Wilma.";

    @Mock
    private Logger logger;

    @Mock
    private WilmaService wilmaService;
    @Mock
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;

    @InjectMocks
    private ShutdownServlet underTest;

    @Mock
    private HttpServletResponse resp;
    @Mock
    private HttpServletRequest req;
    @Mock
    private PrintWriter printWriter;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "logger", logger);
        Whitebox.setInternalState(underTest, "wilmaService", wilmaService);
        Whitebox.setInternalState(underTest, "urlAccessLogMessageAssembler", urlAccessLogMessageAssembler);
        given(resp.getWriter()).willReturn(printWriter);
    }

    @Test
    public void testDoGetShouldStopService() throws ServletException, IOException {
        //GIVEN in setup
        //WHEN
        underTest.doGet(req, resp);
        //THEN
        verify(wilmaService).stop();
    }

    @Test
    public void testDoGetShouldLogMessage() throws ServletException, IOException {
        //GIVEN
        given(urlAccessLogMessageAssembler.assembleMessage(req, SHUTDOWN_MESSAGE)).willReturn("logmessage");
        //WHEN
        underTest.doGet(req, resp);
        //THEN
        verify(logger).info("logmessage");
    }

    @Test
    public void testDoGetShouldWriteResponse() throws ServletException, IOException {
        //GIVEN in setup
        //WHEN
        underTest.doGet(req, resp);
        //THEN
        verify(printWriter).write(SHUTDOWN_MESSAGE);
    }

    @Test
    public void testDoPostShouldDoTheSameAsDoGet() throws ServletException, IOException {
        //GIVEN
        given(urlAccessLogMessageAssembler.assembleMessage(req, SHUTDOWN_MESSAGE)).willReturn("logmessage");
        //WHEN
        underTest.doPost(req, resp);
        //THEN
        verify(wilmaService).stop();
        verify(logger).info("logmessage");
        verify(printWriter).write(SHUTDOWN_MESSAGE);
    }

}
