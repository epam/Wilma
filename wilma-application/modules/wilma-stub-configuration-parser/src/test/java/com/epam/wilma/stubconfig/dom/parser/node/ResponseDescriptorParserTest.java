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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.LinkedHashSet;
import java.util.Set;

import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatter;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatterDescriptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.epam.wilma.domain.stubconfig.dialog.response.MimeType;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateGenerator;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateType;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.sequence.helper.SequenceDescriptorKeyUtil;
import com.epam.wilma.stubconfig.dom.parser.NodeParser;
import com.epam.wilma.stubconfig.dom.parser.node.helper.StubConfigXPathEvaluator;
import com.epam.wilma.domain.stubconfig.exception.DescriptorCannotBeParsedException;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;
import com.epam.wilma.stubconfig.initializer.template.TemplateFileReader;
import com.epam.wilma.stubconfig.initializer.template.TemplateGeneratorInitializer;

/**
 * Provides unit tests for the class {@link ResponseDescriptorParser}.
 * @author Tunde_Kovacs
 *
 */
public class ResponseDescriptorParserTest {

    @Mock
    private Element responseDescriptorNode;
    @Mock
    private TemplateGeneratorInitializer templateGeneratorInitializer;
    @Mock
    private Element templateElement;
    @Mock
    private Element rootElement;
    @Mock
    private TemplateFileReader templateFileReader;
    @Mock
    private Document document;
    @Mock
    private StubConfigXPathEvaluator xPathEvaluator;
    @Mock
    private SequenceDescriptorKeyUtil sequenceDescriptorKeyUtil;
    @Mock
    private NodeParser<Set<ResponseFormatterDescriptor>> templateDescriptorParser;
    @Mock
    private TemplateGenerator templateGenerator;
    @Mock
    private ResponseFormatter templateFormatter;
    private Set<ResponseFormatterDescriptor> templateFormatterSet;
    private final ResponseFormatterDescriptor templateFormatterDescriptor = new ResponseFormatterDescriptor(templateFormatter, new ParameterList());

    @InjectMocks
    private ResponseDescriptorParser underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        templateFormatterSet = new LinkedHashSet<>();
        templateFormatterSet.add(templateFormatterDescriptor);
        given(templateDescriptorParser.parseNode(responseDescriptorNode, document)).willReturn(templateFormatterSet);
        given(responseDescriptorNode.getAttribute("delay")).willReturn("1");
        given(responseDescriptorNode.getAttribute("code")).willReturn("200");
        given(responseDescriptorNode.getAttribute("mimetype")).willReturn(MimeType.TEXT.getOfficialMimeType());
        given(responseDescriptorNode.getAttribute("template")).willReturn("Checker.class");
        given(responseDescriptorNode.getAttribute("sequenceDescriptorName")).willReturn("a-b-c");
        given(document.getDocumentElement()).willReturn(rootElement);
        given(rootElement.getAttribute("groupname")).willReturn("TestGroupname");
        given(xPathEvaluator.getElementByXPath(Mockito.anyString(), Mockito.eq(document))).willReturn(templateElement);
        given(templateElement.getAttribute("type")).willReturn("text");
        given(templateElement.getAttribute("resource")).willReturn("resource");
        given(sequenceDescriptorKeyUtil.createDescriptorKey("TestGroupname", "a-b-c")).willReturn("TestGroupname_a-b-c");
    }

    @Test
    public void testParseNodeShouldReturnResponseDescriptorDelay() {
        //GIVEN in setUp
        //WHEN
        ResponseDescriptor actual = underTest.parseNode(responseDescriptorNode, document);
        //THEN
        assertEquals(actual.getAttributes().getDelay(), 1);
    }

    @Test
    public void testParseNodeShouldReturnResponseDescriptorCode() {
        //GIVEN in setUp
        //WHEN
        ResponseDescriptor actual = underTest.parseNode(responseDescriptorNode, document);
        //THEN
        assertEquals(actual.getAttributes().getCode(), "200");
    }

    @Test
    public void testParseNodeShouldReturnResponseDescriptorMimeType() {
        //GIVEN in setUp
        //WHEN
        ResponseDescriptor actual = underTest.parseNode(responseDescriptorNode, document);
        //THEN
        assertEquals(actual.getAttributes().getMimeType(), MimeType.TEXT.getOfficialMimeType());
    }

    @Test
    public void testParseNodeShouldReturnResponseDescriptorTemplateName() {
        //GIVEN in setUp
        //WHEN
        ResponseDescriptor actual = underTest.parseNode(responseDescriptorNode, document);
        //THEN
        assertEquals(actual.getAttributes().getTemplate().getName(), "Checker.class");
    }

    @Test
    public void testParseNodeShouldReturnSequenceDescriptorNameOfResponseDescriptor() {
        //GIVEN in setUp
        //WHEN
        ResponseDescriptor actual = underTest.parseNode(responseDescriptorNode, document);
        //THEN
        assertEquals(actual.getAttributes().getSequenceDescriptorKey(), "TestGroupname_a-b-c");
    }

    @Test
    public void testParseNodeShouldCallTemplateFileReaderWhenTemplateTypeIsXML() {
        //GIVEN
        given(templateElement.getAttribute("type")).willReturn("xml");
        //WHEN
        ResponseDescriptor actual = underTest.parseNode(responseDescriptorNode, document);
        //THEN
        assertEquals(actual.getAttributes().getTemplate().getType(), TemplateType.XML);
    }

    @Test
    public void testParseNodeShouldCallTemplateFileReaderWhenTemplateTypeIsHTML() {
        //GIVEN
        given(templateElement.getAttribute("type")).willReturn("html");
        //WHEN
        ResponseDescriptor actual = underTest.parseNode(responseDescriptorNode, document);
        //THEN
        assertEquals(actual.getAttributes().getTemplate().getType(), TemplateType.HTML);
    }

    @Test
    public void testParseNodeShouldCallTemplateFileReaderWhenTemplateTypeIsFile() {
        //GIVEN
        given(templateElement.getAttribute("type")).willReturn("xmlfile");
        //WHEN
        ResponseDescriptor actual = underTest.parseNode(responseDescriptorNode, document);
        //THEN
        verify(templateFileReader).readTemplate(anyString());
        assertEquals(actual.getAttributes().getTemplate().getType(), TemplateType.XMLFILE);
    }

    @Test
    public void testParseNodeShouldCallTemplateGeneratorInitializerWhenTemplateTypeIsExternal() {
        //GIVEN
        given(templateElement.getAttribute("type")).willReturn("external");
        given(templateGeneratorInitializer.getTemplateGenerator(anyString())).willReturn(templateGenerator);
        //WHEN
        ResponseDescriptor actual = underTest.parseNode(responseDescriptorNode, document);
        //THEN
        verify(templateGenerator).generateTemplate();
        assertEquals(actual.getAttributes().getTemplate().getType(), TemplateType.EXTERNAL);
    }

    @Test(expectedExceptions = DescriptorCannotBeParsedException.class)
    public void testParseNodeShouldShouldThrowSystemExceptionWhenTemplateGenerationFail() {
        //GIVEN
        given(templateElement.getAttribute("type")).willReturn("external");
        given(templateGeneratorInitializer.getTemplateGenerator(anyString())).willReturn(templateGenerator);
        given(templateGenerator.generateTemplate()).willThrow(new DescriptorValidationFailedException(""));
        //WHEN
        ResponseDescriptor actual = underTest.parseNode(responseDescriptorNode, document);
        //THEN
        assertEquals(actual.getAttributes().getTemplate().getType(), TemplateType.EXTERNAL);
    }

    @Test
    public void testParseNodeShouldReturnResponseDescriptorTemplateResource() {
        String resourceString = "resource";
        //GIVEN
        given(templateFileReader.readTemplate(resourceString)).willReturn(resourceString.getBytes());
        //WHEN
        ResponseDescriptor actual = underTest.parseNode(responseDescriptorNode, document);
        //THEN
        assertEquals(actual.getAttributes().getTemplate().getResource(), resourceString.getBytes());
    }

    @Test
    public void testParseNodeShouldReturnResponseDescriptorTemplateFormatter() {
        //GIVEN in setUp
        //WHEN
        ResponseDescriptor actual = underTest.parseNode(responseDescriptorNode, document);
        //THEN
        assertTrue(actual.getResponseFormatters().contains(templateFormatterDescriptor));
    }

    @Test
    public void testParseNodeWhenNodeIsNullShouldReturnNull() {
        //GIVEN in setUp
        //WHEN
        ResponseDescriptor actual = underTest.parseNode(null, document);
        //THEN
        assertNull(actual);
    }
}
