package com.epam.wilma.engine.configuration.parser;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.core.configuration.domain.WilmaAdminHostsDTO;
import com.epam.wilma.properties.PropertyHolder;
import com.epam.wilma.webapp.config.servlet.helper.BufferedReaderFactory;

/**
 * Class that handles the parsing of the file that contains the hosts with admin privilege.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class WilmaAdminHostsFileParser {

    private static final String WILMA_ADMIN_HOSTS_FILE = "wilma.admin.hosts.file";
    private static final String ERROR_TEMPLATE = "Error occurred at wilma admin hosts file parsing, file path: '%s'";

    @Autowired
    private WilmaAdminHostsDTO wilmaAdminHostsDTO;
    @Autowired
    private PropertyHolder propertyHolder;
    @Autowired
    private BufferedReaderFactory bufferedReaderFactory;

    /**
     * Parses the wilma admin hosts file and sets up the DTO.
     */
    public void parseFile() {
        String filePath = propertyHolder.get(WILMA_ADMIN_HOSTS_FILE);
        List<String> wilmaAdminHosts = new ArrayList<>();
        boolean securityEnabled = !"".equals(filePath) && filePath != null;
        if (securityEnabled) {
            try (BufferedReader bufferedReader = bufferedReaderFactory.createBufferedReaderFromFilePath(filePath)) {
                while (bufferedReader.ready()) {
                    String host = bufferedReader.readLine();
                    wilmaAdminHosts.add(host);
                }
            } catch (IOException e) {
                throw new CannotParseExternalResourceException(String.format(ERROR_TEMPLATE, filePath), e);
            }
        }
        wilmaAdminHostsDTO.setWilmaAdminHosts(wilmaAdminHosts);
        wilmaAdminHostsDTO.setSecurityEnabled(securityEnabled);
    }
}
