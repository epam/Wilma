package com.epam.wilma.message.search.web.controller;
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

import com.epam.wilma.message.search.jms.helper.JmxConnectionBuilder;
import com.epam.wilma.message.search.jms.helper.JmxObjectNameProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import javax.management.AttributeNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the class {@link LoadInformationController}.
 *
 * @author Tibor_Kovacs
 */
public class LoadInformationControllerTest {
    @Mock
    private JmxConnectionBuilder jmxConnectionBuilder;
    @Mock
    private JmxObjectNameProvider jmxObjectNameProvider;
    @Mock
    private MBeanServerConnection mBeanServerConnection;
    @Mock
    private ObjectName jmsQueue;

    @InjectMocks
    private LoadInformationController underTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetLoadInformation() throws Exception {
        //GIVEN
        Long expected = new Long(91);
        given(mBeanServerConnection.getAttribute(jmsQueue, "QueueSize")).willReturn(expected);
        //WHEN
        Map<String, Long> result = underTest.getLoadInformation();
        //THEN
        assertEquals(expected, result.get("loadInformation"));
    }

    @Test
    public final void testGetLoadInformationShouldInitConnectionWhenJMSQueueIsNull() throws Exception {
        // GIVEN
        Whitebox.setInternalState(underTest, "jmsQueue", null);
        given(jmxConnectionBuilder.buildMBeanServerConnection(LoadInformationController.JMS_SERVICE_URL)).willReturn(mBeanServerConnection);
        given(jmxObjectNameProvider.getObjectName(LoadInformationController.JMS_QUEUE_OBJECT_NAME)).willReturn(jmsQueue);
        given(mBeanServerConnection.getAttribute(jmsQueue, "QueueSize")).willReturn(new Long(91));
        // WHEN
        Map<String, Long> result = underTest.getLoadInformation();
        // THEN
        verify(jmxConnectionBuilder).buildMBeanServerConnection(LoadInformationController.JMS_SERVICE_URL);
        verify(jmxObjectNameProvider).getObjectName(LoadInformationController.JMS_QUEUE_OBJECT_NAME);
    }

    @Test
    public final void testGetLoadInformationShouldInitConnectionWhenMBeanServerConnectionJMSIsNull() throws Exception {
        // GIVEN
        Whitebox.setInternalState(underTest, "jmsQueueConnection", null);
        given(jmxConnectionBuilder.buildMBeanServerConnection(LoadInformationController.JMS_SERVICE_URL)).willReturn(mBeanServerConnection);
        given(jmxObjectNameProvider.getObjectName(LoadInformationController.JMS_QUEUE_OBJECT_NAME)).willReturn(jmsQueue);
        given(mBeanServerConnection.getAttribute(jmsQueue, "QueueSize")).willReturn(new Long(91));
        // WHEN
        Map<String, Long> result = underTest.getLoadInformation();
        // THEN
        verify(jmxConnectionBuilder).buildMBeanServerConnection(LoadInformationController.JMS_SERVICE_URL);
        verify(jmxObjectNameProvider).getObjectName(LoadInformationController.JMS_QUEUE_OBJECT_NAME);
    }

    @Test
    public void testGetLoadInformationShouldSendBackEmptyJSONWhenExceptionRaised() throws Exception {
        //GIVEN
        given(jmxConnectionBuilder.buildMBeanServerConnection(LoadInformationController.JMS_SERVICE_URL)).willReturn(mBeanServerConnection);
        given(jmxObjectNameProvider.getObjectName(LoadInformationController.JMS_QUEUE_OBJECT_NAME)).willReturn(jmsQueue);
        given(mBeanServerConnection.getAttribute(jmsQueue, "QueueSize")).willThrow(AttributeNotFoundException.class);
        //WHEN
        Map<String, Long> result = underTest.getLoadInformation();
        //THEN
        assertEquals(0, result.size());
    }
}
