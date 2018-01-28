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
import static org.testng.AssertJUnit.assertEquals;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.sequencehandler.DummySequenceHandler;
import com.epam.wilma.stubconfig.configuration.StubConfigurationAccess;
import com.epam.wilma.stubconfig.configuration.domain.PropertyDto;
import com.epam.wilma.stubconfig.initializer.sequencehandler.SequenceHandlerInitializer;

/**
 * Unit test for {@link SequenceDescriptorAttributesParser}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class SequenceDescriptorAttributesParserTest {

    private static final String GROUP_NAME = "groupname";
    private static final String SEQUENCE_HANDLER_CLASS_NAME = "sequence-handler-class-name";
    private static final String SEQUENCE_DESCRIPTOR_NAME = "sequence-descriptor-name";
    @Mock
    private SequenceHandlerInitializer sequenceHandlerInitializer;
    @Mock
    private StubConfigurationAccess stubConfigurationAccess;

    @InjectMocks
    private SequenceDescriptorAttributesParser underTest;

    @Mock
    private Element element;
    @Mock
    private Document document;
    @Mock
    private PropertyDto value;
    @Mock
    private Element documentElement;
    @Mock
    private NodeList nodes;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(document.getDocumentElement()).willReturn(documentElement);
        given(documentElement.getAttribute(GROUP_NAME)).willReturn(GROUP_NAME);
        given(element.getElementsByTagName("param")).willReturn(nodes);
        given(nodes.getLength()).willReturn(1);
        given(nodes.item(0)).willReturn(element);
    }

    @Test
    public void testParseNodeWhenTimeoutIsGivenShouldUseTheGivenTimeout() {
        //GIVEN
        given(element.getAttribute("name")).willReturn(SEQUENCE_DESCRIPTOR_NAME).willReturn("KEY");
        given(element.getAttribute("class")).willReturn(SEQUENCE_HANDLER_CLASS_NAME);
        given(element.getAttribute("timeout")).willReturn("6000");
        given(element.getAttribute("value")).willReturn("VALUE");
        given(stubConfigurationAccess.getProperties()).willReturn(value);
        DummySequenceHandler dummySequenceHandler = new DummySequenceHandler();
        given(sequenceHandlerInitializer.getExternalClassObject(SEQUENCE_HANDLER_CLASS_NAME)).willReturn(dummySequenceHandler);
        //WHEN
        SequenceDescriptorAttributes result = underTest.parseNode(element, document);
        //THEN
        assertEquals(SEQUENCE_DESCRIPTOR_NAME, result.getName());
        assertEquals(6000L, result.getDefaultTimeout());
        assertEquals(GROUP_NAME, result.getGroupName());
        assertEquals(dummySequenceHandler, result.getHandler());
        assertEquals("VALUE", result.getParameters().get("KEY"));
    }

    @Test
    public void testParseNodeWhenTimeoutIsNotGivenShouldUseTheDefaultTimeout() {
        //GIVEN
        given(element.getAttribute("name")).willReturn(SEQUENCE_DESCRIPTOR_NAME).willReturn("KEY");
        given(element.getAttribute("class")).willReturn(SEQUENCE_HANDLER_CLASS_NAME);
        given(element.getAttribute("timeout")).willReturn("");
        given(element.getAttribute("value")).willReturn("VALUE");
        given(stubConfigurationAccess.getProperties()).willReturn(value);
        given(value.getDefaultSequenceTimeout()).willReturn(9000L);
        DummySequenceHandler dummySequenceHandler = new DummySequenceHandler();
        given(sequenceHandlerInitializer.getExternalClassObject(SEQUENCE_HANDLER_CLASS_NAME)).willReturn(dummySequenceHandler);
        //WHEN
        SequenceDescriptorAttributes result = underTest.parseNode(element, document);
        //THEN
        assertEquals(SEQUENCE_DESCRIPTOR_NAME, result.getName());
        assertEquals(9000L, result.getDefaultTimeout());
        assertEquals(GROUP_NAME, result.getGroupName());
        assertEquals(dummySequenceHandler, result.getHandler());
        assertEquals("VALUE", result.getParameters().get("KEY"));
    }

    @Test
    public void testParseNodeWhenThereIsNoParameter() {
        //GIVEN
        given(nodes.getLength()).willReturn(0);
        given(element.getAttribute("name")).willReturn(SEQUENCE_DESCRIPTOR_NAME);
        given(element.getAttribute("class")).willReturn(SEQUENCE_HANDLER_CLASS_NAME);
        given(element.getAttribute("timeout")).willReturn("");
        given(stubConfigurationAccess.getProperties()).willReturn(value);
        given(value.getDefaultSequenceTimeout()).willReturn(9000L);
        DummySequenceHandler dummySequenceHandler = new DummySequenceHandler();
        given(sequenceHandlerInitializer.getExternalClassObject(SEQUENCE_HANDLER_CLASS_NAME)).willReturn(dummySequenceHandler);
        //WHEN
        SequenceDescriptorAttributes result = underTest.parseNode(element, document);
        //THEN
        assertEquals(SEQUENCE_DESCRIPTOR_NAME, result.getName());
        assertEquals(9000L, result.getDefaultTimeout());
        assertEquals(GROUP_NAME, result.getGroupName());
        assertEquals(dummySequenceHandler, result.getHandler());
    }

}
