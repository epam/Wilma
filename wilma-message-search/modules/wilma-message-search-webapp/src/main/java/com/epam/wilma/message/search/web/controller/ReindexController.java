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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.epam.wilma.message.search.lucene.index.scheduler.IndexTaskScheduler;

/**
 * Provides access to manually call the reindexing functionality.
 * @author Adam_Csaba_Kiraly
 *
 */
@Controller
public class ReindexController {

    private final Logger logger = LoggerFactory.getLogger(ReindexController.class);

    @Autowired
    private IndexTaskScheduler indexTaskScheduler;

    /**
     * Runs reindexing on demand and writes back a response.
     * @param response used for writing back a response
     */
    @RequestMapping(value = "/reindex", method = RequestMethod.GET)
    public void reindex(final HttpServletResponse response) {
        indexTaskScheduler.runReindexOnDemand();
        PrintWriter writer;
        try {
            writer = response.getWriter();
            writer.write("Reindexing!");
            writer.flush();
        } catch (IOException e) {
            logger.info("error occurred when accessing /reindex", e);
        }
    }
}
