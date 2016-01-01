package com.epam.wilma.message.search.lucene.configuration;
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

/**
 * Holds module specific properties.
 * @author Tunde_Kovacs
 *
 */
public class PropertyDto {

    private final String indexDirectory;
    private final String messageDirectories;
    private final String reindexTimer;

    /**
     * Constructs a new property holding object with the given fields.
     * @param indexDirectory the directory lucene will store the index files
     * @param messageDirectories the directory which holds the messages which will get indexed
     * @param reindexTimer the cron expression of the reindexing timer
     */
    public PropertyDto(final String indexDirectory, final String messageDirectories, final String reindexTimer) {
        this.indexDirectory = indexDirectory;
        this.messageDirectories = messageDirectories;
        this.reindexTimer = reindexTimer;
    }

    public String getMessageDirectories() {
        return messageDirectories;
    }

    public String getReindexTimer() {
        return reindexTimer;
    }

    public String getIndexDirectory() {
        return indexDirectory;
    }

}
