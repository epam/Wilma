package com.epam.wilma.router.configuration.domain;
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

import com.epam.wilma.common.helper.OperationMode;

/**
 * Holds module specific properties.
 * @author Tunde_Kovacs
 *
 */
public class PropertyDTO {

    private final Integer proxyPort;
    private final OperationMode operationMode;

    /**
     * Constructs a new property holding object with the given fields.
     * @param proxyPort internal jetty port on which web application is deployed
     * @param operationMode switch between the following operation modes:
     * proxy mode, stub mode and normal mode (valid inputs are: stub, proxy, wilma)
     */
    public PropertyDTO(final Integer proxyPort, final OperationMode operationMode) {
        super();
        this.proxyPort = proxyPort;
        this.operationMode = operationMode;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public OperationMode getOperationMode() {
        return operationMode;
    }

}
