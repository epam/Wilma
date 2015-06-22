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

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.wilma.mock.domain.WilmaMockConfig;
import com.epam.wilma.mock.http.WilmaHttpClient;
import com.epam.wilma.mock.util.UrlBuilderUtils;
import com.google.common.base.Optional;

public abstract class AbstractConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractConfiguration.class);

    private static final JSONObject EMPTY_JSON = new JSONObject();

    private WilmaHttpClient wilmaClient;
    private WilmaMockConfig config;

    public AbstractConfiguration(WilmaMockConfig config) {
        this(config, null);
    }

    public AbstractConfiguration(WilmaMockConfig config, WilmaHttpClient client) {
        checkArgument(config != null, "config must not be null!");
        this.config = config;
        this.wilmaClient = client == null ? new WilmaHttpClient() : client;
    }

    protected JSONObject getterRequest(String prefix) {
        String url = buildUrl(prefix, null);

        LOG.debug("Send getter request to: " + url);
        Optional<String> response = wilmaClient.sendGetterRequest(url);
        return response.isPresent()
                ? new JSONObject(response.get())
                : EMPTY_JSON;
    }

    protected JSONObject getterRequest(String prefix, Map<String, String> params) {
        String url = buildUrl(prefix, params);

        LOG.debug("Send getter request to: " + url);
        Optional<String> response = wilmaClient.sendGetterRequest(url);
        return response.isPresent()
                ? new JSONObject(response.get())
                : EMPTY_JSON;
    }

    protected boolean setterRequest(String postfix) {
        String url = buildUrl(postfix, null);

        LOG.debug("Send setter request to: " + url);
        return wilmaClient.sendSetterRequest(url);
    }

    protected boolean setterRequest(String postfix, Map<String, String> params) {
        String url = buildUrl(postfix, params);

        LOG.debug("Send setter request to: " + url);
        return wilmaClient.sendSetterRequest(url);
    }

    private String buildUrl(String postfix, Map<String, String> params) {
        return UrlBuilderUtils.buildAbsoluteURL(false, config.getHost(), config.getPort().toString(), postfix, params);
    }
}
