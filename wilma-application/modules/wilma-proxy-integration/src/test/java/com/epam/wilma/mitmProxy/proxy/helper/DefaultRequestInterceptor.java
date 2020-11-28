package com.epam.wilma.mitmProxy.proxy.helper;

import net.lightbody.bmp.proxy.http.BrowserMobHttpRequest;
import net.lightbody.bmp.proxy.http.RequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class that is able to intercept and process every request going through the proxy, by implementing the RequestInterceptor interface.
 * It logs every request that is intercepted.
 * @Tamas_Kohegyi
 */
public class DefaultRequestInterceptor implements RequestInterceptor {

    private final Logger logger = LoggerFactory.getLogger(DefaultRequestInterceptor.class);

    private AtomicInteger requestCount;
    private String stubRequestPattern;
    private URI stubUri;

    public DefaultRequestInterceptor(AtomicInteger requestCount, String stubRequestPattern, String stubUrl) {
        this.requestCount = requestCount;
        this.stubRequestPattern = stubRequestPattern;
        try {
            this.stubUri = new URI(stubUrl);
        } catch (URISyntaxException e) {
            this.stubUri = null;
        }
    }


    public void process(final BrowserMobHttpRequest request) {
        requestCount.incrementAndGet();
        String uriPath = request.getMethod().getURI().getPath();
        if (stubRequestPattern != null && stubUri != null && uriPath.contains(stubRequestPattern)) {
            request.getMethod().setURI(stubUri);
            logger.info("Request Interceptor Called - Redirect to STUB: {}", stubUri.toString());
        } else {
            logger.info("Request Interceptor Called - Request untouched.");
        }
    }

}