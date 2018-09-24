package com.epam.wilma.gepard.test.sequence.handling.live;
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
import com.epam.wilma.gepard.test.sequence.handling.SequenceHandlingTestBase;
import com.epam.wilma.gepard.testclient.RequestParameters;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;

/**
 * Tests the sequence handling.
 * <p/>
 * Sends a request gets a JSON answer then sends two requests and expects a
 * value from this JSON answer to appear in the XML responses for these requests.
 *
 * @author Adam_Csaba_Kiraly
 */
@TestClass(id = "SequenceHandling", name = "Session JSON to XML formatting")
public class SequenceHandlingTestJsonToXml extends SequenceHandlingTestBase {

    private static final String ORIGINAL_REQUEST = "resources/sequence/live/req/OriginalRequest.xml";
    private static final String JSON_REQUEST = "resources/sequence/live/req/JsonRequest.json";
    private static final String MAGIC_REQUEST = "resources/sequence/live/req/MagicRequest.xml";
    private static final String EXPECTED_RESPONSE_FILE = "resources/sequence/live/expectedResponseForJsonToXml.txt";
    private static final String STUB_CONFIG_LOCATION = "resources/sequence/live/stubConfigJsonToXml.json";

    /**
     * Test the sequence handling part by using sessions.
     *
     * @throws Exception in case of error
     */
    @Test
    public void testSessionBasedSequence() throws Exception {
        String realTargetUrl = getWilmaInternalUrl() + TARGET_URL;
        clearSequences();
        setOperationModeTo("stub");
        turnOnSequenceHandling();
        setInterceptorModeTo("off");
        Collection<File> templateFiles = FileUtils.listFiles(new File("resources/sequence/live/newtemplates"), new String[]{"xml", "xsl", "json"},
                false);
        for (File file : templateFiles) {
            uploadTemplateToWilma(file.getName(), file.getAbsolutePath());
        }
        uploadStubConfigToWilma(STUB_CONFIG_LOCATION);
        setExpectedResponseMessage("");
        RequestParameters one = createRequestParameters(JSON_REQUEST, "http://cica.fule.net/PancakeLand", "json");
        callWilmaWithPostMethod(one);
        RequestParameters two = createRequestParameters(ORIGINAL_REQUEST, realTargetUrl, "xml");
        callWilmaWithPostMethod(two);
        RequestParameters three = createRequestParameters(MAGIC_REQUEST, realTargetUrl, "xml");
        setExpectedResponseMessageFromFile(EXPECTED_RESPONSE_FILE);
        callWilmaWithPostMethodAndAssertResponse(three);
        setOperationModeTo("wilma");
    }

    /**
     * Prepare request parameter based on parameters, can handle different type (xml, json) of request files.
     *
     * @param resource           to be used in post request
     * @param targetURL          is the target URL
     * @param contentTypePostFix defines the type of the file to send
     * @return with the prepared request parameter
     * @throws FileNotFoundException in case of error
     */
    protected RequestParameters createRequestParameters(final String resource, final String targetURL, final String contentTypePostFix)
        throws FileNotFoundException {
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/" + contentTypePostFix;
        String acceptHeader = "application/" + contentTypePostFix;
        String contentEncoding = "identity";
        String acceptEncoding = "*";
        return new RequestParameters().testServerUrl(targetURL).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(resource)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding).customHeader(SESSION_ID_NAME, SESSION_ID_VALUE);
    }

}
