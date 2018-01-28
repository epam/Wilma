package com.epam.wilma.common.helper;
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

import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.exception.SchedulingCannotBeStartedException;

/**
 * Factory class to create a new CronTrigger.
 * @author Marton_Sereg
 *
 */
@Component
public class CronTriggerFactory {

    /**
     * Creates a new CronTrigger.
     * @param cronExpression the cronExpression that is used for the CronTrigger
     * @return created CronTrigger
     */
    public CronTrigger createCronTrigger(final String cronExpression) {
        CronTrigger result;
        try {
            result = new CronTrigger(cronExpression);
        } catch (IllegalArgumentException e) {
            throw new SchedulingCannotBeStartedException("Scheduling cannot be started, cron expression given in properties file is not valid! "
                    + e.getMessage(), e);
        }
        return result;
    }

}
