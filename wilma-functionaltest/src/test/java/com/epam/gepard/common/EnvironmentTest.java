package com.epam.gepard.common;

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
