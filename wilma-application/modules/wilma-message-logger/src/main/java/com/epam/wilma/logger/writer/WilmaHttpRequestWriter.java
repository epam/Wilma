package com.epam.wilma.logger.writer;
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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Writes a {@link WilmaHttpRequest} to a file.
 *
 * @author Marton_Sereg
 */
@Component
public class WilmaHttpRequestWriter extends WilmaHttpEntityWriter<WilmaHttpRequest> {

    private static final int OUTPUT_BUFFER_SIZE = 262144;
    private static final String INITIAL_FILENAME = "00000000000000.0000req.txt";

    private final Logger logger = LoggerFactory.getLogger(WilmaHttpRequestWriter.class);

    @Autowired
    private BufferedWriterFactory bufferedWriterFactory;

    @Override
    public boolean write(final WilmaHttpRequest request, final boolean bodyDecompressed) {
        boolean successful = false;
        String outputFile = INITIAL_FILENAME;
        try {
            String messageLoggerId = request.getWilmaMessageLoggerId();
            String messageId = request.getWilmaMessageId();
            outputFile = getOutputFileName(messageLoggerId, bodyDecompressed);
            BufferedWriter writer = bufferedWriterFactory.createBufferedWriter(outputFile, OUTPUT_BUFFER_SIZE);
            if (writer != null) {
                String requestLine = request.getRemoteAddr() + " " + request.getRequestLine();
                writeRequestLine(requestLine, writer);
                writeWilmaLoggerId(writer, messageId);
                String headers = prepareHeadersInfo(request);
                writeHeaders(writer, headers);
                String body = request.getBody();
                writeBody(writer, body);
                writer.close();
                successful = true;
            }
        } catch (IOException e) {
            logger.error("Could not write message to file:" + outputFile + "!", e);
        }
        return successful;
    }

    private void writeRequestLine(final String requestLine, final BufferedWriter writer) throws IOException {
        writer.append(requestLine);
        writer.newLine();
    }

}
