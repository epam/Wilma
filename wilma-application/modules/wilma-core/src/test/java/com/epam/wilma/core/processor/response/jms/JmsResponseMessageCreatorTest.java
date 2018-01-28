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
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpResponse;

/**
 * Provides unit tests for the {@link JmsResponseMessageCreator} class.
 * @author Tunde_Kovacs
 */
public class JmsResponseMessageCreatorTest {
    private static final String NO_WILMA_LOGGER_ID = "Custom header";

    @Mock
    private WilmaHttpResponse response;
    @Mock
    private Session session;
    @Mock
    private ObjectMessage objectMessage;

    private JmsResponseMessageCreator underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new JmsResponseMessageCreator(response, true);
    }

    @Test(expectedExceptions = JMSException.class)
    public void testCreateMessageShouldThrowJmsExceptionWhenCanNotCreateObjectMessageFromSession() throws JMSException {
        // GIVEN
        given(session.createObjectMessage()).willThrow(new JMSException(NO_WILMA_LOGGER_ID));
        // WHEN
        underTest.createMessage(session);
        // THEN exception is thrown
    }

    @Test(expectedExceptions = JMSException.class)
    public void testCreateMessageShouldThrowJmsExceptionWhenSetObjectThrowsJMSException() throws JMSException {
        // GIVEN
        given(session.createObjectMessage()).willReturn(objectMessage);
        willThrow(new JMSException(NO_WILMA_LOGGER_ID)).given(objectMessage).setObject(response);
        // WHEN
        underTest.createMessage(session);
        // THEN exception is thrown
    }

    @Test(expectedExceptions = JMSException.class)
    public void testCreateMessageShouldThrowJmsExceptionWhenSetBooleanPropertyThrowsJMSException() throws JMSException {
        // GIVEN
        given(session.createObjectMessage()).willReturn(objectMessage);
        willThrow(new JMSException(NO_WILMA_LOGGER_ID)).given(objectMessage).setBooleanProperty("bodyDecompressed", true);
        // WHEN
        underTest.createMessage(session);
        // THEN exception is thrown
    }

    @Test
    public void testCreateMessageShouldProperly() throws JMSException {
        // GIVEN
        given(session.createObjectMessage()).willReturn(objectMessage);
        // WHEN
        Message result = underTest.createMessage(session);
        // THEN
        verify(objectMessage).setObject(response);
        assertEquals(result, objectMessage);
    }

}
