package com.epam.wilma.extras.circuitbreaker;

import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test code for @CircuitBreakerInformation.
 *
 * @author tkohegyi
 */
public class CircuitBreakerInformationTest {

    private CircuitBreakerInformation underTest;

    @BeforeEach
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
    public void resetErrorLevel() {
    }

    @Test
    public void increaseErrorLevel() {
        int originalValue = underTest.getActualErrorLevel();
        underTest.increaseErrorLevel();
        int newValue = underTest.getActualErrorLevel();
        assertTrue(originalValue + 1 == newValue);
    }

}
