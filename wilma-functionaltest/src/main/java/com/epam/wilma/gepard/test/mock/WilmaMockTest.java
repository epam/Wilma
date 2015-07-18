package com.epam.wilma.gepard.test.mock;

import com.epam.gepard.annotations.TestClass;
import com.epam.gepard.generic.GepardTestClass;
import com.epam.wilma.mock.client.WilmaMock;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test some wilma-mock.jar functionality.
 *
 * @author Tamas_Kohegyi
 */
@TestClass(id = "Mock.jar", name = "General test")
public class WilmaMockTest implements GepardTestClass {

    private WilmaMock wilmaMock;

    @Before
    public void prepareWilmaMockInstance() {
        if (wilmaMock == null) {
            wilmaMock = new WilmaMock(getTestClassExecutionData().getEnvironment().getProperties());
        }
    }

    @Test
    public void getWilmaVersionViaAPI() {
        JSONObject o = wilmaMock.getVersionInformation();
        Assert.assertNotNull(o);
        String version = o.getString("wilmaVersion");
        logComment("Wilma version detected: " + version);
    }

    @Test
    public void getWilmaLoadInformationViaAPI() {
        JSONObject o = wilmaMock.getActualLoadInformation();
        Assert.assertNotNull(o);
        Integer loggerQueueSize = (Integer) o.get("loggerQueueSize");
        logComment("Wilma Logger Queue size is: " + loggerQueueSize);
    }

}
