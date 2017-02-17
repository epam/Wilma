package com.epam.wilma.extras.bulkhead;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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
 * This example service shows a special usage of the proxy part of Wilma. With this plugin, Wilma acts as a bulkhead for a service.
 * Read more about what Bulkhead means: .....
 *
 * @author tkohegyi
 */
public class BulkHeadMapInformation {

    private long lastTime;
    private double lastSpeed;

    public BulkHeadMapInformation(long lastTime) {
        this.lastTime = lastTime;
        this.lastSpeed = -1.0;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public double getLastSpeed() {
        return lastSpeed;
    }

    public void setLastSpeed(double lastSpeed) {
        this.lastSpeed = lastSpeed;
    }
}
