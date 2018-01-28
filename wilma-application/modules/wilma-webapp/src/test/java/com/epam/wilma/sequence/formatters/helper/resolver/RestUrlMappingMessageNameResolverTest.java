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

import java.net.URI;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Unit test for {@link RestUrlMappingMessageNameResolver}.
 *
 * @author Balazs_Berkes
 * @author Tamas_Kohegyi
 */
public class RestUrlMappingMessageNameResolverTest {

    private static final String REQUEST_BODY = "request body";
    private static final String XPATH_QUERY = "this should be an xpath query";
    private ParameterList parameters;
    private WilmaHttpRequest request;

    @Mock
    private XmlXpathResolver xmlTypeResolver;
    @InjectMocks
    private RestUrlMappingMessageNameResolver underTest;

    @BeforeMethod
    public void setup() {
        initializeMocksAndTestDate();
        parameters.addParameter(new Parameter("type:RegexResolvable", "url:.*/urls/regex.html"));
        parameters.addParameter(new Parameter("type:ExactResolvable", "url:www.epam.com/urls/target.html"));
        parameters.addParameter(new Parameter("not_a_type", "url:www.typeless.com/urls/resolvable.html"));
        parameters.addParameter(new Parameter("type:NonResolvable", "not_an_url"));
        parameters.addParameter(new Parameter("typequery:" + XPATH_QUERY, "url:www.xpath.com/query/test"));
    }

    private void initializeMocksAndTestDate() {
        MockitoAnnotations.initMocks(this);
        parameters = new ParameterList();
        request = new WilmaHttpRequest();
        request.setBody(REQUEST_BODY);
    }

    @Test
    public void testResolveShouldNotResolveWithoutType() throws Exception {
        //GIVEN
        givenRequestWithUrl("www.typeless.com/urls/resolvable.html");
        //WHEN
        String resolvedType = underTest.resolve(request, parameters);
        //THEN
        assertEquals(resolvedType, "");
    }

    @Test
    public void testResolveShouldNotResolveWithoutUrl() throws Exception {
        //GIVEN
        givenRequestWithUrl("not_an_url");
        //WHEN
        String resolvedType = underTest.resolve(request, parameters);
        //THEN
        assertEquals(resolvedType, "");
    }

    @Test
    public void testResolveShouldNotResolveWithoutInvalidRegexSyntax() throws Exception {
        //GIVEN
        givenRequestWithUrl("www.typeless.com/urls/resolvable.html");
        parameters = new ParameterList();
        parameters.addParameter(new Parameter("type:InvalidPattern", "url:["));
        //WHEN
        String resolvedType = underTest.resolve(request, parameters);
        //THEN
        assertEquals(resolvedType, "");
    }

    @Test
    public void testResolveShouldNotResolveNonRequestEnities() {
        //GIVEN
        //WHEN
        String result = underTest.resolve(new WilmaHttpEntity(), parameters);
        //THEN
        assertEquals(result, "");
    }

    @Test
    public void testResolveShouldResolveExactUrlTargets() throws Exception {
        //GIVEN
        givenRequestWithUrl("www.epam.com/urls/target.html");
        //WHEN
        String resolvedType = underTest.resolve(request, parameters);
        //THEN
        assertEquals(resolvedType, "ExactResolvable");
    }

    @Test
    public void testResolveShouldResolveRegexUrlTargets() throws Exception {
        //GIVEN
        givenRequestWithUrl("www.anysite.com/urls/regex.html");
        //WHEN
        String resolvedType = underTest.resolve(request, parameters);
        //THEN
        assertEquals(resolvedType, "RegexResolvable");
    }

    @Test
    public void testResolveShouldResolveXpathTypeQueries() throws Exception {
        //GIVEN
        givenRequestWithUrl("www.xpath.com/query/test");
        given(xmlTypeResolver.getValue(XPATH_QUERY, REQUEST_BODY)).willReturn("XpathType");
        //WHEN
        String resolvedType = underTest.resolve(request, parameters);
        //THEN
        assertEquals(resolvedType, "XpathType");
    }

    private void givenRequestWithUrl(final String string) throws Exception {
        request.setUri(new URI(string));
        request.setRequestLine(String.format("POST %s VERSION", string));
    }
}
