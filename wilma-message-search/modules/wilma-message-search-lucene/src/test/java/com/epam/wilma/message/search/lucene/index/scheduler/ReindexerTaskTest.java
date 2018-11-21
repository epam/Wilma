package com.epam.wilma.message.search.lucene.index.scheduler;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.apache.lucene.index.IndexWriter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.message.search.domain.IndexStatus;
import com.epam.wilma.message.search.lucene.LuceneEngine;
import com.epam.wilma.message.search.lucene.configuration.LuceneConfigurationAccess;
import com.epam.wilma.message.search.lucene.configuration.PropertyDto;
import com.epam.wilma.message.search.properties.helper.MessageFoldersUtil;

/**
 * Unit test for {@link ReindexerTask}.
 * @author Adam_Csaba_Kiraly
 */
public class ReindexerTaskTest {

    @Mock
    private IndexWriter indexWriter;
    @Mock
    private LuceneEngine luceneEngine;
    @Mock
    private LuceneConfigurationAccess configurationAccess;
    @Mock
    private MessageFoldersUtil messageFoldersUtil;
    @Mock
    private IndexStatus indexStatus;

    @Spy
    @InjectMocks
    private ReindexerTask underTest;

    @Mock
    private Logger logger;
    @Mock
    private PropertyDto properties;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        given(configurationAccess.getProperties()).willReturn(properties);
        given(properties.getMessageDirectories()).willReturn("something");
        given(messageFoldersUtil.getFolders("something")).willReturn(new String[]{"a"});
        Whitebox.setInternalState(underTest, "logger", logger);
    }

    @Test
    public void testRunShouldDeleteIndex() throws IOException {
        //GIVEN
        doNothing().when(underTest).doCommit();
        //WHEN
        underTest.run();
        //THEN
        verify(indexWriter).deleteAll();
        verify(indexStatus).setReady(false);
    }

    @Test
    public void testRunShouldBuildIndex() throws IOException {
        //GIVEN
        doNothing().when(underTest).doCommit();
        //WHEN
        underTest.run();
        //THEN
        verify(luceneEngine).buildIndex("a");
        verify(indexStatus).setReady(true);
    }

    @Test
    public void testRunWhenExceptionIsThrown() throws IOException {
        //GIVEN
        doThrow(new IOException()).when(underTest).doCommit();
        //WHEN
        underTest.run();
        //THEN
        verify(logger).error(Mockito.anyString(), Mockito.any(IOException.class));
    }

}
