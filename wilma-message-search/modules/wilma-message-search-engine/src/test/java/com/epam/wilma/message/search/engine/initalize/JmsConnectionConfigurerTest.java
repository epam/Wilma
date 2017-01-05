package com.epam.wilma.message.search.engine.initalize;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

import java.net.URI;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit tests for the class {@link JmsConnectionConfigurer}.
 * @author Tunde_Kovacs
 *
 */
public class JmsConnectionConfigurerTest {

    @Mock
    private PooledConnectionFactory pooledConnectionFactory;
    @Mock
    private ActiveMQConnectionFactory connectionFactory;
    @Mock
    private EngineConfigurationAccess configurationAccess;
    @Mock
    private TransportConnector transportConnector;
    @Mock
    private PropertyDto properties;

    @InjectMocks
    private JmsConnectionConfigurer underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetBrokerUrlShouldSetConnectionFactoryBrokerUrl() {
        //GIVEN
        given(configurationAccess.getProperties()).willReturn(properties);
        given(properties.getJmsBrokerHost()).willReturn("localhost");
        given(properties.getJmsBrokerPort()).willReturn(16161);
        //WHEN
        underTest.setBrokerUrl();
        //THEN
        verify(connectionFactory).setBrokerURL("tcp://localhost:" + 16161);
    }

    @Test
    public void testSetBrokerUrlShouldSetTransportConnectorBrokerUrl() {
        //GIVEN
        given(configurationAccess.getProperties()).willReturn(properties);
        given(properties.getJmsBrokerHost()).willReturn("localhost");
        given(properties.getJmsBrokerPort()).willReturn(16161);
        //WHEN
        underTest.setBrokerUrl();
        //THEN
        verify(transportConnector).setUri(Mockito.any(URI.class));
    }
}
