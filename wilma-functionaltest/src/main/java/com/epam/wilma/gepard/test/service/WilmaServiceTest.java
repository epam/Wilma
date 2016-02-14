package com.epam.wilma.gepard.test.service;

import com.epam.gepard.annotations.TestClass;
import com.epam.gepard.generic.GepardTestClass;
import com.epam.wilma.service.client.WilmaService;
import com.epam.wilma.service.domain.WilmaLoadInformation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test some wilma-service-api.jar functionality.
 *
 * @author Tamas_Kohegyi
 */
@TestClass(id = "Wilma Service API", name = "General test")
public class WilmaServiceTest implements GepardTestClass {

    private WilmaService wilmaService;

    @Before
    public void prepareWilmaMockInstance() {
        if (wilmaService == null) {
            wilmaService = new WilmaService(getTestClassExecutionData().getEnvironment().getProperties());
        }
    }

    @Test
    public void getWilmaVersionViaAPI() {
        String version = wilmaService.getVersionInformation();
        Assert.assertNotNull(version);
        logComment("Wilma version detected: " + version);
    }

    @Test
    public void getWilmaLoadInformationViaAPI() {
        WilmaLoadInformation o = wilmaService.getActualLoadInformation();
        Assert.assertNotNull(o);
        logComment("Wilma Logger Queue size is: " + o.getLoggerQueueSize());
        logComment("Wilma Response Queue size is: " + o.getResponseQueueSize());
        logComment("Number of logged messages at Wilma is: " + o.getCountOfMessages());
        logComment("Number of deleted messages at last clean-up period is: " + o.getDeletedFilesCount());
    }

}
