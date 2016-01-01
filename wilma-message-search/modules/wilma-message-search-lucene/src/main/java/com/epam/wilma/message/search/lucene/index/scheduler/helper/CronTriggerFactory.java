package com.epam.wilma.message.search.lucene.index.scheduler.helper;
/*==========================================================================
Copyright 2013-2016 EPAM Systems

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

/**
 * Factory class for {@link CronTrigger}.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class CronTriggerFactory {

    /**
     * Creates a new {@link CronTrigger} instance.
     * @param expression a space-separated list of time fields, following cron expression conventions
     * @return a new {@link CronTrigger} instance
     */
    public CronTrigger create(final String expression) {
        return new CronTrigger(expression);
    }
}
