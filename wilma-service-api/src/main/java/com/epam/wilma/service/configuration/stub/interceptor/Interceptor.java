package com.epam.wilma.service.configuration.stub.interceptor;
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

import com.epam.wilma.service.configuration.stub.helper.common.ConfigurationParameterArray;

/**
 * Class that holds a single interceptor information.
 *
 * @author Tamas_Kohegyi
 */
public class Interceptor {
    private String interceptorName;
    private String className;
    private ConfigurationParameterArray configurationParameterArray;

    /**
     * Constructor when parameter values has meaning.
     *
     * @param interceptorName         is the name of the interceptor
     * @param className               is the name of the used formatter class
     * @param configurationParameterArray is the value of the parameters
     */
    public Interceptor(String interceptorName, String className, ConfigurationParameterArray configurationParameterArray) {
        this.interceptorName = interceptorName;
        this.className = className;
        this.configurationParameterArray = configurationParameterArray;
    }

    /**
     * Generates String value for the interceptor.
     *
     * @return with the config string
     */
    @Override
    public String toString() {
        String interceptorString = "{ \"name\": \"" + interceptorName + "\", \"class\": \"" + className + "\"";
        if (configurationParameterArray != null) {
            //we have parameters too
            interceptorString += ",\n  " + configurationParameterArray.toString();
        }
        interceptorString += "\n }\n";
        return interceptorString;
    }
}
