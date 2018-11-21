package com.epam.wilma.message.search.web.controller;

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

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.epam.wilma.message.search.jms.helper.JmxConnectionBuilder;
import com.epam.wilma.message.search.jms.helper.JmxObjectNameProvider;

/**
 * Controller for load information.
 * @author Tibor_Kovacs
 */
@Controller
public class LoadInformationController {
    static final String JMS_SERVICE_URL = "service:jmx:rmi:///jndi/rmi://localhost:2015/jmxrmi";
    static final String JMS_QUEUE_OBJECT_NAME = "org.apache.activemq:type=Broker,brokerName=localhost,destinationType=Queue,destinationName=queue";

    private final Logger logger = LoggerFactory.getLogger(LoadInformationController.class);

    @Autowired
    private JmxConnectionBuilder jmxConnectionBuilder;
    @Autowired
    private JmxObjectNameProvider jmxObjectNameProvider;

    private MBeanServerConnection jmsQueueConnection;
    private ObjectName jmsQueue;

    /**
     * Returns the JMS queue size as a JSON response.
     * @return the JSON response containing the queue size
     */
    @ResponseBody
    @RequestMapping(value = "/loadinformation", method = RequestMethod.GET)
    public Map<String, Long> getLoadInformation() {
        Map<String, Long> loadInformation = new HashMap<>();
        buildConnection();
        try {
            Long jmsQueueSize = (Long) jmsQueueConnection.getAttribute(jmsQueue, "QueueSize");
            loadInformation.put("loadInformation", jmsQueueSize);
        } catch (Exception e) {
            logger.error("Exception while monitoring queue sizes", e);
        }
        return loadInformation;
    }

    private void buildConnection() {
        if (jmsQueueConnection == null || jmsQueue == null) {
            jmsQueueConnection = jmxConnectionBuilder.buildMBeanServerConnection(JMS_SERVICE_URL);
            jmsQueue = jmxObjectNameProvider.getObjectName(JMS_QUEUE_OBJECT_NAME);
        }
    }
}
