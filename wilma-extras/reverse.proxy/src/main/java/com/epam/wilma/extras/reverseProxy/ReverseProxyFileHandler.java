package com.epam.wilma.extras.reverseProxy;
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
import com.epam.wilma.common.helper.UniqueIdGenerator;
import com.epam.wilma.webapp.config.servlet.stub.upload.helper.FileOutputStreamFactory;
import org.json.JSONException;
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
import java.util.Map;

/**
 * This class provides file save and load utility for the map of {@link ReverseProxyInformation}.
 *
 * @author tkohegyi
 */
class ReverseProxyFileHandler {

    static final Map<String, ReverseProxyInformation> REVERSE_PROXY_INFORMATION_MAP = new HashMap<>();
    static final Object GUARD = new Object();

    private final Logger logger = LoggerFactory.getLogger(ReverseProxyFileHandler.class);

    @Autowired
    private LogFilePathProvider logFilePathProvider;
    @Autowired
    private FileFactory fileFactory;
    @Autowired
    private FileOutputStreamFactory fileOutputStreamFactory;

    /**
     * Saves the map to a folder, to preserve it for later use.
     *
     * @param folder is the folder to be used to save the map of reverse proxy entries
     * @param httpServletResponse is the response object
     * @return with the response body - that is a json info about the result of the call
     */
    String saveReverseProxyInformationMap(String folder, HttpServletResponse httpServletResponse) {
        String path = logFilePathProvider.getLogFilePath() + "/" + folder + "/";
        String response = null;
        String filenamePrefix = "sc" + UniqueIdGenerator.getNextUniqueId() + "_";
        if (!REVERSE_PROXY_INFORMATION_MAP.isEmpty()) {
            String[] keySet = REVERSE_PROXY_INFORMATION_MAP.keySet().toArray(new String[REVERSE_PROXY_INFORMATION_MAP.size()]);
            for (String entryKey : keySet) {
                ReverseProxyInformation information = REVERSE_PROXY_INFORMATION_MAP.get(entryKey);
                if (information != null && information.isValid()) {
                    //save this into file, folder is in folder variable
                    String filename = path + filenamePrefix + UniqueIdGenerator.getNextUniqueId() + ".json";
                    File file = fileFactory.createFile(filename);
                    try {
                        saveMapObject(fileOutputStreamFactory, file, entryKey, information);
                    } catch (IOException e) {
                        String message = "ReverseProxy: save failed at file: " + filename + ", with message: " + e.getLocalizedMessage();
                        logger.info("ReverseProxy: " + message);
                        response = "{ \"resultsFailure\": \"" + message + "\" }";
                        httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        break;
                    }
                }
            }
        }
        if (response == null) {
            String message = "ReverseProxy rule map saved as: " + path + filenamePrefix + "*.json files";
            response = "{ \"resultsSuccess\": \"" + message + "\" }";
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            logger.info("ReverseProxy: " + message);
        }
        return response;
    }

    private void saveMapObject(FileOutputStreamFactory fileOutputStreamFactory, File file, String entryKey, ReverseProxyInformation information) throws IOException {
        // if file does not exists, then create it
        if (!file.exists()) {
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        FileOutputStream fos = fileOutputStreamFactory.createFileOutputStream(file);
        fos.write(("{\n  \"identifier\": \"" + entryKey + "\",\n").getBytes());
        fos.write(("  \"originalTarget\": \"" + information.getOriginalTarget() + "\",\n").getBytes());
        fos.write(("  \"realTarget\": \"" + information.getRealTarget() + "\"\n}\n").getBytes());
        fos.close();
    }

    /**
     * Load the map from a folder, and create a new map from it.
     *
     * @param folder is the folder to be used, under Wilma's message folder.
     */
    void loadReverseProxyInformationMap(String folder) {
        String path = logFilePathProvider.getLogFilePath() + "/" + folder;
        File folderFile = new File(path);
        File[] listOfFiles = folderFile.listFiles();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".json")) {
                    try {
                        ReverseProxyInformation mapObject = loadMapObject(listOfFiles[i].getAbsolutePath());
                        if (mapObject != null) {
                            synchronized (REVERSE_PROXY_INFORMATION_MAP) {
                                REVERSE_PROXY_INFORMATION_MAP.put(mapObject.getIdentifier(), mapObject);
                            }
                        }
                    } catch (JSONException e) {
                        logger.info("Cannot load JSON file to Reverse Proxy map: " + listOfFiles[i].getAbsolutePath() + ", error:" + e.getLocalizedMessage());
                    }
                }
            }
        }
    }

    private ReverseProxyInformation loadMapObject(String fileName) {
        ReverseProxyInformation result = null;
        File file = new File(fileName);
        if (file.exists()) {
            //load the file
            String fileContent = loadFileToString(fileName);
            if (fileContent != null) {
                JSONObject obj = new JSONObject(fileContent);
                String identifier = obj.getString("identifier");
                String originalTarget = obj.getString("originalTarget");
                String realTarget = obj.getString("realTarget");
                ReverseProxyInformation information = null;
                if (identifier != null && originalTarget != null && realTarget != null) {
                    information = new ReverseProxyInformation(identifier, originalTarget, realTarget);
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

}
