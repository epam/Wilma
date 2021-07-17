package com.epam.wilma.mitmProxy.proxy.helper;

public enum ContentEncoding {

    //See further info on Content Encoding here: https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Encoding

    ANY("*"),
    GZIP("gzip"),
    DEFLATE("deflate"),
    BROTLI("br"),
    NONE("identity"); //this is: "no encoding"

    private final String value;

    ContentEncoding(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot create null Content Encoding");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
