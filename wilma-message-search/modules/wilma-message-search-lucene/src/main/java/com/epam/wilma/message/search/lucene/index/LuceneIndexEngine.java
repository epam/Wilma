package com.epam.wilma.message.search.lucene.index;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.message.search.lucene.index.helper.FileFactory;

/**
 * Indexes all text files under the given directory to the given folder
 * with using Lucene indexer mechanism.
 * @author Tamas_Bihari
 *
 */
@Component
public class LuceneIndexEngine {

    private final Logger logger = LoggerFactory.getLogger(LuceneIndexEngine.class);

    @Autowired
    private FolderIndexer folderIndexer;
    @Autowired
    private FileFactory fileFactory;

    /**
     * Indexes all text files under the given directory(if the directory exists) to the given folder
     * with using Lucene indexer mechanism. If the directory cannot be found
     * an error message will be logged.
     * @param docsPath the folder containing the files to be indexed
     */
    public void createIndex(final String docsPath) {
        final File docDir = fileFactory.createFile(docsPath);
        if (!docDir.exists() || !docDir.canRead()) {
            logger.error("Document directory '" + docDir.getAbsolutePath() + "' does not exist or is not readable, please check the path");
        } else {
            indexFolder(docDir);
        }
    }

    /**
     * Adds a new file to the index. If the file already exists in the index,
     * then it will be overwritten.
     * @param fileName the name of the file to be indexed
     */
    public void addFileToIndex(final String fileName) {
        File file = fileFactory.createFile(fileName);
        indexFolder(file);
    }

    private void indexFolder(final File docDir) {
        folderIndexer.indexFolder(docDir);
    }

}
