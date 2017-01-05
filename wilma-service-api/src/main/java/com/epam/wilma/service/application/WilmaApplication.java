package com.epam.wilma.service.application;

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

import com.epam.wilma.service.domain.WilmaLoadInformation;
import com.epam.wilma.service.domain.WilmaServiceConfig;
import com.epam.wilma.service.http.WilmaHttpClient;
import com.epam.wilma.service.util.UrlBuilderUtils;
import com.google.common.base.Optional;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Collects the Wilma server related commands.
 *
 * @author Tamas_Pinter, Tamas_Kohegyi
 *
 */
public class WilmaApplication {
    private static final Logger LOG = LoggerFactory.getLogger(WilmaApplication.class);

    private static final String VERSION_INFO_URL_POSTFIX = "config/public/version";
    private static final String ACTUAL_LOAD_INFO_URL_POSTFIX = "config/public/actualload";
    private static final String SHUTDOWN_URL_POSTFIX = "config/admin/shutdown";
    private static final String SERVICE_URL_POSTFIX = "config/public/service?";

    private WilmaHttpClient wilmaClient;
    private WilmaServiceConfig config;

    /**
     * Constructor.
     *
     * @param config the Wilma server configuration
     */
    public WilmaApplication(WilmaServiceConfig config) {
        this(config, null);
    }

    /**
     * Constructor.
     *
     * @param config the Wilma server configuration
     * @param client the Wilma http client
     */
    public WilmaApplication(WilmaServiceConfig config, WilmaHttpClient client) {
        checkArgument(config != null, "config must not be null!");
        this.config = config;
        this.wilmaClient = client == null ? new WilmaHttpClient() : client;
    }

    /**
     * Gets the version information from Wilma.
     *
     * @return the actual version information, or null. In case of null, see log about the problem.
     */
    public String getVersionInformation() {
        LOG.debug("Call version information API.");
        String url = buildUrl(VERSION_INFO_URL_POSTFIX);

        String version = null;

        LOG.debug("Send getter request to: " + url);
        Optional<String> response = wilmaClient.sendGetterRequest(url);
        if (response.isPresent()) {
            JSONObject o = new JSONObject(response.get());
            version = o.getString("wilmaVersion");
        }

        return version;
    }

    /**
     * Gets the actual load information of the application.
     *
     * @return actual load information
     */
    public WilmaLoadInformation getActualLoadInformation() {
        LOG.debug("Call actual load information API.");

        String url = buildUrl(ACTUAL_LOAD_INFO_URL_POSTFIX);
        WilmaLoadInformation loadInformation = null;

        LOG.debug("Send getter request to: " + url);
        Optional<String> response = wilmaClient.sendGetterRequest(url);
        if (response.isPresent()) {
            loadInformation = new WilmaLoadInformation(new JSONObject(response.get()));
        }

        return loadInformation;
    }

    /**
     * Shutdown the Wilma application.
     *
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean shutdownApplication() {
        LOG.debug("Call application shutdown API.");

        String url = buildUrl(SHUTDOWN_URL_POSTFIX);

        LOG.debug("Send shutdown request to: " + url);
        return wilmaClient.sendSetterRequest(url);
    }

    private String buildUrl(String postfix) {
        return UrlBuilderUtils.buildAbsoluteURL(false, config.getHost(), config.getPort().toString(), postfix, null);
    }

    /**
     * Calls a special service of Wilma.
     *
     * @param serviceQueryString is the selector of the service
     * @return actual service response
     */
    public JSONObject callGetService(final String serviceQueryString) {
        LOG.debug("Call special service with request: " + serviceQueryString);

        String url = buildUrl(SERVICE_URL_POSTFIX + serviceQueryString);
        JSONObject jsonObject = null;

        LOG.debug("Send getter request to: " + url);
        Optional<String> response = wilmaClient.sendGetterRequest(url);
        if (response.isPresent()) {
            jsonObject = new JSONObject(response.get());
        }

        return jsonObject;
    }

}
