package com.epam.wilma.stubconfig.condition.checker.general.header;
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
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

/**
 * Provides unit tests for the class {@link HeaderParameterChecker}.
 *
 * @author Tunde_Kovacs
 */
public class HeaderParameterCheckerTest {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_ENCODING_HEADER = "Content-Encoding";
    private static final String XML_CONTENT = "application/xml";
    private static final String GZIP_CONTENT = "gzip";

    @Mock
    private WilmaHttpRequest request;

    private HeaderParameterChecker underTest;

    private ParameterList parameterList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new HeaderParameterChecker();
        parameterList = new ParameterList();
    }

    @Test
    public void testCheckConditionWhenConditionIsFulfilledShouldReturnTrue() {
        //GIVEN
        parameterList.addParameter(new Parameter(CONTENT_TYPE_HEADER, XML_CONTENT));
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(XML_CONTENT);
        given(request.getBody()).willReturn(XML_CONTENT);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertTrue(actual);
    }

    @Test
    public void testCheckConditionWhenMultipleConditionsAreFulfilledShouldReturnTrue() {
        //GIVEN
        parameterList.addParameter(new Parameter(CONTENT_TYPE_HEADER, XML_CONTENT));
        parameterList.addParameter(new Parameter(CONTENT_ENCODING_HEADER, GZIP_CONTENT));
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(XML_CONTENT);
        given(request.getHeader(CONTENT_ENCODING_HEADER)).willReturn(GZIP_CONTENT);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertTrue(actual);
    }

    @Test
    public void testCheckConditionWhenOneConditionIsFulfilledShouldReturnFalse() {
        //GIVEN
        parameterList.addParameter(new Parameter(CONTENT_TYPE_HEADER, XML_CONTENT));
        parameterList.addParameter(new Parameter(CONTENT_ENCODING_HEADER, GZIP_CONTENT));
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(XML_CONTENT);
        given(request.getHeader(CONTENT_ENCODING_HEADER)).willReturn("something else");
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionWhenOneConditionIsNotFulfilledShouldReturnFalse() {
        //GIVEN
        parameterList.addParameter(new Parameter(CONTENT_TYPE_HEADER, XML_CONTENT));
        parameterList.addParameter(new Parameter(CONTENT_ENCODING_HEADER, GZIP_CONTENT));
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn("something else");
        given(request.getHeader(CONTENT_ENCODING_HEADER)).willReturn(GZIP_CONTENT);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionWhenNoConditionIsFulfilledShouldReturnFalse() {
        //GIVEN
        parameterList.addParameter(new Parameter(CONTENT_TYPE_HEADER, XML_CONTENT));
        parameterList.addParameter(new Parameter(CONTENT_ENCODING_HEADER, GZIP_CONTENT));
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn("something else");
        given(request.getHeader(CONTENT_ENCODING_HEADER)).willReturn("something else");
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionWhenParameterMapIsEmptyShouldReturnFalse() {
        //GIVEN in setUp
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testCheckConditionWhenThereIsNoHeaderWithGivenParameterShouldReturnFalse() {
        //GIVEN
        parameterList.addParameter(new Parameter(CONTENT_TYPE_HEADER, XML_CONTENT));
        given(request.getHeader(CONTENT_TYPE_HEADER)).willReturn(null);
        //WHEN
        boolean actual = underTest.checkCondition(request, parameterList);
        //THEN
        assertFalse(actual);
    }

}
