package com.epam.wilma.message.search.lucene;
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

import java.util.List;

/**
 * Provides indexing and searching capabilities with lucene.
 * @author Tunde_Kovacs
 *
 */
public interface LuceneEngine {

    /**
     * Searches for the given text in the index.
     * @param text which we search for
     * @return with the file names where the text matches found. If there is no match will return with empty list.
     */
    List<String> search(String text);

    /**
     * Adds a new file to the index. If the file already exists in the index,
     * then it will be overwritten.
     * @param fileName the name of the file to be indexed
     * The file name must be provided with an absolute path.
     */
    void addFileToIndex(String fileName);

    /**
     * Deletes a file from the index with the given file name.
     * @param fileName the file to be deleted from the index.
     * The file name must be provided with an absolute path.
     */
    void deleteFileFromIndex(String fileName);

    /**
     * Indexes all text files under the given directory(if the directory exists) to the given folder
     * with using Lucene indexer mechanism. If the directory cannot be found
     * an error message will be logged.
     * @param rootDirectory the folder containing the files to be indexed
     */
    void buildIndex(String rootDirectory);
}
