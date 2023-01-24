package com.epam.wilma.engine.bootstrap.helper;

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

import com.epam.wilma.common.helper.LogFilePathProvider;
import com.epam.wilma.common.helper.VersionTitleProvider;
import com.epam.wilma.domain.exception.SystemException;
import com.epam.wilma.engine.configuration.EngineConfigurationAccess;
import com.epam.wilma.engine.configuration.domain.PropertyDTO;
import com.google.common.util.concurrent.Service.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link WilmaServiceListenerTest}.
 *
 * @author Adam_Csaba_Kiraly
 */
public class WilmaServiceListenerTest {

    private static final String FAILED_MESSAGE = "Error occurred in Wilma";
    private static final String ERR_MSG = "System error";
    private static final String WILMA_START_MESSAGE = "wilmaStartMessage";
    private static final String TERMINATED_MESSAGE = "Wilma stopped.";
    @Mock
    private Logger logger;
    @Mock
    private VersionTitleProvider versionTitleProvider;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private LogFilePathProvider logFilePath;
    @Mock
    private EngineConfigurationAccess configurationAccess;

    @InjectMocks
    private WilmaServiceListener underTest;

    @Mock
    private PropertyDTO properties;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(underTest, "logger", logger);
        ReflectionTestUtils.setField(underTest, "wilmaStartMessage", ERR_MSG);
        given(configurationAccess.getProperties()).willReturn(properties);
        given(logFilePath.getLogFilePath().toAbsolutePath().toString()).willReturn("a");
        given(properties.getProxyPort()).willReturn(1234);
    }

    @Test
    public void testRunningShouldLogStartMessage() {
        //GIVEN
        ReflectionTestUtils.setField(underTest, "wilmaStartMessage", WILMA_START_MESSAGE);
        given(logFilePath.getLogFilePath().toAbsolutePath().toString()).willReturn(WILMA_START_MESSAGE);
        given(logFilePath.getAppLogFilePath().toAbsolutePath().toString()).willReturn(WILMA_START_MESSAGE);
        //WHEN
        underTest.running();
        //THEN
        verify(logger).info(WILMA_START_MESSAGE);
    }

    @Test
    public void testFailedShouldLogError() {
        //GIVEN
        SystemException e = new SystemException(ERR_MSG);
        State state = State.FAILED;
        //WHEN
        underTest.failed(state, e);
        //THEN
        verify(logger).error(FAILED_MESSAGE, e);
    }

    @Test
    public void testFailedWhenSystemExceptionAndErrorCauseExistsShouldLogErrorAtErrorLevel() {
        //GIVEN
        State state = State.RUNNING;
        SystemException exception = new SystemException(ERR_MSG);
        Throwable cause = new Throwable();
        exception.initCause(cause);
        //WHEN
        underTest.failed(state, exception);
        //THEN
        verify(logger).error(FAILED_MESSAGE, exception);
    }

    @Test
    public void testTerminatedShouldLogMessage() {
        //GIVEN
        State state = State.TERMINATED;
        //WHEN
        underTest.terminated(state);
        //THEN
        verify(logger).info(TERMINATED_MESSAGE);
    }

}
