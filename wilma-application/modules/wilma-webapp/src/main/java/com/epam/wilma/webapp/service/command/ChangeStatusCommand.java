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
import com.epam.wilma.domain.stubconfig.StubDescriptorAttributes;
import com.epam.wilma.router.command.StubDescriptorModificationCommand;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;

/**
 * This class changes the status of selected stub descriptor.
 * @author Tibor_Kovacs
 *
 */
public class ChangeStatusCommand implements StubDescriptorModificationCommand {
    private final Logger logger = LoggerFactory.getLogger(ChangeStatusCommand.class);

    private final boolean nextStatus;
    private final String groupName;
    private final HttpServletRequest request;
    private final UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;


    /**
     * Creates a new ChangeStatusCommand which will set the given status.
     * @param nextStatus is the status what we want to set
     * @param groupName is key of searched stub configuration.
     * @param request is only needed for {@link UrlAccessLogMessageAssembler}
     * @param urlAccessLogMessageAssembler is the logger
     */
    public ChangeStatusCommand(final boolean nextStatus, final String groupName, final HttpServletRequest request,
            final UrlAccessLogMessageAssembler urlAccessLogMessageAssembler) {
        this.nextStatus = nextStatus;
        this.groupName = groupName;
        this.request = request;
        this.urlAccessLogMessageAssembler = urlAccessLogMessageAssembler;
    }

    @Override
    public Map<String, StubDescriptor> modify(Map<String, StubDescriptor> stubDescriptors) {
        Map<String, StubDescriptor> updated = new LinkedHashMap<>(stubDescriptors);
        StubDescriptor selected = updated.get(groupName);
        if (selected != null) {
            StubDescriptorAttributes attributes = selected.getAttributes();
            attributes.setActive(nextStatus);
            if (nextStatus) {
                logger.info(urlAccessLogMessageAssembler.assembleMessage(request, groupName + " stub configuration: Enabled"));
            } else {
                logger.info(urlAccessLogMessageAssembler.assembleMessage(request, groupName + " stub configuration: Disabled"));
            }
        }
        return updated;
    }

    public String getGroupName() {
        return groupName;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public boolean isNextStatus() {
        return nextStatus;
    }
}
