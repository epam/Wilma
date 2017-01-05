package com.epam.wilma.message.search.web.support;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Provides unit tests for the class {@link FileChecker}.
 * @author Tibor_Kovacs
 *
 */
public class FileCheckerTest {

    private static final String ERROR_POSTFIX = "NOTEXISTS";

    @InjectMocks
    private FileChecker underTest;

    @BeforeMethod
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckFilesExistsWithPairsWhenThereAreSomeSearchResultButOneRequestDoesntHavePair() {
        //GIVEN
        List<String> list = new ArrayList<>();
        list.add("src/test/resources/first_testreq.txt");
        list.add("src/test/resources/second_testreq.txt");

        List<List<String>> expected = new ArrayList<>();
        List<String> pair = new ArrayList<>();
        pair.add("src/test/resources/first_testreq.txt");
        pair.add("src/test/resources/first_testresp.txt");
        expected.add(pair);
        pair = new ArrayList<>();
        pair.add("src/test/resources/second_testreq.txt");
        pair.add("src/test/resources/second_testresp.txt" + ERROR_POSTFIX);
        expected.add(pair);
        //WHEN
        List<List<String>> result = underTest.checkFilesExistsWithPairs(list);
        //THEN
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testCheckFilesExistsWithPairsWhenThereAreSomeSearchResultButOneResponseDoesntHavePair() {
        //GIVEN
        List<String> list = new ArrayList<>();
        list.add("src/test/resources/first_testreq.txt");
        list.add("src/test/resources/third_testresp.txt");

        List<List<String>> expected = new ArrayList<>();
        List<String> pair = new ArrayList<>();
        pair.add("src/test/resources/first_testreq.txt");
        pair.add("src/test/resources/first_testresp.txt");
        expected.add(pair);
        pair = new ArrayList<>();
        pair.add("src/test/resources/third_testresp.txt");
        pair.add("src/test/resources/third_testreq.txt" + ERROR_POSTFIX);
        expected.add(pair);
        //WHEN
        List<List<String>> result = underTest.checkFilesExistsWithPairs(list);
        //THEN
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testCheckFilesExistsWithPairsWhenSearchResultIsEmptyList() {
        //GIVEN
        List<String> list = new ArrayList<>();
        List<List<String>> expected = new ArrayList<>();
        //WHEN
        List<List<String>> result = underTest.checkFilesExistsWithPairs(list);
        //THEN
        Assert.assertEquals(result, expected);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testCheckFilesExistsWithPairsWhenSearchResultIsNull() {
        //GIVEN
        //WHEN
        List<List<String>> result = underTest.checkFilesExistsWithPairs(null);
        //THEN
    }
}
