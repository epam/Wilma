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
 * This class does the selected moving in the map of stub descriptors.
 * @author Tibor_Kovacs
 *
 */
public class ChangeOrderCommand implements StubDescriptorModificationCommand {
    private final Logger logger = LoggerFactory.getLogger(ChangeOrderCommand.class);

    private final int direction;
    private final String groupName;
    private final HttpServletRequest request;
    private final UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;

    /**
     * Creates a new ChangeOrderCommand, that will do the given changes.
     * @param direction is the direction of moving.
     * @param groupName is key of searched stub configuration.
     * @param request is only needed for {@link UrlAccessLogMessageAssembler}
     * @param urlAccessLogMessageAssembler is the logger
     */
    public ChangeOrderCommand(final int direction, final String groupName, final HttpServletRequest request, final UrlAccessLogMessageAssembler urlAccessLogMessageAssembler) {
        this.direction = direction;
        this.groupName = groupName;
        this.request = request;
        this.urlAccessLogMessageAssembler = urlAccessLogMessageAssembler;
    }

    @Override
    public Map<String, StubDescriptor> modify(Map<String, StubDescriptor> stubDescriptors) {
        Map<String, StubDescriptor> updated;
        updated = doTheMoving(direction, groupName, stubDescriptors, request);
        return updated;
    }

    private Map<String, StubDescriptor> doTheMoving(final int direction, final String groupName,
            final Map<String, StubDescriptor> oldStubDescriptors, final HttpServletRequest request) {
        Map<String, StubDescriptor> result;
        if (direction < 0) {
            result = moveStubDescriptorDownByOne(groupName, oldStubDescriptors, request);
        } else {
            result = moveStubDescriptorUpByOne(groupName, oldStubDescriptors, request);
        }
        return result;
    }

    private Map<String, StubDescriptor> moveStubDescriptorUpByOne(final String groupName, final Map<String, StubDescriptor> descriptors,
            final HttpServletRequest request) {
        Map<String, StubDescriptor> result = new LinkedHashMap<>();
        String[] keys = descriptors.keySet().toArray(new String[descriptors.size()]);
        if (keys.length > 0) {
            if (keys[0].equals(groupName)) { //if we are at the top already
                result = descriptors;
            } else { //moving up
                StubDescriptor selectedDescriptor = descriptors.get(groupName);
                if (selectedDescriptor == null) {
                    result = descriptors;
                } else {
                    for (int i = 0; i < keys.length; i++) {
                        int j = i + 1;
                        if ((j < keys.length) && (keys[j].equals(groupName))) {
                            result.put(groupName, selectedDescriptor);
                            result.put(keys[i], descriptors.get(keys[i]));
                        } else {
                            result.put(keys[i], descriptors.get(keys[i]));
                        }
                    }
                    logger.info(urlAccessLogMessageAssembler.assembleMessage(request, groupName + " has moved UP."));
                }
            }
        } else {
            result = descriptors;
        }
        return result;
    }

    private Map<String, StubDescriptor> moveStubDescriptorDownByOne(final String groupName, final Map<String, StubDescriptor> descriptors,
            final HttpServletRequest request) {
        Map<String, StubDescriptor> result = new LinkedHashMap<>();
        String[] keys = descriptors.keySet().toArray(new String[descriptors.size()]);
        if (keys.length > 0) {
            if (keys[descriptors.size() - 1].equals(groupName)) {
                result = descriptors;
            } else {
                StubDescriptor selectedDescriptor = descriptors.get(groupName);
                if (selectedDescriptor == null) {
                    result = descriptors;
                } else {
                    for (int i = 0; i < keys.length; i++) {
                        int j = i + 1;
                        if ((j < keys.length) && (keys[i].equals(groupName))) {
                            result.put(keys[j], descriptors.get(keys[j]));
                            result.put(groupName, selectedDescriptor);
                        } else {
                            result.put(keys[i], descriptors.get(keys[i]));
                        }
                    }
                    logger.info(urlAccessLogMessageAssembler.assembleMessage(request, groupName + " has moved DOWN."));
                }
            }
        } else {
            result = descriptors;
        }
        return result;
    }

    public String getGroupName() {
        return groupName;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public int getDirection() {
        return direction;
    }
}
