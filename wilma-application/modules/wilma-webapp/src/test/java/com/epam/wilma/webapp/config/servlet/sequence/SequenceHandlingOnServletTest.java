package com.epam.wilma.webapp.config.servlet.sequence;
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

import com.epam.wilma.core.toggle.sequence.SequenceHandlingToggle;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link SequenceHandlingOnServlet}.
 *
 * @author Adam_Csaba_Kiraly
 */
public class SequenceHandlingOnServletTest {

    private static final String MESSAGE = "Sequence handling: ON";

    @Mock
    private SequenceHandlingToggle sequenceHandlingToggle;
    @Mock
    private Logger logger;
    @Mock
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;

    @InjectMocks
    private SequenceHandlingOnServlet underTest;

    private HttpServletRequest req;
    private HttpServletResponse resp;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(underTest, "logger", logger);
        ReflectionTestUtils.setField(underTest, "sequenceHandlingToggle", sequenceHandlingToggle);
        ReflectionTestUtils.setField(underTest, "urlAccessLogMessageAssembler", urlAccessLogMessageAssembler);
    }

    @Test
    public void testDoGetShouldSwitchSequenceHandlingOff() throws ServletException, IOException {
        //GIVEN
        //WHEN
        underTest.doGet(req, resp);
        //THEN
        verify(sequenceHandlingToggle).switchOn();
    }

    @Test
    public void testDoGetShouldLogMessage() throws ServletException, IOException {
        //GIVEN
        given(urlAccessLogMessageAssembler.assembleMessage(req, MESSAGE)).willReturn("something");
        //WHEN
        underTest.doGet(req, resp);
        //THEN
        verify(urlAccessLogMessageAssembler).assembleMessage(req, MESSAGE);
        verify(logger).info("something");
    }

    @Test
    public void testDoPostShouldCallDoGet() throws ServletException, IOException {
        //GIVEN
        given(urlAccessLogMessageAssembler.assembleMessage(req, MESSAGE)).willReturn("something");
        //WHEN
        underTest.doPost(req, resp);
        //THEN
        verify(sequenceHandlingToggle).switchOn();
        verify(urlAccessLogMessageAssembler).assembleMessage(req, MESSAGE);
        verify(logger).info("something");
    }

}
