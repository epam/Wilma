package com.epam.wilma.gepard.test.basic;
/*==========================================================================
Copyright since 2024, EPAM Systems

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
import org.junit.Assert;
import org.junit.Test;

/**
 * Sends get requests to external services via HTTPS calls, checks that the result is OK, 200.
 * @author tkohegyi
 */
@TestClass(id = "ExternalTarget - Proxy - SSL", name = "Proxy Answers")
public class ExternalTargetProxySslBehaviorTest extends WilmaTestCase {

    private String tcName = getDataDrivenTestParameter("PAR0");
    private String tcTargetURI = getDataDrivenTestParameter("PAR1");

    @Test
    public void testExternalTargetViaProxySSL() throws Exception {
        //given
        clearAllOldStubConfigs();
        setLocalhostBlockingTo("off");
        setOperationModeTo("wilma");
        RequestParameters requestParameters = createRequestParameters(tcTargetURI);
        //when
        ResponseHolder response = callWilmaWithGetMethod(requestParameters); //send request, receive response and check if expected result test is in the result
        //then
        Assert.assertTrue("Response code from External Target is not acceptable: " + response.getResponseCode(),
                response.getResponseCode().startsWith("status code: 200"));
    }

    protected RequestParameters createRequestParameters(String tcTargetURI) {
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String acceptEncoding = "gzip";
        return new RequestParameters().testServerUrl(tcTargetURI).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .acceptEncoding(acceptEncoding).customHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
    }

}
