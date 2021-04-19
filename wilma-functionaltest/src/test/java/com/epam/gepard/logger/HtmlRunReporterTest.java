package com.epam.gepard.logger;

import com.epam.gepard.common.Environment;
import com.epam.gepard.common.TestClassExecutionData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * Unit Test class for HtmlRunReporter class.
 *
 * @author tkohegyi
 */
public class HtmlRunReporterTest {

    private TestClassExecutionData classData;
    private Environment environment;

    private HtmlRunReporter underTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        environment = new Environment();
        classData = new TestClassExecutionData("ID", environment);
        classData.setTestClass(this.getClass());
        underTest = new HtmlRunReporter(classData);
    }


    @Test
    public void testDivStepField() {
        //given
        int start = underTest.getDivStep();
        //when
        underTest.increaseDivStep();
        //then
        Assert.assertTrue("divStep variable handling is problematic", start + 1 == underTest.getDivStep());
    }

    @Test
    public void testStepField() throws Exception {
        //given
        int start = underTest.getStep();
        //when
        underTest.increaseStep();
        //then
        Assert.assertTrue("step variable handling is problematic", start + 1 == underTest.getStep());
    }

}
