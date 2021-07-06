package com.epam.wilma.extras.shortcircuit;
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

import com.epam.wilma.common.helper.FileFactory;
import com.epam.wilma.common.helper.LogFilePathProvider;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.webapp.config.servlet.stub.upload.helper.FileOutputStreamFactory;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Map;

/**
 * This class provides the Short Circuit Service calls.
 *
 * @author tkohegyi
 */
class ShortCircuitInterceptorCore {

    private static Map<String, ShortCircuitResponseInformation> shortCircuitMap = ShortCircuitChecker.getShortCircuitMap();
    private final Logger logger = LoggerFactory.getLogger(ShortCircuitInterceptorCore.class);
    private final ShortCircuitResponseInformationFileHandler shortCircuitResponseInformationFileHandler = new ShortCircuitResponseInformationFileHandler();

    @Autowired
    private LogFilePathProvider logFilePathProvider;
    @Autowired
    private FileFactory fileFactory;
    @Autowired
    private FileOutputStreamFactory fileOutputStreamFactory;

    /**
     * Method that generates the general hash code for a request in Short Circuit.
     *
     * @param wilmaHttpRequest is the request, that is the base of the hash code
     * @return with the generated hash code
     */
    String generateKeyForMap(final WilmaHttpRequest wilmaHttpRequest) {
        String hashString = wilmaHttpRequest.getRequestLine() + " - " + wilmaHttpRequest.getBody().hashCode();
        //ensure that it is ok for a header
        //CHECKSTYLE OFF - we must use "new String" here
        hashString = new String(Base64.encodeBase64(hashString.getBytes()));
        //CHECKSTYLE ON
        return hashString;
    }

    /**
     * Method that handles GET (all) and DELETE (all) methods on the actual Short Circuit Map.
     *
     * @param myMethod            is expected as either GET or DELETE
     * @param httpServletResponse is the response object
     * @param path                is the request path
     * @return with the response body (and with the updated httpServletResponse object
     */
    String handleBasicCall(String myMethod, HttpServletResponse httpServletResponse, String path) {
        String response = null;
        if ("get".equalsIgnoreCase(myMethod)) {
            //list the map (circuits + get)
            response = getShortCircuitMap(httpServletResponse);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        }
        if ("delete".equalsIgnoreCase(myMethod)) {
            //we should delete all
            //invalidate map (remove all from map) (circuits + delete)
            shortCircuitMap.clear();
            response = getShortCircuitMap(httpServletResponse);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        }
        return response;
    }

    /**
     * Method that handles request to save and load the Short Circuit Map.
     *
     * @param myMethod            POST (for Save) and GET (for Load)
     * @param folder              is the folder to be used
     * @param httpServletResponse is the response object
     * @return with the response body (and with the updated httpServletResponse object
     */
    String handleComplexCall(String myMethod, String folder, HttpServletResponse httpServletResponse) {
        String response = null;
        if ("post".equalsIgnoreCase(myMethod)) {
            //save map (to files) (post + circuits?folder=...)
            String path = logFilePathProvider.getLogFilePath() + "/" + folder + "/";
            response = shortCircuitResponseInformationFileHandler.savePreservedMessagesFromMap(path, fileFactory, fileOutputStreamFactory, httpServletResponse);
        }
        if ("get".equalsIgnoreCase(myMethod)) {
            //load map (from files) (get + circuits?folder=....)
            String path = logFilePathProvider.getLogFilePath() + "/" + folder;
            shortCircuitResponseInformationFileHandler.loadPreservedMessagesToMap(path);
            response = getShortCircuitMap(httpServletResponse);
        }
        return response;
    }

    /**
     * Method that handles request to delete a specific entry from Short Circuit Map.
     *
     * @param myMethod            DELETE for a selected
     * @param idStr              is the identifier of the entry to be deleted
     * @param httpServletResponse is the response object
     * @return with the response body (and with the updated httpServletResponse object
     */
    String handleDeleteById(String myMethod, String idStr, HttpServletResponse httpServletResponse) {
        String response = null;
        if ("delete".equalsIgnoreCase(myMethod)) {
            //first detect if we have a specified id in it
            try {
                Integer id = Integer.parseInt(idStr);
                //remove a specific map entry
                String[] keySet = shortCircuitMap.keySet().toArray(new String[shortCircuitMap.size()]);
                if (keySet.length > id) {
                    //we can delete it
                    String entryKey = keySet[id];
                    shortCircuitMap.remove(entryKey);
                    response = getShortCircuitMap(httpServletResponse);
                    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                } else {
                    //resource cannot be deleted
                    response = "{ \"Cannot found specified entry in Short Circuit Cache\": \"" + idStr + "\" }";
                    httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (NumberFormatException e) {
                response = "{ \"Specified Short Circuit Cache entry is invalid\": \"" + idStr + "\" }";
                httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        return response;
    }

    /**
     * Method that is used to preserve the response when it arrives first time.
     * Note that only responses with 200 response code will be preserved.
     *
     * @param shortCircuitHashCode is the hash code used as key in the Short Circuit Map
     * @param wilmaHttpResponse    is the response object
     * @param parameterList        may contain the timeout value to be used for the entry
     */
    void preserveResponse(String shortCircuitHashCode, WilmaHttpResponse wilmaHttpResponse, ParameterList parameterList) {
        long timeout;
        if (shortCircuitMap.containsKey(shortCircuitHashCode)) { //only if this needs to be handled
            //do we have the response already, or we need to catch it right now?
            ShortCircuitResponseInformation shortCircuitResponseInformation = shortCircuitMap.get(shortCircuitHashCode);
            if (shortCircuitResponseInformation == null) {
                //we need to store the response now
                //but first check if response code is 200, and content type is text based
                String contentType = wilmaHttpResponse.getHeader("Content-Type");
                if (wilmaHttpResponse.getStatusCode() == HttpServletResponse.SC_OK && contentType != null && allowedContentType(contentType)) {
                    String timeoutParameterName = "timeout";
                    if (parameterList != null && parameterList.get(timeoutParameterName) != null) {
                        timeout = Long.valueOf(parameterList.get(timeoutParameterName))
                                + Calendar.getInstance().getTimeInMillis();
                    } else {
                        timeout = Long.MAX_VALUE; //forever
                    }
                    shortCircuitResponseInformation = new ShortCircuitResponseInformation(wilmaHttpResponse, timeout, shortCircuitHashCode);
                    shortCircuitMap.put(shortCircuitHashCode, shortCircuitResponseInformation);
                    //CHECKSTYLE OFF - we must use "new String" here
                    String decodedEntryKey = new String(Base64.decodeBase64(shortCircuitHashCode)); //make it human readable
                    //CHECKSTYLE ON
                    logger.info("ShortCircuit: Message captured for hashcode: " + decodedEntryKey);

                }
            } else { //we have response
                //take care about timeout
                timeout = Calendar.getInstance().getTimeInMillis();
                if (timeout > shortCircuitResponseInformation.getTimeout()) {
                    shortCircuitMap.remove(shortCircuitHashCode);
                    //CHECKSTYLE OFF - we must use "new String" here
                    String decodedEntryKey = new String(Base64.decodeBase64(shortCircuitHashCode)); //make it human readable
                    //CHECKSTYLE ON
                    logger.info("ShortCircuit: Timeout has happened for hashcode: " + decodedEntryKey);
                }
            }
        }
    }

    boolean allowedContentType(final String contentType) {
        return contentType.contains("text/plain") || contentType.contains("application/json") || contentType.contains("text/html") || contentType.contains("text/css");
    }

    /**
     * List the actual status of the Short Circuit Map.
     *
     * @param httpServletResponse is the response object
     * @return with the response body (and with the updated httpServletResponse object
     */
    private String getShortCircuitMap(HttpServletResponse httpServletResponse) {
        StringBuilder response = new StringBuilder("{\n  \"shortCircuitCache\": [\n");
        if (!shortCircuitMap.isEmpty()) {
            String[] keySet = shortCircuitMap.keySet().toArray(new String[shortCircuitMap.size()]);
            for (int i = 0; i < keySet.length; i++) {
                String entryKey = keySet[i];
                ShortCircuitResponseInformation shortCircuitResponseInformation = shortCircuitMap.get(entryKey);
                boolean cached = shortCircuitResponseInformation != null;
                long usageCount = 0;
                if (shortCircuitResponseInformation != null) {
                    usageCount = shortCircuitResponseInformation.getUsageCount();
                }
                //CHECKSTYLE OFF - we must use "new String" here
                String decodedEntryKey = new String(Base64.decodeBase64(entryKey)); //make it human readable
                //CHECKSTYLE ON
                response.append("    { \"id\": \"").append(i)
                        .append("\", \"hashCode\": \"").append(decodedEntryKey)
                        .append("\", \"cached\": ").append(cached)
                        .append(", \"usageCount\": ").append(usageCount)
                        .append(" }");
                if (i < keySet.length - 1) {
                    response.append(",");
                }
                response.append("\n");
            }
        }
        response.append("  ]\n}\n");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        return response.toString();
    }

}
