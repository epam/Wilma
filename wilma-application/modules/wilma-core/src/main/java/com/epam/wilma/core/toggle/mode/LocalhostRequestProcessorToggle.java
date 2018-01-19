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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.BlockLocalhostUsage;
import com.epam.wilma.core.configuration.CoreConfigurationAccess;
import com.epam.wilma.core.processor.entity.LocalhostRequestProcessor;

/**
 * Handles enabling or disabling the processor that handles localhost usage blocking.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class LocalhostRequestProcessorToggle implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private CoreConfigurationAccess configurationAccess;
    @Autowired
    private LocalhostRequestProcessor localhostRequestProcessor;

    /**
     * Disables the LocalhostRequestPreprocessor.
     */
    public void switchOff() {
        localhostRequestProcessor.setEnabled(false);
    }

    /**
     * Enables the LocalhostRequestPreprocessor.
     */
    public void switchOn() {
        localhostRequestProcessor.setEnabled(true);
    }

    public boolean isOn() {
        return localhostRequestProcessor.isEnabled();
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (configurationAccess.getProperties().getBlockLocalhostUsage() == BlockLocalhostUsage.OFF) {
            switchOff();
        } else {
            switchOn();
        }
    }
}
