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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionDescriptor;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.sequencehandler.DummySequenceHandler;
import com.epam.wilma.stubconfig.dom.parser.node.SequenceDescriptorAttributesParser;
import com.epam.wilma.stubconfig.dom.parser.sequence.helper.DialogDescriptorMapper;

/**
 * Unit test for {@link SequenceDescriptorFactory}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class SequenceDescriptorFactoryTest {

    @Mock
    private SequenceDescriptorAttributesParser sequenceDescriptorAttributesParser;
    @Mock
    private DialogDescriptorReferenceParser dialogDescriptorReferenceParser;
    @Mock
    private DialogDescriptorMapper dialogDescriptorMapper;
    @Mock
    private ConditionDescriptorNodesParser conditionDescriptorNodesParser;

    @InjectMocks
    private SequenceDescriptorFactory underTest;

    @Mock
    private Document document;
    @Mock
    private Element sequenceElement;
    private List<DialogDescriptor> dialogDescriptors;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreate() {
        //GIVEN
        List<ConditionDescriptor> conditionDescriptors = new ArrayList<>();
        List<DialogDescriptor> sequenceDialogDescriptors = new ArrayList<>();
        Map<String, DialogDescriptor> nameToDialogDescriptor = new HashMap<>();
        DummySequenceHandler dummySequenceHandler = new DummySequenceHandler();
        SequenceDescriptorAttributes sequenceDescriptorAttributes = new SequenceDescriptorAttributes.Builder().name("bob")
                .handler(dummySequenceHandler).defaultTimeout(1L).build();
        given(conditionDescriptorNodesParser.parseNodesOfElement(document, sequenceElement)).willReturn(conditionDescriptors);
        given(dialogDescriptorMapper.groupByName(dialogDescriptors)).willReturn(nameToDialogDescriptor);
        given(dialogDescriptorReferenceParser.parse(sequenceElement, nameToDialogDescriptor)).willReturn(sequenceDialogDescriptors);
        given(sequenceDescriptorAttributesParser.parseNode(sequenceElement, document)).willReturn(sequenceDescriptorAttributes);
        //WHEN
        SequenceDescriptor result = underTest.create(document, sequenceElement, dialogDescriptors);
        //THEN
        assertEquals(conditionDescriptors, result.getConditionDescriptors());
        assertEquals(sequenceDialogDescriptors, result.getDialogDescriptors());
        assertEquals(1L, result.getDefaultTimeout());
        assertEquals(dummySequenceHandler, result.getHandler());
        assertEquals("bob", result.getName());
    }
}
