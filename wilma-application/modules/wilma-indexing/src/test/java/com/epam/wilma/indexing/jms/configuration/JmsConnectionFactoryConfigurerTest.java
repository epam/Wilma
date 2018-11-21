package com.epam.wilma.indexing.jms.configuration;
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

import com.epam.wilma.indexing.IndexingConfigurationAccess;
import com.epam.wilma.indexing.domain.PropertyDTO;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.event.ContextRefreshedEvent;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the class {@link JmsConnectionFactoryConfigurer}.
 * @author Tunde_Kovacs
 *
 */
public class JmsConnectionFactoryConfigurerTest {

    @Mock
    private ActiveMQConnectionFactory connectionFactory;
    @Mock
    private IndexingConfigurationAccess configurationAccess;
    @Mock
    private PropertyDTO properties;
    @Mock
    private ContextRefreshedEvent event;

    @InjectMocks
    private JmsConnectionFactoryConfigurer underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnApplicationEvent() {
        //GIVEN
        given(configurationAccess.getProperties()).willReturn(properties);
        given(properties.getBrokerHost()).willReturn("blah");
        given(properties.getBrokerPort()).willReturn(16161);
        //WHEN
        underTest.onApplicationEvent(event);
        //THEN
        verify(connectionFactory).setBrokerURL("tcp://blah:" + 16161);
    }

}
