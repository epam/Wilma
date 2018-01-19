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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.epam.wilma.safeguard.configuration.SafeguardConfigurationAccess;
import com.epam.wilma.safeguard.configuration.domain.PropertyDTO;

/**
 * Used to start the scheduling of the queue monitor mechanism.
 * The period of the scheduling is read from an external property.
 * @author Marton_Sereg
 *
 */
@Component
public class JmsQueueMonitor {

    private static final String SAFEGUARD_INACTIVE = "0";

    private final Logger logger = LoggerFactory.getLogger(JmsQueueMonitor.class);

    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private com.epam.wilma.common.helper.CronTriggerFactory cronTriggerFactory;
    @Autowired
    private JmsQueueMonitorTask jmsQueueMonitorTask;
    @Autowired
    private SafeguardConfigurationAccess configurationAccess;

    private String cronExpression;

    /**
     * Starts the scheduling of the monitor.
     */
    public void startScheduling() {
        getCronExpression();
        if (!SAFEGUARD_INACTIVE.equals(cronExpression)) {
            CronTrigger cronTrigger = createCronTrigger(cronExpression);
            taskScheduler.schedule(jmsQueueMonitorTask, cronTrigger);
        } else {
            logger.info("No safeguard is active based on the property value of 'safeguard.guardperiod' = " + cronExpression + ".");
        }
    }

    private void getCronExpression() {
        PropertyDTO properties = configurationAccess.getProperties();
        cronExpression = properties.getCronExpression();
    }

    private CronTrigger createCronTrigger(final String cronExpression) {
        return cronTriggerFactory.createCronTrigger(cronExpression);
    }

}
