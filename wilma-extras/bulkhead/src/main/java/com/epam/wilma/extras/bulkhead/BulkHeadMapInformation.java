package com.epam.wilma.extras.bulkhead;
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

/**
 * This example service shows a special usage of the proxy part of Wilma. With this plugin, Wilma acts as a Bulkhead for a service.
 * Read more about what Bulkhead means: search for "Bulkhead software pattern" on the WWW.
 *
 * This class is about to calculate the actual load of the service.
 *
 * @author tkohegyi
 */
class BulkHeadMapInformation {

    private long lastTime;
    private double lastSpeed;

    BulkHeadMapInformation(long lastTime) {
        this.lastTime = lastTime;
        this.lastSpeed = -1.0;
    }

    long getLastTime() {
        return lastTime;
    }
    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    double getLastSpeed() {
        return lastSpeed;
    }

    void setLastSpeed(double lastSpeed) {
        this.lastSpeed = lastSpeed;
    }
}
