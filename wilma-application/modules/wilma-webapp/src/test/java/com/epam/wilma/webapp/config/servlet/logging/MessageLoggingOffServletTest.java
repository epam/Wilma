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
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.verify;

/**
 * Test class for {@link MessageLoggingOffServlet}.
 *
 * @author Marton_Sereg
 */
public class MessageLoggingOffServletTest {

    @InjectMocks
    private MessageLoggingOffServlet underTest;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private MessageLoggingToggle messageLoggingToggle;

    @Mock
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(underTest, "messageLoggingToggle", messageLoggingToggle);
        ReflectionTestUtils.setField(underTest, "urlAccessLogMessageAssembler", urlAccessLogMessageAssembler);
    }

    @Test
    public final void testDoGetRemovesBothProcessors() throws ServletException, IOException {
        // GIVEN in setup
        // WHEN
        underTest.doGet(request, response);
        // THEN
        verify(messageLoggingToggle).switchOffMessageLogging();
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
    public final void testDoPostRemovesBothProcessors() throws ServletException, IOException {
        // GIVEN
        // WHEN
        underTest.doPost(request, response);
        // THEN
        verify(messageLoggingToggle).switchOffMessageLogging();
    }

}
