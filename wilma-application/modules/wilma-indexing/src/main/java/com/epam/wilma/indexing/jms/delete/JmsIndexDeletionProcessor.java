package com.epam.wilma.indexing.jms.delete;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.indexing.domain.IndexMessage;
import com.epam.wilma.indexing.domain.IndexingType;
import com.epam.wilma.indexing.jms.JmsMessageIndexer;
import com.epam.wilma.indexing.jms.helper.IndexMessageFactory;

/**
 *Creates an {@link IndexMessage} that will be sent to a {@link JmsMessageIndexer}
 * for deletion.
 * @author Tunde_Kovacs
 *
 */
@Component
public class JmsIndexDeletionProcessor {

    @Autowired
    private JmsMessageIndexer messageIndexer;
    @Autowired
    private IndexMessageFactory indexMessageFactory;

    /**
     * Creates an {@link IndexMessage} that will be sent to a {@link JmsMessageIndexer}
     * for deletion.
     * @param fileName the name of the file to be sent for deletion
     */
    public void process(final String fileName) {
        IndexMessage message = indexMessageFactory.createIndexMessage(fileName, IndexingType.DELETE.getName());
        messageIndexer.sendMessageToIndexer(message);
    }

}
