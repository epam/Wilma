package com.epam.wilma.mock.configuration;

/*==========================================================================
 Copyright 2015 EPAM Systems

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

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.wilma.mock.domain.OperationMode;
import com.epam.wilma.mock.domain.WilmaMockConfig;
import com.epam.wilma.mock.http.WilmaHttpClient;

/**
 * Collects the operation configuration related commands.
 *
 * @author Tamas_Pinter
 *
 */
public class OperationConfiguration extends AbstractConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(MessageLoggingConfiguration.class);

    private static final String STATUS_GETTER_URL_POSTFIX = "config/public/switch/status";
    private static final String STATUS_SETTER_URL_POSTFIX_FORMAT = "config/admin/switch/%s";

    /**
     * Constructor.
     *
     * @param config the Wilma server configuration
     */
    public OperationConfiguration(WilmaMockConfig config) {
        super(config);
    }

    /**
     * Constructor.
     *
     * @param config the Wilma server configuration
     * @param client the Wilma HTTP client
     */
    public OperationConfiguration(WilmaMockConfig config, WilmaHttpClient client) {
        super(config, client);
    }

    /**
     * Gets the actual operation mode.
     *
     * @return actual operation mode in JSONObject
     */
    public JSONObject getOperationMode() {
        LOG.debug("Call operation status API.");

        return getterRequest(STATUS_GETTER_URL_POSTFIX);
    }

    /**
     * Sets the operation mode.
     *
     * @param mode the new operation mode
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean setOperationMode(OperationMode mode) {
        LOG.debug("Call operation mode setter API with value: " + mode);

        String postfix = String.format(STATUS_SETTER_URL_POSTFIX_FORMAT, mode.toString().toLowerCase());
        return setterRequest(postfix);
    }

}
