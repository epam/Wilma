package com.epam.wilma.stubconfig.dom.parser.node.helper;
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

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.epam.wilma.domain.stubconfig.exception.DescriptorCannotBeParsedException;

/**
 * Provides unit tests for the class {@link StubConfigXPathEvaluator}.
 * @author Tunde_Kovacs
 *
 */
public class StubConfigXPathEvaluatorTest {

    @Mock
    private Document document;
    @Mock
    private Element element;
    @Mock
    private NameSpaceProvider nameSpaceProvider;
    @Mock
    private XPathProvider xPathProvider;
    @Mock
    private XPath xPath;
    @Mock
    private XPathExpression xPathExpression;
    @Mock
    private Element node;

    @InjectMocks
    private StubConfigXPathEvaluator underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(xPathProvider.getXPath()).willReturn(xPath);
    }

    @Test
    public void testGetElementByXPathShouldReturnElement() throws XPathExpressionException {
        //GIVEN
        String expression = "";
        given(xPath.compile(expression)).willReturn(xPathExpression);
        given(xPathExpression.evaluate(document, XPathConstants.NODE)).willReturn(node);
        given(node.getNodeType()).willReturn(Node.ELEMENT_NODE);
        //WHEN
        Element actual = underTest.getElementByXPath(expression, document);
        //THEN
        Assert.assertEquals(actual, node);
    }

    @Test
    public void testGetElementByXPathWhenResultIsNotElementShouldReturnNull() throws XPathExpressionException {
        //GIVEN
        String expression = "";
        given(xPath.compile(expression)).willReturn(xPathExpression);
        given(xPathExpression.evaluate(document, XPathConstants.NODE)).willReturn(node);
        given(node.getNodeType()).willReturn(Node.COMMENT_NODE);
        //WHEN
        Element actual = underTest.getElementByXPath(expression, document);
        //THEN
        Assert.assertEquals(actual, null);
    }

    @Test(expectedExceptions = DescriptorCannotBeParsedException.class)
    public void testGetElementByXPathWhenExpressionIsInvalidShouldThrowException() throws XPathExpressionException {
        //GIVEN
        String expression = "";
        given(xPath.compile(expression)).willThrow(new XPathExpressionException("exception"));
        //given(xPathExpression.evaluate(document, XPathConstants.NODE)).willReturn(node);
        //WHEN
        underTest.getElementByXPath(expression, document);
        //THEN
    }
}
