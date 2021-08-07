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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.exception.ConditionEvaluationFailedException;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.stubconfig.condition.checker.xml.helper.XQueryExpressionEvaluator;
import net.sf.saxon.s9api.SaxonApiException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

/**
 * Provides unit tests for the class {@link XmlAttributeChecker}.
 *
 * @author Tunde_Kovacs
 */
public class XmlAttributeCheckerTest {

    private static final String NODE = "ns67:AnyID";
    private static final String ATTRIBUTE = "valid";
    private static final String VALUE = "true";
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
    private XmlAttributeChecker underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parameterList = new ParameterList();
    }

    @Test
    public void testCheckConditionWhenElementExistsShouldReturnTrue() throws SaxonApiException {
        //GIVEN
        parameterList.addParameter(new Parameter(NODE + "/@" + ATTRIBUTE, VALUE));
        String query = "//*[name()='" + NODE + "']/@" + ATTRIBUTE + "='" + VALUE + "'";
        given(queryExpressionEvaluator.evaluateXQuery(REQUEST, query)).willReturn(XML_DECLARATION + "true");
        given(request.getBody()).willReturn(REQUEST);
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(XML_CONTENT);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertTrue(actual);
    }

    @Test
    public void testCheckConditionWhenContentIsFastinfosetShouldReturnTrue() throws SaxonApiException {
        //GIVEN
        parameterList.addParameter(new Parameter(NODE + "/@" + ATTRIBUTE, VALUE));
        String query = "//*[name()='" + NODE + "']/@" + ATTRIBUTE + "='" + VALUE + "'";
        given(queryExpressionEvaluator.evaluateXQuery(REQUEST, query)).willReturn(XML_DECLARATION + "true");
        given(request.getBody()).willReturn(REQUEST);
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(FASTINFOSET_CONTENT);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertTrue(actual);
    }

    @Test
    public void testCheckConditionWhenValueDoesNotExistShouldReturnFalse() throws SaxonApiException {
        //GIVEN
        parameterList.addParameter(new Parameter(NODE + "/@" + ATTRIBUTE, "false"));
        String query = "//*[name()='" + NODE + "']/@" + ATTRIBUTE + "='false'";
        given(queryExpressionEvaluator.evaluateXQuery(REQUEST, query)).willReturn(XML_DECLARATION + "false");
        given(request.getBody()).willReturn(REQUEST);
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(XML_CONTENT);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionWhenRequestIsNotXmlShouldReturnFalse() {
        //GIVEN
        parameterList.addParameter(new Parameter(NODE, VALUE));
        given(request.getBody()).willReturn(REQUEST);
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn("text/plain");
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionWhenRequestDoesNotHaveContentTypeShouldReturnFalse() {
        //GIVEN
        parameterList.addParameter(new Parameter(NODE, VALUE));
        given(request.getBody()).willReturn(REQUEST);
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(null);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertFalse(actual);
    }

    @Test(expected = ConditionEvaluationFailedException.class)
    public void testCheckConditionWhenParameterMapIsEmptyShouldThrowException() {
        //GIVEN in setUp
        //WHEN
        underTest.checkCondition(request, parameterList);
        //THEN exception should be thrown
    }

    @Test(expected = ConditionEvaluationFailedException.class)
    public void testCheckConditionWhenParameterHasMultipleEntriesShouldThrowException() {
        //GIVEN
        parameterList.addParameter(new Parameter(NODE, "178"));
        parameterList.addParameter(new Parameter("ns1:OTHERID", "3001"));
        //WHEN
        underTest.checkCondition(request, parameterList);
        //THEN exception should be thrown
    }

    @Test(expected = ConditionEvaluationFailedException.class)
    public void testCheckConditionWhenErrorDuringXqueryExecutionShouldThrowException() throws SaxonApiException {
        //GIVEN in setUp
        parameterList.addParameter(new Parameter(NODE + "/@" + ATTRIBUTE, VALUE));
        String query = "//*[name()='" + NODE + "']/@" + ATTRIBUTE + "='" + VALUE + "'";
        given(queryExpressionEvaluator.evaluateXQuery(REQUEST, query)).willThrow(new SaxonApiException("exception"));
        given(request.getBody()).willReturn(REQUEST);
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(XML_CONTENT);
        //WHEN
        underTest.checkCondition(request, parameterList);
        //THEN exception should be thrown
    }

    @Test(expected = ConditionEvaluationFailedException.class)
    public void testCheckConditionWhenIncorrectInputElementShouldThrowException() {
        //GIVEN in setUp
        parameterList.addParameter(new Parameter(NODE + "@" + ATTRIBUTE, VALUE));
        given(request.getBody()).willReturn(REQUEST);
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(XML_CONTENT);
        //WHEN
        underTest.checkCondition(request, parameterList);
        //THEN exception should be thrown
    }
}
