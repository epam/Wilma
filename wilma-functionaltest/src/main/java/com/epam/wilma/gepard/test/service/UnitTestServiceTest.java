package com.epam.wilma.gepard.test.service;

import com.epam.gepard.annotations.TestClass;
import com.epam.gepard.util.Util;
import com.epam.wilma.gepard.WilmaTestCase;
import com.epam.wilma.gepard.extension.HostName;
import com.epam.wilma.gepard.testclient.RequestParameters;
import com.epam.wilma.gepard.testclient.ResponseHolder;
import com.epam.wilma.service.client.WilmaService;
import com.epam.wilma.service.configuration.stub.WilmaStub;
import com.epam.wilma.service.configuration.stub.WilmaStubBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

/**
 * Test some wilma-service-api.jar functionality around unit tests.
 *
 * @author Tamas_Kohegyi
 */
@TestClass(id = "Wilma Service API", name = "Unit test with Wilma")
public class UnitTestServiceTest extends WilmaTestCase {

    private WilmaService wilmaService;
    private Util u = new Util();
    private String groupName;

    @Before
    public void prepareWilmaMockInstance() throws Exception {
        if (wilmaService == null) {
            wilmaService = new WilmaService(getTestClassExecutionData().getEnvironment().getProperties());
        }
        groupName = new HostName().getHostName() + Thread.currentThread().toString();
        clearAllOldStubConfigs();
        setLocalhostBlockingTo("off");
        setOperationModeTo("wilma");
    }

    @After
    public void dropStubConfiguration() {
        wilmaService.dropStubConfig(groupName);
    }

    @Test
    public void newStubConfigurationRefused() {
        WilmaStub wilmaStub = new WilmaStubBuilder(groupName)
                .forRequestsLike().condition("AlwaysTrueConditionDoesNotExist") //no such condition class exists
                .willRespondWith().plainTextResponse("response")
                .build();
        String stubConfig = wilmaStub.toString();
        logComment("Prepared Stub Configuration Info", u.escapeHTML(stubConfig));
        boolean b = wilmaService.uploadStubConfiguration(wilmaStub.toString());
        Assert.assertFalse("Stub Configuration should not be accepted.", b);
    }

    @Test
    public void newStubConfigurationAccepted() {
        WilmaStub wilmaStub = new WilmaStubBuilder(groupName)
                .forRequestsLike().condition("AlwaysTrueChecker") //this one is real condition checked class
                .willRespondWith().plainTextResponse("response")
                .build();
        String stubConfig = wilmaStub.toString();
        logComment("Prepared Stub Configuration Info", u.escapeHTML(stubConfig));
        boolean b = wilmaService.uploadStubConfiguration(wilmaStub.toString());
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
        String stubConfig = wilmaStub.toString();
        logStep("Uploading stub configuration.");
        logComment("Prepared Stub Configuration Info", u.escapeHTML(stubConfig));
        boolean b = wilmaService.uploadStubConfiguration(wilmaStub.toString());
        Assert.assertTrue("Stub Configuration should not be accepted.", b);
        RequestParameters requestParameters = createRequestParameters(extraUrl);
        setExpectedResponseMessage(expectedAnswer);
        // now send a request to somewhere via Wilma, expect the answer
        //when
        logStep("Send Unit test request.");
        ResponseHolder responseHolder = callWilmaWithGetMethod(requestParameters);
        //then
        checkResponseCode(Integer.valueOf(200));
        String actualResponse = responseHolder.getResponseMessage();
        Assert.assertTrue("Expected: " + expectedAnswer + ", but received: " + actualResponse, actualResponse.contentEquals(expectedAnswer));
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


}
