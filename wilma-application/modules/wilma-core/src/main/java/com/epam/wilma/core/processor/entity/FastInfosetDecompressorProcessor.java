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

import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.compression.fis.FastInfosetCompressionService;
import com.epam.wilma.domain.http.WilmaHttpEntity;

/**
 * Processes a {@link WilmaHttpEntity} by decompressing it from fastinfosetif needed.
 * @author Tunde_Kovacs
 *
 */
@Component
public class FastInfosetDecompressorProcessor extends ProcessorBase {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String FAST_INFO_SET = "fastinfoset";

    @Autowired
    private FastInfosetCompressionService fastinfosetDecompressor;

    @Override
    public void process(final WilmaHttpEntity entity) {
        if (isContentFISCompressed(entity)) {
            ByteArrayOutputStream compressedBody = fastinfosetDecompressor.decompress(entity.getInputStream());
            entity.setBody(compressedBody.toString());
        }
    }

    private boolean isContentFISCompressed(final WilmaHttpEntity wilmaHttpEntity) {
        String contentType = wilmaHttpEntity.getHeader(CONTENT_TYPE);
        return contentType != null && contentType.contains(FAST_INFO_SET);
    }

}
