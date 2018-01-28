package com.epam.wilma.stubconfig.condition.checker.xml;
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
import net.sf.saxon.s9api.SaxonApiException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.stubconfig.condition.checker.xml.helper.XQueryExpressionEvaluator;
import com.epam.wilma.domain.stubconfig.exception.ConditionEvaluationFailedException;

/**
 * Provides unit tests for the class {@link XmlNodeValueChecker}.
 * @author Tunde_Kovacs
 *
 */

public class XmlNodeValueCheckerTest {

    private static final String ELEMENT = "ns56:AnyID";
    private static final String ELEMENT_VALUE = "179";
    private static final String REQUEST = "request";
    private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String XML_CONTENT = "application/xml";
    private static final String FASTINFOSET_CONTENT = "application/fastinfoset";

    private ParameterList parameterList;
    @Mock
    private WilmaHttpRequest request;
    @Mock
    private XQueryExpressionEvaluator queryExpressionEvaluator;

    @InjectMocks
    private XmlNodeValueChecker underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parameterList = new ParameterList();
    }

    @Test
    public void testCheckConditionWhenElementExistsShouldReturnTrue() throws SaxonApiException {
        //GIVEN
        parameterList.addParameter(new Parameter(ELEMENT, ELEMENT_VALUE));
        String query = "(//*[name()='" + ELEMENT + "'])='" + ELEMENT_VALUE + "'";
        given(queryExpressionEvaluator.evaluateXQuery(REQUEST, query)).willReturn(XML_DECLARATION + "true");
        given(request.getBody()).willReturn(REQUEST);
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(XML_CONTENT);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testCheckConditionWhenContentIsFastinfosetShouldReturnTrue() throws SaxonApiException {
        //GIVEN
        parameterList.addParameter(new Parameter(ELEMENT, ELEMENT_VALUE));
        String query = "(//*[name()='" + ELEMENT + "'])='" + ELEMENT_VALUE + "'";
        given(queryExpressionEvaluator.evaluateXQuery(REQUEST, query)).willReturn(XML_DECLARATION + "true");
        given(request.getBody()).willReturn(REQUEST);
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(FASTINFOSET_CONTENT);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testCheckConditionWhenValueDoesNotExistShouldReturnFalse() throws SaxonApiException {
        //GIVEN
        parameterList.addParameter(new Parameter(ELEMENT, "178"));
        String query = "(//*[name()='" + ELEMENT + "'])='" + 178 + "'";
        given(queryExpressionEvaluator.evaluateXQuery(REQUEST, query)).willReturn(XML_DECLARATION + "false");
        given(request.getBody()).willReturn(REQUEST);
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(XML_CONTENT);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testCheckConditionWhenRequestIsNotXmlShouldReturnFalse() {
        //GIVEN
        parameterList.addParameter(new Parameter(ELEMENT, ELEMENT_VALUE));
        given(request.getBody()).willReturn(REQUEST);
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn("text/plain");
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testCheckConditionWhenRequestDoesNotHaveContentTypeShouldReturnFalse() {
        //GIVEN
        parameterList.addParameter(new Parameter(ELEMENT, ELEMENT_VALUE));
        given(request.getBody()).willReturn(REQUEST);
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(null);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertEquals(actual, false);
    }

    @Test(expectedExceptions = ConditionEvaluationFailedException.class)
    public void testCheckConditionWhenParameterMapIsEmptyShouldThrowException() {
        //GIVEN in setUp
        //WHEN
        underTest.checkCondition(request, parameterList);
        //THEN exception should be thrown
    }

    @Test(expectedExceptions = ConditionEvaluationFailedException.class)
    public void testCheckConditionWhenParameterHasMultipleEntriesShouldThrowException() {
        //GIVEN
        parameterList.addParameter(new Parameter(ELEMENT, "178"));
        parameterList.addParameter(new Parameter("ns1:OTHERID", "3001"));
        //WHEN
        underTest.checkCondition(request, parameterList);
        //THEN exception should be thrown
    }

    @Test(expectedExceptions = ConditionEvaluationFailedException.class)
    public void testCheckConditionWhenErrorDuringXqueryExecutionShouldThrowException() throws SaxonApiException {
        //GIVEN in setUp
        parameterList.addParameter(new Parameter(ELEMENT, ELEMENT_VALUE));
        String query = "(//*[name()='" + ELEMENT + "'])='" + ELEMENT_VALUE + "'";
        given(queryExpressionEvaluator.evaluateXQuery(REQUEST, query)).willThrow(new SaxonApiException("exception"));
        given(request.getBody()).willReturn(REQUEST);
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(XML_CONTENT);
        //WHEN
        underTest.checkCondition(request, parameterList);
        //THEN exception should be thrown
    }
}
