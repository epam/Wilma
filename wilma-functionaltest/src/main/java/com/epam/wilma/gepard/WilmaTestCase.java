package com.epam.wilma.gepard;

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

import com.epam.gepard.util.Util;
import com.epam.wilma.gepard.test.helper.WilmaConfigurationHelperDecorator;
import com.epam.wilma.gepard.testclient.MultiStubRequestParameters;
import com.epam.wilma.gepard.testclient.RequestParameters;
import com.epam.wilma.gepard.testclient.ResponseHolder;
import com.epam.wilma.gepard.testclient.TestClientBootstrap;
import com.epam.wilma.service.client.WilmaService;
import com.epam.wilma.service.configuration.stub.WilmaStub;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a TestCase, which supports HTML logs, and beforeTestCaseSet
 * and afterTestCaseSet event.
 *
 * @author Tamas Kohegyi
 */
public abstract class WilmaTestCase extends WilmaConfigurationHelperDecorator {

    public static final String STUB_CONFIG_FIRST = "resources/enabledisable/stubConfigFirst.json";
    protected static final String MESSAGE_NOT_YET_AVAILABLE = "Requested file not found.";
    private static final int WAIT_PERIOD_FOR_MESSAGE_LOG = 45;
    private static final int ONE_SECOND = 1000; // in msec

    /**
     * Sends a POST request to Wilma.
     *
     * @param requestParameters is the request
     * @return with the response
     * @throws Exception in case of error
     */
    protected ResponseHolder callWilmaWithPostMethod(final RequestParameters requestParameters) throws Exception {
        return new TestClientBootstrap().bootstrap(this, requestParameters);
    }

    /**
     * Run Wilma Tests those tests multiple stub configurations. For this purpose special request parameters are used.
     *
     * @param requestParameters for multi-stub config behavior
     * @return with the response
     * @throws Exception in case of any error
     */
    protected ResponseHolder callWilmaWithPostMethod(final MultiStubRequestParameters requestParameters) throws Exception {
        return new TestClientBootstrap().bootstrap(this, requestParameters);
    }

    /**
     * Sends a GET request to Wilma.
     *
     * @param requestParameters is the request
     * @return with the response
     * @throws Exception in case of error
     */
    protected ResponseHolder callWilmaWithGetMethod(final RequestParameters requestParameters) throws Exception {
        return new TestClientBootstrap().bootstrapGet(this, requestParameters);
    }

    /**
     * Sends a POST request to Wilma, and checks if the response message withs to the expected one.
     *
     * @param requestParameters is the request
     * @throws Exception in case of problem
     */
    protected void callWilmaWithPostMethodAndAssertResponse(final RequestParameters requestParameters) throws Exception {
        ResponseHolder actual = callWilmaWithPostMethod(requestParameters);
        assertExpectedResultMessage(actual.getResponseMessage());
    }

    /**
     * Gets the Wilma Test Server Url.
     *
     * @return with URL of the test server.
     */
    protected String getWilmaTestServerUrl() {
        return String.format("http://%s:%s/example", getTestClassExecutionData().getEnvironment().getProperty("wilma.test.server.host"),
                getTestClassExecutionData().getEnvironment().getProperty("wilma.test.server.port"));
    }

    /**
     * Gets the Wilma Test SSL Server Url.
     *
     * @return with URL of the ssl test server.
     */
    protected String getWilmaSSLTestServerUrl() {
        return String.format("https://%s:%s/example", getTestClassExecutionData().getEnvironment().getProperty("wilma.test.server.host"),
                getTestClassExecutionData().getEnvironment().getProperty("wilma.test.server.ssl.port"));
    }

    protected String getWilmaTestServerUrlBase() {
        return String.format("http://%s:%s/", getTestClassExecutionData().getEnvironment().getProperty("wilma.test.server.host"),
                getTestClassExecutionData().getEnvironment().getProperty("wilma.test.server.port"));
    }

    /**
     * As the method name says, it will clear all existing Stub Configuration groups from Wilma.
     *
     * @throws Exception in case problem occurs.
     */
    public void clearAllOldStubConfigs() throws Exception {
        logStep("Clear all Stub Configurations.");
        RequestParameters requestParameters = createRequestParametersToGetAllStubDescriptors();
        ResponseHolder responseVersion = callWilmaWithGetMethod(requestParameters);
        String answer = responseVersion.getResponseMessage();
        for (String groupname : getGroupNamesFromJson(answer)) {
            MultiStubRequestParameters multiStubRequestParameters = createDropRequestParameters(groupname);
            callWilmaWithPostMethod(multiStubRequestParameters);
            logComment(groupname + "'s config has been dropped.");
        }
    }

    /**
     * Upload a Stub configuration by using Service API.
     *
     * @param wilmaService           is the object of the service
     * @param wilmaStubConfiguration is the string representation of the stub configuration
     * @return true is success or false when upload failed
     */
    public boolean uploadStubConfiguration(WilmaService wilmaService, WilmaStub wilmaStubConfiguration) {
        Util u = new Util();
        logStep("Uploading stub configuration.");
        logComment("Prepared Stub Configuration Info", u.escapeHTML(wilmaStubConfiguration.toString()));

        boolean b =  wilmaService.uploadStubConfiguration(wilmaStubConfiguration);
        if (b) {
            logComment("Stub configuration upload was successful.");
        } else {
            logComment("Stub configuration upload was failed.");
        }
        return b;
    }

    /**
     * Prepare request parameters to get info on all active stub descriptors of Wilma.
     * Note: maybe a Get method would be better.
     *
     * @return with the prepared request parameters.
     * @throws java.io.FileNotFoundException in case error occurs.
     */
    protected RequestParameters createRequestParametersToGetAllStubDescriptors() throws FileNotFoundException {
        String testServerUrl = getWilmaStubConfigDescriptorsUrl();
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/json";
        String acceptHeader = "application/json";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(false).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding).acceptEncoding(acceptEncoding);
    }

    private List<String> getGroupNamesFromJson(final String response) throws Exception {
        List<String> result = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readValue(response, JsonNode.class);
        JsonNode configs = actualObj.path("configs");
        Iterator<JsonNode> iterator = configs.getElements();
        while (iterator.hasNext()) {
            result.add(iterator.next().path("groupname").getTextValue());
        }
        return result;
    }

    /**
     * Prepare request parameters to drop the actual stub descriptors.
     * Note: maybe a Get method would be better.
     *
     * @param groupname the name of the group to be dropped.
     * @return with request parameters in order to drop the specified stub descriptor group.
     * @throws FileNotFoundException in case error occurs
     */
    protected MultiStubRequestParameters createDropRequestParameters(final String groupname) throws FileNotFoundException {
        String testServerUrl = getWilmaDropStubConfigUrl();
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/json";
        String acceptHeader = "application/json";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new MultiStubRequestParameters().testServerUrl(testServerUrl).useProxy(false).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(STUB_CONFIG_FIRST)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding).groupName(groupname);
    }

    private RequestParameters createGetRequestParameters(final String getUrl) throws FileNotFoundException {
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "text/plain";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new RequestParameters().testServerUrl(getUrl).useProxy(false).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .contentType(contentType).contentEncoding(contentEncoding).acceptEncoding(acceptEncoding);
    }

    /**
     * Method to wait till Wilma saves a message, and return with it.
     *
     * @param getUrl url is similar like http://wilma.server.url:1234/config/messages/20140620121508.0000req.txt?source=true
     * @return with the Response
     * @throws Exception in case of problem
     */
    protected ResponseHolder getSlowMessageFromWilma(final String getUrl) throws Exception {
        logComment("Getting message from:" + getUrl);
        RequestParameters requestParameters = createGetRequestParameters(getUrl);
        ResponseHolder wilmaResp = null;
        // however messages should be written to disc first by wilma, so we need to wait a bit - first - this is a SLOW test...
        int waitingForMaxNSecs = WAIT_PERIOD_FOR_MESSAGE_LOG;
        while (waitingForMaxNSecs > 0) {
            wilmaResp = callWilmaWithGetMethod(requestParameters);
            if (wilmaResp.getResponseMessage().contains(MESSAGE_NOT_YET_AVAILABLE)) {
                logComment("Message is not yet arrived...");
                Thread.sleep(ONE_SECOND); //1 sec wait and then retry
                waitingForMaxNSecs--;
            } else {
                int inSec = WAIT_PERIOD_FOR_MESSAGE_LOG - waitingForMaxNSecs;
                logComment("Message arrived in " + inSec + " secs.");
                waitingForMaxNSecs = 0; //exit from the loop, as we got the answer
            }
        }
        return wilmaResp;
    }

}
