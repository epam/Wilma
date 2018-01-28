package com.epam.wilma.sequence.helper;
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

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Provides unit tests for the class {@link SequenceHeaderUtil}.
 * @author Tibor_Kovacs
 *
 */
public class SequenceHeaderUtilTest {

    private SequenceHeaderUtil underTest = new SequenceHeaderUtil();

    @Test
    public void testCreateSequenceHeaderWhenTheSourceIsEmpty() {
        //GIVEN
        String testKey = "TestKey1";
        //WHEN
        String result = underTest.createSequenceHeader(null, testKey);
        //THEN
        Assert.assertEquals(result, testKey);
    }

    @Test
    public void testCreateSequenceHeaderWhenThereIsSourceParameter() {
        //GIVEN
        String testKeyFirst = "TestKey1";
        String testKeySecond = "TestKey2";
        String expected = testKeyFirst + SequenceConstants.SEQUENCE_KEY_SEPARATOR.getConstant() + testKeySecond;
        //WHEN
        String result = underTest.createSequenceHeader(testKeyFirst, testKeySecond);
        //THEN
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testResolveSequenceHeaderWhenThereIsOneKeyInInput() {
        //GIVEN
        String testKey = "TestKey1";
        //WHEN
        String[] result = underTest.resolveSequenceHeader(testKey);
        //THEN
        Assert.assertEquals(result.length, 1);
        Assert.assertEquals(result[0], testKey);
    }

    @Test
    public void testResolveSequenceHeaderWhenThereAreMoreKeysInInput() {
        //GIVEN
        String testKeyFirst = "TestKey1";
        String testKeySecond = "TestKey2";
        String input = testKeyFirst + SequenceConstants.SEQUENCE_KEY_SEPARATOR.getConstant() + testKeySecond;
        //WHEN
        String[] result = underTest.resolveSequenceHeader(input);
        //THEN
        Assert.assertEquals(result.length, 2);
        Assert.assertEquals(result[0], testKeyFirst);
        Assert.assertEquals(result[1], testKeySecond);
    }
}
