package com.epam.wilma.message.search.web.controller;

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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for downloading a file from the search result.
 * @author Tamas_Bihari
 *
 */
@Controller
public class ResultFileDownloadController {
    private static final String DEFAULT_ERROR_MESSAGE = "The selected file have been cleaned up in the meanwhile. Please initiate a new search to identify still existing files.";
    private static final String TEXT_PLAIN = "text/plain";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";

    private final Logger logger = LoggerFactory.getLogger(ResultFileDownloadController.class);

    /**
     * Send a file as response and it can be downloaded.
     * @param filePath is a request parameter to point which file desired to download
     * @param response which contains the desired file if it is exists on the server
     */
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public void downloadFile(@RequestParam(value = "filePath") final String filePath, final HttpServletResponse response) {
        addFileToResponse(filePath, response, true);
    }

    /**
     * Send a file as response and it will be opened on a new tab in the browser.
     * @param filePath is a request parameter to point which file desired to download
     * @param response which contains the desired file if it is exists on the server
     */
    @RequestMapping(value = "/openFile", method = RequestMethod.GET)
    public void openFile(@RequestParam(value = "filePath") final String filePath, final HttpServletResponse response) {
        addFileToResponse(filePath, response, false);
    }

    private void addFileToResponse(final String filePath, final HttpServletResponse response, final boolean asAttachment) {
        if (!"".equals(filePath)) {
            try {
                setHeaders(response, filePath, asAttachment);
                writeFileToResponse(filePath, response);
            } catch (IOException ex) {
                logger.info("File downloading failed with file name: " + filePath, ex);
                sendError(ex, response);
            }
        }
    }

    private void writeFileToResponse(final String filePath, final HttpServletResponse response) throws IOException {
        try (InputStream is = new FileInputStream(filePath)) {
            FileCopyUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        }
    }

    private void sendError(final IOException ex, final HttpServletResponse response) {
        try {
            String temp = DEFAULT_ERROR_MESSAGE;
            FileCopyUtils.copy(temp.getBytes(), response.getOutputStream());
        } catch (IOException e) {
            logger.info("Error sending failed when file downloading failed", e);
        }
    }

    private void setHeaders(final HttpServletResponse resp, final String filePath, final boolean asAttachment) {
        String fileName = getFileName(filePath);
        if (asAttachment) {
            resp.setHeader(CONTENT_DISPOSITION, "attachment;filename=\"" + fileName + "\"");
        }
        resp.setContentType(TEXT_PLAIN);
    }

    private String getFileName(final String filePath) {
        String result = "";
        if (filePath.contains("/")) {
            result = filePath.substring(filePath.lastIndexOf("/") + 1);
        } else if (filePath.contains("\\")) {
            result = filePath.substring(filePath.lastIndexOf("\\") + 1);
        }
        return result;
    }
}
