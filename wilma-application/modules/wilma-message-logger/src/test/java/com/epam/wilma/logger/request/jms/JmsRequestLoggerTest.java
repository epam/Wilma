package com.epam.wilma.logger.request.jms;
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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Queue;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Provides unit tests for <tt>JmsRequestLogger</tt> class.
 *
 * @author Tunde_Kovacs
 */
public class JmsRequestLoggerTest {

    @InjectMocks
    private JmsRequestLogger underTest;

    @Mock
    private JmsTemplate jmsTemplate;
    @Mock
    private Queue queue;
    @Mock
    private JmsRequestMessageCreatorFactory jmsMessageCreatorFactory;
    @Mock
    private WilmaHttpRequest request;
    @Mock
    private JmsRequestMessageCreator jmsMessageCreator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogRequestShouldSendMessageToQueue() {
        //GIVEN
        given(jmsMessageCreatorFactory.createJmsRequestMessageCreator(request)).willReturn(jmsMessageCreator);
        given(request.isLoggingEnabled()).willReturn(true);
        underTest.setMessageLoggingEnabled(true);
        //WHEN
        underTest.logRequest(request);
        //THEN
        verify(jmsTemplate).send(queue, jmsMessageCreator);
    }

    @Test
    public void testLogRequestShouldNotSendMessageToQueueWhenSafeGuarded() {
        //GIVEN
        given(jmsMessageCreatorFactory.createJmsRequestMessageCreator(request)).willReturn(jmsMessageCreator);
        given(request.isLoggingEnabled()).willReturn(true);
        underTest.setMessageLoggingEnabled(false);
        //WHEN
        underTest.logRequest(request);
        //THEN
        verifyNoInteractions(jmsTemplate);
    }

    @Test
    public void testLogRequestShouldNotSendMessageToQueueWhenIndividualLoggingDisabled() {
        //GIVEN
        given(jmsMessageCreatorFactory.createJmsRequestMessageCreator(request)).willReturn(jmsMessageCreator);
        given(request.isLoggingEnabled()).willReturn(false);
        underTest.setMessageLoggingEnabled(false);
        //WHEN
        underTest.logRequest(request);
        //THEN
        verifyNoInteractions(jmsTemplate);
    }

}
