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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.epam.wilma.message.search.domain.IndexStatus;
import com.epam.wilma.message.search.domain.exception.QueryCannotBeParsedException;
import com.epam.wilma.message.search.lucene.LuceneEngine;
import com.epam.wilma.message.search.web.support.FileChecker;
import com.epam.wilma.message.search.web.support.FileZipper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Controller for message searching.
 * @author Tamas_Bihari
 *
 */
@Controller
public class SearchController {
    private static final String QUERY_ERROR_MESSAGE = "This query is not valid.";
    private static final String ZIP_CONTENT_TYPE = "application/zip";
    private static final String FILES = "files";
    private final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private LuceneEngine luceneEngine;
    @Autowired
    private FileZipper fileZipper;
    @Autowired
    private FileChecker fileChecker;
    @Autowired
    private IndexStatus indexStatus;

    /**
     * Searches for an expression using {@link LuceneEngine} and sends back matched files in a compressed ZIP file.
     * @param searchedText the expression that will be searched
     * @param resp is the HTTP response for the request
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public void searchAndZip(@RequestParam("text") final String searchedText, final HttpServletResponse resp) {
        if (searchedText != null && !"".equals(searchedText)) {
            List<List<String>> searchResult;
            try {
                searchResult = searchForText(searchedText);
            } catch (QueryCannotBeParsedException e) {
                logger.warn("Invalid query");
                searchResult = new ArrayList<List<String>>();
            }
            zipSearchResult(searchedText, resp, searchResult);
        }
    }

    /**
     * Searches for an expression using {@link LuceneEngine} and sends back matched files' name.
     * @param searchedText the expression that will be searched
     * @param session is the actual HTTP session
     * @return list of hits as a JSON response
     */
    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseEntity<String> searchForFiles(@RequestParam("searchedText") final String searchedText, final HttpSession session) {
        List<List<String>> searchResult = new ArrayList<List<String>>();
        ResponseEntity<String> result;
        HttpHeaders responseHeaders = setHeadersForJSON();
        try {
            searchResult = searchForText(searchedText);
            addResultToSession(searchResult, session);
            if (!indexStatus.isReady()) {
                result = new ResponseEntity<String>(getJson(searchResult), responseHeaders, HttpStatus.PARTIAL_CONTENT);
            } else {
                result = new ResponseEntity<String>(getJson(searchResult), responseHeaders, HttpStatus.CREATED);
            }
        } catch (QueryCannotBeParsedException e) {
            logger.warn("Invalid query");
            result = new ResponseEntity<String>(QUERY_ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
        }
        return result;
    }

    private List<List<String>> searchForText(final String searchedText) {
        List<String> searchResult = new ArrayList<>();
        logger.info("Searching with GET for text:" + searchedText);
        searchResult = luceneEngine.search(searchedText);
        return fileChecker.checkFilesExistsWithPairs(searchResult);
    }

    private void zipSearchResult(final String searchedText, final HttpServletResponse resp, final List<List<String>> searchResult) {
        try {
            ServletOutputStream output = resp.getOutputStream();
            resp.setContentType(ZIP_CONTENT_TYPE);
            fileZipper.createZipWithFiles(searchResult, output);
        } catch (IOException e) {
            logger.info("Searching for '" + searchedText + "' failed, response's output stream can not get!", e);
        }
    }

    private HttpHeaders setHeadersForJSON() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return responseHeaders;
    }

    private void addResultToSession(final List<List<String>> searchResult, final HttpSession session) {
        session.setAttribute("searchResult", searchResult);
    }

    private String getJson(final Object object) {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(FILES, gson.toJsonTree(object));
        String json = gson.toJson(jsonObject);
        return json;
    }
}
