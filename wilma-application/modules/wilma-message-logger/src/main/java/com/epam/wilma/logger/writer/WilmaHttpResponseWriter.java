package com.epam.wilma.logger.writer;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import java.io.BufferedWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.WilmaConstants;
import com.epam.wilma.domain.http.WilmaHttpResponse;

/**
 * Writes a {@link WilmaHttpResponse} to a file.
 * @author Tunde_Kovacs
 *
 */
@Component
public class WilmaHttpResponseWriter extends WilmaHttpEntityWriter<WilmaHttpResponse> {

    private static final int OUTPUT_BUFFER_SIZE = 262144;
    private static final String INITIAL_FILENAME = "00000000000000.0000resp.txt";

    private final Logger logger = LoggerFactory.getLogger(WilmaHttpRequestWriter.class);

    @Autowired
    private BufferedWriterFactory bufferedWriterFactory;

    @Override
    public boolean write(final WilmaHttpResponse wilmaHttpEntity, final boolean bodyDecompressed) {
        boolean successful = false;
        String outputFile = INITIAL_FILENAME;
        try {
            String messageId = wilmaHttpEntity.getRequestHeader(WilmaConstants.WILMA_LOGGER_ID.getConstant());
            outputFile = getOutputFileName("resp", messageId, bodyDecompressed);
            BufferedWriter writer = bufferedWriterFactory.createBufferedWriter(outputFile, OUTPUT_BUFFER_SIZE);
            if (writer != null) {
                writeWilmaLoggerId(writer, messageId);
                String headers = wilmaHttpEntity.getHeaders().toString();
                if (wilmaHttpEntity.getExtraHeaders() != null) {
                    headers = headers + wilmaHttpEntity.getExtraHeaders().toString();
                }
                writeHeaders(writer, headers);
                int statusCode = wilmaHttpEntity.getStatusCode();
                writeStatusCode(statusCode, writer);
                String body = wilmaHttpEntity.getBody();
                writeBody(writer, body);
                writer.close();
                successful = true;
            }
        } catch (IOException e) {
            logger.error("Could not write message to file:" + outputFile + "!", e);
        }
        return successful;

    }

    private void writeStatusCode(final int statusCode, final BufferedWriter writer) throws IOException {
        writer.append("Status code:" + String.valueOf(statusCode));
        writer.newLine();
    }
}
