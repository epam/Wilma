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

import com.epam.wilma.service.domain.WilmaServiceConfig;
import com.epam.wilma.service.http.WilmaHttpClient;
import com.epam.wilma.service.util.UrlBuilderUtils;
import com.google.common.base.Optional;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Abstract class for Wilma configuration related commands.
 *
 * @author Tamas_Pinter
 *
 */
public abstract class AbstractConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractConfiguration.class);

    private static final JSONObject NULL_JSON = null;

    private WilmaHttpClient wilmaClient;
    private WilmaServiceConfig config;

    /**
     * Constructor.
     *
     * @param config the Wilma server configuration
     */
    public AbstractConfiguration(WilmaServiceConfig config) {
        this(config, null);
    }

    /**
     * Constructor.
     *
     * @param config the Wilma server configuration
     * @param client the Wilma HTTP client
     */
    public AbstractConfiguration(WilmaServiceConfig config, WilmaHttpClient client) {
        checkArgument(config != null, "config must not be null!");
        this.config = config;
        this.wilmaClient = client == null ? new WilmaHttpClient() : client;
    }

    /**
     * Calls Wilma server via Wilma HTTP client with URL build from the given
     * postfix and Wilma configuration. Returns the requested information in
     * JSONObject.
     *
     * @param postfix the URL postfix
     * @return the requested information in JSONObject
     */
    protected JSONObject getterRequest(String postfix) {
        String url = buildUrl(postfix, null);

        LOG.debug("Send getter request to: " + url);
        Optional<String> response = wilmaClient.sendGetterRequest(url);
        return response.isPresent()
                ? new JSONObject(response.get())
                : NULL_JSON;
    }

    /**
     * Calls Wilma server via Wilma HTTP client with URL build from the given
     * postfix, parameters and Wilma configuration. Returns the requested
     * information in JSONObject.
     *
     * @param postfix the URL postfix
     * @param params the URL parameters
     * @return the requested information in JSONObject
     */
    protected JSONObject getterRequest(String postfix, Map<String, String> params) {
        String url = buildUrl(postfix, params);

        LOG.debug("Send getter request to: " + url);
        Optional<String> response = wilmaClient.sendGetterRequest(url);
        return response.isPresent()
                ? new JSONObject(response.get())
                : NULL_JSON;
    }

    /**
     * Calls Wilma server via Wilma HTTP client with URL build from the given
     * postfix and Wilma configuration. Returns <tt>true</tt> if the request was
     * successful, otherwise returns <tt>false</tt>.
     *
     * @param postfix the URL postfix
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    protected boolean setterRequest(String postfix) {
        String url = buildUrl(postfix, null);

        LOG.debug("Send setter request to: " + url);
        return wilmaClient.sendSetterRequest(url);
    }

    /**
     * Calls Wilma server via Wilma HTTP client with URL build from the given
     * postfix, parameters and Wilma configuration. Returns <tt>true</tt> if the
     * request was successful, otherwise returns <tt>false</tt>.
     *
     * @param postfix the URL postfix
     * @param params the URL parameters
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    protected boolean setterRequest(String postfix, Map<String, String> params) {
        String url = buildUrl(postfix, params);

        LOG.debug("Send setter request to: " + url);
        return wilmaClient.sendSetterRequest(url);
    }

    private String buildUrl(String postfix, Map<String, String> params) {
        return UrlBuilderUtils.buildAbsoluteURL(false, config.getHost(), config.getPort().toString(), postfix, params);
    }
}
