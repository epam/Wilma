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
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatter;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatterDescriptor;
import com.epam.wilma.stubconfig.initializer.template.ResponseFormatterInitializer;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.epam.wilma.stubconfig.configuration.StubConfigurationAccess;
import com.epam.wilma.stubconfig.configuration.domain.PropertyDto;
import com.epam.wilma.stubconfig.dom.parser.node.helper.StubConfigXPathEvaluator;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;

/**
 * Provides unit tests for the class {@link TemplateDescriptorParser}.
 * @author Tunde_Kovacs
 *
 */
public class TemplateDescriptorParserTest {

    private static final String TEMPLATE_FORMATTER_TAG = "template-formatter";
    private static final String TEMPLATE_FORMATTER_SET_INVOKER_TAG = "template-formatter-set-invoker";
    private MyNodeList templateFormatters;
    private MyNodeList params;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Element node;
    @Mock
    private Element element;
    @Mock
    private Element paramElement;
    @Mock
    private Document document;
    @Mock
    private StubConfigXPathEvaluator xPathEvaluator;
    @Mock
    private ResponseFormatterInitializer formatterInitializer;
    @Mock
    private ResponseFormatter templateFormatter;
    @Mock
    private StubConfigurationAccess configurationAccess;
    @Mock
    private PropertyDto properties;

    @InjectMocks
    private TemplateDescriptorParser underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        List<Node> paramlist = new ArrayList<>();
        paramlist.add(paramElement);
        params = new MyNodeList(paramlist);
    }

    @Test
    public void testParseNodeShouldReturnTemplateFormatter() {
        //GIVEN
        getChildNodes();
        given(element.getNodeType()).willReturn(Node.ELEMENT_NODE);
        given(element.getTagName()).willReturn(TEMPLATE_FORMATTER_TAG);
        given(formatterInitializer.getExternalClassObject("class")).willReturn(templateFormatter);
        mockTemplateFormatterAttributes();
        //WHEN
        Set<ResponseFormatterDescriptor> actual = underTest.parseNode(node, document);
        //THEN
        assertEquals(((ResponseFormatterDescriptor) actual.toArray()[0]).getResponseFormatter(), templateFormatter);
    }

    @Test
    public void testParseNodeShouldReturnTemplateFormatterParam() {
        //GIVEN
        getChildNodes();
        given(element.getNodeType()).willReturn(Node.ELEMENT_NODE);
        given(element.getTagName()).willReturn(TEMPLATE_FORMATTER_TAG);
        mockTemplateFormatterAttributes();
        //WHEN
        Set<ResponseFormatterDescriptor> actual = underTest.parseNode(node, document);
        //THEN
        assertEquals(((ResponseFormatterDescriptor) actual.toArray()[0]).getParams().getAllParameters().get(0).getValue(), "value");
    }

    @Test
    public void testParseNodeWhenNoParamsShouldReturnEmptyTemplateFormatterParams() {
        //GIVEN
        getChildNodes();
        given(element.getNodeType()).willReturn(Node.ELEMENT_NODE);
        given(element.getTagName()).willReturn(TEMPLATE_FORMATTER_TAG);
        given(element.getAttribute("class")).willReturn("class");
        given(element.getElementsByTagName("param")).willReturn(null);
        //WHEN
        Set<ResponseFormatterDescriptor> actual = underTest.parseNode(node, document);
        //THEN
        assertTrue(((ResponseFormatterDescriptor) actual.toArray()[0]).getParams().getAllParameters().isEmpty());
    }

    @Test
    public void testParseNodeShouldReturnTemplateFormatterSetInvoker() {
        //GIVEN
        getChildNodes();
        given(configurationAccess.getProperties()).willReturn(properties);
        given(properties.getMaxDepthOfTree()).willReturn(10);
        given(element.getNodeType()).willReturn(Node.ELEMENT_NODE);
        given(element.getTagName()).willReturn(TEMPLATE_FORMATTER_SET_INVOKER_TAG, TEMPLATE_FORMATTER_SET_INVOKER_TAG, TEMPLATE_FORMATTER_TAG);
        mockTemplateFormatterAttributes();
        given(xPathEvaluator.getElementByXPath(Mockito.anyString(), Mockito.eq(document))).willReturn(node);
        given(formatterInitializer.getExternalClassObject("class")).willReturn(templateFormatter);
        //WHEN
        Set<ResponseFormatterDescriptor> actual = underTest.parseNode(node, document);
        //THEN
        assertEquals(((ResponseFormatterDescriptor) actual.toArray()[0]).getResponseFormatter(), templateFormatter);
    }

    @Test
    public void testParseNodeShouldReturnEmptySetIfNodeHasNoChildren() {
        //GIVEN
        given(node.getChildNodes()).willReturn(null);
        //WHEN
        Set<ResponseFormatterDescriptor> actual = underTest.parseNode(node, document);
        //THEN
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testParseNodeShouldReturnEmptySetIfNodeTypeIsNotElementNode() {
        //GIVEN
        getChildNodes();
        given(element.getNodeType()).willReturn(Node.COMMENT_NODE);
        //WHEN
        Set<ResponseFormatterDescriptor> actual = underTest.parseNode(node, document);
        //THEN
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testParseNodeShouldReturnEmptySetIfNodeTypeIsNotAMatchingType() {
        //GIVEN
        getChildNodes();
        given(element.getNodeType()).willReturn(Node.ELEMENT_NODE);
        given(element.getTagName()).willReturn("someOtherTag");
        //WHEN
        Set<ResponseFormatterDescriptor> actual = underTest.parseNode(node, document);
        //THEN
        assertTrue(actual.isEmpty());
    }

    @Test(expectedExceptions = DescriptorValidationFailedException.class)
    public void testParseNodeShouldThrowExceptionWhenSubTreeIsTooDeep() {
        //GIVEN
        Whitebox.setInternalState(underTest, "maxDepthOfXmlTree", 1);
        getChildNodes();
        given(element.getNodeType()).willReturn(Node.ELEMENT_NODE);
        given(element.getTagName()).willReturn(TEMPLATE_FORMATTER_SET_INVOKER_TAG, TEMPLATE_FORMATTER_SET_INVOKER_TAG, TEMPLATE_FORMATTER_TAG);
        mockTemplateFormatterAttributes();
        given(xPathEvaluator.getElementByXPath(Mockito.anyString(), Mockito.eq(document))).willReturn(node);
        given(formatterInitializer.getExternalClassObject("class")).willReturn(templateFormatter);
        //WHEN
        underTest.parseNode(node, document);
        //THEN exception is thrown
    }

    private void mockTemplateFormatterAttributes() {
        given(element.getAttribute("class")).willReturn("class");
        given(element.getElementsByTagName("param")).willReturn(params);
        given(paramElement.getAttribute("name")).willReturn("name");
        given(paramElement.getAttribute("value")).willReturn("value");
    }

    private void getChildNodes() {
        List<Node> list = new ArrayList<>();
        list.add(element);
        templateFormatters = new MyNodeList(list);
        given(node.getChildNodes()).willReturn(templateFormatters);
    }
}
