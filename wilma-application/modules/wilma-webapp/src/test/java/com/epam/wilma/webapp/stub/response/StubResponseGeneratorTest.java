package com.epam.wilma.webapp.stub.response;

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

import com.epam.wilma.common.helper.StackTraceToStringConverter;
import com.epam.wilma.core.MapBasedResponseDescriptorAccess;
import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.sequence.WilmaSequence;
import com.epam.wilma.domain.stubconfig.dialog.response.MimeType;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.response.template.Template;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateFormatter;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateFormatterDescriptor;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.router.domain.ResponseDescriptorDTO;
import com.epam.wilma.sequence.helper.SequenceHeaderUtil;
import com.epam.wilma.sequence.matcher.SequenceMatcher;
import com.epam.wilma.webapp.domain.exception.TemplateFormattingFailedException;
import com.epam.wilma.webapp.stub.response.support.HttpServletRequestTransformer;
import com.epam.wilma.webapp.stub.response.support.SequenceResponseGuard;
import com.epam.wilma.webapp.stub.response.support.StubResponseHeaderConfigurer;
import com.epam.wilma.webapp.stub.servlet.helper.WaitProvider;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


/**
 * Tests for {@link StubResponseGenerator} class.
 *
 * @author Tamas_Bihari
 */
public class StubResponseGeneratorTest {
    private static final String SEQUENCE_DESCRIPTOR_KEY = "seqdesckey";
    private static final String WILMA_LOGGER_ID = "test";
    private final byte[] templateResource = "template-resource".getBytes();
    @Mock
    private MapBasedResponseDescriptorAccess responseDescriptorAccess;
    @Mock
    private StackTraceToStringConverter stackTraceConverter;
    @Mock
    private WaitProvider waitProvider;
    @Mock
    private StubResponseHeaderConfigurer headerConfigurer;
    @Mock
    private HttpServletRequestTransformer requestTransformer;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ResponseDescriptorDTO responseDescriptorDTO;
    @Mock
    private ResponseDescriptor responseDescriptor;
    //    @Mock
//    private ResponseDescriptorAttributes responseDescriptorAttributes;
    @Mock
    private Template template;
    @Mock
    private TemplateFormatterDescriptor templateFormatterDescriptor;
    @Mock
    private TemplateFormatter templateFormatter;
    @Mock
    private WilmaHttpRequest wilmaRequest;
    @Mock
    private Logger logger;
    @Mock
    private SequenceHeaderUtil headerCreator;
    @Mock
    private SequenceMatcher matcher;
    @Mock
    private SequenceResponseGuard sequenceResponseGuard;

    @InjectMocks
    private StubResponseGenerator underTest;

    private Set<TemplateFormatterDescriptor> templateFormatterDescriptors;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        templateFormatterDescriptors = new HashSet<>();
        given(responseDescriptorAccess.getResponseDescriptor(anyString())).willReturn(responseDescriptorDTO);
        given(responseDescriptorDTO.getResponseDescriptor()).willReturn(responseDescriptor);
        Template template = new Template(null, null, null);
        ResponseDescriptorAttributes attributes = new ResponseDescriptorAttributes.Builder().delay(0).mimeType(MimeType.HTML.getOfficialMimeType())
                .template(template).sequenceDescriptorKey(SEQUENCE_DESCRIPTOR_KEY).build();
        given(responseDescriptor.getAttributes()).willReturn(attributes);
        given(responseDescriptor.getTemplateFormatters()).willReturn(templateFormatterDescriptors);
        given(requestTransformer.transformToWilmaHttpRequest(WILMA_LOGGER_ID, request, responseDescriptorDTO)).willReturn(wilmaRequest);
        given(stackTraceConverter.getStackTraceAsString(Matchers.any(Exception.class))).willReturn("exception-message");
        given(request.getHeader(WilmaHttpRequest.WILMA_LOGGER_ID)).willReturn(WILMA_LOGGER_ID);
        given(request.getHeader(WilmaHttpEntity.WILMA_SEQUENCE_ID)).willReturn("Test");
        given(headerCreator.resolveSequenceHeader("Test")).willReturn(new String[]{"Test"});
    }

    @Test
    public void testGenerateResponseShouldGetWilmaLoggerIdFromRequestHeaders() {
        //GIVEN in setUp
        //WHEN
        underTest.generateResponse(request, response);
        //THEN
        verify(request).getHeader(WilmaHttpRequest.WILMA_LOGGER_ID);
    }

    @Test
    public void testGenerateResponseShouldGetResponseDescriptorDTOFromResponseDescriptorAccess() {
        //GIVEN in setUp
        //WHEN
        underTest.generateResponse(request, response);
        //THEN
        verify(responseDescriptorAccess).getResponseDescriptor(anyString());
    }

    @Test
    public void testGenerateResponseShouldTemplateFormatterDescriptorsFromResponseDescriptorDTO() {
        //GIVEN in setUp
        //WHEN
        underTest.generateResponse(request, response);
        //THEN
        verify(responseDescriptorDTO, times(2)).getResponseDescriptor();
        verify(responseDescriptor).getTemplateFormatters();
    }

    @Test
    public void testGenerateResponseShouldTransformWilmaHttpRequestFromHttpServletRequestAndResponseDescriptorDTO() {
        //GIVEN in setUp
        //WHEN
        underTest.generateResponse(request, response);
        //THEN
        verify(requestTransformer).transformToWilmaHttpRequest(WILMA_LOGGER_ID, request, responseDescriptorDTO);
    }

    @Test
    public void testGenerateResponseShouldAddWilmaInfoToWilmaHttpRequest() {
        //GIVEN
        String descriptorName = "dialog-descriptor-name";
        given(responseDescriptorDTO.getDialogDescriptorName()).willReturn(descriptorName);
        //WHEN
        underTest.generateResponse(request, response);
        //THEN
        verify(headerConfigurer).addWilmaInfoToResponseHeader(request, response, descriptorName);
    }

    @Test
    public void testGenerateResponseShouldReturnTemplateResourceAndSetResponseHeadersWhenTemplateFormatterDescriptorsArrayIsNull() throws InterruptedException {
        //GIVEN
        mockTemplateResource();
        templateFormatterDescriptors = null;
        //WHEN
        byte[] actual = underTest.generateResponse(request, response);
        //THEN
        //verify set response headers
        verify(headerConfigurer).setResponseContentTypeAndStatus(response, responseDescriptorDTO);
        Assert.assertEquals(actual, templateResource);
    }

    @Test
    public void testGenerateResponseShouldReturnTemplateResourceAndSetResponseHeadersWhenNoTemplateFormatterDefined() throws InterruptedException {
        //GIVEN
        mockTemplateResource();
        //WHEN
        byte[] actual = underTest.generateResponse(request, response);
        //THEN
        verify(headerConfigurer).setResponseContentTypeAndStatus(response, responseDescriptorDTO);
        Assert.assertEquals(actual, templateResource);
    }

    @Test
    public void testGenerateResponseShouldReturnTemplateResourceAndSetResponseHeadersWhenTemplateFormatterDefined() throws Exception {
        //GIVEN
        ParameterList params = new ParameterList();
        templateFormatterDescriptors.add(templateFormatterDescriptor);
        mockTemplateResource();
        given(templateFormatterDescriptor.getParams()).willReturn(params);
        given(templateFormatterDescriptor.getTemplateFormatter()).willReturn(templateFormatter);
        given(templateFormatter.formatTemplate(wilmaRequest, response, templateResource, params)).willReturn(templateResource);
        //WHEN
        byte[] actual = underTest.generateResponse(request, response);
        //THEN
        verify(headerConfigurer).setResponseContentTypeAndStatus(response, responseDescriptorDTO);
        Assert.assertEquals(actual, templateResource);
    }

    @Test
    public void testGenerateResponseShouldReturnStackTraceAsResourceAndSetErrorResponseWhenTemplateFormattingFailed() throws Exception {
        //GIVEN
        ParameterList params = new ParameterList();
        templateFormatterDescriptors.add(templateFormatterDescriptor);
        mockTemplateResource();
        given(templateFormatterDescriptor.getTemplateFormatter()).willReturn(templateFormatter);
        given(templateFormatterDescriptor.getParams()).willReturn(params);
        given(templateFormatter.formatTemplate(wilmaRequest, response, templateResource, params)).willThrow(
                new TemplateFormattingFailedException("Template formatting failed....", new TemplateFormattingFailedException("")));
        //WHEN
        underTest.generateResponse(request, response);
        //THEN
        verify(stackTraceConverter).getStackTraceAsString(Matchers.any(TemplateFormattingFailedException.class));
        verify(headerConfigurer).setErrorResponseContentTypeAndStatus(response);
    }

    @Test(expectedExceptions = InterruptedException.class)
    public void testGenerateResponseShouldLogErrorWhenWaitProviderFail() throws Exception {
        //GIVEN
        mockTemplateResource();
        doThrow(new InterruptedException()).when(waitProvider).waitMilliSeconds(anyInt());
        //WHEN
        waitProvider.waitMilliSeconds(1);
        //THEN we should not arrive here
        Assert.fail();
    }

    private void mockTemplateResource() {
        ResponseDescriptorAttributes attributes = new ResponseDescriptorAttributes.Builder().delay(0).mimeType(MimeType.HTML.getOfficialMimeType())
                .template(template).build();
        given(responseDescriptor.getAttributes()).willReturn(attributes);
        given(template.getResource()).willReturn(templateResource);
    }

    @Test
    public void testGenerateResponseShouldReturnWithNullWhenNotFindWilmaLoggerIdInRequestHeader() throws Exception {
        //GIVEN
        given(request.getHeader(WilmaHttpRequest.WILMA_LOGGER_ID)).willReturn(null);
        //WHEN
        byte[] result = underTest.generateResponse(request, response);
        //THEN
        Assert.assertNull(result);
    }

    @Test
    public void testWhenActualSequenceIsNullShouldNotWaitForSequenceResponses() throws InterruptedException {
        //GIVEN
        given(wilmaRequest.getHeader(WilmaHttpEntity.WILMA_SEQUENCE_ID)).willReturn("abcd");
        String[] sequenceIds = new String[]{"id"};
        given(headerCreator.resolveSequenceHeader("abcd")).willReturn(sequenceIds);
        given(matcher.matchSequenceKeyWithDescriptor(SEQUENCE_DESCRIPTOR_KEY, sequenceIds)).willReturn(null);
        //WHEN
        underTest.generateResponse(request, response);
        //THEN
        verify(sequenceResponseGuard, never()).waitForResponses(wilmaRequest, null);
    }

    @Test
    public void testWhenActualSequenceIsNotNullShouldWaitForSequenceResponses() throws InterruptedException {
        //GIVEN
        WilmaSequence sequence = Mockito.mock(WilmaSequence.class);
        given(wilmaRequest.getHeader(WilmaHttpEntity.WILMA_SEQUENCE_ID)).willReturn("abcd");
        String[] sequenceIds = new String[]{"id"};
        given(headerCreator.resolveSequenceHeader("abcd")).willReturn(sequenceIds);
        given(matcher.matchSequenceKeyWithDescriptor(SEQUENCE_DESCRIPTOR_KEY, sequenceIds)).willReturn(sequence);
        //WHEN
        underTest.generateResponse(request, response);
        //THEN
        verify(sequenceResponseGuard).waitForResponses(wilmaRequest, sequence);
    }
}
