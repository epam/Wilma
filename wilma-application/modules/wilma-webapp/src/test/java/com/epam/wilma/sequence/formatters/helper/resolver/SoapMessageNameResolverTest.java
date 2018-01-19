package com.epam.wilma.sequence.formatters.helper.resolver;
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
import static org.testng.Assert.assertEquals;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Unit test for {@link SoapMessageNameResolver}.
 *
 * @author Balazs_Berkes
 */
public class SoapMessageNameResolverTest {

    private static final String EVALUATED_QUERY = "evaluated query";
    private static final String SOAP_CONTENT = "soap content";
    private static final String SOAP_TYPE_QUERY = "local-name(/*/*/*[1])";

    @Mock
    private XmlXpathResolver xmlTypeResolver;
    @Mock
    private WilmaHttpEntity request;
    @Mock
    private ParameterList parameters;
    @InjectMocks
    private SoapMessageNameResolver underTest;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testResolveShouldResolveEmtyStringWhenXpathCannotBeEvaluated() {
        given(request.getBody()).willReturn(SOAP_CONTENT);
        given(xmlTypeResolver.getValue(SOAP_TYPE_QUERY, SOAP_CONTENT)).willThrow(new RuntimeException());

        String actual = underTest.resolve(request, parameters);

        assertEquals(actual, "");
    }

    @Test
    public void testResolveShouldReturnEvaluatedXpathQuery() {
        given(request.getBody()).willReturn(SOAP_CONTENT);
        given(xmlTypeResolver.getValue(SOAP_TYPE_QUERY, SOAP_CONTENT)).willReturn(EVALUATED_QUERY);

        String actual = underTest.resolve(request, parameters);

        assertEquals(actual, EVALUATED_QUERY);
    }
}
