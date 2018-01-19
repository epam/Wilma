package com.epam.wilma.core.processor.entity;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.compression.base64.Base64Decoder;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpEntity;

/**
 * Processes a {@link WilmaHttpEntity} by decoding it's body from base64.
 * This only makes sense if the {@link WilmaHttpEntity} is a <tt>WilmaHttpResponse</tt>, because
 * only responses come encoded with base64 from underlying proxy implementation.
 * @author Tunde_Kovacs
 *
 */
@Component
public class Base64DecoderProcessor extends ProcessorBase {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String FAST_INFOSET = "fastinfoset";

    @Autowired
    private Base64Decoder decompressor;

    @Override
    public void process(final WilmaHttpEntity entity) throws ApplicationException {
        if (isBinaryContent(entity)) {
            String responseBody = entity.getBody();
            byte[] result = decompressor.decode(responseBody);
            entity.setInputStream(getInputStream(result));
        }
    }

    private boolean isBinaryContent(final WilmaHttpEntity entity) {
        String contentType = entity.getHeader(CONTENT_TYPE);
        return contentType != null && contentType.contains(FAST_INFOSET);
    }

    private ByteArrayInputStream getInputStream(final byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }
}
