package com.epam.wilma.message.search.lucene.index;
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

import com.epam.wilma.message.search.domain.exception.SystemException;
import com.epam.wilma.message.search.lucene.helper.TermFactory;
import com.epam.wilma.message.search.lucene.index.helper.BufferedReaderFactory;
import com.epam.wilma.message.search.lucene.index.helper.DocumentFactory;
import com.epam.wilma.message.search.lucene.index.helper.FileInputStreamFactory;
import com.epam.wilma.message.search.lucene.index.helper.FileWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the class {@link FileIndexer}.
 *
 * @author Tunde_Kovacs
 */
public class FileIndexerTest {

    private static final String FIELD_NAME = "path";
    private Document document;
    private Term term;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private IndexWriter writer;
    @Mock
    private DocumentFactory documentFactory;
    @Mock
    private BufferedReaderFactory bufferedReaderFactory;
    @Mock
    private TermFactory termFactory;
    @Mock
    private FileInputStreamFactory fileInputStreamFactory;
    @Mock
    private FileWrapper fileWrapper;
    @Mock
    private FileInputStream fileInputStream;
    @Mock
    private BufferedReader bufferedReader;
    @Mock
    private Logger logger;

    @InjectMocks
    private FileIndexer underTest;

    @BeforeEach
    public void setUp() throws IOException {
        File file = new File("path");
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(underTest, "fieldName", FIELD_NAME);
        document = new Document();
        term = new Term(FIELD_NAME);
        given(fileWrapper.getFile()).willReturn(file);
        given(fileInputStreamFactory.createFileInputStream(file)).willReturn(fileInputStream);
        given(fileWrapper.getAbsolutePath()).willReturn("path");
        given(bufferedReaderFactory.createReader(fileInputStream)).willReturn(bufferedReader);
        given(documentFactory.createDocument()).willReturn(document);
    }

    @Test
    public void testIndexFileShouldCreateIndexWhenWriterInCreateMode() throws IOException {
        //GIVEN
        given(writer.getConfig().getOpenMode()).willReturn(OpenMode.CREATE);
        //given(file.getPath()).willReturn("path");
        given(termFactory.createTerm(FIELD_NAME, "path")).willReturn(term);
        //WHEN
        underTest.indexFile(fileWrapper);
        //THEN
        verify(writer).addDocument(document);
    }

    @Test
    public void testIndexFileShouldUpdateIndexWhenWriterInAppendMode() throws IOException {
        //GIVEN
        given(writer.getConfig().getOpenMode()).willReturn(OpenMode.APPEND);
        //given(file.getPath()).willReturn("path");
        given(termFactory.createTerm(FIELD_NAME, "path")).willReturn(term);
        //WHEN
        underTest.indexFile(fileWrapper);
        //THEN
        verify(writer).updateDocument(term, document);
    }

    @Test
    public void testIndexFileShouldThrowExceptionWhenFileNotFound() {
        Assertions.assertThrows(SystemException.class, () -> {
            //GIVEN
            given(fileInputStreamFactory.createFileInputStream(fileWrapper.getFile())).willThrow(new FileNotFoundException());
            //WHEN
            underTest.indexFile(fileWrapper);
            //THEN
        });
    }

    @Test
    public void testIndexFileShouldLogErrorWhenCannotAddDocuments() throws IOException {
        //GIVEN
        ReflectionTestUtils.setField(underTest, "logger", logger);
        given(writer.getConfig().getOpenMode()).willReturn(OpenMode.CREATE);
        willThrow(new IOException()).given(writer).addDocument(document);
        //WHEN
        underTest.indexFile(fileWrapper);
        //THEN
        verify(logger).error(Mockito.anyString());
    }
}
