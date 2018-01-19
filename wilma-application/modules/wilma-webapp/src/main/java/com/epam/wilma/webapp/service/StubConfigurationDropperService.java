package com.epam.wilma.webapp.service;
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

import javax.servlet.http.HttpServletRequest;

import com.epam.wilma.webapp.service.external.ServiceMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.router.RoutingService;
import com.epam.wilma.sequence.SequenceManager;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;
import com.epam.wilma.webapp.service.command.DropCommand;

/**
 * This Service make the changes in the map of stub descriptors and then applies the changes at {@link RoutingService}.
 * @author Tibor_Kovacs
 *
 */
@Component
public class StubConfigurationDropperService {

    @Autowired
    private SequenceManager sequenceManager;
    @Autowired
    private RoutingService routingService;
    @Autowired
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;
    @Autowired
    private ServiceMap serviceMap;

    /**
     * Call the changeStubConfigurationDropper drop method and then applies this changes.
     * @param groupName is the groupname of selected stub descriptor
     * @param request is only needed for {@link UrlAccessLogMessageAssembler}
     * @throws ClassNotFoundException in case of problem
     */
    public void dropSelectedStubConfiguration(final String groupName, final HttpServletRequest request) throws ClassNotFoundException {
        sequenceManager.removeSequenceDescriptors(groupName);
        routingService.performModification(new DropCommand(groupName, request, urlAccessLogMessageAssembler));
        serviceMap.detectServices();
    }
}
