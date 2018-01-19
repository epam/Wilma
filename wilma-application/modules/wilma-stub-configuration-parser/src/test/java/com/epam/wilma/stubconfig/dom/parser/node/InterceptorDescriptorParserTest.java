package com.epam.wilma.stubconfig.dom.parser.node;
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

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.epam.wilma.domain.stubconfig.interceptor.InterceptorDescriptor;
import com.epam.wilma.domain.stubconfig.interceptor.MockRequestInterceptor;
import com.epam.wilma.domain.stubconfig.interceptor.MockResponseInterceptor;
import com.epam.wilma.domain.stubconfig.interceptor.RequestInterceptor;
import com.epam.wilma.domain.stubconfig.interceptor.ResponseInterceptor;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;
import com.epam.wilma.stubconfig.initializer.interceptor.RequestInterceptorInitializer;
import com.epam.wilma.stubconfig.initializer.interceptor.ResponseInterceptorInitializer;

/**
 * Unit tests for the class {@link InterceptorDescriptorParser}.
 * @author Tunde_Kovacs
 *
 */
public class InterceptorDescriptorParserTest {

    private static final String NAME = "interceptor1";
    private static final String VALUE = "21";
    private static final String CLASS = "sessionStoringInterceptor";
    private static final String BAD_CLASS = "sessionStoringInterceptor.class";

    @Mock
    private RequestInterceptorInitializer requestInterceptorInitializer;
    @Mock
    private ResponseInterceptorInitializer responseInterceptorInitializer;
    @Mock
    private Document document;
    @Mock
    private Element interceptorNode;
    @Mock
    private Element parameterNode;

    @InjectMocks
    private InterceptorDescriptorParser underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testParseNodeShouldReturnInterceptorDescriptor() {
        //GIVEN
        NodeList paramNodeList = createParamNodeList();
        RequestInterceptor requestInterceptor = new MockRequestInterceptor();
        ResponseInterceptor responseInterceptor = new MockResponseInterceptor();
        given(interceptorNode.getAttribute("name")).willReturn(NAME);
        given(interceptorNode.getAttribute("class")).willReturn(CLASS);
        given(interceptorNode.getElementsByTagName("param")).willReturn(paramNodeList);
        given(requestInterceptorInitializer.getExternalClassObject(CLASS)).willReturn(requestInterceptor);
        given(responseInterceptorInitializer.getExternalClassObject(CLASS)).willReturn(responseInterceptor);
        //WHEN
        InterceptorDescriptor actual = underTest.parseNode(interceptorNode, document);
        //THEN
        assertEquals(actual.getName(), NAME);
        assertEquals(actual.getRequestInterceptor(), requestInterceptor);
        assertEquals(actual.getResponseInterceptor(), responseInterceptor);
        assertEquals(actual.getParams().getAllParameters().get(0).getName(), NAME);
        assertEquals(actual.getParams().getAllParameters().get(0).getValue(), VALUE);
    }

    @Test
    public void testParseNodeShouldReturnInterceptorDescriptorEvenIfOnlyFirstInterfaceIsImplemented() {
        //GIVEN
        NodeList paramNodeList = createParamNodeList();
        ResponseInterceptor responseInterceptor = new MockResponseInterceptor();
        given(interceptorNode.getAttribute("name")).willReturn(NAME);
        given(interceptorNode.getAttribute("class")).willReturn(CLASS);
        given(interceptorNode.getElementsByTagName("param")).willReturn(paramNodeList);
        given(requestInterceptorInitializer.getExternalClassObject(CLASS)).willReturn(null);
        given(responseInterceptorInitializer.getExternalClassObject(CLASS)).willReturn(responseInterceptor);
        //WHEN
        InterceptorDescriptor actual = underTest.parseNode(interceptorNode, document);
        //THEN
        assertEquals(actual.getName(), NAME);
        assertEquals(actual.getResponseInterceptor(), responseInterceptor);
        assertEquals(actual.getParams().getAllParameters().get(0).getName(), NAME);
        assertEquals(actual.getParams().getAllParameters().get(0).getValue(), VALUE);
    }

    @Test
    public void testParseNodeShouldReturnInterceptorDescriptorEvenIfOnlySecondInterfaceIsImplemented() {
        //GIVEN
        NodeList paramNodeList = createParamNodeList();
        RequestInterceptor requestInterceptor = new MockRequestInterceptor();
        given(interceptorNode.getAttribute("name")).willReturn(NAME);
        given(interceptorNode.getAttribute("class")).willReturn(CLASS);
        given(interceptorNode.getElementsByTagName("param")).willReturn(paramNodeList);
        given(requestInterceptorInitializer.getExternalClassObject(CLASS)).willReturn(requestInterceptor);
        given(responseInterceptorInitializer.getExternalClassObject(CLASS)).willReturn(null);
        //WHEN
        InterceptorDescriptor actual = underTest.parseNode(interceptorNode, document);
        //THEN
        assertEquals(actual.getName(), NAME);
        assertEquals(actual.getRequestInterceptor(), requestInterceptor);
        assertEquals(actual.getParams().getAllParameters().get(0).getName(), NAME);
        assertEquals(actual.getParams().getAllParameters().get(0).getValue(), VALUE);
    }

    @Test
    public void testParseNodeWhenNoParamsShouldReturnNotSetParameters() {
        //GIVEN
        RequestInterceptor requestInterceptor = new MockRequestInterceptor();
        ResponseInterceptor responseInterceptor = new MockResponseInterceptor();
        given(interceptorNode.getAttribute("name")).willReturn(NAME);
        given(interceptorNode.getAttribute("class")).willReturn(CLASS);
        given(interceptorNode.getElementsByTagName("param")).willReturn(null);
        given(requestInterceptorInitializer.getExternalClassObject(CLASS)).willReturn(requestInterceptor);
        given(responseInterceptorInitializer.getExternalClassObject(CLASS)).willReturn(responseInterceptor);
        //WHEN
        InterceptorDescriptor actual = underTest.parseNode(interceptorNode, document);
        //THEN
        assertEquals(actual.getParams().getAllParameters().isEmpty(), true);
    }

    @Test(expectedExceptions = DescriptorValidationFailedException.class)
    public void testParseNodeWhenNoInterceptorClassShouldThrowException() {
        //GIVEN
        given(requestInterceptorInitializer.getExternalClassObject(CLASS)).willReturn(null);
        given(responseInterceptorInitializer.getExternalClassObject(CLASS)).willReturn(null);
        //WHEN
        underTest.parseNode(interceptorNode, document);
        //THEN it should throw exception
    }

    @Test(expectedExceptions = DescriptorValidationFailedException.class)
    public void testParseNodeWhenInterceptorClassHasExtraClassInNameShouldThrowException() {
        //GIVEN
        NodeList paramNodeList = createParamNodeList();
        RequestInterceptor requestInterceptor = new MockRequestInterceptor();
        ResponseInterceptor responseInterceptor = new MockResponseInterceptor();
        given(interceptorNode.getAttribute("name")).willReturn(NAME);
        given(interceptorNode.getAttribute("class")).willReturn(BAD_CLASS);
        given(interceptorNode.getElementsByTagName("param")).willReturn(paramNodeList);
        given(requestInterceptorInitializer.getExternalClassObject(BAD_CLASS)).willReturn(requestInterceptor);
        given(responseInterceptorInitializer.getExternalClassObject(BAD_CLASS)).willReturn(responseInterceptor);
        //WHEN
        underTest.parseNode(interceptorNode, document);
        //THEN it should throw exception
    }

    @Test
    public void testParseNodeWhenNoResponseInterceptorShouldCreateDescriptor() {
        //GIVEN
        RequestInterceptor requestInterceptor = new MockRequestInterceptor();
        given(interceptorNode.getAttribute("name")).willReturn(NAME);
        given(interceptorNode.getAttribute("class")).willReturn(CLASS);
        given(interceptorNode.getElementsByTagName("param")).willReturn(null);
        given(requestInterceptorInitializer.getExternalClassObject(CLASS)).willReturn(requestInterceptor);
        given(responseInterceptorInitializer.getExternalClassObject(CLASS)).willReturn(null);
        //WHEN
        InterceptorDescriptor actual = underTest.parseNode(interceptorNode, document);
        //THEN
        assertEquals(actual.getRequestInterceptor(), requestInterceptor);
        assertEquals(actual.getResponseInterceptor(), null);
    }

    @Test
    public void testParseNodeWhenNoRequestInterceptorShouldCreateDescriptor() {
        //GIVEN
        ResponseInterceptor responseInterceptor = new MockResponseInterceptor();
        given(interceptorNode.getAttribute("name")).willReturn(NAME);
        given(interceptorNode.getAttribute("class")).willReturn(CLASS);
        given(interceptorNode.getElementsByTagName("param")).willReturn(null);
        given(requestInterceptorInitializer.getExternalClassObject(CLASS)).willReturn(null);
        given(responseInterceptorInitializer.getExternalClassObject(CLASS)).willReturn(responseInterceptor);
        //WHEN
        InterceptorDescriptor actual = underTest.parseNode(interceptorNode, document);
        //THEN
        assertEquals(actual.getRequestInterceptor(), null);
        assertEquals(actual.getResponseInterceptor(), responseInterceptor);
    }

    @Test
    public void testParseNodeWhenInterceptorNodeIsNullShouldDoNothing() {
        //GIVEN
        //WHEN
        InterceptorDescriptor actual = underTest.parseNode(null, document);
        //THEN
        assertEquals(actual, null);
    }

    private NodeList createParamNodeList() {
        List<Node> paramlist = new ArrayList<>();
        paramlist.add(parameterNode);
        NodeList paramNodeList = new MyNodeList(paramlist);
        given(parameterNode.getAttribute("name")).willReturn(NAME);
        given(parameterNode.getAttribute("value")).willReturn(VALUE);
        return paramNodeList;
    }
}
