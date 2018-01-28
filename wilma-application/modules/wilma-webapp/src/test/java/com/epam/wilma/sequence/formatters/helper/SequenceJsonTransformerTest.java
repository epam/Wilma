package com.epam.wilma.sequence.formatters.helper;
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
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.domain.sequence.RequestResponsePair;
import com.epam.wilma.sequence.formatters.helper.converter.Converter;
import com.epam.wilma.sequence.formatters.helper.converter.NeutralConverter;
import com.epam.wilma.sequence.formatters.helper.converter.XmlConverter;
import com.epam.wilma.sequence.formatters.helper.message.Message;
import com.epam.wilma.sequence.formatters.helper.message.MessageFactory;

/**
 * Unit test for {@link SequenceJsonTransformer}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class SequenceJsonTransformerTest {

    @Mock
    private XmlConverter xmlConverter;
    @Mock
    private NeutralConverter nullConverter;
    @Mock
    private WilmaHttpEntityUtils httpEntityUtils;
    @Mock
    private MessageFactory messageFactory;

    @InjectMocks
    private SequenceJsonTransformer underTest;

    @Mock
    private WilmaHttpRequest wilmaHttpRequest;
    @Mock
    private WilmaHttpResponse wilmaHttpResponse;
    @Mock
    private ParameterList parameterList;
    @Mock
    private RequestResponsePair requestResponsePair;
    @Mock
    private Message messageRequest;
    @Mock
    private Message messageResponse;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetJsonConverterShouldReturnNullConverter() {
        //GIVEN
        //WHEN
        Converter result = underTest.getJsonConverter();
        //THEN
        assertEquals(nullConverter, result);
    }

    @Test
    public void testGetXmlConverterShouldReturnJsonConverter() {
        //GIVEN
        //WHEN
        Converter result = underTest.getXmlConverter();
        //THEN
        assertEquals(xmlConverter, result);
    }

    @Test
    public void testCreateMapShouldReturnEmptyMapWhenNoMessagesExist() {
        //GIVEN
        Map<String, RequestResponsePair> messages = Collections.emptyMap();
        //WHEN
        Map<String, String> result = underTest.transform(parameterList, messages);
        //THEN
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCreateMapShouldStoreEntityBodyByResolvedName() {
        //GIVEN
        Map<String, RequestResponsePair> messages = new HashMap<>();
        messages.put("bab", requestResponsePair);
        given(requestResponsePair.getRequest()).willReturn(wilmaHttpRequest);
        given(requestResponsePair.getResponse()).willReturn(wilmaHttpResponse);
        given(httpEntityUtils.isSoapMessage(wilmaHttpRequest)).willReturn(true);
        given(httpEntityUtils.isSoapMessage(wilmaHttpResponse)).willReturn(true);
        given(messageFactory.createSoapMessage(wilmaHttpRequest, xmlConverter)).willReturn(messageRequest);
        given(messageFactory.createSoapMessage(wilmaHttpResponse, xmlConverter)).willReturn(messageResponse);
        given(messageRequest.resolveName(parameterList)).willReturn("kalap");
        given(messageResponse.resolveName(parameterList)).willReturn("kabat");
        //WHEN
        Map<String, String> result = underTest.transform(parameterList, messages);
        //THEN
        assertEquals(wilmaHttpRequest.getBody(), result.get("kalap"));
        assertEquals(wilmaHttpResponse.getBody(), result.get("kabat"));
    }

    @Test
    public void testCreateMapShouldNotTryToCreateMessageFromNullResponse() {
        //GIVEN
        Map<String, RequestResponsePair> messages = new HashMap<>();
        messages.put("bab", requestResponsePair);
        given(requestResponsePair.getRequest()).willReturn(wilmaHttpRequest);
        given(requestResponsePair.getResponse()).willReturn(null);
        given(httpEntityUtils.isSoapMessage(wilmaHttpRequest)).willReturn(true);
        given(messageFactory.createSoapMessage(wilmaHttpRequest, xmlConverter)).willReturn(messageRequest);
        given(messageRequest.resolveName(parameterList)).willReturn("kalap");
        //WHEN
        Map<String, String> result = underTest.transform(parameterList, messages);
        //THEN
        assertEquals(wilmaHttpRequest.getBody(), result.get("kalap"));
        verify(httpEntityUtils, Mockito.never()).isSoapMessage(null);
    }

    @Test
    public void testCreateMapShouldNotStoreEntityBodyForEmptyResolvedName() {
        //GIVEN
        Map<String, RequestResponsePair> messages = new HashMap<>();
        messages.put("bab", requestResponsePair);
        given(requestResponsePair.getRequest()).willReturn(wilmaHttpRequest);
        given(requestResponsePair.getResponse()).willReturn(wilmaHttpResponse);
        given(httpEntityUtils.isSoapMessage(wilmaHttpRequest)).willReturn(true);
        given(httpEntityUtils.isSoapMessage(wilmaHttpResponse)).willReturn(true);
        given(messageFactory.createSoapMessage(wilmaHttpRequest, xmlConverter)).willReturn(messageRequest);
        given(messageFactory.createSoapMessage(wilmaHttpResponse, xmlConverter)).willReturn(messageResponse);
        given(messageRequest.resolveName(parameterList)).willReturn("");
        given(messageResponse.resolveName(parameterList)).willReturn("");
        //WHEN
        Map<String, String> result = underTest.transform(parameterList, messages);
        //THEN
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCreateMapShouldCreateSoapMessageFromSoapTypeEntity() {
        //GIVEN
        Map<String, RequestResponsePair> messages = new HashMap<>();
        messages.put("bab", requestResponsePair);
        given(requestResponsePair.getRequest()).willReturn(wilmaHttpRequest);
        given(requestResponsePair.getResponse()).willReturn(wilmaHttpResponse);
        given(httpEntityUtils.isSoapMessage(wilmaHttpRequest)).willReturn(true);
        given(httpEntityUtils.isSoapMessage(wilmaHttpResponse)).willReturn(true);
        given(messageFactory.createSoapMessage(wilmaHttpRequest, xmlConverter)).willReturn(messageRequest);
        given(messageFactory.createSoapMessage(wilmaHttpResponse, xmlConverter)).willReturn(messageResponse);
        given(messageRequest.resolveName(parameterList)).willReturn("kalap");
        given(messageResponse.resolveName(parameterList)).willReturn("kabat");
        //WHEN
        underTest.transform(parameterList, messages);
        //THEN
        verify(messageFactory).createSoapMessage(wilmaHttpRequest, xmlConverter);
        verify(messageFactory).createSoapMessage(wilmaHttpResponse, xmlConverter);
    }

    @Test
    public void testCreateMapShouldCreateRestMessageFromXmlTypeEntity() {
        //GIVEN
        Map<String, RequestResponsePair> messages = new HashMap<>();
        messages.put("bab", requestResponsePair);
        given(requestResponsePair.getRequest()).willReturn(wilmaHttpRequest);
        given(requestResponsePair.getResponse()).willReturn(wilmaHttpResponse);
        given(httpEntityUtils.isXmlMessage(wilmaHttpRequest)).willReturn(true);
        given(httpEntityUtils.isXmlMessage(wilmaHttpResponse)).willReturn(true);
        given(messageFactory.createRestRequest(wilmaHttpRequest, xmlConverter)).willReturn(messageRequest);
        given(messageFactory.createRestResponse(wilmaHttpResponse, xmlConverter, wilmaHttpRequest)).willReturn(messageResponse);
        given(messageRequest.resolveName(parameterList)).willReturn("kalap");
        given(messageResponse.resolveName(parameterList)).willReturn("kabat");
        //WHEN
        underTest.transform(parameterList, messages);
        //THEN
        verify(messageFactory).createRestRequest(wilmaHttpRequest, xmlConverter);
        verify(messageFactory).createRestResponse(wilmaHttpResponse, xmlConverter, wilmaHttpRequest);
    }

    @Test
    public void testCreateMapShouldCreateRestMessageFromJsonTypeEntity() {
        //GIVEN
        Map<String, RequestResponsePair> messages = new HashMap<>();
        messages.put("bab", requestResponsePair);
        given(requestResponsePair.getRequest()).willReturn(wilmaHttpRequest);
        given(requestResponsePair.getResponse()).willReturn(wilmaHttpResponse);
        given(httpEntityUtils.isJsonMessage(wilmaHttpRequest)).willReturn(true);
        given(httpEntityUtils.isJsonMessage(wilmaHttpResponse)).willReturn(true);
        given(messageFactory.createRestRequest(wilmaHttpRequest, nullConverter)).willReturn(messageRequest);
        given(messageFactory.createRestResponse(wilmaHttpResponse, nullConverter, wilmaHttpRequest)).willReturn(messageResponse);
        given(messageRequest.resolveName(parameterList)).willReturn("kalap");
        given(messageResponse.resolveName(parameterList)).willReturn("kabat");
        //WHEN
        underTest.transform(parameterList, messages);
        //THEN
        verify(messageFactory).createRestRequest(wilmaHttpRequest, nullConverter);
        verify(messageFactory).createRestResponse(wilmaHttpResponse, nullConverter, wilmaHttpRequest);
    }

    @Test
    public void testCreateMapShouldNotCreateMessageFromEntityWithUnknownType() {
        //GIVEN
        Map<String, RequestResponsePair> messages = new HashMap<>();
        messages.put("bab", requestResponsePair);
        given(requestResponsePair.getRequest()).willReturn(wilmaHttpRequest);
        given(requestResponsePair.getResponse()).willReturn(wilmaHttpResponse);
        given(httpEntityUtils.isJsonMessage(wilmaHttpRequest)).willReturn(false);
        given(httpEntityUtils.isJsonMessage(wilmaHttpResponse)).willReturn(false);
        //WHEN
        Map<String, String> result = underTest.transform(parameterList, messages);
        //THEN
        assertTrue(result.isEmpty());
    }
}
