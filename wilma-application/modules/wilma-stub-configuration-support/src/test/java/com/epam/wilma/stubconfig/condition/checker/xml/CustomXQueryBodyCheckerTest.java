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
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import net.sf.saxon.s9api.SaxonApiException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.stubconfig.condition.checker.xml.helper.XQueryExpressionEvaluator;
import com.epam.wilma.domain.stubconfig.exception.ConditionEvaluationFailedException;

/**
 * Provides unit tests for the class {@link CustomXQueryBodyChecker}.
 * @author Tunde_Kovacs
 *
 */
public class CustomXQueryBodyCheckerTest {

    private static final String FASTINFOSET_CONTENT = "application/fastinfoset";
    private static final String XML_CONTENT = "xml";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String BODY = "body";
    private static final String QUERY = "query";

    private ParameterList parameterList;

    @Mock
    private WilmaHttpRequest request;
    @Mock
    private Logger logger;
    @Mock
    private XQueryExpressionEvaluator queryExpressionEvaluator;

    @InjectMocks
    private CustomXQueryBodyChecker underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parameterList = new ParameterList();
    }

    @Test
    public void testCheckConditionWhenElementExistsShouldReturnTrue() throws SaxonApiException {
        //GIVEN
        parameterList.addParameter(new Parameter("xquery", QUERY));
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(XML_CONTENT);
        given(request.getBody()).willReturn(BODY);
        given(queryExpressionEvaluator.evaluateXQuery(BODY, QUERY)).willReturn(XML_DECLARATION + "true");
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testCheckConditionWhenContentTypeIsFastinfosetShouldReturnTrue() throws SaxonApiException {
        //GIVEN
        parameterList.addParameter(new Parameter("xquery", QUERY));
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(FASTINFOSET_CONTENT);
        given(request.getBody()).willReturn(BODY);
        given(queryExpressionEvaluator.evaluateXQuery(BODY, QUERY)).willReturn(XML_DECLARATION + "true");
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testCheckConditionWhenResultIsANodeShouldReturnTrue() throws SaxonApiException {
        //GIVEN
        parameterList.addParameter(new Parameter("xquery", QUERY));
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(XML_CONTENT);
        given(request.getBody()).willReturn(BODY);
        given(queryExpressionEvaluator.evaluateXQuery(BODY, QUERY)).willReturn(XML_DECLARATION + "<ns5:HotelID >179</ns5:HotelID>");
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertEquals(actual, true);
    }

    @Test
    public void testCheckConditionWhenResultIsANodeShouldLogResult() throws SaxonApiException {
        //GIVEN
        parameterList.addParameter(new Parameter("xquery", QUERY));
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(XML_CONTENT);
        given(request.getBody()).willReturn(BODY);
        given(queryExpressionEvaluator.evaluateXQuery(BODY, QUERY)).willReturn(XML_DECLARATION + "<ns5:HotelID >179</ns5:HotelID>");
        Whitebox.setInternalState(underTest, "logger", logger);
        //WHEN
        underTest.checkCondition(request, parameterList);
        //THEN
        verify(logger).debug(Mockito.anyString(), Mockito.any(SaxonApiException.class));
    }

    @Test
    public void testCheckConditionWhenContentTypeIsNullShouldReturnFalse() {
        //GIVEN
        parameterList.addParameter(new Parameter("xquery", QUERY));
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(null);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testCheckConditionWhenContentTypeIsTextShouldReturnFalse() {
        //GIVEN
        parameterList.addParameter(new Parameter("xquery", QUERY));
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn("text/plain");
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertEquals(actual, false);
    }

    @Test(expectedExceptions = ConditionEvaluationFailedException.class)
    public void testCheckConditionWhenParameterSizeIsZeroShouldThrowException() {
        //GIVEN in setUp
        //WHEN
        underTest.checkCondition(request, parameterList);
        //THEN exception should be thrown
    }

    @Test(expectedExceptions = ConditionEvaluationFailedException.class)
    public void testCheckConditionWhenParameterSizeIsTwoShouldThrowException() {
        //GIVEN
        parameterList.addParameter(new Parameter("xquery", QUERY));
        parameterList.addParameter(new Parameter("xquery1", QUERY));
        //WHEN
        underTest.checkCondition(request, parameterList);
        //THEN exception should be thrown
    }

    @Test(expectedExceptions = ConditionEvaluationFailedException.class)
    public void testCheckConditionWhenSyntaxExceptionShouldThrowException() throws SaxonApiException {
        //GIVEN
        parameterList.addParameter(new Parameter("xquery", QUERY));
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(FASTINFOSET_CONTENT);
        given(request.getBody()).willReturn(BODY);
        given(queryExpressionEvaluator.evaluateXQuery(BODY, QUERY)).willThrow(new SaxonApiException("exception"));
        //WHEN
        underTest.checkCondition(request, parameterList);
        //THEN exception should be thrown
    }
}
