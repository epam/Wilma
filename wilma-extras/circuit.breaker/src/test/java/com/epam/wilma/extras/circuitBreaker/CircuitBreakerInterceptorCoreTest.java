package com.epam.wilma.extras.circuitBreaker;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import org.junit.Before;
import org.junit.Test;

/**
 * Test code for @CircuitBreakerInterceptorCore.
 *
 * @author tkohegyi, created on 2016. 03. 24.
 */
public class CircuitBreakerInterceptorCoreTest {

    private CircuitBreakerInterceptorCore underTest;

    @Before
    public void init() {
        underTest = new CircuitBreakerInterceptorCore();
    }

    @Test
    public void hashGeneration() {
        WilmaHttpRequest request = new WilmaHttpRequest();
        request.setBody("hoopla");
        String requestLine = "GET http://blah.com:1234/gzu?gzu&gzu";
        request.setRequestLine(requestLine);
    }
}
