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

import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptorHolder;
import com.epam.wilma.router.RoutingService;
import com.epam.wilma.stubconfig.StubDescriptorJsonFactory;
import com.epam.wilma.webapp.config.servlet.stub.upload.helper.FileWriter;
import com.epam.wilma.webapp.domain.exception.CannotUploadExternalResourceException;
import com.epam.wilma.webapp.service.command.NewStubDescriptorCommand;
import com.epam.wilma.webapp.service.external.ServiceMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * Class for processing, validating, and storing the resource after uploading.
 *
 * @author Tamas_Bihari
 * @author Tamas Kohegyi
 */
@Component
public class MultiPartFileProcessor {
    private static final String APPLICATION_JAVA = "application/java";
    private static final String OCTET_STREAM_CONTENT_TYPE = "application/octet-stream";
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String EXCEPTION_MESSAGE = "Could not upload external resource ";

    @Autowired
    private StubResourcePathProvider stubResourcePathProvider;
    @Autowired
    private FileWriter fileWriter;
    @Autowired
    private StubDescriptorJsonFactory stubConfigurationJsonBuilder;
    @Autowired
    private RoutingService routingService;
    @Autowired
    private SequenceDescriptorHolder sequenceDescriptorHolder;
    @Autowired
    private ServiceMap serviceMap;

    /**
     * Processes, validates and stores the uploaded resource.
     *
     * @param resource    is the uploaded resource as {@link InputStream}
     * @param contentType is the content type of the resource
     * @param fieldName   is used to identify the resource type
     * @param fileName    is the name of the uploaded file
     * @return with the result message of the processing
     */
    public String processUploadedFile(final InputStream resource, final String contentType, final String fieldName, final String fileName) {
        String result;
        String resFileName = extractFileNameFromAbsolutePath(fileName);

        String classUploadResult = processUploadedClasses(resource, contentType, fieldName, resFileName);
        if (classUploadResult != null) {
            result = classUploadResult;
        } else {
            if ("stub-configuration".equals(fieldName) && JSON_CONTENT_TYPE.equals(contentType)) {
                try {
                    routingService.performModification(new NewStubDescriptorCommand(resource, stubConfigurationJsonBuilder, sequenceDescriptorHolder));
                    serviceMap.detectServices();
                } catch (ClassNotFoundException e) {
                    result = "Uploading " + fileName + " failed with ClassNotFoundException.";
                    throw new CannotUploadExternalResourceException(result, e);
                }
                result = "New stub configuration was uploaded to Wilma.";
            } else if ("stub-template".equals(fieldName)) {
                writeResourceToFile(resource, resFileName, stubResourcePathProvider.getTemplatesPathAsString());
                result = "External template '" + resFileName + "' was uploaded to Wilma.";
            } else {
                result = "Uploading " + fileName + " failed: wrong content type or tried to upload file from unauthorized form!";
                throw new CannotUploadExternalResourceException(result);
            }
        }
        return result;
    }

    private String processUploadedClasses(final InputStream resource, final String contentType, final String fieldName, final String resFileName) {
        String result = null;

        if ("stub-condition-checker".equals(fieldName) && isContentTypeJava(contentType)) {
            writeResourceToFile(resource, resFileName, stubResourcePathProvider.getConditionCheckerPathAsString());
            result = "External condition checker class '" + resFileName + "' was uploaded to Wilma.";
        } else if ("stub-response-formatter".equals(fieldName) && isContentTypeJava(contentType)) {
            writeResourceToFile(resource, resFileName, stubResourcePathProvider.getResponseFormattersPathAsString());
            result = "External response formatter class '" + resFileName + "' was uploaded to Wilma.";
        } else if ("stub-interceptor".equals(fieldName)) {
            writeResourceToFile(resource, resFileName, stubResourcePathProvider.getInterceptorPathAsString());
            result = "External interceptor '" + resFileName + "' was uploaded to Wilma.";
        } else if ("stub-jar".equals(fieldName)) {
            writeResourceToFile(resource, resFileName, stubResourcePathProvider.getJarPathAsString());
            result = "External jar '" + resFileName + "' was uploaded to Wilma.";
        } else if ("stub-sequence-handler".equals(fieldName)) {
            writeResourceToFile(resource, resFileName, stubResourcePathProvider.getSequenceHandlerPathAsString());
            result = "External sequence handler '" + resFileName + "' was uploaded to Wilma.";
        }

        return result;
    }

    private String extractFileNameFromAbsolutePath(final String fileName) {
        String result = fileName;
        if (fileName.contains("\\")) {
            result = fileName.substring(fileName.lastIndexOf("\\") + 1);
        } else if (fileName.contains("/")) {
            result = fileName.substring(fileName.lastIndexOf("/") + 1);
        }
        return result;
    }

    private boolean isContentTypeJava(final String contentType) {
        return OCTET_STREAM_CONTENT_TYPE.equals(contentType) || APPLICATION_JAVA.equals(contentType);
    }

    private void writeResourceToFile(final InputStream resource, final String fileName, final String path) {
        fileWriter.write(resource, path + "/" + fileName, EXCEPTION_MESSAGE);
    }
}
