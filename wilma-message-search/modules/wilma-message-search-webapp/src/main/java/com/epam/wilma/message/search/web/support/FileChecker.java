package com.epam.wilma.message.search.web.support;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Class for files checking whether they exist or not.
 *
 * @author Tibor_Kovacs
 */
@Component
public class FileChecker {
    private static final String RESP_FILE_END = "resp.txt";
    private static final String REQ_FILE_END = "req.txt";
    private static final String ERROR_POSTFIX = "DOES_NOT_EXIST";
    private final Logger logger = LoggerFactory.getLogger(FileChecker.class);

    /**
     * Mark those files which does not exist with ERROR_POSTFIX.
     * @param searchResult contains those files which need to check
     * @return the modified list of searched files
     */
    public List<List<String>> checkFilesExistsWithPairs(final List<String> searchResult) {
        List<List<String>> result = new ArrayList<>();
        for (String actualFile : searchResult) {
            List<String> actualPair = new ArrayList<>();
            markFileIfNotExists(actualFile, actualPair); //adds actualFile to actualPair, mark it with "does not exist" if necessary
            String pairFile = getPair(actualFile);
            markFileIfNotExists(pairFile, actualPair); //adds pairFile to actualPair, mark it with "does not exist" if necessary
            result.add(actualPair);
        }
        return result;
    }

    private void markFileIfNotExists(final String actualFile, final List<String> pair) {
        if (new File(actualFile).exists()) {
            pair.add(actualFile);
        } else {
            pair.add(actualFile.concat(ERROR_POSTFIX));
            logger.debug(actualFile + " does not exist!");
        }
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
