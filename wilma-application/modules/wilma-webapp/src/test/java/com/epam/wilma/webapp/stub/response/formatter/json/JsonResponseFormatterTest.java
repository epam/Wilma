package com.epam.wilma.webapp.stub.response.formatter.json;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for {@code JsonResponseFormatter}.
 *
 * @author Balazs_Berkes
 */
public class JsonResponseFormatterTest {

    private WilmaHttpRequest wilmaRequest;

    private JsonResponseFormatter underTest;

    private HttpServletResponse response;

    @BeforeEach
    public void setup() {
        underTest = new JsonResponseFormatter();
    }

    @Test
    public void testFormatTemplate() {
        String request = refundRequest();
        String template = refundTemplate();
        String expected = refundExpectedResponse();

        givenWilmaRequestWithBody(request);

        byte[] formattedResponse = underTest.formatResponse(wilmaRequest, response, template.getBytes(), null);

        assertEquals(whitespaceFree(expected), whitespaceFree(new String(formattedResponse)));
    }

    private String whitespaceFree(String string) {
        return string.replaceAll("\\s", "");
    }

    private void givenWilmaRequestWithBody(String requestBody) {
        wilmaRequest = new WilmaHttpRequest();
        wilmaRequest.setBody(requestBody);
    }


    private String refundRequest() {
        return "{\n"
                + "   \"request\":[\n"
                + "      {\n"
                + "         \"id\":\"103\",\n"
                + "         \"number\":\"1\",\n"
                + "         \"name\":\"John Smith\",\n"
                + "         \"pet\":\"cat\",\n"
                + "         \"bool\":true,\n"
                + "         \"num\":7,\n"
                + "         \"email\":\"a@gmail.com\"\n"
                + "      }"
                + "   ]\n"
                + "}";
    }

    private String refundTemplate() {
        return "{\n"
                + "   \"id\":\"2\",\n"
                + "   \"response\":[\n"
                + "      {\n"
                + "         \"id\":\"$.request[0].id\",\n"
                + "         \"number\":\"$.request[0].number\",\n"
                + "         \"pet\":\"$.request[0].pet\",\n"
                + "         \"bool2\": \"$.request[0].bool\",\n"
                + "         \"num2\": \"$.request[0].num\"\n"
                + "      }\n"
                + "   ]\n"
                + "}";
    }

    private String refundExpectedResponse() {
        return "{\n"
                + "   \"id\":\"2\",\n"
                + "   \"response\":[\n"
                + "      {\n"
                + "         \"id\":\"103\",\n"
                + "         \"number\":\"1\",\n"
                + "         \"pet\":\"cat\",\n"
                + "         \"bool2\":true,\n"
                + "         \"num2\":7\n"
                + "      }\n"
                + "   ]"
                + "}";
    }

    private String gotRequest() {
        return "{\n" + "   \"characters\":[\n" + "      {\n"
                + "         \"bool\":true,\n"
                + "         \"num\": 7,\n"
                + "         \"name\":\"Jaime Lannister\",\n"
                + "         \"title\":\"The Kingslayer\",\n"
                + "         \"location\":\"King's Landing\"\n" + "      },\n"
                + "      {\n" + "         \"name\":\"Arya Stark\",\n"
                + "         \"title\":\"Cat of the Canals\",\n"
                + "         \"location\":\"Unknown\"\n" + "      }\n"
                + "   ],\n" + "   \"story\":\"Song of Ice and Fire\"\n" + "}";
    }

    private String gotTemplate() {
        return "{\n" + "   \"yourFavorites\":{\n"
                + "      \"theFavorite\":\"$.characters[0].name\",\n"
                + "      \"secondFavorite\":\"$.characters[1].name\"\n"
                + "   }\n" + "}";
    }

    private String gotExpectedResponse() {
        return "{\n" + "   \"yourFavorites\":{\n"
                + "      \"theFavorite\":\"Jaime Lannister\",\n"
                + "      \"secondFavorite\":\"Arya Stark\"\n" + "   }\n" + "}";
    }
}
