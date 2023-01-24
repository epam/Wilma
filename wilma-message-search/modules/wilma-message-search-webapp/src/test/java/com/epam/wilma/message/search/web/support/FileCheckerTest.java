package com.epam.wilma.message.search.web.support;
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Provides unit tests for the class {@link FileChecker}.
 *
 * @author Tibor_Kovacs
 */
public class FileCheckerTest {

    private static final String ERROR_POSTFIX = "DOES_NOT_EXIST";

    @InjectMocks
    private FileChecker underTest;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCheckFilesExistsWithPairsWhenThereAreSomeSearchResultButOneRequestDoesNotHavePair() {
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
        assertEquals(result, expected);
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
        assertEquals(result, expected);
    }

    @Test
    public void testCheckFilesExistsWithPairsWhenSearchResultIsEmptyList() {
        //GIVEN
        List<String> list = new ArrayList<>();
        List<List<String>> expected = new ArrayList<>();
        //WHEN
        List<List<String>> result = underTest.checkFilesExistsWithPairs(list);
        //THEN
        assertEquals(result, expected);
    }

    @Test
    public void testCheckFilesExistsWithPairsWhenSearchResultIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            //GIVEN
            //WHEN
            underTest.checkFilesExistsWithPairs(null);
            //THEN
        });
    }
}
