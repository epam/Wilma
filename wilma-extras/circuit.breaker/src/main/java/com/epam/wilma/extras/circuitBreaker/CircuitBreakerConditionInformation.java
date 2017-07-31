package com.epam.wilma.extras.circuitBreaker;
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
 * This class holds information about a single circuitBreaker.
 *
 * @author tkohegyi
 */
class CircuitBreakerConditionInformation {

    //input settings
    private String path;
    private String timeoutInSec;
    private String successCodes;
    private Integer maxErrorCount;
    //status
    private Integer actualErrorLevel;
    private boolean isActive;
    private Integer timeoutLeftInSec;

    /**
     * Creates a new response information, based on the original response, and specifying a timeout.
     * Timeout value is the system time, when this response become obsolete.
     */
    CircuitBreakerConditionInformation() {
        //get parameters
        //TODO
        //init status
        actualErrorLevel = 0;
        isActive = false;
        timeoutLeftInSec = 0;
    }


    public Integer getActualErrorLevel() {
        return actualErrorLevel;
    }

    public void setActualErrorLevel(Integer actualErrorLevel) {
        this.actualErrorLevel = actualErrorLevel;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Integer getTimeoutLeftInSec() {
        return timeoutLeftInSec;
    }

    public void setTimeoutLeftInSec(Integer timeoutLeftInSec) {
        this.timeoutLeftInSec = timeoutLeftInSec;
    }

    public String getPath() {
        return path;
    }

    public String getTimeoutInSec() {
        return timeoutInSec;
    }

    public String getSuccessCodes() {
        return successCodes;
    }

    public Integer getMaxErrorCount() {
        return maxErrorCount;
    }
}
