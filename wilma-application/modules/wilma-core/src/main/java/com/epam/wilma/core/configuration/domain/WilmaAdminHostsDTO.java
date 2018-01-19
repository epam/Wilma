package com.epam.wilma.core.configuration.domain;
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

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * DTO that hold the host names with admin privileges.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class WilmaAdminHostsDTO {

    private List<String> wilmaAdminHosts;
    private boolean securityEnabled;

    public List<String> getWilmaAdminHosts() {
        return wilmaAdminHosts;
    }

    public void setWilmaAdminHosts(final List<String> wilmaAdminHosts) {
        this.wilmaAdminHosts = wilmaAdminHosts;
    }

    public void setSecurityEnabled(final boolean securityEnabled) {
        this.securityEnabled = securityEnabled;
    }

    public boolean isSecurityEnabled() {
        return securityEnabled;
    }
}
