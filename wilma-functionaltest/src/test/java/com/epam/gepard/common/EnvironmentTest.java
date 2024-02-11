package com.epam.gepard.common;

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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link Environment}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class EnvironmentTest {

    private Environment underTest;

    @Before
    public void setUp() {
        underTest = new Environment();
    }

    @Test
    public void testReadingMultipleValidPropertyFilesShouldLoadProperly() {
        //GIVEN nothing
        //WHEN
        boolean result = underTest.setUp("src/test/resources/testprj.properties,src/test/resources/a.txt,src/test/resources/b.txt");
        //THEN
        Assert.assertTrue(result);
    }

    @Test
    public void testReadingMultipleValidPropertyFilesShouldSetProperties() {
        //GIVEN nothing
        //WHEN
        underTest.setUp("src/test/resources/testprj.properties,src/test/resources/a.txt,src/test/resources/b.txt");
        //THEN
        String result = underTest.getProperty("URL." + underTest.getProperty("TEID"));
        Assert.assertEquals(result, "http://blahqa1.com");
    }

    @Test
    public void testSetUpWithEmptyStringShouldNotTryToLoadProperties() {
        //GIVEN nothing
        //WHEN
        boolean result = underTest.setUp("");
        //THEN
        Assert.assertFalse(result);
    }

    @Test
    public void testSetUpWithNonExistingPropertyFileShouldNotLoadProperties() {
        //GIVEN
        //WHEN
        boolean result = underTest.setUp("idontexist.properties");
        //THEN
        Assert.assertFalse(result);
    }

    @Test
    public void testGetBooleanPropertyWhenPropertyValueStartsWithTrueShouldReturnTrue() {
        //GIVEN
        underTest.setProperty("xyz", "true programmers drink juice");
        //WHEN
        boolean result = underTest.getBooleanProperty("xyz");
        //THEN
        Assert.assertTrue(result);
    }

    @Test
    public void testGetBooleanPropertyWhenPropertyValueDoesNotStartWithTrueShouldReturnFalse() {
        //GIVEN
        underTest.setProperty("xyz", "poser programmers drink juice");
        //WHEN
        boolean result = underTest.getBooleanProperty("xyz");
        //THEN
        Assert.assertFalse(result);
    }

    @Test
    public void testGetBooleanPropertyWhenPropertyValueDoesNotExistShouldReturnFalse() {
        //GIVEN nothing
        //WHEN
        boolean result = underTest.getBooleanProperty("xyz");
        //THEN
        Assert.assertFalse(result);
    }
}
