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

import com.epam.wilma.core.configuration.domain.WilmaAdminHostsDTO;
import com.epam.wilma.webapp.helper.IpAddressResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

/**
 * Unit test for {@link HostValidatorService}.
 *
 * @author Adam_Csaba_Kiraly
 */
public class HostValidatorServiceTest {
    @Mock
    private List<String> allowedHosts;
    @Mock
    private WilmaAdminHostsDTO wilmaAdminHostsDTO;
    @Mock
    private IpAddressResolver ipAddressResolver;

    @InjectMocks
    private HostValidatorService underTest;

    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testOnApplicationEventShouldInitializeAllowedHosts() {
        //GIVEN
        List<String> adminHosts = new ArrayList<>();
        adminHosts.add("a");
        adminHosts.add("b");
        given(wilmaAdminHostsDTO.getWilmaAdminHosts()).willReturn(adminHosts);
        given(ipAddressResolver.resolveToHostName("a")).willReturn("a");
        given(ipAddressResolver.resolveToHostName("b")).willReturn("b");
        //WHEN
        underTest.onApplicationEvent(null);
        //THEN
        assertEquals(adminHosts, ReflectionTestUtils.getField(underTest, "allowedHosts"));
    }

    @Test
    public void testIsRequestFromAdminShouldAlwaysReturnTrueIfSecurityIsDisabled() {
        //GIVEN
        given(httpServletRequest.getRemoteAddr()).willReturn("ip");
        given(ipAddressResolver.resolveToHostName("ip")).willReturn("host");
        given(wilmaAdminHostsDTO.isSecurityEnabled()).willReturn(false);
        //WHEN
        boolean result = underTest.isRequestFromAdmin(httpServletRequest);
        //THEN
        assertTrue(result);
    }

    @Test
    public void testIsRequestFromAdminShouldReturnTrueWhenHostIsAdminAndSecurityIsEnabled() {
        //GIVEN
        given(httpServletRequest.getRemoteAddr()).willReturn("ip");
        given(ipAddressResolver.resolveToHostName("ip")).willReturn("host");
        given(wilmaAdminHostsDTO.isSecurityEnabled()).willReturn(true);
        given(allowedHosts.contains("host")).willReturn(true);
        //WHEN
        boolean result = underTest.isRequestFromAdmin(httpServletRequest);
        //THEN
        assertTrue(result);
    }

    @Test
    public void testIsRequestFromAdminShouldReturnFalseWhenHostIsNotAdminAndSecurityIsEnabled() {
        //GIVEN
        given(httpServletRequest.getRemoteAddr()).willReturn("ip");
        given(ipAddressResolver.resolveToHostName("ip")).willReturn("host");
        given(wilmaAdminHostsDTO.isSecurityEnabled()).willReturn(true);
        given(allowedHosts.contains("host")).willReturn(false);
        //WHEN
        boolean result = underTest.isRequestFromAdmin(httpServletRequest);
        //THEN
        assertFalse(result);
    }

}
