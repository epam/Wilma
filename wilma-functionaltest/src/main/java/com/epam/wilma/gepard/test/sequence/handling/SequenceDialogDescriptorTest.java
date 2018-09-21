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

import com.epam.gepard.annotations.TestClass;
import com.epam.wilma.gepard.testclient.RequestParameters;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Functional test for dialog descriptor reference usage.
 *
 * @author Adam_Csaba_Kiraly
 */
@TestClass(id = "SequenceHandling", name = "Sequence DialogDescriptor - tests the usage of a referenced dialog descriptor")
public class SequenceDialogDescriptorTest extends SequenceHandlingTestBase {

    private static final String EXPECTED_RESPONSE_FILE_LOCATION = "resources/sequence/dialogDescriptorReferenceExpectedResponse.txt";
    private static final String STUB_CONFIG_LOCATION = "resources/sequence/sequenceDialogDescriptorStubConfig.json";
    private static final String JAR_FILE_LOCATION = "resources/sequence/message-sequence.jar";

    @Test
    public void testSequenceHandling() throws Exception {
        clearSequences();
        uploadJarToWilma("message-sequence.jar", JAR_FILE_LOCATION);
        uploadStubConfigToWilma(STUB_CONFIG_LOCATION);

        RequestParameters firstRequestParameter = createRequestParametersWithBody("something");
        setExpectedResponseMessageFromFile(EXPECTED_RESPONSE_FILE_LOCATION);
        callWilmaWithPostMethodAndAssertResponse(firstRequestParameter);

        updateDefaultGroupOfStubConfiguration(STUB_CONFIG_LOCATION);
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
}
