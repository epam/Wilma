package com.epam.wilma.sequence.maintainer;
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
import com.epam.wilma.sequence.maintainer.configuration.SequenceMaintainerConfigurationAccess;
import com.epam.wilma.sequence.maintainer.configuration.domain.SequenceProperties;

/**
 * Unit tests for the class {@link SequenceMaintainer}.
 * @author Tibor_Kovacs
 *
 */
public class SequenceMaintainerTest {
    private static final String CRON_EXPRESSION = "0/5 * * * * * ";
    @Mock
    private TaskScheduler taskScheduler;
    @Mock
    private CronTriggerFactory cronTriggerFactory;
    @Mock
    private SequenceMaintainerConfigurationAccess configurationAccess;
    @Mock
    private SequenceCleanerTask maintainerTask;
    @Mock
    private SequenceProperties properties;
    @Mock
    private CronTrigger cronTrigger;

    @InjectMocks
    private SequenceMaintainer underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(configurationAccess.getProperties()).willReturn(properties);
        given(properties.getCronExpression()).willReturn(CRON_EXPRESSION);
    }

    @Test
    public void testStartSchedulingShouldStartSequenceCleanMaintainerTask() {
        // GIVEN
        given(cronTriggerFactory.createCronTrigger(CRON_EXPRESSION)).willReturn(cronTrigger);
        // WHEN
        underTest.startScheduling();
        // THEN
        verify(cronTriggerFactory).createCronTrigger(CRON_EXPRESSION);
        verify(taskScheduler).schedule(maintainerTask, cronTrigger);
    }

    @Test(expectedExceptions = SchedulingCannotBeStartedException.class)
    public void testStartSchedulingShouldThrowException() {
        // GIVEN
        given(cronTriggerFactory.createCronTrigger(CRON_EXPRESSION)).willReturn(cronTrigger);
        Whitebox.setInternalState(underTest, "maintainerTask", null);
        // WHEN
        underTest.startScheduling();
        // THEN EXPECT AN EXCEPTION
    }

}
