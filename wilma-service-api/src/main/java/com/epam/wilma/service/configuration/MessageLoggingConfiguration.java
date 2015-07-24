package com.epam.wilma.service.configuration;

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

import com.epam.wilma.service.domain.MessageLoggingControlStatus;
import com.epam.wilma.service.domain.WilmaServiceConfig;
import com.epam.wilma.service.http.WilmaHttpClient;

/**
 * Collects the message logging configuration related commands.
 *
 * @author Tamas_Pinter
 *
 */
public class MessageLoggingConfiguration extends AbstractConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(MessageLoggingConfiguration.class);

    private static final String STATUS_GETTER_URL_POSTFIX = "config/public/logging/status";
    private static final String STATUS_SETTER_URL_POSTFIX_FORMAT = "config/admin/logging/%s";

    /**
     * Constructor.
     *
     * @param config the Wilma server configuration
     */
    public MessageLoggingConfiguration(WilmaServiceConfig config) {
        super(config);
    }

    /**
     * Constructor.
     *
     * @param config the Wilma server configuration
     * @param client the Wilma HTTP client
     */
    public MessageLoggingConfiguration(WilmaServiceConfig config, WilmaHttpClient client) {
        super(config, client);
    }

    /**
     * Gets the message logging status.
     *
     * @return message logging status in MessageLoggingControlStatus object, that can be null in case of problem.
     */
    public MessageLoggingControlStatus getMessageLoggingStatus() {
        LOG.debug("Call message logging status API.");
        MessageLoggingControlStatus status = null;

        JSONObject o = getterRequest(STATUS_GETTER_URL_POSTFIX);
        if (o != null) {
            Boolean requestLogging = (Boolean) o.get("requestLogging");
            Boolean responseLogging = (Boolean) o.get("responseLogging");

            if (requestLogging & responseLogging) {
                status = MessageLoggingControlStatus.ON;
            } else {
                status = MessageLoggingControlStatus.OFF;
            }
        }
        return status;
    }

    /**
     * Sets the message logging status.
     *
     * @param control the new message logging status
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean setMessageLoggingStatus(MessageLoggingControlStatus control) {
        LOG.debug("Call message logging status setter API with value: " + control);

        String postfix = String.format(STATUS_SETTER_URL_POSTFIX_FORMAT, control.toString().toLowerCase());
        return setterRequest(postfix);
    }
}
