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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

/**
 * Tests for {@link XmlMessageTypeChecker} class.
 *
 * @author Tamas_Bihari
 */
public class XmlMessageTypeCheckerTest {

    private static final String HEADER_VAL_FAST_INFOSET = "application/fastinfoset";
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        parameterList = new ParameterList();
    }

    @Test
    public void testCheckConditionShouldReturnFalseWhenContentTypeIsNotXMLType() {
        //GIVEN
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(TEXT_PLAIN);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionShouldReturnFalseWhenContentTypeIsNullWhenHttpGetProcessing() {
        //GIVEN
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(null);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionShouldThrowExceptionWhenNoParameterIsDefinedInConfig() {
        Assertions.assertThrows(ConditionEvaluationFailedException.class, () -> {
            //GIVEN
            given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(HEADER_VAL_XML);
            //WHEN
            underTest.checkCondition(request, parameterList);
            //THEN exception is thrown
        });
    }

    @Test
    public void testCheckConditionShouldThrowExceptionWhenTooMuchParametersAreDefinedInConfig() {
        Assertions.assertThrows(ConditionEvaluationFailedException.class, () -> {
            //GIVEN
            given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(HEADER_VAL_FAST_INFOSET);
            parameterList.addParameter(new Parameter(TEXT_PLAIN, HEADER_VAL_XML));
            parameterList.addParameter(new Parameter(CONTENT_TYPE_HEADER, HEADER_VAL_XML));
            //WHEN
            underTest.checkCondition(request, parameterList);
            //THEN exception is thrown
        });
    }

    @Test
    public void testCheckConditionShouldThrowExceptionWhenXQueryEvaluationFailed() {
        Assertions.assertThrows(ConditionEvaluationFailedException.class, () -> {
            //GIVEN
            given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(HEADER_VAL_XML);
            parameterList.addParameter(new Parameter(TEXT_PLAIN, HEADER_VAL_XML));
            given(xQueryEvaluator.evaluateXQuery(anyString(), anyString())).willThrow(new SaxonApiException(""));
            given(request.getBody()).willReturn(TEXT_PLAIN);
            //WHEN
            underTest.checkCondition(request, parameterList);
            //THEN exception is thrown
        });
    }

    @Test
    public void testCheckConditionShouldReturnFalseWhenQueryResultFalse() throws SaxonApiException {
        //GIVEN
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(HEADER_VAL_XML);
        parameterList.addParameter(new Parameter(TEXT_PLAIN, HEADER_VAL_XML));
        given(xQueryEvaluator.evaluateXQuery(anyString(), anyString())).willReturn("false");
        given(request.getBody()).willReturn(TEXT_PLAIN);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionShouldReturnTrueWhenQueryResultFalse() throws SaxonApiException {
        //GIVEN
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(HEADER_VAL_XML);
        parameterList.addParameter(new Parameter(TEXT_PLAIN, HEADER_VAL_XML));
        given(xQueryEvaluator.evaluateXQuery(anyString(), anyString())).willReturn("true");
        given(request.getBody()).willReturn(TEXT_PLAIN);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertTrue(actual);
    }

}
