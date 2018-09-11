package com.epam.wilma.webapp.stub.response;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.webapp.stub.response.processor.StubResponseProcessor;

/**
 * Tests for {@link StubResponseWriter} class.
 * @author Tamas_Bihari
 *
 */
public class StubResponseWriterTest {

    private static final String ERROR_MESSAGE_FOR_UNWANTED_REQUESTS = "Wilma has declined this request.";
    @Mock
    private StubResponseGenerator stubResponseGenerator;
    @Mock
    private StubResponseProcessor stubResponseProcessor;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private ServletOutputStream outputStream;

    private byte[] byteArray;

    @InjectMocks
    private StubResponseWriter underTest;

    @BeforeMethod
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        byteArray = new byte[1];
        given(resp.getOutputStream()).willReturn(outputStream);
    }

    @Test
    public void testWriteResponseShouldGenerateResponse() throws IOException {
        //GIVEN in setUp
        //WHEN
        underTest.writeResponse(req, resp);
        //THEN
        verify(stubResponseGenerator).generateResponse(req, resp);
    }

    @Test
    public void testWriteResponseShouldProcessResponse() throws IOException {
        //GIVEN
        given(stubResponseGenerator.generateResponse(req, resp)).willReturn(byteArray);
        //WHEN
        underTest.writeResponse(req, resp);
        //THEN
        verify(stubResponseProcessor).processResponse(req, resp, byteArray);
    }

    @Test
    public void testWriteResponseShouldWriteResponseToServletOutputStream() throws IOException {
        //GIVEN
        given(stubResponseGenerator.generateResponse(req, resp)).willReturn(byteArray);
        given(stubResponseProcessor.processResponse(req, resp, byteArray)).willReturn(byteArray);
        //WHEN
        underTest.writeResponse(req, resp);
        //THEN
        verify(resp).getOutputStream();
        verify(outputStream).write(byteArray);
        verify(outputStream).close();
    }

    @Test
    public void testWriteResponseShouldAnswerWithErrorWhenGetUnwantedRequest() throws IOException {
        //GIVEN
        given(stubResponseGenerator.generateResponse(req, resp)).willReturn(null);
        //WHEN
        underTest.writeResponse(req, resp);
        //THEN
        verify(resp).getOutputStream();
        verify(resp).setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        verify(outputStream).write(ERROR_MESSAGE_FOR_UNWANTED_REQUESTS.getBytes());
        verify(outputStream).close();
    }

}
