package com.epam.wilma.message.search.web.support;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Class for files checking whether they exists or not.
 * @author Tibor_Kovacs
 *
 */
@Component
public class FileChecker {
    private static final String RESP_FILE_END = "resp.txt";
    private static final String REQ_FILE_END = "req.txt";
    private static final String ERROR_POSTFIX = "NOTEXISTS";
    private final Logger logger = LoggerFactory.getLogger(FileChecker.class);

    /**
     * Mark those files which not exists with ERROR_POSTFIX.
     * @param searchResult contains those files which need to check
     * @return the modified list of searched files
     */
    public List<List<String>> checkFilesExistsWithPairs(final List<String> searchResult) {
        List<List<String>> result = new ArrayList<>();
        for (String actualFile : searchResult) {
            List<String> actualPair = new ArrayList<>();
            actualPair = markFileIfNotExists(actualFile, actualPair);
            String pair = getPair(actualFile);
            actualPair = markFileIfNotExists(pair, actualPair);
            result.add(actualPair);
        }
        return result;
    }

    private List<String> markFileIfNotExists(final String actualFile, final List<String> pair) {
        List<String> result = pair;
        if (new File(actualFile).exists()) {
            result.add(actualFile);
        } else {
            result.add(actualFile.concat(ERROR_POSTFIX));
            logger.debug(actualFile + " does not exist!");
        }
        return result;
    }

    private String getPair(final String filePath) {
        String pairFilePath = "";
        if (filePath.endsWith(REQ_FILE_END)) {
            pairFilePath = filePath.replace(REQ_FILE_END, RESP_FILE_END);
        } else if (filePath.endsWith(RESP_FILE_END)) {
            pairFilePath = filePath.replace(RESP_FILE_END, REQ_FILE_END);
        }
        return pairFilePath;
    }

}
