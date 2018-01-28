package com.epam.wilma.core.toggle.sequence;
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

import com.epam.wilma.common.helper.SequenceHandlingState;
import com.epam.wilma.core.configuration.CoreConfigurationAccess;
import com.epam.wilma.core.processor.entity.SequenceRequestHandlingProcessor;
import com.epam.wilma.sequence.SequenceManager;

/**
 * Handles the enabling/disabling of sequence handling functionality.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class SequenceHandlingToggle implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private CoreConfigurationAccess configurationAccess;
    @Autowired
    private SequenceRequestHandlingProcessor sequenceRequestHandlingProcessor;
    @Autowired
    private SequenceManager sequenceManager;

    /**
     * Turns on the sequence handling functionality.
     */
    public void switchOn() {
        sequenceRequestHandlingProcessor.setEnabled(true);
    }

    /**
     * Turns off the sequence handling functionality and drops all of the collected sequences of sequence descriptors.
     */
    public void switchOff() {
        sequenceManager.dropAllSequences();
        sequenceRequestHandlingProcessor.setEnabled(false);
    }

    public boolean isOn() {
        return sequenceRequestHandlingProcessor.isEnabled();
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (configurationAccess.getProperties().getSequenceHandlingUsage() == SequenceHandlingState.ON) {
            switchOn();
        } else {
            switchOff();
        }
    }
}
