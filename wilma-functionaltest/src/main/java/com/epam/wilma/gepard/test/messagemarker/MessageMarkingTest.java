package com.epam.wilma.gepard.test.messagemarker;
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
import org.apache.commons.httpclient.Header;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Tests the Message Marking Feature.
 *
 * Created by tkohegyi.
 */
@TestClass(id = "MessageMarking", name = "Message Marking Test")
public class MessageMarkingTest extends WilmaTestCase {
    private static final String EXAMPLE_2 = "resources/example2.xml";
    private static final String WILMA_LOGGER_ID = "Wilma-Logger-ID";

    @Test
    public void testMessageMarkingOn() throws Exception {
        //given
        setMessageMarkingTo("on");
        additionalSetup();
        RequestParameters requestParameters = createRequestParameters();
        //when and then
        ResponseHolder responseHolder = callWilmaWithPostMethod(requestParameters); //send request, receive response
        Header header = responseHolder.getHttpPost().getResponseHeader(WILMA_LOGGER_ID);
        setMessageMarkingTo("off");
        Assert.assertNotNull("Missing Wilma message mark.", header);
        Assert.assertNotNull("Missing Wilma message mark.", header.getValue());
    }

    @Test
    public void testMessageMarkingOff() throws Exception {
        //given
        setMessageMarkingTo("off");
        additionalSetup();
        RequestParameters requestParameters = createRequestParameters();
        //when and then
        ResponseHolder responseHolder = callWilmaWithPostMethod(requestParameters); //send request, receive response
        Header header = responseHolder.getHttpPost().getResponseHeader(WILMA_LOGGER_ID);
        setMessageMarkingTo("off");
        Assert.assertNull("Wilma message mark found unexpectedly.", header);
    }

    private void additionalSetup() throws Exception {
        clearAllOldStubConfigs();
        setLocalhostBlockingTo("off");
        setOriginalRequestMessageFromFile(EXAMPLE_2);
        setExpectedResponseMessageFromFile("resources/uc3_1TestResponse.txt");
    }

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaTestServerUrl();
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/xml";
        String acceptHeader = "application/xml";
        String contentEncoding = "gzip";
        String acceptEncoding = "gzip";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(EXAMPLE_2)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding);
    }

}
