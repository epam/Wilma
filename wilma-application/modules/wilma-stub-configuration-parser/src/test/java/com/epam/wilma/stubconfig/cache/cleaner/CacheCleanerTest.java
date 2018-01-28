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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.helper.FileFactory;
import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.stubconfig.cache.cleaner.helper.StubConfigPathProvider;
import com.epam.wilma.stubconfig.configuration.StubConfigurationAccess;

/**
 * Provides unit tests for the class {@link CacheCleaner}.
 * @author Tibor_Kovacs
 *
 */
public class CacheCleanerTest {
    @Mock
    private StubConfigPathProvider pathProvider;
    @Mock
    private StubConfigurationAccess configurationAccess;
    @Mock
    private StubResourcePathProvider stubResourcePathProvider;
    @Mock
    private File file;
    @Mock
    private FileFactory fileFactory;

    @InjectMocks
    private CacheCleaner underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(stubResourcePathProvider.getCachePath()).willReturn("test/path");
    }

    @Test
    public void testCleanCache() {
        //GIVEN
        List<String> filePaths = new ArrayList<>();
        filePaths.add("first");
        filePaths.add("second");

        given(pathProvider.getConfigPathsFromCache("test/path")).willReturn(filePaths);
        given(fileFactory.createFile("first")).willReturn(file);
        given(fileFactory.createFile("second")).willReturn(file);
        //WHEN
        underTest.cleanCache();
        //THEN
        verify(configurationAccess).setProperties();
        verify(fileFactory).createFile("first");
        verify(fileFactory).createFile("second");
    }
}
