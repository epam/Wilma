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

import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;

/**
 * Unit test for {@link SequenceDescriptorParser}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class SequenceDescriptorParserTest {

    private static final String SEQUENCE_DESCRIPTOR_TAG_NAME = "sequence-descriptor";

    private SequenceDescriptor sequenceDescriptor;
    private List<DialogDescriptor> dialogDescriptors;

    @Mock
    private SequenceDescriptorFactory sequenceDescriptorFactory;

    @InjectMocks
    private SequenceDescriptorParser underTest;

    @Mock
    private Element rootElement;
    @Mock
    private Document document;

    @Mock
    private NodeList nodeList;
    @Mock
    private Element sequenceElement;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testParseShouldReturnEmptyListWhenNoSequenceDescriptorExists() {
        //GIVEN
        given(rootElement.getElementsByTagName(SEQUENCE_DESCRIPTOR_TAG_NAME)).willReturn(nodeList);
        given(nodeList.getLength()).willReturn(0);
        //WHEN
        List<SequenceDescriptor> result = underTest.parse(document, rootElement, dialogDescriptors);
        //THEN
        assertTrue(result.isEmpty());
    }

    @Test
    public void testParse() {
        //GIVEN
        List<SequenceDescriptor> expected = new ArrayList<>();
        expected.add(sequenceDescriptor);
        expected.add(sequenceDescriptor);
        dialogDescriptors = new ArrayList<>();
        given(rootElement.getElementsByTagName(SEQUENCE_DESCRIPTOR_TAG_NAME)).willReturn(nodeList);
        given(nodeList.getLength()).willReturn(2);
        given(nodeList.item(0)).willReturn(sequenceElement);
        given(nodeList.item(1)).willReturn(sequenceElement);
        given(sequenceDescriptorFactory.create(document, sequenceElement, dialogDescriptors)).willReturn(sequenceDescriptor);
        //WHEN
        List<SequenceDescriptor> result = underTest.parse(document, rootElement, dialogDescriptors);
        //THEN
        assertEquals(expected, result);
    }
}
