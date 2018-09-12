package com.epam.wilma.webapp.stub.response.formatter.xsl;
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

import java.io.File;
import java.io.IOException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.helper.FileFactory;
import com.epam.wilma.common.helper.FileUtils;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.webapp.domain.exception.ResponseFormattingFailedException;

import javax.servlet.http.HttpServletResponse;

/**
 * Tests for {@link XslBasedResponseFormatter}.
 * @author Tamas_Bihari
 *
 */
public class XslBasedResponseFormatterTest {
    private static final String XSL_PARAM_KEY = "xslFile";

    @Mock
    private FileUtils fileUtils;
    @Mock
    private FileFactory fileFactory;
    @Mock
    private StubResourcePathProvider stubResourcePathProvider;
    @Mock
    private XslResponseGenerator xslResponseGenerator;
    @Mock
    private WilmaHttpRequest wilmaRequest;
    @Mock
    private File file;

    @InjectMocks
    private XslBasedResponseFormatter underTest;

    private byte[] templateResource;

    private ParameterList params;
    private HttpServletResponse response;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        templateResource = new byte[1];
        params = new ParameterList();
    }

    @Test(expectedExceptions = ResponseFormattingFailedException.class)
    public void testFormatTemplateShouldThrowExceptionWhenXslFileParamDoesNotExist() throws Exception {
        //GIVEN in setUp
        //WHEN
        underTest.formatResponse(wilmaRequest, response, templateResource, params);
        //THEN exception is thrown
    }

    @Test(expectedExceptions = ResponseFormattingFailedException.class)
    public void testFormatTemplateShouldThrowExceptionWhenXslFileResourceDoesNotExist() throws Exception {
        //GIVEN
        params.addParameter(new Parameter(XSL_PARAM_KEY, XSL_PARAM_KEY));
        given(stubResourcePathProvider.getTemplatesPathAsString()).willReturn(XSL_PARAM_KEY);
        given(fileFactory.createFile(Mockito.anyString())).willReturn(file);
        given(fileUtils.getFileAsByteArray(file)).willThrow(new IOException());
        //WHEN
        underTest.formatResponse(wilmaRequest, response, templateResource, params);
        //THEN exception is thrown
    }

    @Test
    public void testFormatTemplateShouldGetRequestBodyFromWilmaRequest() throws Exception {
        //GIVEN
        params.addParameter(new Parameter(XSL_PARAM_KEY, XSL_PARAM_KEY));
        given(stubResourcePathProvider.getTemplatesPathAsString()).willReturn(XSL_PARAM_KEY);
        given(fileFactory.createFile(Mockito.anyString())).willReturn(file);
        given(fileUtils.getFileAsByteArray(file)).willReturn(templateResource);
        given(wilmaRequest.getBody()).willReturn(templateResource.toString());
        //WHEN
        underTest.formatResponse(wilmaRequest, response, templateResource, params);
        //THEN
        verify(wilmaRequest).getBody();
    }

    @Test
    public void testFormatTemplateShouldCallGenerateResponseOnXslResponseGenerator() throws Exception {
        //GIVEN
        params.addParameter(new Parameter(XSL_PARAM_KEY, XSL_PARAM_KEY));
        given(stubResourcePathProvider.getTemplatesPathAsString()).willReturn(XSL_PARAM_KEY);
        given(fileFactory.createFile(Mockito.anyString())).willReturn(file);
        given(fileUtils.getFileAsByteArray(file)).willReturn(templateResource);
        given(wilmaRequest.getBody()).willReturn(XSL_PARAM_KEY);
        //WHEN
        underTest.formatResponse(wilmaRequest, response, templateResource, params);
        //THEN
        verify(xslResponseGenerator).generateResponse(XSL_PARAM_KEY.getBytes(), templateResource, templateResource);
    }

}
