package com.epam.wilma.gepard.test.localhost;
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
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.Inet4Address;

/**
 * Tests the localhost request blocking functionality.
 *
 * @author Adam_Csaba_Kiraly
 */
@TestClass(id = "BlockLocalhostUsage", name = "Localhost request blocking")
public class BlockLocalhostUsageTest extends WilmaTestCase {

    private static final String REQUEST = "resources/example2.xml";

    private String tcName = getDataDrivenTestParameter("PAR0");
    private String tcTargetUrl = getDataDrivenTestParameter("PAR1");
    private String tcState = getDataDrivenTestParameter("PAR2");
    private String tcStubOperationState = getDataDrivenTestParameter("PAR3");
    private String tcExpectedCode = getDataDrivenTestParameter("PAR4");

    @Test
    public void testLocalhostBlocking() throws Exception {
        //given
        String actualHost = Inet4Address.getLocalHost().getHostAddress();
        logComment("Test Environment IP: " + actualHost);
        if (!tcTargetUrl.contains(actualHost) && !tcTargetUrl.contains("127.0.0.1")) {
            //in case we are in foreign test environment, or not testing the localhost, this TC should be N/A
            naTestCase("Running on unknown test environment, marking this Test as N/A.");
        }
        setOperationModeTo(tcStubOperationState);
        setLocalhostBlockingTo(tcState);
        RequestParameters requestParameters = createRequestParameters();
        //when
        callWilmaWithGetMethod(requestParameters);
        //then
        checkResponseCode(Integer.valueOf(tcExpectedCode));
    }

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = tcTargetUrl;
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/xml";
        String acceptHeader = "application/xml";
        String contentEncoding = "no";
        String acceptEncoding = "no";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(REQUEST)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding);
    }
}
