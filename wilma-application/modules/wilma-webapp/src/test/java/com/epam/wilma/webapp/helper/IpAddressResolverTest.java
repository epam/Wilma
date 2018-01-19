package com.epam.wilma.webapp.helper;
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

import static org.mockito.BDDMockito.given;
import static org.testng.AssertJUnit.assertEquals;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for {@link IpAddressResolver}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class IpAddressResolverTest {

    @Mock
    private InetAddressFactory inetAddressFactory;

    @InjectMocks
    private IpAddressResolver underTest;

    @Mock
    private InetAddress inetAddress;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testResolveToHostNameShouldReturnHostName() throws UnknownHostException {
        //GIVEN
        String expectedHostName = "expectedHostName";
        given(inetAddressFactory.createByName("anIPAddress")).willReturn(inetAddress);
        given(inetAddress.getHostName()).willReturn(expectedHostName);
        //WHEN
        String actualHostName = underTest.resolveToHostName("anIPAddress");
        //THEN
        assertEquals(expectedHostName, actualHostName);
    }

    @Test
    public void testResolveToHostNameShouldReturnUnknownHostNameWhenUnknownHostExceptionOccurs() throws UnknownHostException {
        //GIVEN
        String expectedHostName = "UNKNOWN HOST(anIPAddress)";
        given(inetAddressFactory.createByName("anIPAddress")).willThrow(new UnknownHostException());
        //WHEN
        String actualHostName = underTest.resolveToHostName("anIPAddress");
        //THEN
        assertEquals(expectedHostName, actualHostName);
    }
}
