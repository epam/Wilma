package com.epam.wilma.webapp.config.servlet.stub.upload;

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
import static org.testng.Assert.assertEquals;

import java.io.InputStream;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.router.RoutingService;
import com.epam.wilma.stubconfig.StubDescriptorFactory;
import com.epam.wilma.webapp.config.servlet.stub.upload.helper.FileWriter;
import com.epam.wilma.webapp.domain.exception.CannotUploadExternalResourceException;
import com.epam.wilma.webapp.service.command.NewStubDescriptorCommand;

/**
 * Tests for {@link MultiPartFileProcessor}.
 * @author Tamas_Bihari
 *
 */
public class MultiPartFileProcessorTest {
    private static final String APPLICATION_JAVA = "application/java";
    private static final String XML_CONTENT_TYPE = "text/xml";
    private static final String OCTET_STREAM_CONTENT_TYPE = "application/octet-stream";
    private static final String EXCEPTION_MESSAGE = "Could not upload external resource ";
    private static final String FILE_PATH = "proba.file";
    private static final String FILE_PATH_W_SLASH = "proba/proba.file";
    private static final String FILE_PATH_W_BACK_SLASH = "proba\\proba.file";

    @Mock
    private StubResourcePathProvider stubResourcePathProvider;
    @Mock
    private FileWriter fileWriter;
    @Mock
    private StubDescriptorFactory stubConfigurationBuilder;
    @Mock
    private RoutingService routingService;
    @Mock
    private InputStream resource;

    @InjectMocks
    private MultiPartFileProcessor underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessUploadedFileShouldReturnWhenStubConfigIsValid() throws ClassNotFoundException {
        //GIVEN in setUp
        //WHEN
        String actual = underTest.processUploadedFile(resource, XML_CONTENT_TYPE, "stub-configuration", FILE_PATH);
        //THEN
        verify(routingService).performModification(Mockito.any(NewStubDescriptorCommand.class));
        assertEquals(actual, "New stub configuration was uploaded to Wilma.");
    }

    @Test(expectedExceptions = CannotUploadExternalResourceException.class)
    public void testProcessUploadedFileShouldThrowExceptionWhenUploadedStubConfigContentTypeIsWrong() {
        //GIVEN in setUp
        //WHEN
        underTest.processUploadedFile(resource, OCTET_STREAM_CONTENT_TYPE, "stub-configuration", FILE_PATH);
        //THEN exception is thrown
    }

    @Test
    public void testProcessUploadedFileShouldReturnWhenConditionCheckerUploaded() {
        //GIVEN
        given(stubResourcePathProvider.getConditionCheckerPathAsString()).willReturn("");
        //WHEN
        String actual = underTest.processUploadedFile(resource, APPLICATION_JAVA, "stub-condition-checker", FILE_PATH);
        //THEN
        verify(fileWriter).write(resource, "/" + FILE_PATH, EXCEPTION_MESSAGE);
        assertEquals(actual, "External condition checker class '" + FILE_PATH + "' was uploaded to Wilma.");
    }

    @Test(expectedExceptions = CannotUploadExternalResourceException.class)
    public void testProcessUploadedFileShouldThrowExceptionWhenUploadedFileContentTypeNotJavaOrOctetStream() {
        //GIVEN in setUp
        //WHEN
        underTest.processUploadedFile(resource, XML_CONTENT_TYPE, "stub-condition-checker", FILE_PATH);
        //THEN exception is thrown
    }

    @Test
    public void testProcessUploadedFileShouldReturnWhenTemplateFormatterUploaded() {
        //GIVEN
        given(stubResourcePathProvider.getTemplateFormattersPathAsString()).willReturn("");
        //WHEN
        String actual = underTest.processUploadedFile(resource, OCTET_STREAM_CONTENT_TYPE, "stub-template-formatter", FILE_PATH);
        //THEN
        verify(fileWriter).write(resource, "/" + FILE_PATH, EXCEPTION_MESSAGE);
        assertEquals(actual, "External template formatter class '" + FILE_PATH + "' was uploaded to Wilma.");
    }

    @Test(expectedExceptions = CannotUploadExternalResourceException.class)
    public void testProcessUploadedFileShouldThrowExceptionWhenUploadedTemplateFormatterContentTypeNotJavaOrOctetStream() {
        //GIVEN in setUp
        //WHEN
        underTest.processUploadedFile(resource, XML_CONTENT_TYPE, "stub-template-formatter", FILE_PATH);
        //THEN exception is thrown
    }

    @Test
    public void testProcessUploadedFileShouldReturnWhenTemplateUploaded() {
        //GIVEN
        given(stubResourcePathProvider.getTemplatesPathAsString()).willReturn("");
        //WHEN
        String actual = underTest.processUploadedFile(resource, "some-content-type", "stub-template", FILE_PATH);
        //THEN
        verify(fileWriter).write(resource, "/" + FILE_PATH, EXCEPTION_MESSAGE);
        assertEquals(actual, "External template '" + FILE_PATH + "' was uploaded to Wilma.");
    }

    @Test(expectedExceptions = CannotUploadExternalResourceException.class)
    public void testProcessUploadedFileShouldReturnWhenUploadedingFileFromUnknownInputField() {
        //GIVEN in setUp
        //WHEN
        underTest.processUploadedFile(resource, XML_CONTENT_TYPE, "asdfasdf", FILE_PATH);
        //THEN exception is thrown
    }

    @Test
    public void testProcessUploadedFileShouldReturnWhenInterceptorUploaded() {
        //GIVEN
        given(stubResourcePathProvider.getInterceptorPathAsString()).willReturn("");
        //WHEN
        String actual = underTest.processUploadedFile(resource, "some-content-type", "stub-interceptor", FILE_PATH);
        //THEN
        verify(fileWriter).write(resource, "/" + FILE_PATH, EXCEPTION_MESSAGE);
        assertEquals(actual, "External interceptor '" + FILE_PATH + "' was uploaded to Wilma.");
    }

    @Test
    public void testProcessUploadedFileShouldReturnWhenJarUploaded() {
        //GIVEN
        given(stubResourcePathProvider.getJarPathAsString()).willReturn("");
        //WHEN
        String actual = underTest.processUploadedFile(resource, "some-content-type", "stub-jar", FILE_PATH);
        //THEN
        verify(fileWriter).write(resource, "/" + FILE_PATH, EXCEPTION_MESSAGE);
        assertEquals(actual, "External jar '" + FILE_PATH + "' was uploaded to Wilma.");
    }

    @Test
    public void testProcessUploadedFileShouldGetFileNameFromPathWhichContainsSlash() {
        //GIVEN
        given(stubResourcePathProvider.getTemplatesPathAsString()).willReturn("");
        //WHEN
        underTest.processUploadedFile(resource, "some-content-type", "stub-template", FILE_PATH_W_SLASH);
        //THEN
        verify(fileWriter).write(resource, "/" + FILE_PATH, EXCEPTION_MESSAGE);
    }

    @Test
    public void testProcessUploadedFileShouldGetFileNameFromPathWhichContainsBackSlash() {
        //GIVEN
        given(stubResourcePathProvider.getTemplatesPathAsString()).willReturn("");
        //WHEN
        underTest.processUploadedFile(resource, "some-content-type", "stub-template", FILE_PATH_W_BACK_SLASH);
        //THEN
        verify(fileWriter).write(resource, "/" + FILE_PATH, EXCEPTION_MESSAGE);
    }

}
