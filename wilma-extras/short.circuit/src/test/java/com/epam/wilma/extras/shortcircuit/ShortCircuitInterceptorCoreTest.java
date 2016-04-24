package com.epam.wilma.extras.shortcircuit;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test code for @ShortCircuitInterceptorCore.
 * @author tkohegyi, created on 2016. 03. 24.
 */
public class ShortCircuitInterceptorCoreTest {

    private ShortCircuitInterceptorCore underTest;

    @Before
    public void init() {
        underTest = new ShortCircuitInterceptorCore();
    }

    @Test
    public void hashGeneration() {
        WilmaHttpRequest request = new WilmaHttpRequest();
        request.setBody("hooopla");
        String requestLine = "GET http://blah.com:1234/gzu?gzu&gzu";
        request.setRequestLine(requestLine);
        String hash = underTest.generateKeyForMap(request);
        Assert.assertTrue("Generated hash is not correct", hash.startsWith(requestLine));
    }
}
