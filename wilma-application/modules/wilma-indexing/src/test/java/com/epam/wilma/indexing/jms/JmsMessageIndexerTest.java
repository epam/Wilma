package com.epam.wilma.indexing.jms;
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

import com.epam.wilma.indexing.domain.IndexMessage;
import com.epam.wilma.indexing.jms.helper.JmsIndexMessageCreator;
import com.epam.wilma.indexing.jms.helper.MessageCreatorFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the class {@link JmsMessageIndexer}.
 *
 * @author Tunde_Kovacs
 */
public class JmsMessageIndexerTest {

    @Mock
    private JmsTemplate jmsTemplateForIndexing;
    @Mock
    private MessageCreatorFactory messageCreatorFactory;
    @Mock
    private Logger logger;
    @Mock
    private IndexMessage message;
    @Mock
    private JmsIndexMessageCreator messageCreator;
    @Mock
    private ActiveMQConnectionFactory connectionFactory;

    @InjectMocks
    private JmsMessageIndexer underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        given(connectionFactory.getBrokerURL()).willReturn("");
    }

    @Test
    public void testSendMessageToIndexerShouldSendMessage() {
        //GIVEN
        given(messageCreatorFactory.createJmsMessageCreator(message)).willReturn(messageCreator);
        //WHEN
        underTest.sendMessageToIndexer(message);
        //THEN
        verify(jmsTemplateForIndexing).send(messageCreator);
    }

    @Test
    public void testSendMessageToIndexerWhenCannotConnectShouldLogError() {
        //GIVEN
        ReflectionTestUtils.setField(underTest, "logger", logger);
        given(messageCreatorFactory.createJmsMessageCreator(message)).willThrow(new RuntimeException());
        //WHEN
        underTest.sendMessageToIndexer(message);
        //THEN
        verify(logger).info(Mockito.anyString(), Mockito.anyString(), Mockito.any(Exception.class));
    }
}
