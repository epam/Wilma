package com.epam.wilma.gepard.test.basic;

/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.epam.gepard.annotations.TestClass;
import com.epam.gepard.annotations.TestParameter;
import com.epam.wilma.gepard.WilmaTestCase;
import com.epam.wilma.gepard.testclient.RequestParameters;
import com.epam.wilma.gepard.testclient.ResponseHolder;

/**
 * Test the JSON path based condition checking and template formatting.
 *
 * @author Balazs_Berkes
 */
@TestClass(id = "JsonStubbing", name = "Json stubbing")
public class JsonTest extends WilmaTestCase {

    private static final String STUB_CONFIG_XML = "resources/stub/JSON/stubConfigWithJson.xml";
    private static final String REQUEST_JSON = "resources/stub/JSON/request.json";
    private static final String EXPECTED_JSON = "resources/stub/JSON/response.json";
    private static final String TEMPLATE_JSON = "resources/stub/JSON/template.json";
    private static final String TEMPLATE_FILE_NAME = "template.json";

    @TestParameter(id = "PAR0")
    private String tcName;
    @TestParameter(id = "PAR1")
    private String tcContentType;
    @TestParameter(id = "PAR2")
    private String tcAcceptHeader;

    /**
     * A, send the req1-xml message to Apache (use wilma as proxy), but when the
     * this request arrives to Wilma, Wilma sends resp-xml1 back as response
     * instead of forwarding te request to Apache. (don't forget to log the
     * messages)
     *
     * @throws Exception
     */
    public void testJsonStubbing() throws Exception {
        uploadTemplateToWilma(TEMPLATE_FILE_NAME, TEMPLATE_JSON);
        uploadStubConfigToWilma(STUB_CONFIG_XML);
        setOriginalRequestMessageFromFile(REQUEST_JSON);
        setExpectedResponseMessageFromFile(EXPECTED_JSON);
        RequestParameters requestParameters = createRequestParameters();
        ResponseHolder actual = callWilmaWithPostMethod(requestParameters);
        assertJsonContent(actual.getResponseMessage());
    }

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaTestServerUrl();
        String wilmaHost = getClassData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getClassData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/" + tcContentType;
        String acceptHeader = "application/" + tcAcceptHeader;
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .xmlIS(new FileInputStream(REQUEST_JSON)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding("").acceptEncoding("");
    }
}
