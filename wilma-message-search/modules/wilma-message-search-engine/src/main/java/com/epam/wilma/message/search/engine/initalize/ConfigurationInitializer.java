package com.epam.wilma.message.search.engine.initalize;

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

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.epam.wilma.message.search.configuration.ConfigurationAccessBase;
import com.epam.wilma.message.search.engine.bootstrap.StartUpMessageGenerator;
import com.epam.wilma.message.search.engine.properties.PropertyLoader;
import com.epam.wilma.message.search.lucene.index.scheduler.IndexTaskScheduler;

/**
 * After Spring properties are set in Spring managed classes, external message search properties are loaded and classes that need
 * external properties are initialized.
 * @author Tunde_Kovacs
 *
 */
@Component
public class ConfigurationInitializer {

    @Autowired
    private PropertyLoader propertyLoader;
    @Autowired
    private StartUpMessageGenerator startUpMessageGenerator;
    @Autowired
    private JmsConnectionConfigurer jmsConnectionConfigurer;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private List<ConfigurationAccessBase> configurationAccesses;
    private IndexTaskScheduler indexTaskScheduler;

    /**
     * This method reads in property file of wilma-message-search.
     * @throws Exception {@link PropertyLoader}, {@link ApplicationContext} can throw different exceptions.
     */
    @PostConstruct
    void afterPropertiesSet() throws Exception {
        propertyLoader.loadProperties();
        loadProperties();
        startUpMessageGenerator.logStartUpMessage();
        jmsConnectionConfigurer.setBrokerUrl();
        indexTaskScheduler = applicationContext.getBean("indexTaskScheduler", IndexTaskScheduler.class);
        indexTaskScheduler.runReindexOnDemand();
        indexTaskScheduler.startReindexScheduling();
    }

    private void loadProperties() {
        for (ConfigurationAccessBase configAccess : configurationAccesses) {
            configAccess.loadProperties();
        }
    }

}
