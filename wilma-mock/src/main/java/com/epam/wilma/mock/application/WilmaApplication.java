package com.epam.wilma.mock.application;

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

import static com.google.common.base.Preconditions.checkArgument;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.wilma.mock.domain.WilmaMockConfig;
import com.epam.wilma.mock.http.WilmaHttpClient;
import com.epam.wilma.mock.util.UrlBuilderUtils;
import com.google.common.base.Optional;

public class WilmaApplication {
    private static final Logger LOG = LoggerFactory.getLogger(WilmaApplication.class);

    private static final JSONObject EMPTY_JSON = new JSONObject();

    private static final String ACTUAL_LOAD_INFO_URL_POSTFIX = "config/public/actualload";
    private static final String SHUTDOWN_URL_POSTFIX = "config/admin/shutdown";

    private WilmaHttpClient wilmaClient;
    private WilmaMockConfig config;

    public WilmaApplication(WilmaMockConfig config) {
        this(config, null);
    }

    public WilmaApplication(WilmaMockConfig config, WilmaHttpClient client) {
        checkArgument(config != null, "config must not be null!");
        this.config = config;
        this.wilmaClient = client == null ? new WilmaHttpClient() : client;
    }

    public JSONObject getActualLoadInformation() {
        LOG.debug("Call actual load information API.");

        String url = buildUrl(ACTUAL_LOAD_INFO_URL_POSTFIX);

        LOG.debug("Send getter request to: " + url);
        Optional<String> response = wilmaClient.sendGetterRequest(url);
        return response.isPresent()
                ? new JSONObject(response.get())
                : EMPTY_JSON;
    }

    public boolean shutdownApplication() {
        LOG.debug("Call application shutdown API.");

        String url = buildUrl(SHUTDOWN_URL_POSTFIX);

        LOG.debug("Send shutdown request to: " + url);
        return wilmaClient.sendSetterRequest(url);
    }

    private String buildUrl(String postfix) {
        return UrlBuilderUtils.buildAbsoluteURL(false, config.getHost(), config.getPort().toString(), postfix, null);
    }

}
