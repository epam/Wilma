package com.epam.wilma.message.search.web.controller;
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.epam.wilma.message.search.web.service.LogFileProvider;

/**
 * Controller for accessing the application log files.
 * @author Adam_Csaba_Kiraly
 *
 */
@Controller
public class AppLogController {

    private static final String JSON_NAME = "files";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String ATTACHMENT_TEMPLATE = "attachment; filename=%s";
    @Autowired
    private LogFileProvider logFileProvider;

    /**
     * Serves the applog page.
     * @return the name of the applog jsp file
     */
    @RequestMapping(value = "/applog", method = RequestMethod.GET)
    public String applog() {
        return "applog";
    }

    /**
     * Gets the list of log files.
     * @return with the list of log files as a JSON response
     */
    @ResponseBody
    @RequestMapping(value = "/logs", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Collection<String>> getLogFiles() {
        Map<String, Collection<String>> jsonResponse = new HashMap<>();
        jsonResponse.put(JSON_NAME, logFileProvider.getLogFileNames());
        return jsonResponse;
    }

    /**
     * Gets the content of the log file.
     * @param fileName the name of the log file
     * @param source true if the content should be written directly, false for attachment
     * @param userAgent the User-Agent of the request header
     * @return the content of the log file
     */
    @RequestMapping(value = "/logs/{fileName:.+}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> getLogFileContent(@PathVariable("fileName") final String fileName,
            @RequestParam(value = "source", defaultValue = "false") final boolean source,
            @RequestHeader(value = "User-Agent", defaultValue = "") final String userAgent) {
        String body = logFileProvider.getLogContent(fileName);
        body = convertLineBreaksIfOnWindows(body, userAgent);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        if (!source) {
            headers.set(CONTENT_DISPOSITION, String.format(ATTACHMENT_TEMPLATE, fileName));
        }
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(body, headers, HttpStatus.OK);
        return responseEntity;
    }

    private String convertLineBreaksIfOnWindows(final String body, final String userAgent) {
        String result = body;
        if (userIsOnWindows(userAgent)) {
            result = body.replace("\r", "").replace("\n", "\r\n");
        }
        return result;
    }

    private boolean userIsOnWindows(final String userAgent) {
        return userAgent.toLowerCase().contains("windows");
    }
}
