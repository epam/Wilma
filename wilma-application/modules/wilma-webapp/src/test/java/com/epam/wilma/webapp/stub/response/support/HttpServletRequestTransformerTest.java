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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.router.domain.ResponseDescriptorDTO;
import com.epam.wilma.router.helper.WilmaHttpRequestCloner;
import org.apache.tools.ant.util.VectorSet;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link HttpServletRequestTransformer} class.
 * @author Tamas_Bihari
 *
 */
public class HttpServletRequestTransformerTest {
    private static final String REQUEST_BODY = "REQUEST_BODY";
    private static final String WILMA_LOGGER_ID = "test";

    @Mock
    private WilmaHttpRequest wilmaRequest;
    @Mock
    private WilmaHttpRequestCloner requestCloner;
    @Mock
    private HttpServletRequest request;
    @Mock
    private ResponseDescriptorDTO responseDescriptorDTO;
    private Enumeration<String> enumeration;
    private VectorSet headers;

    @InjectMocks
    private HttpServletRequestTransformer underTest;

    @SuppressWarnings("unchecked")
    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        headers = new VectorSet();
        enumeration = headers.elements();
        given(requestCloner.cloneRequest(any(WilmaHttpRequest.class))).willReturn(wilmaRequest);
    }

    @Test
    public void testTransformToWilmaHttpRequestShouldGetHeadersToCopyIntoResultWhenNoHeader() {
        //GIVEN
        given(request.getHeaderNames()).willReturn(enumeration);
        //WHEN
        underTest.transformToWilmaHttpRequest(WILMA_LOGGER_ID, request, responseDescriptorDTO);
        //THEN
        verify(request, Mockito.times(0)).getHeader(Matchers.anyString());
    }

    @Test
    public void testTransformToWilmaHttpRequestShouldGetHeadersToCopyIntoResultWhenOneHeader() {
        //GIVEN
        headers.add("Content-type");
        headers.add("Any-other-header");
        given(request.getHeaderNames()).willReturn(enumeration);
        //WHEN
        underTest.transformToWilmaHttpRequest(WILMA_LOGGER_ID, request, responseDescriptorDTO);
        //THEN
        verify(request, Mockito.times(2)).getHeader(Matchers.anyString());
    }

    @Test
    public void testTransformToWilmaHttpRequestShouldCopyUnCompressedBodyFromResponseDescriptorDTOIntoResult() {
        //GIVEN
        given(request.getHeaderNames()).willReturn(enumeration);
        given(responseDescriptorDTO.getRequestBody()).willReturn(REQUEST_BODY);
        //WHEN
        WilmaHttpRequest actual = underTest.transformToWilmaHttpRequest(WILMA_LOGGER_ID, request, responseDescriptorDTO);
        //THEN
        Assert.assertEquals(actual, wilmaRequest);
    }

}
