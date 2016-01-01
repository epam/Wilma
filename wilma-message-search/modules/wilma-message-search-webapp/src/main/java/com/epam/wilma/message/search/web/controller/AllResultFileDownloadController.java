package com.epam.wilma.message.search.web.controller;
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

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.epam.wilma.message.search.web.support.FileZipper;

/**
 * Controller for downloading previous search result's files in a compressed ZIP.
 * @author Tamas_Bihari
 *
 */
@Controller
public class AllResultFileDownloadController {
    private static final String SEARCH_RESULT_KEY = "searchResult";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final Logger LOGGER = LoggerFactory.getLogger(AllResultFileDownloadController.class);

    @Autowired
    private FileZipper fileZipper;

    /**
     * Compress the previously found files into one ZIP and sends it as response.
     * @param session the {@link HttpSession} instance to get previous search result
     * @param response which contains the compressed ZIP with the results
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/downloadAll")
    public void downloadFiles(final HttpSession session, final HttpServletResponse response) {
        List<List<String>> searchResultWithPairs = (List<List<String>>) session.getAttribute(SEARCH_RESULT_KEY);
        if (searchResultWithPairs != null && !searchResultWithPairs.isEmpty()) {
            writeZipToResponse(response, searchResultWithPairs);
            clearSearchResultFromSession(session);
        }
    }

    private void writeZipToResponse(final HttpServletResponse response, final List<List<String>> searchResultWithPairs) {
        try {
            response.setHeader(CONTENT_DISPOSITION, "attachment;filename=\"searchResult.zip\"");
            fileZipper.createZipWithFiles(searchResultWithPairs, response.getOutputStream());
            LOGGER.info("Previous search result successfully zipped! Files:" + searchResultWithPairs);
        } catch (IOException e) {
            LOGGER.warn("Downloadable ZIP can not be created from search result!", e);
            sendError(response);
        }
    }

    private void clearSearchResultFromSession(final HttpSession session) {
        session.removeAttribute(SEARCH_RESULT_KEY);
    }

    private void sendError(final HttpServletResponse response) {
        try {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IOException e1) {
            LOGGER.warn("Sending ERROR failed when downloadable ZIP can not be created from search result!", e1);
        }
    }
}
