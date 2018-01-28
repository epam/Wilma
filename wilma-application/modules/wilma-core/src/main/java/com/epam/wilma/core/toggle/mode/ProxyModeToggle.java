package com.epam.wilma.core.toggle.mode;
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

import com.epam.wilma.common.helper.OperationMode;
import com.epam.wilma.core.configuration.CoreConfigurationAccess;
import com.epam.wilma.core.configuration.domain.PropertyDto;
import com.epam.wilma.core.processor.request.WilmaHttpRequestProcessor;

/**
 * Switches proxy mode on and off in the application.
 * @author Tunde_Kovacs
 *
 */
@Component
public class ProxyModeToggle implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    @Qualifier("routerProcessor")
    private ProcessorBase routerProcessor;
    @Autowired
    private WilmaHttpRequestProcessor requestProcessor;
    @Autowired
    private CoreConfigurationAccess configurationAccess;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        PropertyDto properties = configurationAccess.getProperties();
        OperationMode operationMode = properties.getOperationMode();
        if (operationMode == OperationMode.PROXY) {
            switchProxyModeOn();
        }

    }

    /**
     * Switches on proxy mode by removing the router processor from the chain.
     */
    public void switchProxyModeOn() {
        if (requestProcessor.containsProcessor(routerProcessor)) {
            requestProcessor.disableProcessor(routerProcessor);
        }
    }

    /**
     * Switches off proxy mode by adding the router processor to the chain.
     */
    public void switchProxyModeOff() {
        if (requestProcessor.containsProcessor(routerProcessor)) {
            requestProcessor.enableProcessor(routerProcessor);
        }
    }

    /**
     * Returns true if proxy mode is turned on.
     * @return status of request message logging
     */
    public boolean isProxyModeOn() {
        return !requestProcessor.isProcessorEnabled(routerProcessor);
    }

}
