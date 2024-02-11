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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for {@link IpAddressResolver}.
 *
 * @author Adam_Csaba_Kiraly
 */
public class IpAddressResolverTest {

    @InjectMocks
    private IpAddressResolver underTest;

    @BeforeEach
    public void setUp() throws Exception {
        underTest = new IpAddressResolver();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testResolveToHostNameShouldReturnHostNameLocalIp() {
        //GIVEN
        String expectedHostName = "127.0.0.1";
        //WHEN
        String actualHostName = underTest.resolveToHostName("127.0.0.1");
        //THEN
        assertTrue("127.0.0.1".equalsIgnoreCase(actualHostName) || "localhost".equalsIgnoreCase(actualHostName),
                "Actual Host Name is: " + actualHostName);
    }

    @Test
    public void testResolveToHostNameShouldReturnHostNameLocalHost() {
        //GIVEN
        String expectedHostName = "localhost";
        //WHEN
        String actualHostName = underTest.resolveToHostName("localhost");
        //THEN
        assertEquals(expectedHostName, actualHostName, "Actual Host Name is: " + actualHostName);
    }

    @Test
    public void testResolveToHostNameShouldReturnHostNameGoogleDns() {
        //GIVEN
        String expectedHostName = "dns.google";
        //WHEN
        String actualHostName = underTest.resolveToHostName("8.8.8.8");
        //THEN
        assertEquals(expectedHostName, actualHostName, "Actual Host Name is: " + actualHostName);
    }

    @Test
    public void testResolveToHostNameShouldReturnUnknownHostNameWhenUnknownHostExceptionOccursBadString() {
        //GIVEN
        String expectedHostName = "UNKNOWN HOST(notgood)";
        //WHEN
        String actualHostName = underTest.resolveToHostName("notgood");
        //THEN
        assertEquals(expectedHostName, actualHostName, "Actual Host Name is: " + actualHostName);
    }

    @Test
    public void testResolveToHostNameShouldReturnUnknownHostNameWhenUnknownHostExceptionOccursBadIp() {
        //GIVEN
        String expectedHostName = "UNKNOWN HOST(56.46.45.260)";
        //WHEN
        String actualHostName = underTest.resolveToHostName("56.46.45.260");
        //THEN
        assertEquals(expectedHostName, actualHostName, "Actual Host Name is: " + actualHostName);
    }

}
