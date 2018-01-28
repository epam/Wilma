package com.epam.wilma.stubconfig.initializer.template;
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
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.FileFactory;
import com.epam.wilma.common.helper.FileUtils;

/**
 * Class for loading files' names from the specified folder and it's child folders.
 * @author Tamas_Bihari
 *
 */
@Component
public class FilePathReader {

    @Autowired
    private FileFactory fileFactory;

    @Autowired
    private FileUtils fileUtils;

    /**
     * Loads file's paths from the specified path and it's child folders.
     * @param path is the location of the target folder
     * @return list of the file paths in the target folder and it's child folder
     */
    public List<String> getFilePaths(final String path) {
        List<String> result = new ArrayList<>();
        File folder = fileFactory.createFile(path);
        Collection<File> files = fileUtils.listFiles(folder);
        for (File file : files) {
            result.add(file.toString());
        }
        return result;
    }

    /**
     * Loads file's short paths (without the target path) from the specified path and it's child folders.
     * @param path is the location of the target folder
     * @return list of the file short paths in the target folder and it's child folder
     */
    public List<String> getShortFilePaths(final String path) {
        List<String> result = new ArrayList<>();
        File folder = fileFactory.createFile(path);
        Collection<File> files = fileUtils.listFiles(folder);
        for (File file : files) {
            result.add(getSimpleName(path, file));
        }
        return result;
    }

    private String getSimpleName(final String path, final File file) {
        String filePath = file.toString();
        String targetPath = path.replace("/", "\\") + "\\";
        return filePath.replace(targetPath, "");
    }
}
