package com.epam.wilma.maintainer.configuration.domain;
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
 * Properties needed by the maintainer.
 * @author Tunde_Kovacs
 *
 */
public class MaintainerProperties {

    private final String cronExpression;
    private final String maintainerMethod;
    private final Integer fileLimit;
    private final String timeLimit;

    /**
     * Constructs an object with the maintainer properties.
     * @param cronExpression cron expression for task scheduling
     * @param maintainerMethod decides the type of the maintainer mechanism
     * @param fileLimit specifies the number of log files
     * @param timeLimit specifies the time a log file should be kept
     */
    public MaintainerProperties(final String cronExpression, final String maintainerMethod, final Integer fileLimit, final String timeLimit) {
        super();
        this.cronExpression = cronExpression;
        this.maintainerMethod = maintainerMethod;
        this.fileLimit = fileLimit;
        this.timeLimit = timeLimit;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public String getMaintainerMethod() {
        return maintainerMethod;
    }

    public Integer getFileLimit() {
        return fileLimit;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

}
