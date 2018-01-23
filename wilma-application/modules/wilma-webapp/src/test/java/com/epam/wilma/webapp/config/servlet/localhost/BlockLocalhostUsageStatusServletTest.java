package com.epam.wilma.webapp.config.servlet.localhost;
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

import com.epam.wilma.core.toggle.mode.LocalhostRequestProcessorToggle;

/**
 * Unit test for {@link BlockLocalhostUsageStatusServlet}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class BlockLocalhostUsageStatusServletTest {
    @Mock
    private LocalhostRequestProcessorToggle localhostRequestProcessorToggle;

    @InjectMocks
    private BlockLocalhostUsageStatusServlet underTest;

    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private PrintWriter printWriter;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "localhostRequestProcessorToggle", localhostRequestProcessorToggle);
    }

    @Test
    public void doGetShouldWriteJsonMessage() throws ServletException, IOException {
        //GIVEN request and response
        given(resp.getWriter()).willReturn(printWriter);
        given(localhostRequestProcessorToggle.isOn()).willReturn(true);
        //WHEN
        underTest.doGet(req, resp);
        //THEN
        verify(resp).setContentType("application/json");
        verify(localhostRequestProcessorToggle).isOn();
        verify(printWriter).write("{\"localhostMode\":true}");
    }

    @Test
    public void doPostShouldCallDoGet() throws ServletException, IOException {
        //GIVEN request and response
        given(resp.getWriter()).willReturn(printWriter);
        given(localhostRequestProcessorToggle.isOn()).willReturn(true);
        //WHEN
        underTest.doPost(req, resp);
        //THEN
        verify(resp).setContentType("application/json");
        verify(localhostRequestProcessorToggle).isOn();
        verify(printWriter).write("{\"localhostMode\":true}");
    }

}
