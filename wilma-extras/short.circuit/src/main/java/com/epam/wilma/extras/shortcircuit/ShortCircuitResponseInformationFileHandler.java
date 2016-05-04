package com.epam.wilma.extras.shortcircuit;
/*==========================================================================
Copyright 2013-2016 EPAM Systems

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
import com.epam.wilma.common.helper.UniqueIdGenerator;
import com.epam.wilma.webapp.config.servlet.stub.upload.helper.FileOutputStreamFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides file save and load utility for {@link ShortCircuitResponseInformation}.
 *
 * @author tkohegyi
 */
class ShortCircuitResponseInformationFileHandler {

    private static final Object SHORT_CIRCUIT_MAP_GUARD = ShortCircuitChecker.getShortCircuitMapGuard();
    private static Map<String, ShortCircuitResponseInformation> shortCircuitMap = ShortCircuitChecker.getShortCircuitMap();
    private final Logger logger = LoggerFactory.getLogger(ShortCircuitResponseInformationFileHandler.class);
    private final int e500 = 500;


    @Autowired
    private LogFilePathProvider logFilePathProvider;
    @Autowired
    private FileFactory fileFactory;
    @Autowired
    private FileOutputStreamFactory fileOutputStreamFactory;

    /**
     * Saves the map to a folder, to preserve it for later use.
     *
     * @param httpServletResponse is the response object
     * @param folder              is the folder to be used, under Wilma'S message folder.
     * @return with the response body - that is a json info about the result of the call
     */
    String savePreservedMessagesFromMap(HttpServletResponse httpServletResponse, String folder) {
        String response = null;
        String path = logFilePathProvider.getLogFilePath() + "/" + folder + "/";
        String filenamePrefix = "sc" + UniqueIdGenerator.getNextUniqueId() + "_";
        if (!shortCircuitMap.isEmpty()) {
            String[] keySet = shortCircuitMap.keySet().toArray(new String[shortCircuitMap.size()]);
            for (String entryKey : keySet) {
                ShortCircuitResponseInformation information = shortCircuitMap.get(entryKey);
                if (information != null) { //save only the cached files
                    //save this into file, folder is in folder variable
                    String filename = path + filenamePrefix + UniqueIdGenerator.getNextUniqueId() + ".json";
                    File file = fileFactory.createFile(filename);
                    try {
                        saveMapObject(file, entryKey, information);
                    } catch (IOException e) {
                        String message = "Cache save failed at file: " + filename + ", with message: " + e.getLocalizedMessage();
                        logger.info("ShortCircuit: " + message);
                        response = "{ \"resultsFailure\": \"" + message + "\" }";
                        httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        break;
                    }
                }
            }
        }
        if (response == null) {
            String message = "Cache saved as: " + path + filenamePrefix + "*.json files";
            response = "{ \"resultsSuccess\": \"" + message + "\" }";
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            logger.info("ShortCircuit: " + message);
        }
        return response;
    }

    /**
     * Load the map from a folder, and create a new map from it.
     *
     * @param folder              is the folder to be used, under Wilma'S message folder.
     */
    void loadPreservedMessagesToMap(String folder) {
        Map<String, ShortCircuitResponseInformation> newShortCircuitMap = new HashMap<>();

        String path = logFilePathProvider.getLogFilePath() + "/" + folder;
        File folderFile = new File(path);
        File[] listOfFiles = folderFile.listFiles();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    String pattern = "sc*.json";
                    // Create a Pattern object
                    Pattern r = Pattern.compile(pattern);
                    // Now create matcher object.
                    Matcher m = r.matcher(listOfFiles[i].getName());
                    if (m.find()) {
                        ShortCircuitResponseInformation mapObject = loadMapObject(listOfFiles[i].getName());
                        if (mapObject != null) {
                            newShortCircuitMap.put(Integer.toString(i), mapObject);
                        }
                    }
                }
            }
            //adding the new map object
            synchronized (SHORT_CIRCUIT_MAP_GUARD) {
                shortCircuitMap.putAll(newShortCircuitMap);
            }
        }
    }

    private ShortCircuitResponseInformation loadMapObject(String fileName) {
        ShortCircuitResponseInformation result = null;
        File file = new File(fileName);
        if (file.exists()) {
            //load the file
            String fileContent = loadFileToString(fileName);
            if (fileContent != null) { //TODO - this part can fail even if the string is not null
                JSONObject obj = new JSONObject(fileContent);
                String hashKey = obj.getString("Key");
                int responseCode;
                try {
                    responseCode = Integer.valueOf(obj.getString("ResponseCode"));
                } catch (NumberFormatException e) {
                    responseCode = e500; //E500 if cannot parse the response code
                }
                String contentType = obj.getString("ContentType");
                String body = obj.getJSONObject("Body").getString("Body");
                JSONArray headerArray = obj.getJSONArray("Headers");
                if (hashKey != null && contentType != null && body != null && headerArray != null) {
                    ShortCircuitResponseInformation information = new ShortCircuitResponseInformation(Integer.MAX_VALUE);
                    information.setHashCode(hashKey);
                    information.setStatusCode(responseCode);
                    information.setContentType(contentType);
                    information.setBody(body);
                    Map<String, String> headers = new HashMap<>();
                    for (int i = 0; i < headerArray.length(); i++) {
                        JSONObject o = headerArray.getJSONObject(i);
                        Iterator j = o.keys();
                        while (j.hasNext()) {
                            String key = (String) j.next();
                            headers.put(key, o.getString(key));
                        }
                    }
                    information.setHeaders(headers);
                    result = information;
                }
            }
        }
        return result;
    }

    private String loadFileToString(final String fileName) {
        String text;
        try {
            //CHECKSTYLE OFF - ignoring new String() error, as this is the most effective implementation
            text = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
            //CHECKSTYLE ON
        } catch (IOException e) {
            //cannot read a file
            text = null;
        }
        return text;
    }

    private void saveMapObject(File file, String entryKey, ShortCircuitResponseInformation information) throws IOException {
        // if file does not exists, then create it
        if (!file.exists()) {
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        FileOutputStream fos = fileOutputStreamFactory.createFileOutputStream(file);
        fos.write(("{\n  \"Key\": \"" + entryKey + "\",\n").getBytes());
        fos.write(("  \"ResponseCode\": \"" + information.getStatusCode() + "\",\n").getBytes());
        fos.write(("  \"ContentType\": \"" + information.getContentType() + "\",\n").getBytes());
        Map<String, String> headers = information.getHeaders();
        fos.write("  \"Headers\": [".getBytes());
        int j = 1;
        for (String key : headers.keySet()) {
            fos.write(("    { \"" + key + "\": \"" + headers.get(key) + "\" }").getBytes());
            if (j != headers.size()) {
                fos.write(",".getBytes());
            }
            fos.write("\n".getBytes());
            j++;
        }
        fos.write("  ],\n  \"Body\": ".getBytes());
        String myBody = new JSONObject().put("Body", information.getBody()).toString();
        fos.write((myBody + "\n}").getBytes());
        fos.close();
    }

}
