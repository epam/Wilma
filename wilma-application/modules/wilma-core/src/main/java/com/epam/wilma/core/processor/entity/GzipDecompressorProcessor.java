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
import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.compression.gzip.GzipCompressionService;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpEntity;

/**
 * Processes a {@link WilmaHttpEntity} by decompressing it from gzip.
 * @author Tunde_Kovacs
 *
 */
@Component
public class GzipDecompressorProcessor extends ProcessorBase {

    private static final String GZIP_ENCODING = "Content-Encoding";
    private static final String GZIP = "gzip";

    @Autowired
    private GzipCompressionService decompressor;

    @Override
    public void process(final WilmaHttpEntity entity) throws ApplicationException {
        if (isContentGzipEncoded(entity)) {
            ByteArrayOutputStream baos = decompressor.decompress(entity.getInputStream());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
            entity.setInputStream(inputStream);
            entity.setBody(baos.toString());
        }
    }

    private boolean isContentGzipEncoded(final WilmaHttpEntity wilmaHttpEntity) {
        String contentEconding = wilmaHttpEntity.getHeader(GZIP_ENCODING);
        return contentEconding != null && contentEconding.contains(GZIP);
    }

}
