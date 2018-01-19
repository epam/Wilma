package com.epam.wilma.stubconfig.cache.cleaner.helper;
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

import com.epam.wilma.common.helper.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class check the cache folder whether it contains any saved stub configurations or not.
 *
 * @author Tibor_Kovacs
 */
@Component
public class StubConfigPathProvider {
    @Autowired
    private FileUtils fileUtils;

    /**
     * This method is dedicated for selecting files from the given cache folder with *_stubConfig.xml pattern.
     *
     * @param cachePath is the relative path of the cache folder
     * @return List&lt;String&gt; which contains paths of result files of the selecting.
     */
    public List<String> getConfigPathsFromCache(final String cachePath) {
        List<String> resultPaths = new ArrayList<>();
        File folder = new File(cachePath);
        folder = folder.getAbsoluteFile();
        for (File file : fileUtils.listFilesWithFilter(folder, "^[1-9]([0-9]*)_stubConfig.xml$")) {
            resultPaths.add(file.getPath());
        }
        return resultPaths;
    }

    /**
     * This method provides the paths of selecting with the given pattern. This pattern is like endsWith.
     *
     * @param sourceFolderPath is the relative path of a folder
     * @param pattern          is a pattern, example: *something.xml -&gt; ABsomething.xml, Asomething.xml
     * @return List&lt;String&gt; which contains paths of result files of the selecting.
     */
    public List<String> getConfigPathsFromSpecificFolder(final String sourceFolderPath, final String pattern) {
        List<String> resultPaths = new ArrayList<>();
        if (!pattern.contains("*")) {
            String specificFilePath;
            if ("".equals(sourceFolderPath)) {
                specificFilePath = pattern;
            } else {
                specificFilePath = FilenameUtils.separatorsToSystem(sourceFolderPath + "/" + pattern);
            }
            resultPaths.add(specificFilePath);
        } else {
            File folder = new File(sourceFolderPath);
            folder = folder.getAbsoluteFile();
            int cutFromHere = pattern.indexOf("*") + 1;
            String endOfFiles = pattern.substring(cutFromHere);
            for (File file : fileUtils.listFilesWithFilter(folder, "^(.+)" + endOfFiles + "$")) {
                resultPaths.add(file.getPath());
            }
        }
        return resultPaths;
    }

}
