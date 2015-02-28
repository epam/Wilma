package com.epam.wilma.message.search.lucene.helper;
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
import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;

import com.epam.wilma.message.search.lucene.configuration.LuceneConfigurationAccess;
import com.epam.wilma.message.search.lucene.configuration.PropertyDto;

/**
 * Creates an {@link FSDirectory} instance for Lucene components.
 * @author Tamas_Bihari
 *
 */
public class FSDirectoryFactory {

    @Autowired
    private LuceneConfigurationAccess configAccess;

    /**
     * Creates a {@link FSDirectory} instance using the given path.
     * @return with the created {@link Directory} instance
     * @throws IOException when the given path does not exist
     */
    public Directory createDirectory() throws IOException {
        String indexDirectory = getIndexDirectory();
        return FSDirectory.open(new File(indexDirectory));
    }

    private String getIndexDirectory() {
        PropertyDto properties = configAccess.getProperties();
        String indexDirectory = properties.getIndexDirectory();
        return indexDirectory;
    }
}
