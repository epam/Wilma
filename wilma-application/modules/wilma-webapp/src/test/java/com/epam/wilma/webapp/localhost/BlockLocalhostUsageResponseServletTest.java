package com.epam.wilma.webapp.localhost;
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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for {@link BlockLocalhostUsageResponseServlet}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class BlockLocalhostUsageResponseServletTest {

    private static final int ERROR_CODE = 404;
    private static final String TEXT_PLAIN = "text/plain";
    private static final String BLOCKED_REQUEST_MESSAGE = "Wilma is configured to ignore request targeting to localhost/127.0.0.1";

    @InjectMocks
    private BlockLocalhostUsageResponseServlet underTest;

    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private PrintWriter printWriter;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoGetShouldWriteResponse() throws ServletException, IOException {
        //GIVEN
        given(resp.getWriter()).willReturn(printWriter);
        //WHEN
        underTest.doGet(req, resp);
        //THEN
        verify(resp).setContentType(TEXT_PLAIN);
        verify(resp).setStatus(ERROR_CODE);
        verify(printWriter).write(BLOCKED_REQUEST_MESSAGE);
    }

    @Test
    public void testDoPostShouldDoTheSameAsDoGet() throws ServletException, IOException {
        //GIVEN
        given(resp.getWriter()).willReturn(printWriter);
        //WHEN
        underTest.doPost(req, resp);
        //THEN
        verify(resp).setContentType(TEXT_PLAIN);
        verify(resp).setStatus(ERROR_CODE);
        verify(printWriter).write(BLOCKED_REQUEST_MESSAGE);
    }
}
