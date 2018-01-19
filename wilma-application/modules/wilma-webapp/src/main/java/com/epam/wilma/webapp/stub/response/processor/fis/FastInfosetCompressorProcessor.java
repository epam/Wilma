package com.epam.wilma.webapp.stub.response.processor.fis;
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.compression.fis.FastInfosetCompressionService;
import com.epam.wilma.webapp.stub.response.processor.ResponseProcessor;

/**
 * Compress the response body with FastInfoset if desired by the request.
 * @author Tamas_Bihari
 *
 */
@Component
public class FastInfosetCompressorProcessor implements ResponseProcessor {
    private static final String FASTINFOSET = "fastinfoset";
    private static final String XML = "xml";
    private static final String ACCEPT_VALUE_FASTINFOSET = "application/fastinfoset";
    private static final String ACCEPT_HEADER_KEY = "Accept";
    private static final String HEADER_KEY_SUPPRESS_ENCODING = "Wilma-Suppress-Encoding";

    @Autowired
    private FastInfosetCompressionService fastInfosetCompressor;

    @Override
    public byte[] process(final HttpServletRequest req, final HttpServletResponse resp, final byte[] responseBody) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(responseBody);
        byte[] result = responseBody;
        if (isFastInfoSetCompressionNeeded(req, resp)) {
            resp.setContentType(ACCEPT_VALUE_FASTINFOSET);
            result = compressResponseXmlToFastInfoset(inputStream);
        }

        return result;
    }

    private boolean isFastInfoSetCompressionNeeded(final HttpServletRequest req, final HttpServletResponse resp) {
        String acceptHeader = req.getHeader(ACCEPT_HEADER_KEY);
        boolean isAcceptFastInfoset = acceptHeader != null && acceptHeader.contains(ACCEPT_VALUE_FASTINFOSET);
        boolean isResponseContentTypeXml = resp.getContentType().contains(XML) || resp.getContentType().contains(FASTINFOSET);
        String suppressEncodingHeader = resp.getHeader(HEADER_KEY_SUPPRESS_ENCODING);
        boolean forcedToSuppressFastInfosetEncoding = suppressEncodingHeader != null && suppressEncodingHeader.contains(FASTINFOSET);
        return isAcceptFastInfoset && isResponseContentTypeXml && !forcedToSuppressFastInfosetEncoding;
    }

    private byte[] compressResponseXmlToFastInfoset(final InputStream inputStream) {
        return fastInfosetCompressor.compress(inputStream).toByteArray();
    }

}
