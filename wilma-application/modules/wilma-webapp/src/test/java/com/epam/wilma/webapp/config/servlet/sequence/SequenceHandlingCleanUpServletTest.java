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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.io.IOException;

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

import com.epam.wilma.sequence.maintainer.SequenceMaintainer;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;

/**
 * Unit test for {@link SequenceHandlingOffServlet}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class SequenceHandlingCleanUpServletTest {
    private static final String MESSAGE = "Sequence maintenance task invoked manually";

    @Mock
    private SequenceMaintainer sequenceMaintainer;
    @Mock
    private Logger logger;
    @Mock
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;

    @InjectMocks
    private SequenceHandlingCleanUpServlet underTest;

    private HttpServletRequest req;
    private HttpServletResponse resp;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "logger", logger);
        Whitebox.setInternalState(underTest, "urlAccessLogMessageAssembler", urlAccessLogMessageAssembler);
        Whitebox.setInternalState(underTest, "sequenceMaintainer", sequenceMaintainer);
    }

    @Test
    public void testDoGetShouldRunMaintainerTaskOnDemand() throws ServletException, IOException {
        //GIVEN
        //WHEN
        underTest.doGet(req, resp);
        //THEN
        verify(sequenceMaintainer).runOnDemand();
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
        verify(sequenceMaintainer).runOnDemand();
        verify(urlAccessLogMessageAssembler).assembleMessage(req, MESSAGE);
        verify(logger).info("something");
    }

}
