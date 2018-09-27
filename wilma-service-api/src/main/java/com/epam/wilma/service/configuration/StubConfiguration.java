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

import com.epam.wilma.service.domain.StubConfigOrder;
import com.epam.wilma.service.domain.StubConfigStatus;
import com.epam.wilma.service.domain.WilmaServiceConfig;
import com.epam.wilma.service.http.WilmaHttpClient;
import com.google.common.collect.ImmutableMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Collects the stub configuration related commands.
 *
 * @author Tamas_Pinter
 */
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

    /**
     * Constructor.
     *
     * @param config the Wilma server configuration
     */
    public StubConfiguration(WilmaServiceConfig config) {
        super(config);
    }

    /**
     * Constructor.
     *
     * @param config the Wilma server configuration
     * @param client the Wilma HTTP client
     */
    public StubConfiguration(WilmaServiceConfig config, WilmaHttpClient client) {
        super(config, client);
    }

    /**
     * Gets the stub configuration information.
     *
     * @return stub configuration information in JSONObject
     */
    public JSONObject getStubConfigInformation() {
        LOG.debug("Call stub configuration API.");

        return getterRequest(STATUS_GETTER_URL_POSTFIX);
    }

    /**
     * Sets the status of the given stub group.
     *
     * @param groupName the name of the stub group
     * @param status    the new status
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean setStubConfigStatus(String groupName, StubConfigStatus status) {
        LOG.debug("Call stub status setter API with value: {}, for group: {}", status, groupName);

        return setterRequest(STUB_CONFIG_STATUS_CHANGE_SETTER_URL_POSTFIX,
                ImmutableMap.of(GROUP_NAME, groupName, NEXT_STATUS, Boolean.toString(status.getNextStatus())));
    }

    /**
     * Sets the new order of the given stub group.
     *
     * @param groupName the name of the stub group
     * @param order     the new order
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean setStubConfigOrder(String groupName, StubConfigOrder order) {
        LOG.debug("Call stub order setter API with value: {}, for group: {}", order, groupName);

        return setterRequest(STUB_CONFIG_ORDER_CHANGE_SETTER_URL_POSTFIX,
                ImmutableMap.of(GROUP_NAME, groupName, DIRECTION, Integer.toString(order.getDirection())));
    }

    /**
     * Drops the given stub group configuration.
     *
     * @param groupName the name of the stub group
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean dropStubConfig(String groupName) {
        LOG.debug("Call drop stub configuration API for group: {}", groupName);

        return setterRequest(DROP_STUB_CONFIG_URL_POSTFIX, ImmutableMap.of(GROUP_NAME, groupName));
    }

    /**
     * Drops the all stub configuration.<br>
     * Whichever drop try was unsuccessful then return {@code false} but try to
     * drop the others. The supposed stub configuration information JSON format
     * is the following:<br>
     * <pre>
     * {
     *   "configs": [
     *     {
     *       "sequenceDescriptors": [ { ... } ],
     *       "dialogDescriptors": [ { ... } ],
     *       "groupname": "Default",
     *       "active": "true"
     *     }
     *   ]
     * }
     * </pre>
     *
     * @return <tt>true</tt> if all the stub configuration is dropped
     * successfully (or was empty and nothing to be dropped), otherwise return <tt>false</tt>
     */
    public boolean dropAllStubConfig() {
        LOG.debug("Call drop all stub configuration.");
        boolean droppedAllStubConfig = true;

        JSONObject stubConfig = getStubConfigInformation();
        if ((stubConfig != null) && (stubConfig.length() > 0)) {
            try {
                LOG.debug("Gets stub configs array from all stub configuration JSON.");
                JSONArray configs = stubConfig.getJSONArray("configs");
                for (int i = 0; i < configs.length(); i++) {
                    LOG.debug("Get the stub group name.");
                    String groupName = configs.getJSONObject(i).getString("groupname");

                    droppedAllStubConfig &= dropStubConfig(groupName);
                    LOG.info("Dropped stub configuration: {}", groupName);
                }
            } catch (JSONException e) {
                LOG.error("Error occurred while dropping sub configuration. ", e);
                droppedAllStubConfig = false;
            }
        } else {
            droppedAllStubConfig = false;
        }

        return droppedAllStubConfig;
    }

    /**
     * Save the actual stub configuration.
     *
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean persistActualStubConfig() {
        LOG.debug("Call save stub configuration API.");

        return setterRequest(SAVE_STUB_CONFIG_URL_POSTFIX);
    }
}
