package com.epam.wilma.webapp.config.servlet.stub.upload;
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
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit tests for {@link MultiPartFileParser} class.
 * @author Tamas_Bihari
 *
 */
public class MultiPartFileParserTest {

    @Mock
    private MultiPartFileProcessor multiPartFileProcessor;
    @Mock
    private FileItem fileItem;
    @Mock
    private InputStream uploadedFileStream;

    private MultiPartFileParser underTest;
    private List<FileItem> uploadedFiles;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new MultiPartFileParser(multiPartFileProcessor);
        uploadedFiles = new ArrayList<>();
    }

    @Test
    public void testParseMultiPartFilesShouldReturnWithMsgWhenParamListIsEmpty() throws IOException {
        //GIVEN in setUp
        //WHEN
        String actual = underTest.parseMultiPartFiles(uploadedFiles);
        //THEN
        assertEquals(actual, "No file uploaded");
    }

    @Test
    public void testParseMultiPartFilesShouldReturnWithEmptyMsgWhenParameterIsFormField() throws IOException {
        //GIVEN
        given(fileItem.isFormField()).willReturn(true);
        uploadedFiles.add(fileItem);
        //WHEN
        String actual = underTest.parseMultiPartFiles(uploadedFiles);
        //THEN
        assertEquals(actual, "");
    }

    @Test
    public void testParseMultiPartFilesShouldReturnWithProcessingResultMsgWhenParameterIsNotFormField() throws IOException {
        //GIVEN
        String stringValue = "STRING_VALUE";
        given(fileItem.isFormField()).willReturn(false);
        given(fileItem.getName()).willReturn(stringValue);
        given(fileItem.getContentType()).willReturn(stringValue);
        given(fileItem.getFieldName()).willReturn(stringValue);
        given(fileItem.getInputStream()).willReturn(uploadedFileStream);
        given(multiPartFileProcessor.processUploadedFile(uploadedFileStream, stringValue, stringValue, stringValue)).willReturn(
                "processing result message");
        uploadedFiles.add(fileItem);
        //WHEN
        String actual = underTest.parseMultiPartFiles(uploadedFiles);
        //THEN
        assertEquals(actual, "processing result message");
    }

    @Test(expectedExceptions = IOException.class)
    public void testParseMultiPartFilesShouldThrowExceptionWhenFileCanNotBeParsed() throws IOException {
        //GIVEN
        given(fileItem.isFormField()).willReturn(false);
        given(fileItem.getInputStream()).willThrow(new IOException());
        uploadedFiles.add(fileItem);
        //WHEN
        underTest.parseMultiPartFiles(uploadedFiles);
        //THEN exception is thrown
    }

}
