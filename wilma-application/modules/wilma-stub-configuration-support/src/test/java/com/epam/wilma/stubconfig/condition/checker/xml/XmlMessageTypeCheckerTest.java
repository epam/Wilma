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
import static org.mockito.Matchers.anyString;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
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
 * Tests for {@link XmlMessageTypeChecker} class.
 * @author Tamas_Bihari
 *
 */
public class XmlMessageTypeCheckerTest {

    private static final String HEADER_VAL_FASTINFOSET = "application/fastinfoset";
    private static final String HEADER_VAL_XML = "application/xml";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String TEXT_PLAIN = "text/plain";

    private ParameterList parameterList;

    @Mock
    private XQueryExpressionEvaluator xQueryEvaluator;
    @Mock
    private WilmaHttpRequest request;

    @InjectMocks
    private XmlMessageTypeChecker underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parameterList = new ParameterList();
    }

    @Test
    public void testCheckConditionShouldReturnFalseWhenContentTypeIsNotXMLType() {
        //GIVEN
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(TEXT_PLAIN);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //TNEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionShouldReturnFalseWhenContentTypeIsNullWhenHttpGetProcessing() {
        //GIVEN
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(null);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //TNEN
        assertFalse(actual);
    }

    @Test(expectedExceptions = ConditionEvaluationFailedException.class)
    public void testCheckConditionShouldThrowExceptionWhenNoParameterIsDefinedInConfig() {
        //GIVEN
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(HEADER_VAL_XML);
        //WHEN
        underTest.checkCondition(request, parameterList);
        //TNEN exception is thrown
    }

    @Test(expectedExceptions = ConditionEvaluationFailedException.class)
    public void testCheckConditionShouldThrowExceptionWhenTooMuchParametersAreDefinedInConfig() {
        //GIVEN
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(HEADER_VAL_FASTINFOSET);
        parameterList.addParameter(new Parameter(TEXT_PLAIN, HEADER_VAL_XML));
        parameterList.addParameter(new Parameter(CONTENT_TYPE_HEADER, HEADER_VAL_XML));
        //WHEN
        underTest.checkCondition(request, parameterList);
        //TNEN exception is thrown
    }

    @Test(expectedExceptions = ConditionEvaluationFailedException.class)
    public void testCheckConditionShouldThrowExceptionWhenXQueryEvaulationFailed() throws SaxonApiException {
        //GIVEN
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(HEADER_VAL_XML);
        parameterList.addParameter(new Parameter(TEXT_PLAIN, HEADER_VAL_XML));
        given(xQueryEvaluator.evaluateXQuery(anyString(), anyString())).willThrow(new SaxonApiException(""));
        //WHEN
        underTest.checkCondition(request, parameterList);
        //TNEN exception is thrown
    }

    @Test
    public void testCheckConditionShouldReturnFalseWhenQueryResultFalse() throws SaxonApiException {
        //GIVEN
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(HEADER_VAL_XML);
        parameterList.addParameter(new Parameter(TEXT_PLAIN, HEADER_VAL_XML));
        given(xQueryEvaluator.evaluateXQuery(anyString(), anyString())).willReturn("false");
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //TNEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionShouldReturnTrueWhenQueryResultFalse() throws SaxonApiException {
        //GIVEN
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(HEADER_VAL_XML);
        parameterList.addParameter(new Parameter(TEXT_PLAIN, HEADER_VAL_XML));
        given(xQueryEvaluator.evaluateXQuery(anyString(), anyString())).willReturn("true");
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //TNEN
        assertTrue(actual);
    }

}
