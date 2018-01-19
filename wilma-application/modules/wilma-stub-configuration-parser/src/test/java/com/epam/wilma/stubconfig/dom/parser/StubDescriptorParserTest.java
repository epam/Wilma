package com.epam.wilma.stubconfig.dom.parser;
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
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.domain.stubconfig.StubDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptorUsage;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.condition.SimpleCondition;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptor;
import com.epam.wilma.domain.stubconfig.interceptor.InterceptorDescriptor;
import com.epam.wilma.domain.stubconfig.interceptor.MockRequestInterceptor;
import com.epam.wilma.domain.stubconfig.interceptor.MockResponseInterceptor;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.stubconfig.dom.parser.node.DialogDescriptorAttributeParser;
import com.epam.wilma.stubconfig.dom.parser.node.InterceptorDescriptorParser;
import com.epam.wilma.stubconfig.dom.parser.node.MyNodeList;
import com.epam.wilma.stubconfig.dom.parser.sequence.SequenceDescriptorParser;

/**
 * Provides unit tests for the class {@link StubDescriptorParser}.
 * @author Tunde_Kovacs
 *
 */
public class StubDescriptorParserTest {

    private static final String INTERCEPTOR_NAME = "sessionStoringInterceptor";
    private MyNodeList dialogDescriptorList;
    private MyNodeList interceptorDescriptorList;
    private ConditionDescriptor conditionDescriptor;
    private ResponseDescriptor responseDescriptor;
    private List<DialogDescriptor> parsedDialogDescriptors;

    @Mock
    private NodeParser<ConditionDescriptor> conditionDescriptorParser;
    @Mock
    private NodeParser<ResponseDescriptor> responseDescriptorParser;
    @Mock
    private Document document;
    @Mock
    private Element root;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Element element;
    @Mock
    private ConditionChecker conditionChecker;
    @Mock
    private DialogDescriptorAttributeParser dialogDescriptorAttributeParser;
    @Mock
    private InterceptorDescriptorParser interceptorDescriptorParser;
    @Mock
    private SequenceDescriptorParser sequenceDescriptorParser;
    @InjectMocks
    private StubDescriptorParser underTest;
    private DialogDescriptorAttributes dialogDescriptorAttributes;
    private StubDescriptorAttributes stubDescriptorAttributes;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        List<Node> list = new ArrayList<>();
        list.add(element);
        dialogDescriptorList = new MyNodeList(list);
        interceptorDescriptorList = new MyNodeList(list);

        conditionDescriptor = new ConditionDescriptor(new SimpleCondition(conditionChecker, false, null));
        responseDescriptor = new ResponseDescriptor(null, null);
        dialogDescriptorAttributes = new DialogDescriptorAttributes("name", DialogDescriptorUsage.ALWAYS);
        stubDescriptorAttributes = new StubDescriptorAttributes("test");
        given(document.getDocumentElement()).willReturn(root);
        given(element.getElementsByTagName("condition-descriptor").item(0)).willReturn(element);
        given(element.getElementsByTagName("response-descriptor").item(0)).willReturn(element);
        given(conditionDescriptorParser.parseNode(Mockito.any(Node.class), Mockito.eq(document))).willReturn(conditionDescriptor);
        given(responseDescriptorParser.parseNode(Mockito.any(Node.class), Mockito.eq(document))).willReturn(responseDescriptor);
    }

    @Test
    public void testParseShouldReturnStubDescriptorAttributesWithGroupname() {
        //GIVEN
        given(root.getAttribute("groupname")).willReturn("test");
        //WHEN
        StubDescriptor actual = underTest.parse(document);
        //THEN
        assertEquals(actual.getAttributes().getGroupName(), stubDescriptorAttributes.getGroupName());
    }

    @Test
    public void testParseShouldReturnStubDescriptorAttributesWithActiveValue() {
        //GIVEN
        given(root.getAttribute("active")).willReturn("false");
        StubDescriptorAttributes expectedAttributes = new StubDescriptorAttributes("Default", false);
        //WHEN
        StubDescriptor actual = underTest.parse(document);
        //THEN
        assertEquals(actual.getAttributes().isActive(), expectedAttributes.isActive());
    }

    @Test
    public void testParseShouldReturnStubDescriptorAttributesWithDefaultActiveValue() {
        //GIVEN
        given(root.getAttribute("active")).willReturn("");
        StubDescriptorAttributes expectedAttributes = new StubDescriptorAttributes("Default", true);
        //WHEN
        StubDescriptor actual = underTest.parse(document);
        //THEN
        assertEquals(actual.getAttributes().isActive(), expectedAttributes.isActive());
    }

    @Test
    public void testParseShouldReturnDialogDescriptorAttributes() {
        //GIVEN
        given(root.getElementsByTagName("dialog-descriptor")).willReturn(dialogDescriptorList);
        given(dialogDescriptorAttributeParser.getAttributes(element)).willReturn(dialogDescriptorAttributes);
        //WHEN
        StubDescriptor actual = underTest.parse(document);
        //THEN
        assertEquals(actual.getDialogDescriptors().get(0).getAttributes(), dialogDescriptorAttributes);

    }

    @Test
    public void testParseShouldReturnConditionDescriptor() {
        //GIVEN
        given(root.getElementsByTagName("dialog-descriptor")).willReturn(dialogDescriptorList);
        //WHEN
        StubDescriptor actual = underTest.parse(document);
        //THEN
        assertEquals(actual.getDialogDescriptors().get(0).getConditionDescriptor(), conditionDescriptor);

    }

    @Test
    public void testParseShouldReturnResponseDescriptor() {
        //GIVEN
        given(root.getElementsByTagName("dialog-descriptor")).willReturn(dialogDescriptorList);
        //WHEN
        StubDescriptor actual = underTest.parse(document);
        //THEN
        assertEquals(actual.getDialogDescriptors().get(0).getResponseDescriptor(), responseDescriptor);

    }

    @Test
    public void testParseShouldReturnEmptyStubDescriptorWhenThereAreNoDialogDescriptors() {
        //GIVEN
        given(root.getElementsByTagName("dialog-descriptor")).willReturn(null);
        //WHEN
        StubDescriptor actual = underTest.parse(document);
        //THEN
        assertEquals(actual.getDialogDescriptors(), Collections.EMPTY_LIST);

    }

    @Test
    public void testParseShouldReturnInterceptorDescriptor() {
        //GIVEN
        given(root.getElementsByTagName("interceptor")).willReturn(interceptorDescriptorList);
        MockRequestInterceptor requestInterceptor = new MockRequestInterceptor();
        MockResponseInterceptor responseInterceptor = new MockResponseInterceptor();
        ParameterList parameterList = new ParameterList();
        InterceptorDescriptor interceptorDescriptor = new InterceptorDescriptor(INTERCEPTOR_NAME, requestInterceptor, responseInterceptor,
                parameterList);
        given(interceptorDescriptorParser.parseNode(Mockito.any(Node.class), Mockito.eq(document))).willReturn(interceptorDescriptor);
        //WHEN
        StubDescriptor actual = underTest.parse(document);
        //THEN
        assertEquals(actual.getInterceptorDescriptors().get(0).getName(), INTERCEPTOR_NAME);
        assertEquals(actual.getInterceptorDescriptors().get(0).getRequestInterceptor(), requestInterceptor);
        assertEquals(actual.getInterceptorDescriptors().get(0).getResponseInterceptor(), responseInterceptor);
        assertEquals(actual.getInterceptorDescriptors().get(0).getParams(), parameterList);
    }

    @Test
    public void testParseShouldReturnSequenceDescriptors() {
        //GIVEN
        parsedDialogDescriptors = new ArrayList<>();
        //WHEN
        underTest.parse(document);
        //THEN
        verify(sequenceDescriptorParser).parse(document, root, parsedDialogDescriptors);
    }
}
