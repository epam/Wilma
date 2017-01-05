package com.epam.wilma.service.configuration.stub.helper.other;
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

import com.epam.wilma.service.configuration.stub.helper.common.ConfigurationParameter;

/**
 * Class that holds a single interceptor information.
 *
 * @author Tamas_Kohegyi
 */
public class Interceptor {
    private String interceptorName;
    private String className;
    private ConfigurationParameter[] configurationParameters;

    /**
     * Constructor when parameter values has meaning.
     *
     * @param interceptorName         is the name of the interceptor
     * @param className               is the name of the used formatter class
     * @param configurationParameters is the value of the parameters
     */
    public Interceptor(String interceptorName, String className, ConfigurationParameter[] configurationParameters) {
        this.interceptorName = interceptorName;
        this.className = className;
        this.configurationParameters = configurationParameters;
    }

    /**
     * Generates String value for the interceptor.
     *
     * @return with the config string
     */
    @Override
    public String toString() {
        String interceptorString = "    <interceptor name=\"" + interceptorName + "\" class=\"" + className + "\" >\n";
        if (configurationParameters != null) {
            //we have parameters too
            for (ConfigurationParameter configurationParameter : configurationParameters) {
                interceptorString += "    " + configurationParameter.toString() + "\n";
            }
        }
        interceptorString += "    </interceptor>\n";
        return interceptorString;
    }
}
