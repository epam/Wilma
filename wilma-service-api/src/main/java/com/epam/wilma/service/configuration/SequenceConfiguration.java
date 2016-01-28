package com.epam.wilma.service.configuration;

/*==========================================================================
 Copyright 2013-2016 EPAM Systems

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

import com.epam.wilma.service.domain.WilmaServiceConfig;
import com.epam.wilma.service.http.WilmaHttpClient;

/**
 * Collects the sequence configuration related commands.
 *
 * @author Tamas_Pinter
 *
 */
public class SequenceConfiguration extends AbstractConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(SequenceConfiguration.class);

    private static final String STATUS_GETTER_URL_POSTFIX = "config/public/livesequences";

    /**
     * Constructor.
     *
     * @param config the Wilma server configuration
     */
    public SequenceConfiguration(WilmaServiceConfig config) {
        super(config);
    }

    /**
     * Constructor.
     *
     * @param config the Wilma server configuration
     * @param client the Wilma HTTP client
     */
    public SequenceConfiguration(WilmaServiceConfig config, WilmaHttpClient client) {
        super(config, client);
    }

    /**
     * Gets the sequences information.
     *
     * @return sequences information in JSONObject
     */
    public JSONObject getSequencesLiveInformation() {
        LOG.debug("Call sequences status API.");

        return getterRequest(STATUS_GETTER_URL_POSTFIX);
    }

}
