package com.epam.wilma.safeguard.monitor;
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
import com.epam.wilma.safeguard.configuration.SafeguardConfigurationAccess;
import com.epam.wilma.safeguard.configuration.domain.PropertyDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Test class for {@link JmsQueueMonitor}.
 *
 * @author Marton_Sereg
 */
public class JmsQueueMonitorTest {

    @InjectMocks
    private JmsQueueMonitor underTest;

    @Mock
    private TaskScheduler taskScheduler;
    @Mock
    private CronTriggerFactory cronTriggerFactory;
    @Mock
    private CronTrigger cronTrigger;
    @Mock
    private JmsQueueMonitorTask jmsQueueMonitorTask;
    @Mock
    private Logger logger;
    @Mock
    private SafeguardConfigurationAccess configurationAccess;
    @Mock
    private PropertyDTO properties;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(underTest, "logger", logger);
        given(configurationAccess.getProperties()).willReturn(properties);
    }

    @Test
    public final void testStartSchedulingShouldLogNoSafeguardInfoIfPropertyIsZero() {
        // GIVEN
        given(properties.getCronExpression()).willReturn("0");
        // WHENn
        underTest.startScheduling();
        // THEN
        verify(logger).info("No safeguard is active based on the property value of 'safeguard.guardperiod' = {}.", "0");
    }

    @Test
    public final void testStartSchedulingShouldStartSchedulingWhenCronExpressionIsNotZero() {
        // GIVEN
        String cronExpression = "0/3 * * * * *";
        given(properties.getCronExpression()).willReturn(cronExpression);
        given(cronTriggerFactory.createCronTrigger(cronExpression)).willReturn(cronTrigger);
        // WHEN
        underTest.startScheduling();
        // THEN
        verify(taskScheduler).schedule(jmsQueueMonitorTask, cronTrigger);
    }
}
