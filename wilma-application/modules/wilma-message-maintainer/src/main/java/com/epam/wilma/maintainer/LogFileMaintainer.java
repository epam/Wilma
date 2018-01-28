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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.CronTriggerFactory;
import com.epam.wilma.domain.exception.SchedulingCannotBeStartedException;
import com.epam.wilma.maintainer.configuration.MaintainerConfigurationAccess;
import com.epam.wilma.maintainer.configuration.domain.MaintainerProperties;
import com.epam.wilma.maintainer.domain.MaintainerMethod;
import com.epam.wilma.maintainer.task.MaintainerTask;

/**
 * This class is responsible for maintaining the log files by deleting old ones.
 * @author Marton_Sereg
 *
 */
@Component
public class LogFileMaintainer {

    private String cronExpression;
    private String maintainerMethod;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private CronTriggerFactory cronTriggerFactory;

    @Autowired
    private MaintainerConfigurationAccess configurationAccess;

    @Resource
    private final Map<MaintainerMethod, MaintainerTask> maintainerTasks = new HashMap<>();

    /**
     * Starts the scheduling of the LogFileMaintainerTask.
     */
    public void startScheduling() {
        CronTrigger cronTrigger = createCronTrigger();
        MaintainerMethod method = getMaintainerMethod();
        MaintainerTask maintainerTask = maintainerTasks.get(method);
        if (maintainerTask != null) {
            taskScheduler.schedule(maintainerTask, cronTrigger);
            maintainerTask.logParameters();
        } else {
            throw new SchedulingCannotBeStartedException("Scheduling cannot be started, maintenance method given in properties file is not valid!");
        }
    }

    private MaintainerMethod getMaintainerMethod() {
        MaintainerProperties properties = configurationAccess.getProperties();
        maintainerMethod = properties.getMaintainerMethod();
        return MaintainerMethod.getMethod(maintainerMethod);
    }

    private CronTrigger createCronTrigger() {
        getCronExpression();
        return cronTriggerFactory.createCronTrigger(cronExpression);
    }

    private void getCronExpression() {
        MaintainerProperties properties = configurationAccess.getProperties();
        cronExpression = properties.getCronExpression();
    }
}
