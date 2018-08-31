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
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.epam.wilma.domain.stubconfig.dialog.condition.CompositeCondition;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionType;
import com.epam.wilma.domain.stubconfig.dialog.condition.SimpleCondition;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.stubconfig.configuration.StubConfigurationAccess;
import com.epam.wilma.stubconfig.configuration.domain.PropertyDto;
import com.epam.wilma.stubconfig.dom.parser.node.helper.CustomXQueryCheckerValidator;
import com.epam.wilma.stubconfig.dom.parser.node.helper.SimpleConditionParser;
import com.epam.wilma.stubconfig.dom.parser.node.helper.StubConfigXPathEvaluator;
import com.epam.wilma.stubconfig.initializer.condition.ConditionCheckerInitializer;

/**
 * Tests for {@link ConditionDescriptorParser}.
 * @author Tamas_Bihari
 *
 */
public class ConditionDescriptorParserTest {
    private static final String TAGNAME_CONDITION_SET = "condition-set";
    private static final String TAGNAME_COND_SET_INVOKER = "condition-set-invoker";
    private static final String TAGNAME_NOT = "not";
    private static final String TAGNAME_OR = "or";
    private static final String TAGNAME_AND = "and";
    private static final String PARAM_NAME = "exampleParam";
    private static final String PARAM_ATTRIBUTE = "param";
    private static final String CLASS_NAME = "SIMPLE_CLASS_NAME";
    private static final String CLASS_ATTRIBUTE = "class";
    private static final String TAGNAME_CONDITION = "condition";
    private SimpleConditionParser simpleConditionParser;

    @Mock
    private Element node;
    @Mock
    private MyNodeList nodeList;
    @Mock
    private Document document;
    @Mock
    private StubConfigXPathEvaluator xPathEvaluator;
    @Mock
    private Element paramElement;
    @Mock
    private Element paramElement2;
    @Mock
    private Element conditionSetElement;
    @Mock
    private ConditionCheckerInitializer conditionCheckerInitializer;
    @Mock
    private ConditionChecker conditionChecker;
    @Mock
    private CustomXQueryCheckerValidator xQueryCheckerValidator;
    @Mock
    private StubConfigurationAccess configurationAccess;
    @Mock
    private PropertyDto properties;

    @InjectMocks
    private ConditionDescriptorParser underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        simpleConditionParser = new SimpleConditionParser();
        Whitebox.setInternalState(simpleConditionParser, "conditionCheckerInitializer", conditionCheckerInitializer);
        Whitebox.setInternalState(simpleConditionParser, "xQueryCheckerValidator", xQueryCheckerValidator);
        Whitebox.setInternalState(underTest, "simpleConditionParser", simpleConditionParser);
    }

    @Test
    public void testParseNodeShouldReturnNullConditionWhenConditionNodeArgumentIsNull() {
        //GIVEN in setUp
        //WHEN
        ConditionDescriptor actual = underTest.parseNode(null, document);
        //THEN
        assertNull(actual.getCondition());
    }

    @Test
    public void testParseNodeShouldReturnNullConditionWhenConditionNodeArgumentHasNoChildren() {
        //GIVEN
        given(node.getChildNodes()).willReturn(nodeList);
        given(nodeList.getLength()).willReturn(0);
        //WHEN
        ConditionDescriptor actual = underTest.parseNode(node, document);
        //THEN
        assertNull(actual.getCondition());
    }

    @Test
    public void testParseNodeShouldReturnNullConditionWhenConditionNodeArgumentGetChildrenIsNull() {
        //GIVEN
        given(node.getChildNodes()).willReturn(null);
        //WHEN
        ConditionDescriptor actual = underTest.parseNode(node, document);
        //THEN
        assertNull(actual.getCondition());
    }

    @Test
    public void testParseNodeShouldReturnWithNullConditionWhenConditionNodeArgumentHasOneChildWithUnknownTagName() {
        //GIVEN
        given(node.getChildNodes()).willReturn(nodeList);
        given(nodeList.getLength()).willReturn(1);
        given(nodeList.item(0)).willReturn(node);
        given(node.getNodeType()).willReturn(Node.ELEMENT_NODE);
        given(node.getTagName()).willReturn("UNKNOWN_TAG_NAME");
        //WHEN
        ConditionDescriptor actual = underTest.parseNode(node, document);
        //THEN
        assertNull(actual.getCondition());
    }

    @Test
    public void testParseNodeShouldReturnWithASimpleConditionWithEmptyConditionListWhenConditionNodeArgumentHasOneNotElementChild() {
        //GIVEN
        given(node.getChildNodes()).willReturn(nodeList);
        given(nodeList.getLength()).willReturn(1);
        given(nodeList.item(0)).willReturn(paramElement);
        given(paramElement.getNodeType()).willReturn(Node.TEXT_NODE);
        //WHEN
        ConditionDescriptor actual = underTest.parseNode(node, document);
        //THEN
        assertNull(actual.getCondition());
    }

    @Test
    public void testParseNodeShouldReturnWithASimpleConditionWhenConditionNodeArgumentHasOneChildWithConditionTagName() {
        //GIVEN
        given(node.getChildNodes()).willReturn(nodeList);
        given(nodeList.getLength()).willReturn(1);
        given(nodeList.item(0)).willReturn(node);
        given(node.getNodeType()).willReturn(Node.ELEMENT_NODE);
        given(node.getTagName()).willReturn(TAGNAME_CONDITION);
        given(node.getAttribute(CLASS_ATTRIBUTE)).willReturn(CLASS_NAME);
        given(conditionCheckerInitializer.getExternalClassObject(CLASS_NAME)).willReturn(conditionChecker);
        //WHEN
        ConditionDescriptor actual = underTest.parseNode(node, document);
        //THEN
        SimpleCondition actCondition = (SimpleCondition) actual.getCondition();
        Assert.assertEquals(actCondition.getConditionChecker(), conditionChecker);
    }

    @Test
    public void testParseNodeShouldReturnWithASimpleConditionParamsWhenConditionNodeArgumentHasOneChildWithConditionTagNameWithChildParam() {
        //GIVEN
        given(node.getChildNodes()).willReturn(nodeList);
        given(nodeList.getLength()).willReturn(1);
        given(nodeList.item(0)).willReturn(node);
        given(node.getNodeType()).willReturn(Node.ELEMENT_NODE);
        given(node.getTagName()).willReturn(TAGNAME_CONDITION);
        given(node.getAttribute(CLASS_ATTRIBUTE)).willReturn(CLASS_NAME);
        List<Node> paramList = new ArrayList<Node>();
        paramList.add(paramElement);
        given(node.getElementsByTagName(PARAM_ATTRIBUTE)).willReturn(new MyNodeList(paramList));
        given(paramElement.getAttribute("name")).willReturn(PARAM_NAME);
        given(paramElement.getAttribute("value")).willReturn(CLASS_NAME);
        //WHEN
        ConditionDescriptor actual = underTest.parseNode(node, document);
        //THEN
        SimpleCondition actCondition = (SimpleCondition) actual.getCondition();
        Assert.assertEquals(actCondition.getParameters().getAllParameters().get(0).getValue(), CLASS_NAME);
    }

    @Test
    public void testParseNodeShouldReturnWithASimpleConditionWithEmptyParamsWhenConditionNodeArgumentHasOneChildWithConditionTagNameWithoutChildParam() {
        //GIVEN
        given(node.getChildNodes()).willReturn(nodeList);
        given(nodeList.getLength()).willReturn(1);
        given(nodeList.item(0)).willReturn(node);
        given(node.getNodeType()).willReturn(Node.ELEMENT_NODE);
        given(node.getTagName()).willReturn(TAGNAME_CONDITION);
        given(node.getAttribute(CLASS_ATTRIBUTE)).willReturn(CLASS_NAME);
        List<Node> paramList = new ArrayList<Node>();
        given(node.getElementsByTagName(PARAM_ATTRIBUTE)).willReturn(new MyNodeList(paramList));
        given(paramElement.getAttribute("name")).willReturn(PARAM_NAME);
        given(paramElement.getAttribute("value")).willReturn(CLASS_NAME);
        //WHEN
        ConditionDescriptor actual = underTest.parseNode(node, document);
        //THEN
        SimpleCondition actCondition = (SimpleCondition) actual.getCondition();
        Assert.assertEquals(actCondition.getParameters().getAllParameters().size(), 0);
    }

    @Test
    public void testParseNodeShouldReturnWithACompositeConditionWhenConditionNodeArgumentHasOneChildWithAndTagName() {
        //GIVEN
        given(node.getChildNodes()).willReturn(nodeList);
        given(nodeList.getLength()).willReturn(1);
        given(nodeList.item(0)).willReturn(paramElement);
        given(paramElement.getNodeType()).willReturn(Node.ELEMENT_NODE);
        given(paramElement.getTagName()).willReturn(TAGNAME_AND);
        given(paramElement.getChildNodes()).willReturn(new MyNodeList(new ArrayList<Node>()));
        //WHEN
        ConditionDescriptor actual = underTest.parseNode(node, document);
        //THEN
        CompositeCondition actCondition = (CompositeCondition) actual.getCondition();
        Assert.assertEquals(actCondition.getConditionType(), ConditionType.AND);
    }

    @Test
    public void testParseNodeShouldReturnWithACompositeConditionWhenConditionNodeArgumentHasOneChildWithOrTagName() {
        //GIVEN
        given(node.getChildNodes()).willReturn(nodeList);
        given(nodeList.getLength()).willReturn(1);
        given(nodeList.item(0)).willReturn(paramElement);
        given(paramElement.getNodeType()).willReturn(Node.ELEMENT_NODE);
        given(paramElement.getTagName()).willReturn(TAGNAME_OR);
        given(paramElement.getChildNodes()).willReturn(new MyNodeList(new ArrayList<Node>()));
        //WHEN
        ConditionDescriptor actual = underTest.parseNode(node, document);
        //THEN
        CompositeCondition actCondition = (CompositeCondition) actual.getCondition();
        Assert.assertEquals(actCondition.getConditionType(), ConditionType.OR);
    }

    @Test
    public void testParseNodeShouldReturnWithACompositeConditionWhenConditionNodeArgumentHasOneChildWithNotTagName() {
        //GIVEN
        given(node.getChildNodes()).willReturn(nodeList);
        given(nodeList.getLength()).willReturn(1);
        given(nodeList.item(0)).willReturn(paramElement);
        given(paramElement.getNodeType()).willReturn(Node.ELEMENT_NODE);
        given(paramElement.getTagName()).willReturn(TAGNAME_NOT);
        given(paramElement.getChildNodes()).willReturn(new MyNodeList(new ArrayList<Node>()));
        //WHEN
        ConditionDescriptor actual = underTest.parseNode(node, document);
        //THEN
        CompositeCondition actCondition = (CompositeCondition) actual.getCondition();
        Assert.assertEquals(actCondition.getConditionType(), ConditionType.NOT);
    }

    @Test
    public void testParseNodeShouldReturnWithACompositeConditionWhenConditionNodeArgumentHasChildWithConditionSetInvokerTagName() {
        //GIVEN
        given(configurationAccess.getProperties()).willReturn(properties);
        given(properties.getMaxDepthOfTree()).willReturn(10);
        given(node.getChildNodes()).willReturn(nodeList);
        given(nodeList.getLength()).willReturn(1);
        given(nodeList.item(0)).willReturn(paramElement);
        given(paramElement.getNodeType()).willReturn(Node.ELEMENT_NODE);
        given(paramElement.getTagName()).willReturn(TAGNAME_COND_SET_INVOKER);
        given(paramElement.getAttribute("name")).willReturn("CONDITION_SET_NAME");
        given(xPathEvaluator.getElementByXPath(Mockito.anyString(), (Document) Mockito.any())).willReturn(conditionSetElement);
        given(conditionSetElement.getNodeType()).willReturn(Node.ELEMENT_NODE);
        given(conditionSetElement.getTagName()).willReturn(TAGNAME_CONDITION_SET);
        List<Node> conditionList = new ArrayList<Node>();
        conditionList.add(paramElement2);
        given(conditionSetElement.getChildNodes()).willReturn(new MyNodeList(conditionList));
        given(paramElement2.getNodeType()).willReturn(Node.ELEMENT_NODE);
        given(paramElement2.getTagName()).willReturn(TAGNAME_CONDITION);
        given(paramElement2.getAttribute(CLASS_ATTRIBUTE)).willReturn(CLASS_NAME);
        given(conditionCheckerInitializer.getExternalClassObject(CLASS_NAME)).willReturn(conditionChecker);
        List<Node> paramList = new ArrayList<Node>();
        given(paramElement2.getElementsByTagName(PARAM_ATTRIBUTE)).willReturn(new MyNodeList(paramList));
        //WHEN
        ConditionDescriptor actual = underTest.parseNode(node, document);
        //THEN
        SimpleCondition actCondition = (SimpleCondition) actual.getCondition();
        assertEquals(actCondition.getConditionChecker(), conditionChecker);
    }
}
