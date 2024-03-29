package com.epam.wilma.webapp.config.servlet.logging;
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

import com.epam.wilma.core.toggle.message.MessageLoggingToggle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link MessageLoggingStatusServlet}.
 *
 * @author Marton_Sereg
 */
public class MessageLoggingStatusServletTest {

    @InjectMocks
    private MessageLoggingStatusServlet underTest;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private MessageLoggingToggle messageLoggingToggle;

    @Mock
    private PrintWriter printWriter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(underTest, "messageLoggingToggle", messageLoggingToggle);
    }

    @Test
    public final void testDoGetSetsResponseTypeAndWritesResponse() throws ServletException, IOException {
        // GIVEN
        given(response.getWriter()).willReturn(printWriter);
        given(messageLoggingToggle.isRequestLoggingOn()).willReturn(true);
        given(messageLoggingToggle.isResponseLoggingOn()).willReturn(true);
        // WHEN
        underTest.doGet(request, response);
        // THEN
        verify(response).setContentType("application/json");
        verify(messageLoggingToggle).isRequestLoggingOn();
        verify(messageLoggingToggle).isResponseLoggingOn();
        verify(printWriter).write("{\"requestLogging\":true,\"responseLogging\":true}");
    }

    @Test
    public final void testDoPostSetsResponseTypeAndWritesResponse() throws ServletException, IOException {
        // GIVEN
        given(response.getWriter()).willReturn(printWriter);
        given(messageLoggingToggle.isRequestLoggingOn()).willReturn(false);
        given(messageLoggingToggle.isResponseLoggingOn()).willReturn(false);
        // WHEN
        underTest.doPost(request, response);
        // THEN
        verify(response).setContentType("application/json");
        verify(messageLoggingToggle).isRequestLoggingOn();
        verify(messageLoggingToggle).isResponseLoggingOn();
        verify(printWriter).write("{\"requestLogging\":false,\"responseLogging\":false}");
    }

}
