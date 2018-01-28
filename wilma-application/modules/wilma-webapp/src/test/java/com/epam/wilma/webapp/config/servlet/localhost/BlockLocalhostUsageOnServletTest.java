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
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.core.toggle.mode.LocalhostRequestProcessorToggle;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;

/**
 * Unit test for {@link BlockLocalhostUsageOnServlet}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class BlockLocalhostUsageOnServletTest {

    @Mock
    private Logger logger;
    @Mock
    private LocalhostRequestProcessorToggle localhostRequestProcessorToggle;
    @Mock
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;
    @InjectMocks
    private BlockLocalhostUsageOnServlet underTest;

    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "localhostRequestProcessorToggle", localhostRequestProcessorToggle);
        Whitebox.setInternalState(underTest, "urlAccessLogMessageAssembler", urlAccessLogMessageAssembler);
    }

    @Test
    public void doGetShouldLogMessageAtInfoLevel() throws ServletException, IOException {
        //GIVEN request and response
        Whitebox.setInternalState(underTest, "logger", logger);
        //WHEN
        underTest.doGet(req, resp);
        //THEN
        verify(logger).info(Mockito.anyString());
    }

    @Test
    public void doGetShouldSwitchLocalhostRequestProcessingOn() throws ServletException, IOException {
        //GIVEN request and response
        //WHEN
        underTest.doGet(req, resp);
        //THEN
        verify(localhostRequestProcessorToggle).switchOn();
    }

    @Test
    public void doPostShouldCallDoGet() throws ServletException, IOException {
        //GIVEN request and response
        Whitebox.setInternalState(underTest, "logger", logger);
        //WHEN
        underTest.doPost(req, resp);
        //THEN
        verify(logger).info(Mockito.anyString());
        verify(localhostRequestProcessorToggle).switchOn();
    }
}
