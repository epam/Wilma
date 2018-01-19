package com.epam.wilma.router;

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
import static org.mockito.Mockito.verify;
import static org.testng.AssertJUnit.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.router.configuration.RouteEngineConfigurationAccess;
import com.epam.wilma.router.configuration.domain.PropertyDTO;

/**
 * Unit test for {@link LocalhostRequestRouter}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class LocalhostRequestRouterTest {

    @Mock
    private RouteEngineConfigurationAccess routeEngineConfigurationAccess;

    @InjectMocks
    private LocalhostRequestRouter underTest;

    @Mock
    private PropertyDTO propertyDTO;
    @Mock
    private WilmaHttpRequest request;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testWhenOnApplicationEventReceivedShouldSetInternalPort() {
        //GIVEN
        given(routeEngineConfigurationAccess.getProperties()).willReturn(propertyDTO);
        given(propertyDTO.getProxyPort()).willReturn(9876);
        //WHEN
        underTest.onApplicationEvent(null);
        //THEN
        assertEquals(9876, Whitebox.getInternalState(underTest, "internalPort"));
    }

    @Test
    public void testThatRerouteShouldSetRequestUriToServletAddress() throws URISyntaxException {
        //GIVEN
        URI expected = new URI("http://127.0.0.1:0/local/");
        //WHEN
        underTest.reroute(request);
        //THEN
        verify(request).setUri(expected);
    }

}
