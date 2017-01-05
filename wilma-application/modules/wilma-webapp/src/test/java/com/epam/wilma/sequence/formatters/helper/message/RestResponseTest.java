package com.epam.wilma.sequence.formatters.helper.message;
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
import static org.testng.AssertJUnit.assertEquals;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.sequence.formatters.helper.converter.Converter;
import com.epam.wilma.sequence.formatters.helper.resolver.RestUrlMappingMessageNameResolver;

/**
 * Unit test for {@link RestResponse}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class RestResponseTest {

    private static final String POSTFIX = "Response";

    @Mock
    private RestUrlMappingMessageNameResolver restUrlMappingMessageNameResolver;
    @Mock
    private WilmaHttpEntity entity;
    @Mock
    private Converter converter;
    @Mock
    private WilmaHttpRequest request;

    @InjectMocks
    private RestResponse underTest;

    @Mock
    private ParameterList parameters;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "entity", entity);
        Whitebox.setInternalState(underTest, "converter", converter);
        Whitebox.setInternalState(underTest, "restUrlMappingMessageNameResolver", restUrlMappingMessageNameResolver);
        Whitebox.setInternalState(underTest, "request", request);
    }

    @Test
    public void testResolveNameShouldReturnResolvedNameWithPostfix() {
        //GIVEN
        given(restUrlMappingMessageNameResolver.resolve(request, parameters)).willReturn("name");
        //WHEN
        String result = underTest.resolveName(parameters);
        //THEN
        String expected = "name" + POSTFIX;
        assertEquals(expected, result);
    }

    @Test
    public void testResolveNameShouldReturnEmptyStringWhenResolvedNameIsEmpty() {
        //GIVEN
        given(restUrlMappingMessageNameResolver.resolve(request, parameters)).willReturn("");
        //WHEN
        String result = underTest.resolveName(parameters);
        //THEN
        String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void testConvertShouldReturnWhatConverterReturns() {
        //GIVEN
        given(entity.getBody()).willReturn("data");
        given(converter.convert("data", "name")).willReturn("converted data");
        //WHEN
        String result = underTest.convert("name");
        //THEN
        assertEquals("converted data", result);
    }

}
