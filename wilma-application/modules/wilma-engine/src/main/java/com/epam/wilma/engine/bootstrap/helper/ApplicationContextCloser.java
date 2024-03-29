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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 * Closes running cron trigger, executor and application context.
 * @author Tamas_Bihari
 *
 */
@Component
public class ApplicationContextCloser implements ApplicationContextAware {
    @Autowired
    private ThreadPoolTaskExecutor executor;
    @Autowired
    private ThreadPoolTaskScheduler scheduler;
    private ClassPathXmlApplicationContext appContext;

    /**
     * Closes running cron trigger, executor and application context.
     */
    public void closeTriggers() {
        scheduler.shutdown();
        executor.shutdown();
        appContext.close();
    }

    @Override
    public void setApplicationContext(final ApplicationContext appContext) {
        this.appContext = (ClassPathXmlApplicationContext) appContext;
    }

}
