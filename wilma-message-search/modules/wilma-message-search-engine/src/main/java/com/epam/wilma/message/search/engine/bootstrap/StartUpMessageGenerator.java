package com.epam.wilma.message.search.engine.bootstrap;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.epam.wilma.message.search.engine.initalize.EngineConfigurationAccess;
import com.epam.wilma.message.search.engine.initalize.PropertyDto;
import com.epam.wilma.message.search.web.support.VersionTitleProvider;

/**
 * Logs the startup message.
 * @author Tunde_Kovacs
 *
 */
@Component
public class StartUpMessageGenerator {

    private final Logger logger = LoggerFactory.getLogger(StartUpMessageGenerator.class);

    @Value("#{startMessage}")
    private String startMessage;
    @Autowired
    private EngineConfigurationAccess configurationAccess;
    @Autowired
    private VersionTitleProvider versionTitleProvider;

    /**
     * Logs the startup message.
     */
    public void logStartUpMessage() {
        PropertyDto properties = configurationAccess.getProperties();
        Integer port = properties.getPort();
        String indexFolder = properties.getIndexFolder();
        String messageFolders = properties.getMessageFolders();
        String startUpMessage = String.format(startMessage, versionTitleProvider.getVersionTitle(), port, indexFolder, messageFolders);
        logger.info(startUpMessage);

    }

}
