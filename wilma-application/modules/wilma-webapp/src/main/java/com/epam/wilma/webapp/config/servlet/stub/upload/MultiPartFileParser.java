package com.epam.wilma.webapp.config.servlet.stub.upload;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class for parsing multipart files and sending them to processing mechanism.
 * @author Tamas_Bihari
 *
 */
@Component
public class MultiPartFileParser {

    private final MultiPartFileProcessor multiPartFileProcessor;

    /**
     * Creates a new {@link MultiPartFileParser} instance with parameter.
     * @param multiPartFileProcessor processes the parsed multipart files
     */
    @Autowired
    public MultiPartFileParser(final MultiPartFileProcessor multiPartFileProcessor) {
        this.multiPartFileProcessor = multiPartFileProcessor;
    }

    /**
     * Parses a list of multipart files and sends them to {@link MultiPartFileProcessor}.
     * @param fields a list of multipart files that will be processed
     * @return with the processing status message or "No file uploaded" when the list is empty
     * @throws IOException was thrown file parsing failed
     */
    public String parseMultiPartFiles(final List<FileItem> fields) throws IOException {
        String msg = "";
        Iterator<FileItem> it = fields.iterator();
        if (!fields.isEmpty() && it.hasNext()) {
            while (it.hasNext()) {
                FileItem fileItem = it.next();
                if (!fileItem.isFormField()) {
                    String uploadedFileName = fileItem.getName();
                    InputStream uploadedResource = fileItem.getInputStream();
                    String contentType = fileItem.getContentType();
                    String fieldName = fileItem.getFieldName();
                    msg += multiPartFileProcessor.processUploadedFile(uploadedResource, contentType, fieldName, uploadedFileName);
                }
            }
        } else {
            msg = "No file uploaded";
        }

        return msg;
    }

}
