package com.epam.wilma.indexing.jms.add;
/*==========================================================================
Copyright 2013-2016 EPAM Systems

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

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.indexing.domain.IndexMessage;
import com.epam.wilma.indexing.domain.IndexingType;
import com.epam.wilma.indexing.jms.JmsMessageIndexer;
import com.epam.wilma.indexing.jms.helper.FileNameProvider;
import com.epam.wilma.indexing.jms.helper.IndexMessageFactory;

/**
 * Unit tests for the class {@link JmsRequestIndexingProcessor}.
 * @author Tunde_Kovacs
 *
 */
public class JmsRequestIndexingProcessorTest {

    private static final String FILE_NAME = "fileName";
    @Mock
    private JmsMessageIndexer messageIndexer;
    @Mock
    private IndexMessageFactory indexMessageFactory;
    @Mock
    private IndexMessage message;
    @Mock
    private FileNameProvider fileNameProvider;
    @Mock
    private WilmaHttpRequest request;

    @InjectMocks
    private JmsRequestIndexingProcessor underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessShouldCallIndexMessage() {
        //GIVEN
        given(fileNameProvider.getFileName(request)).willReturn(FILE_NAME);
        given(indexMessageFactory.createIndexMessage(FILE_NAME, IndexingType.ADD.getName())).willReturn(message);
        //WHEN
        underTest.process(request);
        //THEN
        verify(messageIndexer).sendMessageToIndexer(message);
    }
}
