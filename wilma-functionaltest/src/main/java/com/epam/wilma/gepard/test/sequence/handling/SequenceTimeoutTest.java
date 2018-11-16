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
 * Tests if the timed out sequences are cleared correctly.
 *
 * Uploads a stubconfig with a sequence descriptor that has one milisecond as timeout.
 * Sends a request then asks Wilma to clean up the timed out sequences after that sends another
 * sequence which belongs to the same sequence as the first one then checks if only this second
 * request is present in the sequence.
 *
 * @author Adam_Csaba_Kiraly
 */
@TestClass(id = "SequenceHandling", name = "Sequence Timeout - 1st request should be cleared from sequence before 2nd request")
public class SequenceTimeoutTest extends SequenceHandlingTestBase {

    @Test
    public void testSequenceHandling() throws Exception {
        uploadJarToWilma("message-sequence.jar", "resources/sequence/message-sequence.jar");
        uploadStubConfigToWilma("resources/sequence/timeoutStubConfig.json");

        RequestParameters firstRequestParameter = createRequestParametersWithBody("one");
        setExpectedResponseMessage("welcome");
        callWilmaWithPostMethodAndAssertResponse(firstRequestParameter);

        RequestParameters cleanSequencesRequestParameter = createRequestParameters();
        callWilmaWithGetMethod(cleanSequencesRequestParameter);

        setExpectedResponseMessageFromFile("resources/sequence/timeoutExpectedResponse.txt");
        RequestParameters secondRequestParameter = createRequestParametersWithBody("two");
        callWilmaWithPostMethodAndAssertResponse(secondRequestParameter);

        updateDefaultGroupOfStubConfiguration("resources/sequence/sequenceDialogDescriptorStubConfig.json");
    }

    protected RequestParameters createRequestParametersWithBody(final String text) throws FileNotFoundException {
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

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        InputStream requestBody = new ByteArrayInputStream("".getBytes());
        String testServerUrl = getWilmaInternalUrl() + "config/admin/sequence/cleanup";
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "text/plain";
        String acceptHeader = "xml";
        String contentEncoding = "no";
        String acceptEncoding = "no";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(false).wilmaHost(wilmaHost).wilmaPort(wilmaPort).requestInputStream(requestBody)
                .contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding).acceptEncoding(acceptEncoding);
    }
}
