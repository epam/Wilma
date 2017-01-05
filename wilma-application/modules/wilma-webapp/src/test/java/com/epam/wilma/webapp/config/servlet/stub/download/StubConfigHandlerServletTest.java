package com.epam.wilma.webapp.config.servlet.stub.download;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import com.epam.wilma.domain.stubconfig.StubResourceHolder;
import com.epam.wilma.stubconfig.dom.transformer.DomBasedDocumentTransformer;
import com.epam.wilma.domain.stubconfig.exception.DocumentTransformationException;
import com.epam.wilma.webapp.config.servlet.stub.download.helper.ByteArrayConverter;

/**
 * Tests for {@link StubConfigHandlerServlet}.
 * @author Tamas_Bihari
 *
 */
public class StubConfigHandlerServletTest {
    private static final String FILE_CONTENT = "content \n newline";
    private static final String DEFAULT_GROUPNAME = "test";
    @Mock
    private DomBasedDocumentTransformer domBasedDocumentTransformer;
    @Mock
    private StubResourceHolder stubResourceHolder;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter writer;
    @Mock
    private Document document;
    @Mock
    private ByteArrayConverter byteArrayConverter;

    @InjectMocks
    private StubConfigHandlerServlet underTest;

    @BeforeMethod
    public void setUp() throws IOException {
        initMocks(this);
        given(response.getWriter()).willReturn(writer);
        given(request.getParameter("groupname")).willReturn("test");
    }

    @Test
    public void testDoPostShouldCallDoGet() throws ServletException, IOException {
        //GIVEN
        given(stubResourceHolder.getActualStubConfigDocument(DEFAULT_GROUPNAME)).willReturn(null);
        //WHEN
        underTest.doPost(request, response);
        //THEN doGet was called
        verify(writer).write(anyString());
        verify(writer).flush();
        verify(writer).close();
    }

    @Test
    public void testDoGetShouldWriteActualXmlToResponse() throws ServletException, IOException, DocumentTransformationException {
        //GIVEN
        given(request.getHeader("User-Agent")).willReturn("windows");
        given(stubResourceHolder.getActualStubConfigDocument(DEFAULT_GROUPNAME)).willReturn(document);
        byte[] xmlAsByteArray = "DOCUMENT".getBytes();
        given(domBasedDocumentTransformer.transform(document)).willReturn(xmlAsByteArray);
        given(byteArrayConverter.toString(xmlAsByteArray)).willReturn(FILE_CONTENT);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(writer).write("content \r\n newline");
        verify(writer).flush();
        verify(writer).close();
    }

    @Test
    public void testDoGetShouldWriteErrorResponseWhenActualXmlIsNull() throws ServletException, IOException {
        //GIVEN
        given(stubResourceHolder.getActualStubConfigDocument(DEFAULT_GROUPNAME)).willReturn(null);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(writer).write(anyString());
    }

    @Test
    public void testDoGetShouldWriteErrorResponseWhenActualXmlCanNotBeTransformed() throws ServletException, IOException,
        DocumentTransformationException {
        //GIVEN
        given(stubResourceHolder.getActualStubConfigDocument(DEFAULT_GROUPNAME)).willReturn(document);
        given(domBasedDocumentTransformer.transform(document)).willThrow(new DocumentTransformationException("", new IOException()));
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(writer).write(anyString());
    }

    @Test
    public void testDoGetShouldNotReplaceNewLineWhenNotOnWindows() throws ServletException, IOException, DocumentTransformationException {
        //GIVEN
        given(request.getHeader("User-Agent")).willReturn("mac");
        given(stubResourceHolder.getActualStubConfigDocument(DEFAULT_GROUPNAME)).willReturn(document);
        byte[] xmlAsByteArray = "DOCUMENT".getBytes();
        given(domBasedDocumentTransformer.transform(document)).willReturn(xmlAsByteArray);
        given(byteArrayConverter.toString(xmlAsByteArray)).willReturn(FILE_CONTENT);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(writer).write(FILE_CONTENT);
    }

    @Test
    public void testDoGetShouldNotReplaceNewLineWhenUserAgentIsNull() throws ServletException, IOException, DocumentTransformationException {
        //GIVEN
        given(request.getHeader("User-Agent")).willReturn(null);
        given(stubResourceHolder.getActualStubConfigDocument(DEFAULT_GROUPNAME)).willReturn(document);
        byte[] xmlAsByteArray = "DOCUMENT".getBytes();
        given(domBasedDocumentTransformer.transform(document)).willReturn(xmlAsByteArray);
        given(byteArrayConverter.toString(xmlAsByteArray)).willReturn(FILE_CONTENT);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(writer).write(FILE_CONTENT);
    }

    @Test
    public void testDoGetShouldSetContentTypeToXmlWhenReqParamSourceIsTrue() throws ServletException, IOException, DocumentTransformationException {
        //GIVEN
        given(request.getParameter("source")).willReturn("true");
        given(request.getHeader("User-Agent")).willReturn(null);
        given(stubResourceHolder.getActualStubConfigDocument(DEFAULT_GROUPNAME)).willReturn(document);
        byte[] xmlAsByteArray = "DOCUMENT".getBytes();
        given(domBasedDocumentTransformer.transform(document)).willReturn(xmlAsByteArray);
        given(byteArrayConverter.toString(xmlAsByteArray)).willReturn(FILE_CONTENT);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(response).setContentType("application/xml");
    }

    @Test
    public void testDoGetShouldSetContentTypeToTextWhenReqParamSourceIsNotTrue() throws ServletException, IOException,
        DocumentTransformationException {
        //GIVEN
        given(request.getParameter("source")).willReturn("");
        given(request.getHeader("User-Agent")).willReturn(null);
        given(stubResourceHolder.getActualStubConfigDocument(DEFAULT_GROUPNAME)).willReturn(document);
        byte[] xmlAsByteArray = "DOCUMENT".getBytes();
        given(domBasedDocumentTransformer.transform(document)).willReturn(xmlAsByteArray);
        given(byteArrayConverter.toString(xmlAsByteArray)).willReturn(FILE_CONTENT);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(response).setContentType("text/plain");
    }

    @Test
    public void testDoGetShouldSetContentTypeToTextWhenReqParamSourceIsNull() throws ServletException, IOException, DocumentTransformationException {
        //GIVEN
        given(request.getParameter("source")).willReturn(null);
        given(request.getHeader("User-Agent")).willReturn(null);
        given(stubResourceHolder.getActualStubConfigDocument(DEFAULT_GROUPNAME)).willReturn(document);
        byte[] xmlAsByteArray = "DOCUMENT".getBytes();
        given(domBasedDocumentTransformer.transform(document)).willReturn(xmlAsByteArray);
        given(byteArrayConverter.toString(xmlAsByteArray)).willReturn(FILE_CONTENT);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(response).setContentType("text/plain");
    }
}
