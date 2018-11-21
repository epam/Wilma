package com.epam.wilma.indexing.jms.add;
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

import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.indexing.domain.IndexMessage;
import com.epam.wilma.indexing.domain.IndexingType;
import com.epam.wilma.indexing.jms.JmsMessageIndexer;
import com.epam.wilma.indexing.jms.helper.FileNameProvider;
import com.epam.wilma.indexing.jms.helper.IndexMessageFactory;

/**
 * Extracts file name information from  a {@link WilmaHttpResponse} and creates an
 * {@link IndexMessage} that will be sent to a {@link JmsMessageIndexer}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class JmsResponseIndexingProcessor {

    @Autowired
    private FileNameProvider fileNameProvider;
    @Autowired
    private JmsMessageIndexer messageIndexer;
    @Autowired
    private IndexMessageFactory indexMessageFactory;

    /**
     * Extracts file name information from  a {@link WilmaHttpResponse} and creates an
     * {@link IndexMessage} that will be sent to a {@link JmsMessageIndexer}.
     * @param response to be processed
     */
    public void process(final WilmaHttpResponse response) {
        String fileName = fileNameProvider.getFileName(response);
        IndexMessage message = indexMessageFactory.createIndexMessage(fileName, IndexingType.ADD.getName());
        messageIndexer.sendMessageToIndexer(message);
    }

}
