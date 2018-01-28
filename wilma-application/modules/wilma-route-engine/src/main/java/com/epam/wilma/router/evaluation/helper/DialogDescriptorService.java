package com.epam.wilma.router.evaluation.helper;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.CurrentDateProvider;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptorUsage;

/**
 * Provides services for a {@link DialogDescriptor}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class DialogDescriptorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DialogDescriptorService.class);
    @Autowired
    private CurrentDateProvider currentDateProvider;

    /**
     * Decreases the hit count of a dialog descriptor when the
     * usage is {@link DialogDescriptorUsage}.HITCOUNT. If the hit count reaches
     * zero, the usage will become {@link DialogDescriptorUsage}.DISABLED.
     * @param dialogDescriptor the {@link DialogDescriptor} that will be updated
     */
    public void decreaseHitcountWhenUsageIsHitcount(final DialogDescriptor dialogDescriptor) {
        if (dialogDescriptor.getAttributes().getUsage() == DialogDescriptorUsage.HITCOUNT) {
            long validityValue = dialogDescriptor.getAttributes().decreaseHitcount();
            LOGGER.debug("Hitcount was decreased to " + validityValue + " in dialog descriptor '" + dialogDescriptor.getAttributes().getName() + "'!");
            if (validityValue == 0) {
                dialogDescriptor.getAttributes().setUsage(DialogDescriptorUsage.DISABLED);
                LOGGER.debug("Dialog descriptor '" + dialogDescriptor.getAttributes().getName() + "' was disabled!");
            }
        }
    }

    /**
     * Decides if a dialog descriptor is enabled i.e. should be used
     * for condition evaluation or not. If its usage was {@link DialogDescriptorUsage}.TIMEOUT and
     * it expired, it will change the usage to {@link DialogDescriptorUsage}.DISABLED.
     * @param dialogDescriptor dialogDescriptor the {@link DialogDescriptor} that's usage
     * is checked
     * @return true if it is enabled, false otherwise
     */
    public boolean isEnabled(final DialogDescriptor dialogDescriptor) {
        boolean result = false;
        DialogDescriptorUsage usage = dialogDescriptor.getAttributes().getUsage();
        if (usage == DialogDescriptorUsage.DISABLED) {
            result = false;
        } else {
            boolean notTimedOut = isNotTimedOut(dialogDescriptor, usage);
            if (!notTimedOut) {
                dialogDescriptor.getAttributes().setUsage(DialogDescriptorUsage.DISABLED);
                LOGGER.debug("Dialog descriptor '" + dialogDescriptor.getAttributes().getName() + "' was disabled!");
            }
            result = notTimedOut;
        }
        return result;
    }

    private boolean isNotTimedOut(final DialogDescriptor dialogDescriptor, final DialogDescriptorUsage usage) {
        boolean result = true;
        if (usage == DialogDescriptorUsage.TIMEOUT) {
            long timeout = dialogDescriptor.getAttributes().getTimeout();
            long currentTimeInMillis = currentDateProvider.getCurrentTimeInMillis();
            result = currentTimeInMillis <= timeout;
        }
        return result;
    }
}
