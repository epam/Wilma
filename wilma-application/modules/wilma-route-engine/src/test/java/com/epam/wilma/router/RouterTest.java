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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.springframework.context.event.ContextRefreshedEvent;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.router.configuration.RouteEngineConfigurationAccess;
import com.epam.wilma.router.configuration.domain.PropertyDTO;

/**
 * Provides unit tests for the <tt>Rerouter</tt> class.
 * @author Tunde_Kovacs
 */
public class RouterTest {

    private URI uri;
    private Integer internalPort;

    @Mock
    private WilmaHttpRequest request;
    @Mock
    private Logger logger;
    @Mock
    private RoutingService routingService;
    @Mock
    private RouteEngineConfigurationAccess configurationAccess;
    @Mock
    private PropertyDTO properties;
    @Mock
    private ContextRefreshedEvent event;

    @InjectMocks
    private Router underTest;

    @BeforeMethod
    public void setUp() {
        underTest = Mockito.spy(new Router());
        MockitoAnnotations.initMocks(this);
        internalPort = 1234;
        underTest.setHost("http://127.0.0.1:");
        underTest.setPath("/stub/");
        Whitebox.setInternalState(underTest, "logger", logger);
        Whitebox.setInternalState(underTest, "internalPort", internalPort);
    }

    @Test
    public void testRerouteShouldSetNewUri() throws URISyntaxException {
        //GIVEN
        uri = new URI("http://127.0.0.1:" + internalPort + "/stub/");
        given(routingService.redirectRequestToStub(request)).willReturn(true);
        //WHEN
        underTest.reroute(request);
        //TEST
        verify(request).setUri(uri);
    }

    @Test
    public void testRerouteShouldNotSetNewUri() throws URISyntaxException {
        //GIVEN
        uri = new URI("http://127.0.0.1:" + internalPort + "/stub/");
        given(routingService.redirectRequestToStub(request)).willReturn(false);
        //WHEN
        underTest.reroute(request);
        //TEST
        verify(request, times(0)).setUri(uri);
    }

    @Test
    public void testRerouteShouldReturnRequestWithNewUri() throws URISyntaxException {
        //GIVEN
        uri = new URI("http://127.0.0.1:" + internalPort + "/stub/");
        given(request.getUri()).willReturn(uri);
        given(routingService.redirectRequestToStub(request)).willReturn(true);
        //WHEN
        underTest.reroute(request);
        //TEST
        verify(request).setUri(uri);
    }

    @Test
    public void testRerouteWhenUriNotCorrectShouldLogError() throws URISyntaxException {
        //GIVEN
        doThrow(new URISyntaxException("", "")).when(underTest).getURI();
        given(routingService.redirectRequestToStub(request)).willReturn(true);
        //WHEN
        underTest.reroute(request);
        //THEN
        verify(logger).error(Mockito.anyString());
    }

    @Test
    public void testOnApplicationEventShouldSetInternalPort() {
        //GIVEN
        given(configurationAccess.getProperties()).willReturn(properties);
        given(properties.getProxyPort()).willReturn(internalPort);
        //WHEN
        underTest.onApplicationEvent(event);
        //THEN
        Integer actual = (Integer) Whitebox.getInternalState(underTest, "internalPort");
        assertEquals(actual, internalPort);
    }
}
