package com.epam.wilma.core.processor.entity;
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
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.logger.writer.WilmaHttpRequestWriter;
import com.epam.wilma.logger.writer.WilmaHttpResponseWriter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Test for {@link FileWriterQueueListener}.
 */
public class FileWriterQueueListenerTest {

    @Mock
    private WilmaHttpRequestWriter requestWriter;
    @Mock
    private WilmaHttpResponseWriter responseWriter;
    @Mock
    private ObjectMessage objectMessage;
    @Mock
    private Message message;
    @Mock
    private WilmaHttpRequest request;
    @Mock
    private WilmaHttpResponse response;

    @InjectMocks
    private FileWriterQueueListener underTest;

    @BeforeMethod
    public void setUp() {
        underTest = Mockito.spy(new FileWriterQueueListener());
        initMocks(this);
    }

    @Test
    public void testOnMessageShouldCallWriteOnWilmaHttpRequestWriter() throws JMSException {
        //GIVEN
        given(objectMessage.getObject()).willReturn(request);
        given(objectMessage.getBooleanProperty("bodyDecompressed")).willReturn(true);
        doReturn(true).when(underTest).isEntityARequest(request);
        //WHEN
        underTest.onMessage(objectMessage);
        //THEN
        verify(requestWriter).write(request, true);
    }

    @Test
    public void testOnMessageShouldCallWriteOnWilmaHttpResponseWriter() throws JMSException {
        //GIVEN
        given(objectMessage.getObject()).willReturn(response);
        given(objectMessage.getBooleanProperty("bodyDecompressed")).willReturn(true);
        doReturn(false).when(underTest).isEntityARequest(response);
        //WHEN
        underTest.onMessage(objectMessage);
        //THEN
        verify(responseWriter).write(response, true);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testOnMessageShouldThrowIllegalArgumentExceptionWhenMessageNotInstanceOfObjectMessage() {
        //GIVEN in setUp
        //WHEN
        underTest.onMessage(message);
        //THEN exception is thrown
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testOnMessageShouldThrowRuntimeExceptionWhenObjectMessageNotInstanceOfWilmaHttpEntity() throws JMSException {
        //GIVEN
        given(objectMessage.getObject()).willThrow(new JMSException("Can't cast to WilmaHttpEntity!"));
        //WHEN
        underTest.onMessage(objectMessage);
        //THEN exception is thrown
    }
}
