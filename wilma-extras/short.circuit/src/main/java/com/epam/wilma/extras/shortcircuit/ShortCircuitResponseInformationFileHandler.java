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
import com.epam.wilma.common.helper.UniqueIdGenerator;
import com.epam.wilma.webapp.config.servlet.stub.upload.helper.FileOutputStreamFactory;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

/**
 * This class provides file save and load utility for {@link ShortCircuitResponseInformation}.
 *
 * @author tkohegyi
 */
class ShortCircuitResponseInformationFileHandler {

    private static final Object SHORT_CIRCUIT_MAP_GUARD = ShortCircuitChecker.getShortCircuitMapGuard();
    private static final Map<String, ShortCircuitResponseInformation> SHORT_CIRCUIT_MAP = ShortCircuitChecker.getShortCircuitMap();
    private final Logger logger = LoggerFactory.getLogger(ShortCircuitResponseInformationFileHandler.class);

    /**
     * Saves the map to a folder, to preserve it for later use.
     *
     * @param httpServletResponse is the response object
     * @return with the response body - that is a json info about the result of the call
     */
    String savePreservedMessagesFromMap(String path, FileFactory fileFactory, FileOutputStreamFactory fileOutputStreamFactory,
                                        HttpServletResponse httpServletResponse) {
        String response = null;
        String filenamePrefix = "sc" + UniqueIdGenerator.getNextUniqueId() + "_";
        if (!SHORT_CIRCUIT_MAP.isEmpty()) {
            String[] keySet = SHORT_CIRCUIT_MAP.keySet().toArray(new String[SHORT_CIRCUIT_MAP.size()]);
            for (String entryKey : keySet) {
                ShortCircuitResponseInformation information = SHORT_CIRCUIT_MAP.get(entryKey);
                if (information != null) { //save only the cached files
                    //save this into file, folder is in folder variable
                    String filename = path + filenamePrefix + UniqueIdGenerator.getNextUniqueId() + ".json";
                    File file = fileFactory.createFile(filename);
                    try {
                        saveMapObject(fileOutputStreamFactory, file, entryKey, information);
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

    private void saveMapObject(FileOutputStreamFactory fileOutputStreamFactory, File file, String entryKey, ShortCircuitResponseInformation information) throws IOException {
        // if file does not exists, then create it
        if (!file.exists()) {
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        FileOutputStream fos = fileOutputStreamFactory.createFileOutputStream(file);
        fos.write(("{\n  \"Key\": \"" + entryKey + "\",\n").getBytes());
        fos.write(("  \"ResponseCode\": " + information.getStatusCode() + ",\n").getBytes());
        fos.write(("  \"ContentType\": \"" + information.getContentType() + "\",\n").getBytes());
        Map<String, String> headers = information.getHeaders();
        if (headers != null) {
            fos.write("  \"Headers\": [".getBytes());
            int j = 1;
            for (String key : headers.keySet()) {
                fos.write(("    { \"" + key + "\": \"" + encodeString(headers.get(key)) + "\" }").getBytes());
                if (j != headers.size()) {
                    fos.write(",".getBytes());
                }
                fos.write("\n".getBytes());
                j++;
            }
            fos.write("  ],\n".getBytes());
        }
        fos.write("  \"Body\": ".getBytes());
        String myBody = new JSONObject().put("Body", encodeString(information.getBody())).toString();
        fos.write((myBody + "\n}").getBytes());
        fos.close();
    }

    /**
     * Load the map from a folder, and create a new map from it.
     *
     * @param path is the folder to be used, under Wilma's message folder.
     */
    void loadPreservedMessagesToMap(String path) {
        //Map<String, ShortCircuitResponseInformation> newShortCircuitMap = new HashMap<>();

        File folderFile = new File(path);
        File[] listOfFiles = folderFile.listFiles();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".json")) {
                    try {
                        ShortCircuitResponseInformation mapObject = loadMapObject(listOfFiles[i].getAbsolutePath());
                        if (mapObject != null) {
                            synchronized (SHORT_CIRCUIT_MAP_GUARD) {
                                SHORT_CIRCUIT_MAP.put(mapObject.getHashCode(), mapObject);
                            }
                        }
                    } catch (JSONException e) {
                        logger.info("Cannot load JSON file to Short Circuit map: " + listOfFiles[i].getAbsolutePath() + ", error:" + e.getLocalizedMessage());
                    }
                }
            }
        }
    }

    private ShortCircuitResponseInformation loadMapObject(String fileName) {
        ShortCircuitResponseInformation result = null;
        File file = new File(fileName);
        if (file.exists()) {
            //load the file
            String fileContent = loadFileToString(fileName);
            if (fileContent != null) {
                JSONObject obj = new JSONObject(fileContent);
                String hashKey = obj.getString("Key");
                int responseCode = obj.getInt("ResponseCode");
                String contentType = obj.getString("ContentType");
                String body = decodeString(obj.getJSONObject("Body").getString("Body"));
                JSONArray headerArray = obj.getJSONArray("Headers");
                ShortCircuitResponseInformation information = null;
                if (hashKey != null && contentType != null && body != null) {
                    information = new ShortCircuitResponseInformation(Long.MAX_VALUE);
                    information.setHashCode(hashKey);
                    information.setStatusCode(responseCode);
                    information.setContentType(contentType);
                    information.setBody(body);
                }
                if (headerArray != null && information != null) {
                    Map<String, String> headers = new HashMap<>();
                    for (int i = 0; i < headerArray.length(); i++) {
                        JSONObject o = headerArray.getJSONObject(i);
                        Iterator j = o.keys();
                        while (j.hasNext()) {
                            String key = (String) j.next();
                            headers.put(key, decodeString(o.getString(key)));
                        }
                    }
                    information.setHeaders(headers);
                }
                result = information;
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

    private String encodeString(final String toBeEncoded) {
        //CHECKSTYLE OFF - ignoring new String() error, as this is the most effective implementation
        return new String(Base64.encodeBase64(toBeEncoded.getBytes()));
        //CHECKSTYLE ON
    }

    private String decodeString(final String toBeDecoded) {
        //CHECKSTYLE OFF - ignoring new String() error, as this is the most effective implementation
        return new String(Base64.decodeBase64(toBeDecoded.getBytes()));
        //CHECKSTYLE ON
    }
}
