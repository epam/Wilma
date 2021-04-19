package com.epam.gepard.filter.matcher;

/*==========================================================================
Copyright 2004-2015 EPAM Systems

This file is part of Gepard.

Gepard is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Gepard is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Gepard.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link SimpleMatcher}.
 * @author Adam_Csaba_Kiraly
 */
public class SimpleMatcherTest {

    private static final String JOKER = "?";
    private SimpleMatcher underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new SimpleMatcher(JOKER);
    }

    @Test
    public void testMatchWithEmptyFilterShouldMatchEmptyText() {
        //GIVEN
        String textToFilter = "";
        String filter = "";
        //WHEN
        boolean result = underTest.match(filter, textToFilter);
        //THEN
        Assert.assertTrue(result);
    }

    @Test
    public void testMatchWithEmptyFilterShouldNotMatchNonEmptyText() {
        //GIVEN
        String textToFilter = "a";
        String filter = "";
        //WHEN
        boolean result = underTest.match(filter, textToFilter);
        //THEN
        Assert.assertFalse(result);
    }

    @Test
    public void testMatchingBeginningWithJoker() {
        //GIVEN
        String textToFilter = "abcd";
        String filter = "a?";
        //WHEN
        boolean result = underTest.match(filter, textToFilter);
        //THEN
        Assert.assertTrue(result);
    }

    @Test
    public void testNonMatchingBeginningWithJoker() {
        //GIVEN
        String textToFilter = "abcd";
        String filter = "b?";
        //WHEN
        boolean result = underTest.match(filter, textToFilter);
        //THEN
        Assert.assertFalse(result);
    }

    @Test
    public void testNonMatchingEndWithJoker() {
        //GIVEN
        String textToFilter = "abcd";
        String filter = "a?x";
        //WHEN
        boolean result = underTest.match(filter, textToFilter);
        //THEN
        Assert.assertFalse(result);
    }

    @Test
    public void testMatchingWithMultipleJokers() {
        //GIVEN
        String textToFilter = "abcd";
        String filter = "??cd";
        //WHEN
        boolean result = underTest.match(filter, textToFilter);
        //THEN
        Assert.assertTrue(result);
    }

    @Test
    public void testMatchWithJokersShouldActAsEmptyString() {
        //GIVEN
        String textToFilter = "abcd";
        String filter = "??abc???d?";
        //WHEN
        boolean result = underTest.match(filter, textToFilter);
        //THEN
        Assert.assertTrue(result);
    }

    @Test
    public void testMatchWithMultipleJokersAtEndShouldNotActAsEmptyString() {
        //GIVEN
        String textToFilter = "abcd";
        String filter = "abcd??";
        //WHEN
        boolean result = underTest.match(filter, textToFilter);
        //THEN
        Assert.assertFalse(result);
    }

}
