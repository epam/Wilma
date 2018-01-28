package com.epam.wilma.gepard.test.helper;
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

import com.epam.wilma.gepard.testclient.ResponseHolder;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Uploads a resource to a given url.
 *
 * @author Tunde_Kovacs
 */
public abstract class ResourceUploaderDecorator extends WilmaTestAssertDecorator {

    private static final int ACCEPTED = 200;

    /**
     * Upload any kind of resource file (classes, templates, and so on) into Wilma.
     * This upload method will not accept failure in upload.
     *
     * @param url      the target url that receives the uploaded resource
     * @param fileName that should be uploaded
     * @return with response code
     * @throws Exception in case of error
     */
    public String uploadResource(final String url, final String fileName) throws Exception {
        ResponseData responseData = uploadResourceWithExpectedError(url, fileName);

        if (ACCEPTED != responseData.getStatusCode()) {
            throw new Exception("Cannot upload resource " + fileName + " to " + url);
        }
        return responseData.getResult();
    }

    /**
     * Upload any kind of resource file (classes, templates, and so on) into Wilma.
     * This upload method WILL accept failure in upload.
     *
     * @param url      the target url that receives the uploaded resource
     * @param fileName that should be uploaded
     * @return with response code
     * @throws Exception in case of error
     */
    public ResponseData uploadResourceWithExpectedError(final String url, final String fileName) throws Exception {
        ResponseData responseData;
        String responseMessage;
        int statusCode;

        HttpClient httpClient = new HttpClient();
        PostMethod httpPost = new PostMethod(url);
        try {
            String content = createRequest(fileName, httpPost);
            logResourceUploadRequestEvent(fileName, content, url);
            statusCode = httpClient.executeMethod(httpPost);
            responseMessage = createResponse(httpPost);
        } catch (IOException e) {
            throw new Exception("Error during Resource Upload", e);
        }
        responseData = new ResponseData(statusCode, responseMessage);
        ResponseHolder responseHolder = new ResponseHolder();
        responseHolder.setResponseCode(responseData.getResponseCode());
        responseHolder.setResponseMessage(responseData.getMessage());
        logResponseEvent(responseHolder);

        return responseData;
    }

    private String createRequest(final String fileName, final PostMethod httpPost) throws IOException {
        InputStream inputStream = new FileInputStream(new File(fileName).getAbsoluteFile());
        String content = getInputStreamAsString(inputStream);
        InputStream inputStream2 = new FileInputStream(new File(fileName).getAbsoluteFile());
        InputStreamRequestEntity entity = new InputStreamRequestEntity(inputStream2);
        httpPost.setRequestEntity(entity);
        return content;
    }

    private String createResponse(final PostMethod httpPost) throws IOException {
        InputStream responseBodyAsStream = httpPost.getResponseBodyAsStream();
        return getInputStreamAsString(responseBodyAsStream);
    }

    private String getInputStreamAsString(final InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream, "UTF-8");
    }

    /**
     * This inter-class holds necessary information about http response message.
     */
    public class ResponseData {
        private final int statusCode;
        private final String message;
        private final String responseCode;

        /**
         * Create the response holder class.
         *
         * @param statusCode status code of the received answer, like 200 in case everything fine.
         * @param message    message content of the response.
         */
        public ResponseData(int statusCode, String message) {
            this.statusCode = statusCode;
            this.message = message;
            this.responseCode = "status code: " + statusCode + "\n";
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getMessage() {
            return message;
        }

        public String getResponseCode() {
            return responseCode;
        }

        public String getResult() {
            return "status code: " + statusCode + "\n" + message;
        }
    }
}
