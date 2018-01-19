package com.epam.wilma.engine.bootstrap.helper;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.LogFilePathProvider;
import com.epam.wilma.common.helper.VersionTitleProvider;
import com.epam.wilma.engine.configuration.EngineConfigurationAccess;
import com.epam.wilma.engine.configuration.domain.PropertyDTO;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.Service.State;

/**
 * Listener for the WilmaService, logs out the different state changes.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class WilmaServiceListener extends Service.Listener {

    private static final String FAILED_MESSAGE = "Error occurred in Wilma";
    private static final String TERMINATED_MESSAGE = "Wilma stopped.";
    private final Logger logger = LoggerFactory.getLogger(WilmaServiceListener.class);
    @Qualifier("wilmaStartMessage")
    @Autowired
    private String wilmaStartMessage;
    @Autowired
    private LogFilePathProvider logFilePath;
    @Autowired
    private VersionTitleProvider versionTitleProvider;
    @Autowired
    private EngineConfigurationAccess configurationAccess;

    @Override
    public void running() {
        logger.info(generateStartMessage());
    }

    @Override
    public void terminated(final State from) {
        logger.info(TERMINATED_MESSAGE);
    }

    @Override
    public void failed(final State from, final Throwable failure) {
        logger.error(FAILED_MESSAGE, failure);
    }

    private String generateStartMessage() {
        int proxyPort = getProxyPort();
        int wilmaPort = getWilmaPort();
        String appLogPath = logFilePath.getAppLogFilePath().toAbsolutePath().toString();
        String messagesPath = logFilePath.getLogFilePath().toAbsolutePath().toString();
        return String.format(wilmaStartMessage, versionTitleProvider.getVersionTitle(), proxyPort, wilmaPort, appLogPath, messagesPath);
    }

    private int getProxyPort() {
        PropertyDTO properties = configurationAccess.getProperties();
        return properties.getProxyPort();
    }

    private int getWilmaPort() {
        PropertyDTO properties = configurationAccess.getProperties();
        return properties.getWilmaPort();
    }

}
