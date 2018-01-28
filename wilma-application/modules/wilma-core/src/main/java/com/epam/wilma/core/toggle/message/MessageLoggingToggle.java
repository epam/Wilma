package com.epam.wilma.core.toggle.message;
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

import com.epam.wilma.core.processor.entity.ProcessorBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.epam.wilma.core.configuration.CoreConfigurationAccess;
import com.epam.wilma.core.configuration.domain.PropertyDto;
import com.epam.wilma.core.processor.request.WilmaHttpRequestProcessor;
import com.epam.wilma.core.processor.response.WilmaHttpResponseProcessor;

/**
 * Switches message logging on and off.
 * @author Marton_Sereg
 *
 */
@Component
public class MessageLoggingToggle implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    @Qualifier("jmsResponseProcessor")
    private ProcessorBase jmsResponseProcessor;
    @Autowired
    @Qualifier("jmsRequestLoggerProcessor")
    private ProcessorBase jmsRequestLoggerProcessor;
    @Autowired
    private WilmaHttpRequestProcessor requestProcessor;
    @Autowired
    private WilmaHttpResponseProcessor responseProcessor;
    @Autowired
    private CoreConfigurationAccess configurationAccess;

    /**
     * It runs after all beans have been initialized and their properties are set.
     * Turns off message logging if message logging is turned off in the configuration.
     * @param event raised when an ApplicationContext gets initialized or refreshed
     */
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        PropertyDto properties = configurationAccess.getProperties();
        String messageLogging = properties.getMessageLogging();
        if (messageLogging != null && "off".equals(messageLogging)) {
            switchOffMessageLogging();
        }
    }

    /**
     * Switches off message logging by removing the logging processors from the chain.
     */
    public void switchOffMessageLogging() {
        requestProcessor.disableProcessor(jmsRequestLoggerProcessor);
        responseProcessor.disableProcessor(jmsResponseProcessor);
    }

    /**
     * Switches on message logging by adding the logging processors to the chain.
     */
    public void switchOnMessageLogging() {
        requestProcessor.enableProcessor(jmsRequestLoggerProcessor);
        responseProcessor.enableProcessor(jmsResponseProcessor);
    }

    /**
     * Returns true if request message logging is turned on.
     * @return status of request message logging
     */
    public boolean isRequestLoggingOn() {
        return requestProcessor.isProcessorEnabled(jmsRequestLoggerProcessor);
    }

    /**
     * Returns true if response message logging is turned on.
     * @return status of response message logging
     */
    public boolean isResponseLoggingOn() {
        return responseProcessor.isProcessorEnabled(jmsResponseProcessor);
    }

}
