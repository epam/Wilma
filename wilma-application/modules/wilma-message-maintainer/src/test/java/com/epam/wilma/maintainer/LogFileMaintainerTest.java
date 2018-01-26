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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.helper.CronTriggerFactory;
import com.epam.wilma.domain.exception.SchedulingCannotBeStartedException;
import com.epam.wilma.maintainer.configuration.MaintainerConfigurationAccess;
import com.epam.wilma.maintainer.configuration.domain.MaintainerProperties;
import com.epam.wilma.maintainer.domain.MaintainerMethod;
import com.epam.wilma.maintainer.task.MaintainerTask;
import com.epam.wilma.maintainer.task.filelimit.FileLimitMaintainerTask;
import com.epam.wilma.maintainer.task.timelimit.TimeLimitMaintainerTask;

/**
 * Test class for LogFileMaintainer.
 * @author Marton_Sereg
 *
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

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Map<MaintainerMethod, MaintainerTask> maintainerTasks = new HashMap<>();
        maintainerTasks.put(MaintainerMethod.FILELIMIT, fileLimitMaintainerTask);
        maintainerTasks.put(MaintainerMethod.TIMELIMIT, timeLimitMaintainerTask);
        Whitebox.setInternalState(underTest, "maintainerTasks", maintainerTasks);
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

    @Test(expectedExceptions = SchedulingCannotBeStartedException.class)
    public void testStartSchedulingShouldThrowExceptionWhenWrongValueIsSetInProperties() {
        // GIVEN
        given(properties.getMaintainerMethod()).willReturn("asd");
        given(cronTriggerFactory.createCronTrigger(CRON_EXPRESSION)).willReturn(cronTrigger);
        // WHEN
        underTest.startScheduling();
        // THEN
        verify(taskScheduler, never()).schedule(fileLimitMaintainerTask, cronTrigger);
    }

}
