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

import com.epam.wilma.message.search.lucene.index.helper.FileWrapperFactory;
import com.epam.wilma.message.search.lucene.index.helper.FileWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Processes through a folder structure and sends files files to {@link FileIndexer}.
 *
 * @author Tamas_Bihari
 */
@Component
public class FolderIndexer {

    @Autowired
    private FileIndexer fileIndexer;

    @Autowired
    private FileWrapperFactory fileWrapperFactory;

    /**
     * Processes through a folder structure and send files to {@link FileIndexer}.
     * @param file is the given file, folder or folder structure which will be processed
     */
    public void indexFolder(final FileWrapper file) {
        // do not try to index files that cannot be read
        if (file.canRead()) {
            if (file.isDirectory()) {
                processFolder(file, file.list());
            } else {
                fileIndexer.indexFile(file);
            }
        }
    }

    private void processFolder(final FileWrapper file, final String[] fileList) {
        if (fileList != null) {
            for (int i = 0; i < fileList.length; i++) {
                FileWrapper newFile = fileWrapperFactory.createFile(file, fileList[i]);
                indexFolder(newFile);
            }
        }
    }
}
