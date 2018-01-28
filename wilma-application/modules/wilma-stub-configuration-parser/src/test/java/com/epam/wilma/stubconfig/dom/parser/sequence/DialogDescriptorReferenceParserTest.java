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

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;

/**
 * Unit test for {@link DialogDescriptorReferenceParser}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class DialogDescriptorReferenceParserTest {

    private static final String DIALOG_DESCRIPTOR_REFERENCE_NAME_ATTRIBUTE = "ref";
    private static final String DIALOG_DESCRIPTOR_REFERENCE_TAG_NAME = "dialog-descriptor-ref";

    private DialogDescriptorReferenceParser underTest;

    private DialogDescriptor dialogDescriptorOne;
    private DialogDescriptor dialogDescriptorTwo;
    private Map<String, DialogDescriptor> nameToDialogDescriptor;

    @Mock
    private Element sequenceElement;
    @Mock
    private NodeList nodeList;
    @Mock
    private Element dialogDescriptorReferenceElement;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new DialogDescriptorReferenceParser();
        nameToDialogDescriptor = new HashMap<String, DialogDescriptor>();
    }

    @Test
    public void testParse() {
        //GIVEN
        dialogDescriptorOne = new DialogDescriptor(null, null, null);
        dialogDescriptorTwo = new DialogDescriptor(null, null, null);
        List<DialogDescriptor> expected = new ArrayList<>();
        expected.add(dialogDescriptorOne);

        nameToDialogDescriptor.put("one", dialogDescriptorOne);
        nameToDialogDescriptor.put("two", dialogDescriptorTwo);
        given(sequenceElement.getElementsByTagName(DIALOG_DESCRIPTOR_REFERENCE_TAG_NAME)).willReturn(nodeList);
        given(nodeList.getLength()).willReturn(1);
        given(nodeList.item(0)).willReturn(dialogDescriptorReferenceElement);
        given(dialogDescriptorReferenceElement.getAttribute(DIALOG_DESCRIPTOR_REFERENCE_NAME_ATTRIBUTE)).willReturn("one");
        //WHEN
        List<DialogDescriptor> result = underTest.parse(sequenceElement, nameToDialogDescriptor);
        //THEN
        assertEquals(expected, result);
    }
}
