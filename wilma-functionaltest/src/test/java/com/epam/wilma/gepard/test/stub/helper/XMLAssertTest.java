package com.epam.wilma.gepard.test.stub.helper;

import org.junit.Before;
import org.junit.Test;
import org.xmlunit.XMLUnitException;

public class XMLAssertTest {

    private XMLAssert underTest;

    @Before
    public void setUp() {
        underTest = new XMLAssert();
    }

    @Test
    public void testIdenticalOK() {
        String actual = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<ExampleRequest>\n"
                + "   <stuff exampleID=\"456\"></stuff>\n"
                + "</ExampleRequest>";
        String expected = String.copyValueOf(actual.toCharArray());
        underTest.testIdentical(actual, expected);
    }

    @Test(expected = XMLUnitException.class)
    public void testIdenticalXmlError() {
        String actual = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<ExampleRequest>\n"
                + "   <stuff exampleID=\"456\"></stuff>\n"
                + "</ExampleRequest"; //wrong closure of the xml
        String expected = String.copyValueOf(actual.toCharArray());
        underTest.testIdentical(actual, expected);
    }

    @Test(expected = Error.class)
    public void testIdenticalFail1() {
        String actual = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<ExampleRequest>\n" //here is the diff
                + "   <stuff exampleID=\"456\"></stuff>\n"
                + "</ExampleRequest>";
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<ExampleResponse>\n"
                + "   <stuff exampleID=\"456\"></stuff>\n"
                + "</ExampleResponse>";
        underTest.testIdentical(actual, expected);
    }

    @Test(expected = Error.class)
    public void testIdenticalFail2() {
        String actual = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<ExampleRequest>\n"
                + "   <stuff exampleID=\"456\"></stuff>\n"
                + "</ExampleRequest>";
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<ExampleRequest>\n"
                + "   <stuff exampleID=\"123\"></stuff>\n" //here is the diff
                + "</ExampleRequest>";
        underTest.testIdentical(actual, expected);
    }

}