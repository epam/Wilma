package com.epam.wilma.core.processor.response.jms;
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

import javax.jms.Queue;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jms.core.JmsTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpResponse;

/**
 * Tests for <tt>JmsResopnseBuilder</tt> class.
 */
public class JmsResponseBuilderTest {
    @Mock
    private WilmaHttpResponse wilmaHttpResponse;
    @Mock
    private JmsTemplate jmsTemplate;
    @Mock
    private Queue responseQueue;
    @Mock
    private JmsResponseMessageCreator jmsResponseMessageCreator;
    @Mock
    private JmsResponseMessageCreatorFactory messageCreatorFactory;

    @InjectMocks
    private JmsResponseBuilder underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBuildResponseShouldSendResponseToJmsQueueWhenHeadersContainsWilmaLoggerId() {
        //GIVEN
        given(messageCreatorFactory.create(wilmaHttpResponse, true)).willReturn(jmsResponseMessageCreator);
        //WHEN
        underTest.buildResponse(wilmaHttpResponse);
        //THEN
        verify(jmsTemplate).send(responseQueue, jmsResponseMessageCreator);
    }

}
