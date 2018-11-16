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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.epam.wilma.gepard.WilmaTestCase;
import com.epam.wilma.gepard.testclient.MultiStubRequestParameters;
import com.epam.wilma.gepard.testclient.RequestParameters;

/**
 * Base class for the various sequence handling functional tests.
 * @author Adam_Csaba_Kiraly
 *
 */
public abstract class SequenceHandlingTestBase extends WilmaTestCase {

    protected static final String TARGET_URL = "config/public/version";
    protected static final String SESSION_ID_NAME = "SESSION-ID";
    protected static final String SESSION_ID_VALUE = "abc";

    /**
     * Clear all sequences storied within Wilma.
     * Clearing performed by turning off sequence handling at Wilma (this will clear the existing sequences),
     * then turning sequence handling back.
     *
     * @throws Exception in case of error
     */
    public void clearSequences() throws Exception {
        turnOffSequenceHandling();
        turnOnSequenceHandling();
    }

    /**
     * Turns off Wilma sequence handling.
     *
     * @throws Exception in case of any error.
     */
    public void turnOffSequenceHandling() throws Exception {
        logStep("Turn Wilma Sequence handling: off");
        callWilmaWithGetMethod(createRequestParametersForSequenceHandlingMode("off"));
    }

    /**
     * Turns on Wilma sequence handling.
     *
     * @throws Exception in case of any error.
     */
    public void turnOnSequenceHandling() throws Exception {
        logStep("Turn Wilma Sequence handling: on");
        callWilmaWithGetMethod(createRequestParametersForSequenceHandlingMode("on"));
    }

    /**
     * Prepare request parameters for setting Sequence handling mode on/off.
     *
     * @param option whether to turn sequence handling on or off
     * @return with teh prepared request parameters
     */
    protected RequestParameters createRequestParametersForSequenceHandlingMode(final String option) {
        InputStream requestBody = new ByteArrayInputStream("".getBytes());
        String testServerUrl = getWilmaInternalUrl() + "config/admin/sequence/" + option;
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "text/plain";
        String acceptHeader = "xml";
        String contentEncoding = "no";
        String acceptEncoding = "no";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(false).wilmaHost(wilmaHost).wilmaPort(wilmaPort).requestInputStream(requestBody)
                .contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding).acceptEncoding(acceptEncoding);
    }

    /**
     * Update the Default group of Stub Configuration.
     *
     * @param resourcePath the stub descriptor xml to be used when updating the default group
     * @throws Exception in case of error
     */
    public void updateDefaultGroupOfStubConfiguration(final String resourcePath) throws Exception {
        MultiStubRequestParameters multiStubRequestParameters = createMultiStubRequestParameters("Default", resourcePath);
        callWilmaWithPostMethod(multiStubRequestParameters);
    }

    /**
     * Create request parameters for sequence tests.
     *
     * @param groupname is the stub config group name
     * @param resourcePath is the config xml to upload
     * @return with the prepared request
     * @throws FileNotFoundException in case of error
     */
    protected MultiStubRequestParameters createMultiStubRequestParameters(final String groupname, final String resourcePath) throws FileNotFoundException {
        String testServerUrl = getWilmaDropStubConfigUrl();
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/xml";
        String acceptHeader = "application/json";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new MultiStubRequestParameters().testServerUrl(testServerUrl).useProxy(false).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(resourcePath)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding).groupName(groupname);
    }
}
