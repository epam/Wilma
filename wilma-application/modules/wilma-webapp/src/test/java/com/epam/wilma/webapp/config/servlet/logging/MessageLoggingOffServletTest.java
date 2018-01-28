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

import com.epam.wilma.core.toggle.message.MessageLoggingToggle;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;

/**
 * Test class for {@link MessageLoggingOffServlet}.
 * @author Marton_Sereg
 *
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

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "messageLoggingToggle", messageLoggingToggle);
        Whitebox.setInternalState(underTest, "urlAccessLogMessageAssembler", urlAccessLogMessageAssembler);
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
