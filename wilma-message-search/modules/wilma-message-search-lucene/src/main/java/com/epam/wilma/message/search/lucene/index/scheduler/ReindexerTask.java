package com.epam.wilma.message.search.lucene.index.scheduler;
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

import org.apache.lucene.index.IndexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.epam.wilma.message.search.domain.IndexStatus;
import com.epam.wilma.message.search.lucene.LuceneEngine;
import com.epam.wilma.message.search.lucene.configuration.LuceneConfigurationAccess;
import com.epam.wilma.message.search.properties.helper.MessageFoldersUtil;

/**
 * Deletes and recreates the Lucene indexes.
 * @author Adam_Csaba_Kiraly
 */
@Component
public class ReindexerTask implements Runnable {

    private static final String STARTING_INDEXING = "Starting indexing.";
    private static final String INDEXING_FINISHED = "Indexing finished.";

    private static final String DELETING_DOCUMENTS_MESSAGE = "Deleting indexes.";
    private static final String DELETED_DOCUMENTS_MESSAGE = "Deleted indexes.";

    private static final String ERROR_MESSAGE = "Error occurred while reindexing.";

    private final Logger logger = LoggerFactory.getLogger(ReindexerTask.class);

    @Autowired
    @Qualifier("indexWriter")
    private IndexWriter indexWriter;
    @Autowired
    private LuceneEngine luceneEngine;
    @Autowired
    private LuceneConfigurationAccess configurationAccess;
    @Autowired
    private MessageFoldersUtil messageFoldersUtil;
    @Autowired
    private IndexStatus indexStatus;

    @Override
    public void run() {
        try {
            deleteIndex();
            buildIndex();
        } catch (IOException e) {
            logger.error(ERROR_MESSAGE, e);
        }
    }

    private void deleteIndex() throws IOException {
        logger.info(DELETING_DOCUMENTS_MESSAGE);
        indexWriter.deleteAll();
        doCommit();
        indexStatus.setReady(false);
        logger.info(DELETED_DOCUMENTS_MESSAGE);
    }

    private void buildIndex() throws IOException {
        logger.info(STARTING_INDEXING);
        String[] folders = messageFoldersUtil.getFolders(configurationAccess.getProperties().getMessageDirectories());
        for (String folder : folders) {
            luceneEngine.buildIndex(folder);
        }
        doCommit();
        indexStatus.setReady(true);
        logger.info(INDEXING_FINISHED);
    }

    /**
     * This method commits the pending changes to the index.
     * @throws IOException is thrown if the IndexWriter throws IOException.
     */
    protected void doCommit() throws IOException {
        indexWriter.commit();
    }
}
