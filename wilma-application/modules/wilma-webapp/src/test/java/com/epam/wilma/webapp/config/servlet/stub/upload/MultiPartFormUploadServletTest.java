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
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.webapp.config.servlet.stub.upload.helper.ServletFileUploadFactory;
import com.epam.wilma.webapp.domain.exception.CannotUploadExternalResourceException;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;

/**
 * Unit tests for {@link MultiPartFormUploadServlet}.
 * @author Tamas_Bihari
 *
 */
public class MultiPartFormUploadServletTest {

    @Mock
    private MultiPartFileParser multiPartFileParser;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpServletRequest request;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private ServletFileUploadFactory servletFileUploadFactory;
    @Mock
    private ServletFileUpload servletFileUpload;
    @Mock
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;

    private MultiPartFormUploadServlet underTest;

    @BeforeMethod
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        given(response.getWriter()).willReturn(printWriter);
        given(request.getContentType()).willReturn("multipart/");
        given(servletFileUploadFactory.createInstance()).willReturn(servletFileUpload);
        given(urlAccessLogMessageAssembler.assembleMessage(Mockito.any(HttpServletRequest.class), Mockito.anyString())).willReturn("message");
        underTest = new MultiPartFormUploadServlet(servletFileUploadFactory, multiPartFileParser, urlAccessLogMessageAssembler);
        Whitebox.setInternalState(underTest, "urlAccessLogMessageAssembler", urlAccessLogMessageAssembler);
        Whitebox.setInternalState(underTest, "multiPartFileParser", multiPartFileParser);
    }

    @Test
    public void testDoGetShouldSetResponseContentType() throws ServletException, IOException {
        //GIVEN
        given(request.getContentType()).willReturn("GET");
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(response).setContentType("text/html");
    }

    @Test
    public void testDoGetShouldWriteResopnseWhenGotNotMultiPartRequest() throws ServletException, IOException {
        //GIVEN
        given(request.getContentType()).willReturn("GET");
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(response).setContentType("text/html");
        verify(printWriter).println("You are not trying to upload");
    }

    @Test
    public void testDoGetShouldSendErrorResponseWhenRequestParsingFailedByTheServletFileUpload() throws ServletException, IOException,
        FileUploadException {
        //GIVEN
        given(request.getMethod()).willReturn("POST");
        given(servletFileUpload.parseRequest(request)).willThrow(new FileUploadException());
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(response).setContentType("text/html");
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(printWriter).write(Matchers.anyString());
    }

    @Test
    public void testDoGetShouldSendErrorResponseWhenFileParsingFailed() throws ServletException, IOException, FileUploadException {
        //GIVEN
        List<FileItem> uploadedFiles = new ArrayList<>();
        given(request.getMethod()).willReturn("POST");
        given(servletFileUpload.parseRequest(request)).willReturn(uploadedFiles);
        given(multiPartFileParser.parseMultiPartFiles(uploadedFiles)).willThrow(new CannotUploadExternalResourceException("", null));
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(response).setContentType("text/html");
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(printWriter).write(Matchers.anyString());
    }

    @Test
    public void testDoGetShouldSendFileParserMessageWhenUploadingSuccess() throws ServletException, IOException, FileUploadException {
        //GIVEN
        List<FileItem> uploadedFiles = new ArrayList<>();
        given(request.getMethod()).willReturn("POST");
        given(servletFileUpload.parseRequest(request)).willReturn(uploadedFiles);
        given(multiPartFileParser.parseMultiPartFiles(uploadedFiles)).willReturn("");
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(response).setContentType("text/html");
        verify(printWriter).write("");
    }

    @Test
    public void testDoPostShouldCallDoGet() throws ServletException, IOException {
        //GIVEN
        given(request.getContentType()).willReturn("GET");
        //WHEN
        underTest.doPost(request, response);
        //THEN
        verify(response).setContentType("text/html");
    }

}
