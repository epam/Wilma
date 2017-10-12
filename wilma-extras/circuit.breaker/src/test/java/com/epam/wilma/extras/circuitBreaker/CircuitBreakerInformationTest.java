package com.epam.wilma.extras.circuitBreaker;

import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test code for @CircuitBreakerInformation.
 *
 * @author tkohegyi
 */
public class CircuitBreakerInformationTest {

    private CircuitBreakerInformation underTest;

    @Before
    public void init() {
        ParameterList parameterList = new ParameterList();
        parameterList.addParameter(new Parameter("identifier", "TEST"));
        parameterList.addParameter(new Parameter("path", "http://v"));
        parameterList.addParameter(new Parameter("timeoutInSec", "15"));
        parameterList.addParameter(new Parameter("successCodes", "200,201,303"));
        parameterList.addParameter(new Parameter("maxErrorCount", "4"));
        underTest = new CircuitBreakerInformation("TEST", parameterList);
    }

    @Test
    public void resetErrorLevel() throws Exception {
    }

    @Test
    public void increaseErrorLevel() throws Exception {
        int originalValue = underTest.getActualErrorLevel();
        underTest.increaseErrorLevel();
        int newValue = underTest.getActualErrorLevel();
        Assert.assertTrue(originalValue + 1 == newValue);
    }

}
