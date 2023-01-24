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

import com.epam.wilma.core.toggle.mode.LocalhostRequestProcessorToggle;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link BlockLocalhostUsageOffServlet}.
 *
 * @author Adam_Csaba_Kiraly
 */
public class BlockLocalhostUsageOffServletTest {

    private final String messageAssemblerResult = "messageAssemblerResult";

    @Mock
    private Logger logger;
    @Mock
    private LocalhostRequestProcessorToggle localhostRequestProcessorToggle;
    @Mock
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;
    @InjectMocks
    private BlockLocalhostUsageOffServlet underTest;

    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(underTest, "localhostRequestProcessorToggle", localhostRequestProcessorToggle);
        ReflectionTestUtils.setField(underTest, "urlAccessLogMessageAssembler", urlAccessLogMessageAssembler);
    }

    @Test
    public void doGetShouldLogMessageAtInfoLevel() throws ServletException, IOException {
        //GIVEN request and response
        ReflectionTestUtils.setField(underTest, "logger", logger);
        given(urlAccessLogMessageAssembler.assembleMessage(any(), anyString())).willReturn(messageAssemblerResult);
        //WHEN
        underTest.doGet(req, resp);
        //THEN
        verify(logger).info(Mockito.anyString());
    }

    @Test
    public void doGetShouldSwitchLocalhostRequestProcessingOff() throws ServletException, IOException {
        //GIVEN request and response
        //WHEN
        underTest.doGet(req, resp);
        //THEN
        verify(localhostRequestProcessorToggle).switchOff();
    }

    @Test
    public void doPostShouldCallDoGet() throws ServletException, IOException {
        //GIVEN request and response
        ReflectionTestUtils.setField(underTest, "logger", logger);
        given(urlAccessLogMessageAssembler.assembleMessage(any(), anyString())).willReturn(messageAssemblerResult);
        //WHEN
        underTest.doPost(req, resp);
        //THEN
        verify(logger).info(Mockito.anyString());
        verify(localhostRequestProcessorToggle).switchOff();
    }
}
