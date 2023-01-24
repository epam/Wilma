package com.epam.wilma.maintainer;
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

import com.epam.wilma.common.helper.CronTriggerFactory;
import com.epam.wilma.domain.exception.SchedulingCannotBeStartedException;
import com.epam.wilma.maintainer.configuration.MaintainerConfigurationAccess;
import com.epam.wilma.maintainer.configuration.domain.MaintainerProperties;
import com.epam.wilma.maintainer.domain.MaintainerMethod;
import com.epam.wilma.maintainer.task.MaintainerTask;
import com.epam.wilma.maintainer.task.filelimit.FileLimitMaintainerTask;
import com.epam.wilma.maintainer.task.timelimit.TimeLimitMaintainerTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Test class for LogFileMaintainer.
 *
 * @author Marton_Sereg
 */
public class LogFileMaintainerTest {

    private static final String CRON_EXPRESSION = "0/5 * * * * * ";
    @InjectMocks
    private LogFileMaintainer underTest;
    @Mock
    private TaskScheduler taskScheduler;
    @Mock
    private CronTriggerFactory cronTriggerFactory;
    @Mock
    private TimeLimitMaintainerTask timeLimitMaintainerTask;
    @Mock
    private FileLimitMaintainerTask fileLimitMaintainerTask;
    @Mock
    private CronTrigger cronTrigger;
    @Mock
    private MaintainerConfigurationAccess configurationAccess;
    @Mock
    private MaintainerProperties properties;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Map<MaintainerMethod, MaintainerTask> maintainerTasks = new HashMap<>();
        maintainerTasks.put(MaintainerMethod.FILELIMIT, fileLimitMaintainerTask);
        maintainerTasks.put(MaintainerMethod.TIMELIMIT, timeLimitMaintainerTask);
        ReflectionTestUtils.setField(underTest, "maintainerTasks", maintainerTasks);
        given(configurationAccess.getProperties()).willReturn(properties);
        given(properties.getCronExpression()).willReturn(CRON_EXPRESSION);
    }

    @Test
    public void testStartSchedulingShouldStartTimeLimitTaskWhenItIsSetInProperties() {
        // GIVEN
        given(properties.getMaintainerMethod()).willReturn("timelimit");
        given(cronTriggerFactory.createCronTrigger(CRON_EXPRESSION)).willReturn(cronTrigger);
        // WHEN
        underTest.startScheduling();
        // THEN
        verify(cronTriggerFactory).createCronTrigger(CRON_EXPRESSION);
        verify(taskScheduler).schedule(timeLimitMaintainerTask, cronTrigger);
    }

    @Test
    public void testStartSchedulingShouldStartFileLimitTaskWhenItIsSetInProperties() {
        // GIVEN
        given(properties.getMaintainerMethod()).willReturn("filelimit");
        given(cronTriggerFactory.createCronTrigger(CRON_EXPRESSION)).willReturn(cronTrigger);
        // WHEN
        underTest.startScheduling();
        // THEN
        verify(cronTriggerFactory).createCronTrigger(CRON_EXPRESSION);
        verify(taskScheduler).schedule(fileLimitMaintainerTask, cronTrigger);
    }

    @Test
    public void testStartSchedulingShouldThrowExceptionWhenWrongValueIsSetInProperties() {
        Assertions.assertThrows(SchedulingCannotBeStartedException.class, () -> {
            // GIVEN
            given(properties.getMaintainerMethod()).willReturn("asd");
            given(cronTriggerFactory.createCronTrigger(CRON_EXPRESSION)).willReturn(cronTrigger);
            // WHEN
            underTest.startScheduling();
            // THEN
            verify(taskScheduler, never()).schedule(fileLimitMaintainerTask, cronTrigger);
        });
    }

}
