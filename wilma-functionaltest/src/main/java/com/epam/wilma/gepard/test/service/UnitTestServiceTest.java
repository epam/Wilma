package com.epam.wilma.gepard.test.service;

import com.epam.gepard.annotations.TestClass;
import com.epam.wilma.gepard.WilmaTestCase;
import com.epam.wilma.gepard.testclient.RequestParameters;
import com.epam.wilma.gepard.testclient.ResponseHolder;
import com.epam.wilma.service.client.WilmaService;
import com.epam.wilma.service.configuration.stub.WilmaStub;
import com.epam.wilma.service.configuration.stub.WilmaStubBuilder;
import com.epam.wilma.service.configuration.stub.helper.common.UniqueGroupNameGenerator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test some wilma-service-api.jar functionality around unit tests.
 *
 * @author Tamas_Kohegyi
 */
@TestClass(id = "Wilma Service API", name = "Unit test with Wilma")
public class UnitTestServiceTest extends WilmaTestCase {

    private static final String INTERCEPTOR_RESOURCE_BASE = "resources/interceptor/custompostfix/";
    private static final String INTERCEPTOR_CLASS = "CustomMessagePostfixInterceptor.class";
    private static final String TEST_SERVER_RESPONSE = "resources/interceptor/usage/resetSequenceResponse.txt";

    private WilmaService wilmaService;
    private String groupName;

    @Before
    public void prepareWilmaMockInstance() throws Exception {
        if (wilmaService == null) {
            wilmaService = new WilmaService(getTestClassExecutionData().getEnvironment().getProperties());
        }
        groupName = UniqueGroupNameGenerator.getUniqueGroupName();
        clearAllOldStubConfigs();
        setLocalhostBlockingTo("off");
        setOperationModeTo("wilma");
        setInterceptorModeTo("on");
        setMessageMarkingTo("on");
    }

    @After
    public void dropStubConfiguration() throws Exception {
        wilmaService.dropStubConfig(groupName);
        setMessageMarkingTo("off");
    }

    @Test
    public void newStubConfigurationRefused() {
        WilmaStub wilmaStub = new WilmaStubBuilder(groupName)
                .forRequestsLike().condition("AlwaysTrueConditionDoesNotExist") //no such condition class exists
                .willRespondWith().plainTextResponse("response")
                .build();
        boolean b = uploadStubConfiguration(wilmaService, wilmaStub);
        Assert.assertFalse("Stub Configuration should not be accepted.", b);
    }

    @Test
    public void newStubConfigurationAccepted() {
        WilmaStub wilmaStub = new WilmaStubBuilder(groupName)
                .forRequestsLike().condition("AlwaysTrueChecker") //this one is real condition checked class
                .willRespondWith().plainTextResponse("response")
                .build();
        boolean b = uploadStubConfiguration(wilmaService, wilmaStub);
        Assert.assertTrue("Stub Configuration should not be accepted.", b);
    }

    @Test
    public void unitTestWithWilma() throws Exception {
        //given
        String extraUrl = "unitTest";
        String expectedAnswer = "response";
        WilmaStub wilmaStub = new WilmaStubBuilder(groupName)
                .forRequestsLike()
                .andStart()
                .condition("AlwaysTrueChecker") //this one is real condition checked class
                .textInUrl(extraUrl)
                .andEnd()
                .willRespondWith().plainTextResponse(expectedAnswer)
                .build();
        boolean b = uploadStubConfiguration(wilmaService, wilmaStub);
        Assert.assertTrue("Stub Configuration should not be accepted.", b);
        RequestParameters requestParameters = createRequestParameters(extraUrl);
        setExpectedResponseMessage(expectedAnswer);
        // now send a request to somewhere via Wilma, expect the answer
        //when
        logStep("Send Unit test request.");
        ResponseHolder responseHolder = callWilmaWithGetMethod(requestParameters);
        //then
        checkResponseCode(200);
        String actualResponse = responseHolder.getResponseMessage();
        Assert.assertTrue("Expected: " + expectedAnswer + ", but received: " + actualResponse, actualResponse.contentEquals(expectedAnswer));
    }

    /**
     * See com.epam.wilma.gepard.test.interceptor.CustomMessagePostfixTest.
     * This is the same, but the stub configuration is generated.
     *
     * @throws Exception if something bad happens
     */
    @Test
    public void testCustomPostfixViaApi() throws Exception {
        //given
        setMessageMarkingTo("on");
        uploadInterceptorToWilma(INTERCEPTOR_CLASS, INTERCEPTOR_RESOURCE_BASE + INTERCEPTOR_CLASS);
        WilmaStub wilmaStub = new WilmaStubBuilder(groupName)
                .forAnyRequest()
                .addInterceptor("Custom-Postfix-Interceptor", "CustomMessagePostfixInterceptor")
                .build();
        boolean b = uploadStubConfiguration(wilmaService, wilmaStub);
        Assert.assertTrue("Stub Configuration should not be accepted.", b);
        //when - send the request
        RequestParameters requestParameters2 = createRequest(TEST_SERVER_RESPONSE);
        ResponseHolder response = callWilmaWithGetMethod(requestParameters2);
        //then - receive and analyse the response
        //identify the message id first
        String[] lines = response.getResponseMessage().split("\n");

        assertNotNull(lines);
        assertTrue(lines.length == 3);
        String id = lines[2].substring(26);
        logComment("Used message id: " + id);
        // now get req and resp messages stored by wilma,
        // url is similar like http://wilma.server.url:1234/config/messages/20140620121508.0000req.txt?source=true
        String reqRequestUrl = getWilmaInternalUrl() + "config/public/messages/" + id + "req_APOST.txt?source=true";
        String respRequestUrl = getWilmaInternalUrl() + "config/public/messages/" + id + "resp_BPOST.txt?source=true";
        ResponseHolder wilmaReq = getSlowMessageFromWilma(reqRequestUrl);
        ResponseHolder wilmaResp = getSlowMessageFromWilma(respRequestUrl);
        //now we can analyse
        assertNotNull("Problem during waiting for the request.", wilmaReq);
        assertNotNull("Problem during waiting for the response.", wilmaResp);
        assertTrue("Request was not arrived.", !wilmaReq.getResponseMessage().contains(MESSAGE_NOT_YET_AVAILABLE));
        assertTrue("Response was not arrived.", !wilmaResp.getResponseMessage().contains(MESSAGE_NOT_YET_AVAILABLE));
    }

    protected RequestParameters createRequestParameters(String extraPath) throws FileNotFoundException {
        String testServerUrl = getWilmaTestServerUrlBase() + extraPath;
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "text/plain";
        String acceptHeader = "";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding).acceptEncoding(acceptEncoding);
    }

    private RequestParameters createRequest(final String response) throws Exception {
        setOriginalRequestMessageEmpty();
        setExpectedResponseMessageFromFile(response);
        return createRequestParameters();
    }

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaTestServerUrlBase() + "resetsequences";
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "text/plain;charset=UTF-8";
        String acceptHeader = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
        String contentEncoding = "";
        String acceptEncoding = "gzip";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .contentType(contentType).acceptHeader(acceptHeader).acceptEncoding(acceptEncoding).contentEncoding(contentEncoding);
    }

}
