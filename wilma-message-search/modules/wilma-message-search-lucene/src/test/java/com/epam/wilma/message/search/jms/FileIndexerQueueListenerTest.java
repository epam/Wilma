package com.epam.wilma.message.search.jms;
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
import com.epam.wilma.message.search.domain.exception.SystemException;
import com.epam.wilma.message.search.lucene.LuceneEngine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the class {@link FileIndexerQueueListener}.
 *
 * @author Tunde_Kovacs
 */
public class FileIndexerQueueListenerTest {

    private static final String FILENAME = "filename";

    private IndexMessage indexMessage;

    @Mock
    private LuceneEngine luceneEngine;
    @Mock
    private Message message;
    @Mock
    private ObjectMessage objectMessage;

    @InjectMocks
    private FileIndexerQueueListener underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnMessageWhenTypeIsAddShouldAddFileToIndex() throws JMSException {
        //GIVEN
        indexMessage = new IndexMessage(FILENAME, IndexingType.ADD.getName());
        given(objectMessage.getObject()).willReturn(indexMessage);
        //WHEN
        underTest.onMessage(objectMessage);
        //THEN
        verify(luceneEngine).addFileToIndex(FILENAME);
    }

    @Test
    public void testOnMessageWhenTypeIsDeleteShouldAddDeleteFromIndex() throws JMSException {
        //GIVEN
        indexMessage = new IndexMessage(FILENAME, IndexingType.DELETE.getName());
        given(objectMessage.getObject()).willReturn(indexMessage);
        //WHEN
        underTest.onMessage(objectMessage);
        //THEN
        verify(luceneEngine).deleteFileFromIndex(FILENAME);
    }

    @Test
    public void testOnMessageWhenTypeIsNotAddOrDeleteShouldDoNothing() throws JMSException {
        //GIVEN
        indexMessage = new IndexMessage(FILENAME, "modify");
        given(objectMessage.getObject()).willReturn(indexMessage);
        //WHEN
        underTest.onMessage(objectMessage);
        //THEN
        verify(luceneEngine, never()).addFileToIndex(FILENAME);
        verify(luceneEngine, never()).deleteFileFromIndex(FILENAME);
    }

    @Test
    public void testOnMessageWhenMessageIsNotObjectMessageShouldDoNothing() throws JMSException {
        //GIVEN
        indexMessage = new IndexMessage(FILENAME, "modify");
        given(objectMessage.getObject()).willReturn(indexMessage);
        //WHEN
        underTest.onMessage(message);
        //THEN
        verify(luceneEngine, never()).addFileToIndex(FILENAME);
        verify(luceneEngine, never()).deleteFileFromIndex(FILENAME);
    }

    @Test(expected = SystemException.class)
    public void testOnMessageWhenCannotGetObjectShouldThrowSystemException() throws JMSException {
        //GIVEN
        indexMessage = new IndexMessage(FILENAME, IndexingType.ADD.getName());
        given(objectMessage.getObject()).willThrow(new JMSException("exception"));
        //WHEN
        underTest.onMessage(objectMessage);
        //THEN exception should be thrown
    }

}
