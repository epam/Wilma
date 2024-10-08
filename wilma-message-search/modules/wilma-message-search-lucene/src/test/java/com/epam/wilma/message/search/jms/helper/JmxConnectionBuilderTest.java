package com.epam.wilma.message.search.jms.helper;
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

import com.epam.wilma.message.search.jms.exception.JmxConnectionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;


/**
 * Provides unit tests for the class <tt>JmxConnectionBuilder</tt>.
 *
 * @author Tibor_Kovacs
 */
public class JmxConnectionBuilderTest {

    private final String url = "url";

    @Mock
    private JmxServiceUrlFactory urlFactory;
    @Mock
    private JMXServiceURL jmxServiceUrl;
    @Mock
    private JMXConnector jmxConnector;
    @Mock
    private MBeanServerConnection serverConnection;

    @InjectMocks
    private JmxConnectionBuilder underTest;

    @BeforeEach
    public void setUp() {
        underTest = spy(new JmxConnectionBuilder());
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBuildMBeanServerConnectionShouldCreateUrl() throws IOException {
        //GIVEN
        given(urlFactory.createJmxServiceUrl(url)).willReturn(jmxServiceUrl);
        doReturn(jmxConnector).when(underTest).getJmxConnector(jmxServiceUrl);
        //WHEN
        underTest.buildMBeanServerConnection(url);
        //THEN
        verify(urlFactory).createJmxServiceUrl(url);
    }

    @Test
    public void testBuildMBeanServerConnectionShouldReturnServerConnection() throws IOException {
        //GIVEN
        given(urlFactory.createJmxServiceUrl(url)).willReturn(jmxServiceUrl);
        doReturn(jmxConnector).when(underTest).getJmxConnector(jmxServiceUrl);
        given(jmxConnector.getMBeanServerConnection()).willReturn(serverConnection);
        //WHEN
        MBeanServerConnection actual = underTest.buildMBeanServerConnection(url);
        //THEN
        assertEquals(actual, serverConnection);
    }

    @Test
    public void testBuildMBeanServerConnectionShouldThrowExceptionWhenMalformedUrl() {
        Assertions.assertThrows(JmxConnectionException.class, () -> {
            //GIVEN
            given(urlFactory.createJmxServiceUrl(url)).willThrow(new MalformedURLException());
            //WHEN
            underTest.buildMBeanServerConnection(url);
            //THEN it should throw exception
        });
    }

    @Test
    public void testBuildMBeanServerConnectionShouldThrowExceptionWhenCannotGetConnector() {
        Assertions.assertThrows(JmxConnectionException.class, () -> {
            //GIVEN
            given(urlFactory.createJmxServiceUrl(url)).willReturn(jmxServiceUrl);
            doThrow(new IOException()).when(underTest).getJmxConnector(jmxServiceUrl);
            //WHEN
            underTest.buildMBeanServerConnection(url);
            //THEN it should throw exception
        });
    }
}
