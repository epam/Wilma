package com.epam.wilma.service.configuration;

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

import com.epam.wilma.service.domain.ResponseMessageVolatilityStatus;
import com.epam.wilma.service.domain.WilmaServiceConfig;
import com.epam.wilma.service.http.WilmaHttpClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Collects the message marking related commands.
 *
 * @author Tamas_Kohegyi
 */
public class ResponseMessageVolatilityConfiguration extends AbstractConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(ResponseMessageVolatilityConfiguration.class);

    private static final String STATUS_GETTER_URL_POSTFIX = "config/public/responsevolatility/status";
    private static final String STATUS_SETTER_URL_POSTFIX_FORMAT = "config/admin/responsevolatility/%s";

    /**
     * Constructor.
     *
     * @param config the Wilma server configuration
     */
    public ResponseMessageVolatilityConfiguration(WilmaServiceConfig config) {
        super(config);
    }

    /**
     * Constructor.
     *
     * @param config the Wilma server configuration
     * @param client the Wilma HTTP client
     */
    public ResponseMessageVolatilityConfiguration(WilmaServiceConfig config, WilmaHttpClient client) {
        super(config, client);
    }

    /**
     * Gets the response volatility status.
     *
     * @return response volatility status in JSONObject
     */
    public ResponseMessageVolatilityStatus getResponseMessageVolatilityStatus() {
        LOG.debug("Call response message volatility status API.");
        ResponseMessageVolatilityStatus status = null;

        JSONObject o = getterRequest(STATUS_GETTER_URL_POSTFIX);
        if (o != null) {
            Boolean responseVolatility = (Boolean) o.get("responseVolatility");

            if (responseVolatility) {
                status = ResponseMessageVolatilityStatus.ON;
            } else {
                status = ResponseMessageVolatilityStatus.OFF;
            }
        }
        return status;
    }

    /**
     * Sets the response volatility status.
     *
     * @param control the new response volatility status
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean setResponseMessageVolatilityStatus(ResponseMessageVolatilityStatus control) {
        LOG.debug("Call response message volatility setter API with value: " + control);

        String postfix = String.format(STATUS_SETTER_URL_POSTFIX_FORMAT, control.toString().toLowerCase());
        return setterRequest(postfix);
    }

}
