package com.epam.wilma.stubconfig.dom.parser.sequence;
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
import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionDescriptor;
import com.epam.wilma.stubconfig.dom.parser.node.ConditionDescriptorParser;

/**
 * Unit test for {@link ConditionDescriptorNodesParser}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class ConditionDescriptorNodesParserTest {

    private ConditionDescriptor conditionDescriptor;
    @Mock
    private ConditionDescriptorParser conditionDescriptorParser;

    @InjectMocks
    private ConditionDescriptorNodesParser underTest;
    @Mock
    private Document document;
    @Mock
    private Element element;
    @Mock
    private NodeList nodeList;
    @Mock
    private Element conditionDescriptorElement;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testParseNodesOfElementShouldReturnEmptyListWhenNoConditionDescriptorsExist() {
        //GIVEN
        given(element.getElementsByTagName(ConditionDescriptor.TAG_NAME)).willReturn(nodeList);
        given(nodeList.getLength()).willReturn(0);
        //WHEN
        List<ConditionDescriptor> result = underTest.parseNodesOfElement(document, element);
        //THEN
        assertTrue(result.isEmpty());

    }

    @Test
    public void testParseNodesOfElement() {
        //GIVEN
        List<ConditionDescriptor> expected = new ArrayList<>();
        expected.add(conditionDescriptor);
        expected.add(conditionDescriptor);
        given(element.getElementsByTagName(ConditionDescriptor.TAG_NAME)).willReturn(nodeList);
        given(nodeList.getLength()).willReturn(2);
        given(nodeList.item(0)).willReturn(conditionDescriptorElement);
        given(nodeList.item(1)).willReturn(conditionDescriptorElement);
        given(conditionDescriptorParser.parseNode(conditionDescriptorElement, document)).willReturn(conditionDescriptor);
        //WHEN
        List<ConditionDescriptor> result = underTest.parseNodesOfElement(document, element);
        //THEN
        assertEquals(expected, result);
    }
}
