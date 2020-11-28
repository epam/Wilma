package com.epam.wilma.mitmProxy.proxy.helper;

import net.lightbody.bmp.proxy.http.BrowserMobHttpResponse;
import net.lightbody.bmp.proxy.http.ResponseInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class that is able to intercept and process every response going through the proxy, by implementing the ResponseInterceptor interface.
 * It logs every response that is intercepted.
 * @Tamas_Kohegyi
 */
public class DefaultResponseInterceptor implements ResponseInterceptor {

    private final Logger logger = LoggerFactory.getLogger(DefaultResponseInterceptor.class);

    private AtomicInteger responseCount;

    public DefaultResponseInterceptor(AtomicInteger responseCount) {
        this.responseCount = responseCount;
    }

    public void process(final BrowserMobHttpResponse response) {
        responseCount.incrementAndGet();
        logger.info("Response Interceptor Called, status: {}", response.getStatus());
    }

}