package com.epam.wilma.mitmProxy.proxy;

import com.epam.wilma.mitmProxy.proxy.helper.ContentEncoding;

/**
 * Tests just a single basic proxy running as a man in the middle.
 */
public class ProxyServerTestWithEncodingIdentity extends ProxyServerTestWithEncodingBase {

    @Override
    protected void setUpContentEncoding() {
        contentEncoding = ContentEncoding.NONE;
    }
}
