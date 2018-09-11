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

import static org.testng.Assert.assertEquals;

import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.webapp.domain.exception.ResponseFormattingFailedException;

import javax.servlet.http.HttpServletResponse;

/**
 * Provides unit tests for the class {@link StringRegexpReplaceResponseFormatter}.
 * @author Tunde_Kovacs
 *
 */
public class StringRegexpReplaceResponseFormatterTest {

    private byte[] templateResource;
    private ParameterList parameterList;

    @Mock
    private WilmaHttpRequest request;

    private HttpServletResponse response;
    private StringRegexpReplaceResponseFormatter underTest;

    @BeforeMethod
    public void setUp() {
        parameterList = new ParameterList();
        underTest = new StringRegexpReplaceResponseFormatter();

    }

    @Test
    public void testFormatTemplateShouldReplaceRegexp() throws Exception {
        //GIVEN
        templateResource = "123 this is a test template 123".getBytes();
        parameterList.addParameter(new Parameter("[0-2]+", "HELLO"));
        //WHEN
        byte[] actual = underTest.formatResponse(request, response, templateResource, parameterList);
        //THEN
        assertEquals(actual, "HELLO3 this is a test template HELLO3".getBytes());
    }

    @Test
    public void testFormatTemplateWhenMultipleParametersShouldReplaceString() throws Exception {
        //GIVEN
        templateResource = "123 this is a test template 123".getBytes();
        parameterList.addParameter(new Parameter("[0-2]+", "HELLO"));
        parameterList.addParameter(new Parameter("is", "is not"));
        //WHEN
        byte[] actual = underTest.formatResponse(request, response, templateResource, parameterList);
        //THEN
        assertEquals(actual, "HELLO3 this not is not a test template HELLO3".getBytes());
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

    @Test(expectedExceptions = ResponseFormattingFailedException.class)
    public void testFormatTemplateWhenRegexpSyntaxIncorrectShouldThrowException() throws Exception {
        //GIVEN
        templateResource = "123 this is a test template 123".getBytes();
        parameterList.addParameter(new Parameter("{0-2}+", "HELLO"));
        //WHEN
        underTest.formatResponse(request, response, templateResource, parameterList);
        //THEN
    }

}
