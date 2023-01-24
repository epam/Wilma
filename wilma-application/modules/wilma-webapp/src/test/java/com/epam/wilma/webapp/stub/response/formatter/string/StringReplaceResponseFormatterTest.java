package com.epam.wilma.webapp.stub.response.formatter.string;
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

import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Provides unit tests for the class {@link StringReplaceResponseFormatter}.
 *
 * @author Tunde_Kovacs
 */
public class StringReplaceResponseFormatterTest {

    private byte[] templateResource;
    private ParameterList parameterList;

    @Mock
    private WilmaHttpRequest request;

    private HttpServletResponse response;
    private StringReplaceResponseFormatter underTest;

    @BeforeEach
    public void setUp() {
        parameterList = new ParameterList();
        underTest = new StringReplaceResponseFormatter();

    }

    @Test
    public void testFormatTemplateShouldReplaceString() throws Exception {
        //GIVEN
        templateResource = "this is a test template".getBytes();
        parameterList.addParameter(new Parameter("t", "blabla"));
        //WHEN
        byte[] actual = underTest.formatResponse(request, response, templateResource, parameterList);
        //THEN
        String actualString = new String(actual);
        String expected = "blablahis is a blablaesblabla blablaemplablablae";
        assertEquals(expected, actualString);
    }

    @Test
    public void testFormatTemplateWhenMultipleParametersShouldReplaceString() throws Exception {
        //GIVEN
        templateResource = "this is a test template".getBytes();
        parameterList.addParameter(new Parameter("t", "blabla"));
        parameterList.addParameter(new Parameter("is", "is not"));
        parameterList.addParameter(new Parameter("a", "o"));
        //WHEN
        byte[] actual = underTest.formatResponse(request, response, templateResource, parameterList);
        //THEN
        String actualString = new String(actual);
        String expected = "bloblohis not is not o blobloesbloblo blobloemploblobloe";
        assertEquals(expected, actualString);
    }

    @Test
    public void testFormatTemplateWhenNoParametersShouldDoNothing() throws Exception {
        //GIVEN
        templateResource = "this is a test template".getBytes();
        //WHEN
        byte[] actual = underTest.formatResponse(request, response, templateResource, parameterList);
        //THEN
        assertEquals(actual, templateResource);
    }
}
