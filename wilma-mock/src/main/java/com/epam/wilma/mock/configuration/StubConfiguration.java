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

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.wilma.mock.domain.StubConfigOrder;
import com.epam.wilma.mock.domain.StubConfigStatus;
import com.epam.wilma.mock.domain.WilmaMockConfig;
import com.epam.wilma.mock.http.WilmaHttpClient;

public class StubConfiguration extends AbstractConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(StubConfiguration.class);

    private static final String STATUS_GETTER_URL_POSTFIX = "config/public/stubdescriptor";
    private static final String STUB_CONFIG_STATUS_CHANGE_SETTER_URL_POSTFIX = "config/admin/stub/changestatus";
    private static final String STUB_CONFIG_ORDER_CHANGE_SETTER_URL_POSTFIX = "config/admin/stub/changeorder";
    private static final String DROP_STUB_CONFIG_URL_POSTFIX = "config/admin/stub/drop";
    private static final String SAVE_STUB_CONFIG_URL_POSTFIX = "config/admin/stub/save";
    private static final String GROUP_NAME = "groupname";
    private static final String DIRECTION = "direction";
    private static final String NEXT_STATUS = "nextstatus";

    public StubConfiguration(WilmaMockConfig config) {
        super(config);
    }

    public StubConfiguration(WilmaMockConfig config, WilmaHttpClient client) {
        super(config, client);
    }

    public JSONObject getStubConfigInformation() {
        LOG.debug("Call stub configuration API.");

        return getterRequest(STATUS_GETTER_URL_POSTFIX);
    }

    public boolean setStubConfigStatus(String groupName, StubConfigStatus status) {
        LOG.debug("Call stub status setter API with value: {}, for group: {}", status, groupName);

        Map<String, String> params = new HashMap<>();
        params.put(GROUP_NAME, groupName);
        params.put(NEXT_STATUS, Boolean.toString(status.getNextStatus()));

        return setterRequest(STUB_CONFIG_STATUS_CHANGE_SETTER_URL_POSTFIX, params);
    }

    public boolean setStubConfigOrder(String groupName, StubConfigOrder order) {
        LOG.debug("Call stub order setter API with value: {}, for group: {}", order, groupName);

        Map<String, String> params = new HashMap<>();
        params.put(GROUP_NAME, groupName);
        params.put(DIRECTION, Integer.toString(order.getDirection()));

        return setterRequest(STUB_CONFIG_ORDER_CHANGE_SETTER_URL_POSTFIX, params);
    }

    public boolean dropStubConfig(String groupName) {
        LOG.debug("Call drop stub configuration API for group: {}", groupName);

        Map<String, String> params = new HashMap<>();
        params.put(GROUP_NAME, groupName);

        return setterRequest(DROP_STUB_CONFIG_URL_POSTFIX, params);
    }

    public boolean persistActualStubConfig() {
        LOG.debug("Call save stub configuration API.");

        return setterRequest(SAVE_STUB_CONFIG_URL_POSTFIX);
    }
}
