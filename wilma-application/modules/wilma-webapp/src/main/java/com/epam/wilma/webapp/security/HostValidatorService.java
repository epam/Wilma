package com.epam.wilma.webapp.security;
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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.epam.wilma.core.configuration.domain.WilmaAdminHostsDTO;
import com.epam.wilma.webapp.helper.IpAddressResolver;

/**
 * Class which determines if the request originates from an admin host or not.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class HostValidatorService implements ApplicationListener<ContextRefreshedEvent> {
    private List<String> allowedHosts;

    @Autowired
    private WilmaAdminHostsDTO wilmaAdminHostsDTO;
    @Autowired
    private IpAddressResolver ipAddressResolver;

    /**
     * Determines if the provided request, came from an admin.
     * @param request the provided request
     * @return true if the request originated from an admin, false otherwise.
     */
    public boolean isRequestFromAdmin(final ServletRequest request) {
        String host = ipAddressResolver.resolveToHostName(request.getRemoteAddr());
        return !wilmaAdminHostsDTO.isSecurityEnabled() || allowedHosts.contains(host);
    }

    private void initializeAllowedHosts() {
        allowedHosts = new ArrayList<>();
        for (String ipOrHost : wilmaAdminHostsDTO.getWilmaAdminHosts()) {
            allowedHosts.add(ipAddressResolver.resolveToHostName(ipOrHost));
        }
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        initializeAllowedHosts();
    }
}
