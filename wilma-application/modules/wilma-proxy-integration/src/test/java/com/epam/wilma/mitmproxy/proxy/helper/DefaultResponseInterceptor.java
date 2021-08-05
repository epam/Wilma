package com.epam.wilma.mitmproxy.proxy.helper;

import website.magyar.mitm.proxy.ResponseInterceptor;
import website.magyar.mitm.proxy.http.MitmJavaProxyHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class that is able to intercept and process every response going through the proxy, by implementing the ResponseInterceptor interface.
 * It logs every response that is intercepted.
 *
 * This class is originated from project: https://github.com/tkohegyi/mitmJavaProxy
 * @author Tamas_Kohegyi
 */
public class DefaultResponseInterceptor implements ResponseInterceptor {

    private final Logger logger = LoggerFactory.getLogger(DefaultResponseInterceptor.class);

    private AtomicInteger responseCount;

    public DefaultResponseInterceptor(AtomicInteger responseCount) {
        this.responseCount = responseCount;
    }

    @Override
    public void process(final MitmJavaProxyHttpResponse response) {
        responseCount.incrementAndGet();
        logger.info("Response Interceptor Called, status: {}", response.getStatus());
    }

}