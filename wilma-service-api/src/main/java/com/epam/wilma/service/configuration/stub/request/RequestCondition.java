package com.epam.wilma.service.configuration.stub.request;
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

/**
 * A Request Condition class.
 *
 * @author Tamas_Kohegyi
 */
public class RequestCondition {

    private String configurationString;

    /**
     * Creates a Request Condition object by specifying the string representation of the request condition part of the stub configuration.
     *
     * @param requestConditionConfigurationString string representation of the request condition
     */
    public RequestCondition(String requestConditionConfigurationString) {
        configurationString = requestConditionConfigurationString;
    }

    /**
     * Gets the request condition part of the stub configuration.
     *
     * @return with the configuration
     */
    @Override
    public String toString() {
        return configurationString;
    }
}
