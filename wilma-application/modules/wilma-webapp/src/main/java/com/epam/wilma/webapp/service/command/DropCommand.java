package com.epam.wilma.webapp.service.command;
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

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.router.command.StubDescriptorModificationCommand;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;

/**
 * This class drops the selected stub descriptor from the stub descriptors.
 * @author Tibor_Kovacs
 *
 */
public class DropCommand implements StubDescriptorModificationCommand {
    private final Logger logger = LoggerFactory.getLogger(DropCommand.class);

    private final String groupName;
    private final HttpServletRequest request;
    private final UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;

    /**
     * Creates a new DropCommand.
     * @param groupName is key of searched stub configuration.
     * @param request is only needed for {@link UrlAccessLogMessageAssembler}
     * @param urlAccessLogMessageAssembler is the logger
     */
    public DropCommand(final String groupName, final HttpServletRequest request, final UrlAccessLogMessageAssembler urlAccessLogMessageAssembler) {
        this.groupName = groupName;
        this.request = request;
        this.urlAccessLogMessageAssembler = urlAccessLogMessageAssembler;
    }

    @Override
    public Map<String, StubDescriptor> modify(final Map<String, StubDescriptor> stubDescriptors) {
        Map<String, StubDescriptor> updated = new LinkedHashMap<>(stubDescriptors);
        StubDescriptor removed = updated.remove(groupName);
        if (removed != null) {
            logger.info(urlAccessLogMessageAssembler.assembleMessage(request, groupName + "'s stub configuration has been deleted."));
        }
        return updated;
    }

    public String getGroupName() {
        return groupName;
    }

    public HttpServletRequest getRequest() {
        return request;
    }
}
