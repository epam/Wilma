package com.epam.wilma.mitmProxy.proxy.helper;

import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.entity.InputStreamFactory;
import org.brotli.dec.BrotliInputStream;

import java.io.IOException;
import java.io.InputStream;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class BrotliInputStreamFactory implements InputStreamFactory {

    private static final BrotliInputStreamFactory INSTANCE = new BrotliInputStreamFactory();

    /**
     * Gets the singleton instance.
     *
     * @return the singleton instance.
     */
    public static BrotliInputStreamFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public InputStream create(final InputStream inputStream) throws IOException {
        return new BrotliInputStream(inputStream);
    }

}
