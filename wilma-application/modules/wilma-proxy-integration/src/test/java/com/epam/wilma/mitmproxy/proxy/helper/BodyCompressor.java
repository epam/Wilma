package com.epam.wilma.mitmproxy.proxy.helper;

import com.nixxcode.jvmbrotli.common.BrotliLoader;
import com.nixxcode.jvmbrotli.enc.BrotliOutputStream;
import com.nixxcode.jvmbrotli.enc.Encoder;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * This class is originated from project: https://github.com/tkohegyi/mitmJavaProxy
 *
 * @author Tamas_Kohegyi
 */

public class BodyCompressor {

    public ByteArrayOutputStream compressGzip(final InputStream inputStream) {
        InputStream source = inputStream;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gout = new GZIPOutputStream(baos);
            //... Code to read from your original uncompressed data and write to gout.
            IOUtils.copy(source, gout);
            gout.finish();
            gout.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not encode the message body with gzip!", e);
        }
        return baos;
    }

    public ByteArrayOutputStream compressDeflate(final InputStream inputStream) {
        InputStream source = inputStream;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            DeflaterOutputStream gout = new DeflaterOutputStream(baos);
            //... Code to read from your original uncompressed data and write to gout.
            IOUtils.copy(source, gout);
            gout.finish();
            gout.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not encode the message body with deflate!", e);
        }
        return baos;
    }

    public ByteArrayOutputStream compressBrotli(final InputStream inputStream) {
        BrotliLoader.isBrotliAvailable();
        InputStream source = inputStream;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Encoder.Parameters params = new Encoder.Parameters().setQuality(4);
            BrotliOutputStream brotliOutputStream = new BrotliOutputStream(baos, params);
            int read = source.read();
            while(read > -1) { // -1 means EOF
                brotliOutputStream.write(read);
                read = source.read();
            }

            // It's important to close the BrotliOutputStream object.
            brotliOutputStream.close();
            source.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not encode the message body with brotli!", e);
        }
        return baos;
    }
}
