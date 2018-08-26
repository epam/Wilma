package com.epam.wilma.gepard.test.basic;

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

import com.epam.gepard.annotations.TestClass;
import com.epam.wilma.gepard.WilmaTestCase;
import com.epam.wilma.gepard.testclient.RequestParameters;
import com.epam.wilma.gepard.testclient.ResponseHolder;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Test the JSON path based condition checking and template formatting.
 *
 * @author Tamas_Kohegyi
 */
@TestClass(id = "JsonStubbingWithSchemaCheck", name = "Json stubbing with Schema Check")
public class JsonSchemaTest extends WilmaTestCase {

    private static final String STUB_CONFIG_XML = "resources/stub/JSON/stubConfigWithJsonSchema.xml";
    private static final String SCHEMA_JSON = "resources/stub/JSON/jsonTestSchema.json";
    private static final String SCHEMA_FILE_NAME = "jsonTestSchema.json";
    private static final String GOOD_RESPONSE_FILE = "resources/stub/JSON/responseWithSchema.json";
    private static final String GOOD_RESPONSE_FILE_NAME = "responseWithSchema.json";
    private static final String BAD_RESPONSE_FILE = "resources/stub/JSON/responseWithBadSchema.json";
    private static final String BAD_RESPONSE_FILE_NAME = "responseWithBadSchema.json";

    private String tcName = getDataDrivenTestParameter("PAR0");
    private String tcRequestFile = getDataDrivenTestParameter("PAR1");
    private String tcResponseFile = getDataDrivenTestParameter("PAR2");

    @Before
    public void initSchemaTest() throws Exception {
        uploadTemplateToWilma(SCHEMA_FILE_NAME, SCHEMA_JSON);
        uploadTemplateToWilma(GOOD_RESPONSE_FILE_NAME, GOOD_RESPONSE_FILE);
        uploadTemplateToWilma(BAD_RESPONSE_FILE_NAME, BAD_RESPONSE_FILE);
        uploadStubConfigToWilma(STUB_CONFIG_XML);
    }

    /**
     * A, send re that fits to a schema -> good answer.
     * B, but when the req does no fulfill the expected schema -> bad answer.
     *
     * @throws Exception
     */
    @Test
    public void testJsonStubbing() throws Exception {
        setOriginalRequestMessageFromFile(tcRequestFile);
        setExpectedResponseMessageFromFile(tcResponseFile);
        RequestParameters requestParameters = createRequestParameters();
        ResponseHolder actual = callWilmaWithPostMethod(requestParameters);
        assertJsonContent(actual.getResponseMessage());
    }

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaTestServerUrl();
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/json";
        String acceptHeader = "application/json";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .xmlIS(new FileInputStream(tcRequestFile)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding("").acceptEncoding("");
    }
}
