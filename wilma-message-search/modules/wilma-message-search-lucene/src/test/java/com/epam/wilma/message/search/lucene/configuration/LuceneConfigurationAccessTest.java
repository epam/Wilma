package com.epam.wilma.message.search.lucene.configuration;
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
import static org.testng.Assert.assertEquals;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.message.search.properties.PropertyHolder;

/**
 * Unit tests for the class {@link LuceneConfigurationAccess}.
 * @author Tunde_Kovacs
 *
 */
public class LuceneConfigurationAccessTest {

    private static final String REINDEX_CRON_EXPRESSION = "reindex-cron-expression";
    private static final String MESSAGE_FOLDER = "message-folder";
    private static final String INDEX_DIR = "test-folder";

    @Mock
    private PropertyHolder propertyHolder;

    @InjectMocks
    private LuceneConfigurationAccess underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(propertyHolder.get("lucene.index.folder")).willReturn(INDEX_DIR);
        given(propertyHolder.get("message.folders")).willReturn(MESSAGE_FOLDER);
        given(propertyHolder.get("lucene.reindex.cron")).willReturn(REINDEX_CRON_EXPRESSION);
    }

    @Test
    public void testLoadPropertiesShouldSetMessageFolders() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDto actual = underTest.getProperties();
        assertEquals(actual.getMessageDirectories(), MESSAGE_FOLDER);
    }

    @Test
    public void testLoadPropertiesShouldSetIndexFolders() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDto actual = underTest.getProperties();
        assertEquals(actual.getIndexDirectory(), INDEX_DIR);
    }

    @Test
    public void testLoadPropertiesShouldSetReindexCronExpression() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDto actual = underTest.getProperties();
        assertEquals(actual.getReindexTimer(), REINDEX_CRON_EXPRESSION);
    }
}
