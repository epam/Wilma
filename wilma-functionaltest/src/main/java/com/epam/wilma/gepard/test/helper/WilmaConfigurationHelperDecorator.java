package com.epam.wilma.gepard.test.helper;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import com.epam.wilma.gepard.test.localhost.BlockLocalhostUsageSwitch;
import com.epam.wilma.gepard.test.operation.OperationModeSwitch;
import com.epam.wilma.gepard.test.sequence.InterceptorModeSwitch;

/**
 * Helper class to configure Wilma.
 *
 * @author Tamas Kohegyi
 */
public class WilmaConfigurationHelperDecorator extends WilmaResourceUploaderDecorator {

    private final BlockLocalhostUsageSwitch blockLocalhostUsageSwitch;
    private final OperationModeSwitch operationModeSwitch;
    private final InterceptorModeSwitch interceptorModeSwitch;

    /**
     * This class adds configuration helper methods to the Test Class.
     */
    public WilmaConfigurationHelperDecorator() {
        blockLocalhostUsageSwitch = new BlockLocalhostUsageSwitch();
        operationModeSwitch = new OperationModeSwitch();
        interceptorModeSwitch = new InterceptorModeSwitch();
    }

    /**
     * Set Wilma Localhost blocking settings on/off.
     *
     * @param state is either "on" or "off"
     * @throws Exception in case of error
     */
    public void setLocalhostBlockingTo(final String state) throws Exception {
        blockLocalhostUsageSwitch.setLocalhostBlockingModeTo(this, getWilmaInternalUrl(), state);
    }

    /**
     * Set Wilma operation mode.
     *
     * @param state can be "wilma", "stub", "proxy"
     * @throws Exception in case of error
     */
    public void setOperationModeTo(final String state) throws Exception {
        operationModeSwitch.switchOperationMode(this, getWilmaInternalUrl() + "config/admin/switch/" + state);
    }

    /**
     * Set Wilma interceptor handling mode.
     * @param state can be "on", "off"
     * @throws Exception in case of error
     */
    public void setInterceptorModeTo(final String state) throws Exception {
        interceptorModeSwitch.switchInterceptorMode(this, getWilmaInternalUrl() + "config/admin/interceptor/" + state);
    }

    /**
     * Gets the internal Wilma GUI url for getting the actual list of Stub Descriptors.
     *
     * @return with URL
     */
    public String getWilmaStubConfigDescriptorsUrl() {
        return getWilmaInternalUrl() + "config/public/stubdescriptor";
    }

    public String getWilmaChangeStubConfigOrderUrl() {
        return getWilmaInternalUrl() + "config/admin/stub/changeorder";
    }

    public String getWilmaChangeStubConfigStatusUrl() {
        return getWilmaInternalUrl() + "config/admin/stub/changestatus";
    }

    public String getWilmaDropStubConfigUrl() {
        return getWilmaInternalUrl() + "config/admin/stub/drop";
    }

    public String getWilmaVersionInfoUrl() {
        return getWilmaInternalUrl() + "config/public/version";
    }

}