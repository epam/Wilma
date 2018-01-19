package com.epam.wilma.webapp.stub.response.support;
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

import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.stubconfig.dialog.response.MimeType;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.response.template.Template;
import com.epam.wilma.router.domain.ResponseDescriptorDTO;
import org.mockito.Answers;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link StubResponseHeaderConfigurer}.
 * @author Tamas_Bihari
 *
 */
public class StubResponseHeaderConfigurerTest {
    private static final String WILMA_LOGGER_ID = "wilma-logger-id";
    private static final String DIALOG_DESCRIPTOR_NAME = "DIALOG-DESCRIPTOR-NAME";
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ResponseDescriptorDTO responseDescriptorDTO;

    private StubResponseHeaderConfigurer underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new StubResponseHeaderConfigurer();
    }

    @Test
    public void testAddWilmaInfoToResponseHeaderShouldAddNewHeadersToResponseWhenWilmaSequencNotExistInRequestHeader() {
        //GIVEN
        given(request.getHeader(WilmaHttpEntity.WILMA_SEQUENCE_ID)).willReturn(null);
        //WHEN
        underTest.addWilmaInfoToResponseHeader(request, response, DIALOG_DESCRIPTOR_NAME);
        //THEN
        verify(response).addHeader(WilmaHttpEntity.WILMA_SEQUENCE_ID, DIALOG_DESCRIPTOR_NAME);
    }

    @Test
    public void testAddWilmaInfoToResponseHeaderShouldAddNewHeadersToResponseWhenWilmaSequencExistsInRequestHeader() {
        //GIVEN
        given(request.getHeader(WilmaHttpEntity.WILMA_SEQUENCE_ID)).willReturn(DIALOG_DESCRIPTOR_NAME);
        //WHEN
        underTest.addWilmaInfoToResponseHeader(request, response, DIALOG_DESCRIPTOR_NAME);
        //THEN
        verify(response).addHeader(WilmaHttpEntity.WILMA_SEQUENCE_ID, DIALOG_DESCRIPTOR_NAME + "," + DIALOG_DESCRIPTOR_NAME);
    }

    @Test
    public void testSetResponseContentTypeAndStatusShouldSetContentTypeAndStatusCodeOnResponse() {
        //GIVEN
        String statusCode = "404";
        Template template = new Template(null, null, null);
        ResponseDescriptorAttributes attributes = new ResponseDescriptorAttributes.Builder().delay(0).code(statusCode).template(template)
                .mimeType(MimeType.TEXT.getOfficialMimeType()).build();
        given(responseDescriptorDTO.getResponseDescriptor().getAttributes()).willReturn(attributes);
        //WHEN
        underTest.setResponseContentTypeAndStatus(response, responseDescriptorDTO);
        //THEN
        verify(response).setStatus(Integer.valueOf(statusCode));
        verify(response).setContentType(MimeType.TEXT.getOfficialMimeType());
    }

    @Test
    public void testSetErrorResponseContentTypeAndStatusShouldSetContentTypeAndStatusCodeOnResponse() {
        //GIVEN in setUp
        //WHEN
        underTest.setErrorResponseContentTypeAndStatus(response);
        //THEN
        verify(response).setStatus(anyInt());
        verify(response).setContentType(Matchers.anyString());
    }

}
