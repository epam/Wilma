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

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.CronTriggerFactory;
import com.epam.wilma.domain.exception.SchedulingCannotBeStartedException;
import com.epam.wilma.sequence.maintainer.configuration.SequenceMaintainerConfigurationAccess;
import com.epam.wilma.sequence.maintainer.configuration.domain.SequenceProperties;

/**
 * This class is responsible for maintaining and scheduling the cleaning process of sequence module.
 * @author Tibor_Kovacs
 *
 */
@Component
public class SequenceMaintainer {

    private String cronExpression;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private CronTriggerFactory cronTriggerFactory;

    @Autowired
    private SequenceMaintainerConfigurationAccess configurationAccess;

    @Autowired
    private SequenceCleanerTask maintainerTask;

    /**
     * Starts the scheduling of the {@link SequenceCleanerTask}.
     */
    public void startScheduling() {
        CronTrigger cronTrigger = createCronTrigger();
        if (maintainerTask != null) {
            taskScheduler.schedule(maintainerTask, cronTrigger);
        } else {
            throw new SchedulingCannotBeStartedException("Scheduling cannot be started, in Sequence module.");
        }
    }

    /**
     * Runs the maintenance task when invoked.
     */
    public void runOnDemand() {
        taskScheduler.schedule(maintainerTask, new Date());
    }

    private CronTrigger createCronTrigger() {
        getCronExpression();
        return cronTriggerFactory.createCronTrigger(cronExpression);
    }

    private void getCronExpression() {
        SequenceProperties properties = configurationAccess.getProperties();
        cronExpression = properties.getCronExpression();
    }
}
