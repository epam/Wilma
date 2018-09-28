package com.epam.wilma.service.configuration.stub.helper.common;
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
 * Class that holds a Parameter array.
 *
 * @author Tamas_Kohegyi
 */
public class ConfigurationParameterArray {
    private ConfigurationParameter[] configurationParameters;

    /**
     * New parameter array object.
     *
     * @param configurationParameters ae the initial parameters
     */
    public ConfigurationParameterArray(ConfigurationParameter[] configurationParameters) {
        this.configurationParameters = configurationParameters;
    }

    /**
     * Generates String value for the configuration parameters array.
     *
     * @return with the config string
     */
    @Override
    public String toString() {
        String string = "";
        if (configurationParameters != null) {
            //we have parameters too
            string += "  \"parameters\": [\n";
            int i = 0;
            for (ConfigurationParameter configurationParameter : configurationParameters) {
                if (i > 0) {
                    string += ",\n";
                }
                string += "    " + configurationParameter.toString();
                i++;
            }
            string += "\n  ]\n";
        }
        return string;
    }
}
