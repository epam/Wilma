package com.epam.wilma.gepard.test.sequence.handling;
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

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.epam.gepard.annotations.TestClass;
import com.epam.wilma.gepard.testclient.RequestParameters;
import org.junit.Test;

/**
 * Tests that multiple sequences of a sequence descriptor can be built simultaneously.
 *
 * Sends four requests. The first and third request belongs to 'sequence one' while the
 * second and fourth request belongs to 'sequence two'. On the third request checks if
 * 'sequence one' is correct and on the fourth request checks if 'sequence two' is correct.
 *
 * @author Adam_Csaba_Kiraly
 */
@TestClass(id = "SequenceHandling", name = "Multiple Sequences - 1st and 3rd request belongs to one sequence; 2nd and 4th belongs to another sequence")
public class MultipleSequencesTest extends SequenceHandlingTestBase {
    private static final String JAR_FILE_LOCATION = "resources/sequence/message-sequence.jar";
    private static final String STUB_CONFIG_LOCATION = "resources/sequence/firstBodyLetterStubConfig.json";
    private static final String EXPECTED_FIRST_RESPONSE_FILE_LOCATION = "resources/sequence/firstBodyLetterExpectedResponse1.txt";
    private static final String EXPECTED_SECOND_RESPONSE_FILE_LOCATION = "resources/sequence/firstBodyLetterExpectedResponse2.txt";

    @Test
    public void testSequenceHandling() throws Exception {
        clearSequences();
        uploadJarToWilma("message-sequence.jar", JAR_FILE_LOCATION);
        uploadStubConfigToWilma(STUB_CONFIG_LOCATION);
        sendRequestAndAssertMessage("a1", "welcome");
        sendRequestAndAssertMessage("b1", "welcome");
        sendRequestAndAssertMessageFromFile("a2", EXPECTED_FIRST_RESPONSE_FILE_LOCATION);
        sendRequestAndAssertMessageFromFile("b2", EXPECTED_SECOND_RESPONSE_FILE_LOCATION);

        updateDefaultGroupOfStubConfiguration(STUB_CONFIG_LOCATION);
    }

    private void sendRequestAndAssertMessage(final String requestBody, final String expectedAnswer) throws Exception {
        logComment("Sending Request, Assert Message...");
        RequestParameters requestParameters = createRequestParameters(requestBody);
        setExpectedResponseMessage(expectedAnswer);
        callWilmaWithPostMethodAndAssertResponse(requestParameters);
    }

    private void sendRequestAndAssertMessageFromFile(final String requestBody, final String expectedAnswerFileLocation) throws Exception {
        RequestParameters requestParameters = createRequestParameters(requestBody);
        setExpectedResponseMessageFromFile(expectedAnswerFileLocation);
        callWilmaWithPostMethodAndAssertResponse(requestParameters);
    }

    protected RequestParameters createRequestParameters(final String text) throws FileNotFoundException {
        InputStream requestBody = new ByteArrayInputStream(text.getBytes());
        String testServerUrl = getWilmaVersionInfoUrl();
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "text/plain";
        String acceptHeader = "xml";
        String contentEncoding = "no";
        String acceptEncoding = "no";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort).requestInputStream(requestBody)
                .contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding).acceptEncoding(acceptEncoding);
    }
}
