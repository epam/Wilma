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

import com.epam.wilma.common.helper.CronTriggerFactory;
import com.epam.wilma.domain.exception.SchedulingCannotBeStartedException;
import com.epam.wilma.sequence.maintainer.configuration.SequenceMaintainerConfigurationAccess;
import com.epam.wilma.sequence.maintainer.configuration.domain.SequenceProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the class {@link SequenceMaintainer}.
 *
 * @author Tibor_Kovacs
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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

    @Test
    public void testStartSchedulingShouldThrowException() {
        Assertions.assertThrows(SchedulingCannotBeStartedException.class, () -> {
            // GIVEN
            given(cronTriggerFactory.createCronTrigger(CRON_EXPRESSION)).willReturn(cronTrigger);
            ReflectionTestUtils.setField(underTest, "maintainerTask", null);
            // WHEN
            underTest.startScheduling();
            // THEN EXPECT AN EXCEPTION
        });
    }

}
