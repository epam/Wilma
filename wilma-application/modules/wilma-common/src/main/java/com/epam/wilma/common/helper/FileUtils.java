package com.epam.wilma.common.helper;

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
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Class for wrapping {@link FileUtils} static methods.
 * @author Tamas_Bihari
 *
 */
@Component
public class FileUtils {

    private static final String EXCEPTION_MESSAGE = "Exception occurred with parameter: ";
    private final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * Reads a file as byte array using {@link FileUtils}'s readFileToByteArray method.
     * @param templateFile is the template
     * @return with the template parsed to byte array
     * @throws IOException when the file can not be read
     */
    public byte[] getFileAsByteArray(final File templateFile) throws IOException {
        return org.apache.commons.io.FileUtils.readFileToByteArray(templateFile);
    }

    /**
     * Collects the file names in the target folder and it's child folders.
     * @param folder is the target folder
     * @return with the file names
     */
    public Collection<File> listFiles(final File folder) {
        Collection<File> result;
        try {
            result = org.apache.commons.io.FileUtils.listFiles(folder, new RegexFileFilter("^(.*?)"), DirectoryFileFilter.DIRECTORY);
        } catch (IllegalArgumentException e) {
            result = logAndReturnEmptyList(folder, e);
        }
        return result;
    }

    /**
     * Collects the file names in the target folder.
     * @param folder is the target folder
     * @return with the file names
     */
    public Collection<File> listFilesNonRecursive(final File folder) {
        Collection<File> result;
        try {
            result = org.apache.commons.io.FileUtils.listFiles(folder, null, false);
        } catch (IllegalArgumentException e) {
            result = logAndReturnEmptyList(folder, e);
        }
        return result;
    }

    /**
     * Collects the file names in the target folder with the given extension.
     * @param folder is the target folder
     * @param extension the given extension
     * @return with the file names
     */
    public Collection<File> listFiles(final File folder, final String extension) {
        Collection<File> result;
        try {
            result = org.apache.commons.io.FileUtils.listFiles(folder, new String[]{extension}, false);
        } catch (IllegalArgumentException e) {
            result = logAndReturnEmptyList(folder, e);
        }
        return result;
    }

    /**
     * Collects the file names in the target folder with the given filter in non recursive way.
     * @param folder is the target folder
     * @param filter is the regular expression of filtering logic
     * @return with the file names
     */
    public Collection<File> listFilesWithFilter(final File folder, final String filter) {
        Collection<File> result;
        try {
            result = org.apache.commons.io.FileUtils.listFiles(folder, new RegexFileFilter(filter), FalseFileFilter.INSTANCE);
        } catch (IllegalArgumentException e) {
            result = logAndReturnEmptyList(folder, e);
        }
        return result;
    }

    private Collection<File> logAndReturnEmptyList(final File folder, final IllegalArgumentException e) {
        logger.debug(EXCEPTION_MESSAGE + folder, e);
        return Collections.emptyList();
    }
}
