package com.epam.wilma.sequence.formatters.helper.message;
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
import static org.testng.AssertJUnit.assertEquals;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.sequence.formatters.helper.converter.Converter;
import com.epam.wilma.sequence.formatters.helper.resolver.SoapMessageNameResolver;

/**
 * Unit test for {@link SoapMessage}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class SoapMessageTest {

    @Mock
    private SoapMessageNameResolver soapMessageNameResolver;
    @Mock
    private WilmaHttpEntity entity;
    @Mock
    private Converter converter;

    @InjectMocks
    private SoapMessage underTest;

    @Mock
    private ParameterList parameters;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "entity", entity);
        Whitebox.setInternalState(underTest, "converter", converter);
        Whitebox.setInternalState(underTest, "soapMessageNameResolver", soapMessageNameResolver);
    }

    @Test
    public void testResolveNameShouldReturnWhatSoapMessageNameResolverReturns() {
        //GIVEN
        given(soapMessageNameResolver.resolve(entity, parameters)).willReturn("name");
        //WHEN
        String result = underTest.resolveName(parameters);
        //THEN
        assertEquals("name", result);
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
