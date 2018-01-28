package com.epam.wilma.webapp.stub.servlet.helper;
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

import org.springframework.stereotype.Component;


/**
 * Provides waiting mechanism.
 * @author Tunde_Kovacs
 *
 */
@Component
public class WaitProvider {

    /**
     * Sleeps for a given time of period.
     * @param time the amount of time to wait
     * @throws InterruptedException  if the value of millis is negative
     */
    public void waitMilliSeconds(final int time) throws InterruptedException {
        Thread.sleep(time);
    }
}
