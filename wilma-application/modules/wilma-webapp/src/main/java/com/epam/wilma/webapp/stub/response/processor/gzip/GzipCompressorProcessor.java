package com.epam.wilma.webapp.stub.response.processor.gzip;
/*==========================================================================
Copyright since 2013, EPAM Systems

This file is part of Wilma.

Wilma is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Wilma is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wilma.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.compression.gzip.GzipCompressionService;
import com.epam.wilma.webapp.stub.response.processor.ResponseProcessor;
import com.epam.wilma.webapp.stub.servlet.helper.ByteArrayInputStreamFactory;

/**
 * Compress the response body with GZIP if desired by the request.
 * @author Tamas_Bihari
 *
 */
@Component
public class GzipCompressorProcessor implements ResponseProcessor {
    private static final String HEADER_VALUE_GZIP = "gzip";
    private static final String HEADER_KEY_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String HEADER_KEY_CONTENT_ENCODING = "Content-Encoding";
    private static final String HEADER_KEY_SUPPRESS_ENCODING = "Wilma-Suppress-Encoding";

    @Autowired
    private GzipCompressionService gzipCompressor;
    @Autowired
    private ByteArrayInputStreamFactory inputStreamFactory;

    @Override
    public byte[] process(final HttpServletRequest req, final HttpServletResponse resp, final byte[] responseBody) {
        byte[] result = responseBody;
        if (isGzipCompressionNeeded(req, resp)) {
            resp.addHeader(HEADER_KEY_CONTENT_ENCODING, HEADER_VALUE_GZIP);
            result = compressResponseStreamWithGzip(inputStreamFactory.createByteArrayInputStream(responseBody));
        }
        return result;
    }

    private boolean isGzipCompressionNeeded(final HttpServletRequest req, final HttpServletResponse resp) {
        boolean result = false;
        String acceptEncodingHeader = req.getHeader(HEADER_KEY_ACCEPT_ENCODING);
        String suppressEncodingHeader = resp.getHeader(HEADER_KEY_SUPPRESS_ENCODING);
        boolean needToGzipUponRequest = acceptEncodingHeader != null && acceptEncodingHeader.contains(HEADER_VALUE_GZIP);
        boolean forcedToSuppressGzipEncoding = suppressEncodingHeader != null && suppressEncodingHeader.contains(HEADER_VALUE_GZIP);
        if (needToGzipUponRequest && !forcedToSuppressGzipEncoding) {
            result = true;
        }
        return result;
    }

    private byte[] compressResponseStreamWithGzip(final InputStream inputStream) {
        return gzipCompressor.compress(inputStream).toByteArray();
    }
}
