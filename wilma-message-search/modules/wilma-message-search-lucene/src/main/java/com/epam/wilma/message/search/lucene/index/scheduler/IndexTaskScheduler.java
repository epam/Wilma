package com.epam.wilma.message.search.lucene.index.scheduler;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.epam.wilma.message.search.lucene.configuration.LuceneConfigurationAccess;
import com.epam.wilma.message.search.lucene.index.scheduler.helper.CronTriggerFactory;
import com.epam.wilma.message.search.lucene.index.scheduler.helper.DateFactory;

/**
 * Schedules and runs the index related tasks.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class IndexTaskScheduler {

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;
    @Autowired
    private LuceneConfigurationAccess configurationAccess;
    @Autowired
    private CronTriggerFactory cronTriggerFactory;
    @Autowired
    private DateFactory dateFactory;

    @Autowired
    private ReindexerTask luceneReindexerTask;

    /**
     * Schedules the reindexing task.
     */
    public void startReindexScheduling() {
        String cronExpression = configurationAccess.getProperties().getReindexTimer();
        CronTrigger cronTrigger = cronTriggerFactory.create(cronExpression);
        taskScheduler.schedule(luceneReindexerTask, cronTrigger);
    }

    /**
     * Schedules the reindexing task to run now.
     */
    public void runReindexOnDemand() {
        Date now = dateFactory.createNewDate();
        taskScheduler.schedule(luceneReindexerTask, now);
    }

}
