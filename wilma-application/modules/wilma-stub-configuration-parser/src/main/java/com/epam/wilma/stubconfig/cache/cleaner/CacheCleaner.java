package com.epam.wilma.stubconfig.cache.cleaner;
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
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.FileFactory;
import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.stubconfig.cache.cleaner.helper.StubConfigPathProvider;
import com.epam.wilma.stubconfig.configuration.StubConfigurationAccess;

/**
 * This class provides the ability to clean the cache folder.
 * The path of this cache folder comes from properties.
 * @author Tibor_Kovacs
 *
 */
@Component
public class CacheCleaner {

    @Autowired
    private StubConfigurationAccess configurationAccess;
    @Autowired
    private StubResourcePathProvider stubResourcePathProvider;
    @Autowired
    private StubConfigPathProvider pathProvider;
    @Autowired
    private FileFactory fileFactory;

    private String cacheFolderPath;

    /**
     * This method will look up the path of the cache folder from properties.
     * Then it deletes all files in that folder.
     */
    public void cleanCache() {
        getCacheFolderPath();
        List<String> filePaths = pathProvider.getConfigPathsFromCache(cacheFolderPath);
        for (String filePath : filePaths) {
            File actualFile = fileFactory.createFile(filePath);
            actualFile.delete();
        }
    }

    private void getCacheFolderPath() {
        configurationAccess.setProperties();
        cacheFolderPath = stubResourcePathProvider.getCachePath();
    }
}
