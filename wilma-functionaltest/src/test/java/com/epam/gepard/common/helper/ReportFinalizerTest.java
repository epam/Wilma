package com.epam.gepard.common.helper;

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

import com.epam.gepard.common.Environment;
import com.epam.gepard.generic.GenericListTestSuite;
import com.epam.gepard.helper.AllTestResults;
import com.epam.gepard.helper.DateHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Properties;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link ReportFinalizer}.
 *
 * @author Tibor_Kovacs
 */
public class ReportFinalizerTest {

    private static final String APPLICATION_VERSION = "DEV";

    @Mock
    private GenericListTestSuite gSuite;
    @Mock
    private AllTestResults allTestResults;
    @Mock
    private Properties props;

    private Environment environment;

    private ReportFinalizer underTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        environment = new Environment();
        underTest = new ReportFinalizer(environment);
        given(allTestResults.getRunned()).willReturn(10);
        given(allTestResults.getFailed()).willReturn(2);
        given(allTestResults.getPassed()).willReturn(8);
        given(allTestResults.getDummy()).willReturn(0);
        given(allTestResults.getNotApplicable()).willReturn(0);
        given(gSuite.getUsedTc()).willReturn(5);
    }

    @Test
    public void testFinalizeTheReportCheckPropertySettings() {
        //GIVEN
        long elapsedTime = 100000L;
        Calendar cal = Calendar.getInstance();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(0);
        nf.setMaximumFractionDigits(0);
        int expectedMinuteDuration = 1;
        double expectedDuration = 100;
        //WHEN
        underTest.finalizeTheReport(gSuite, allTestResults, APPLICATION_VERSION, elapsedTime, props);
        //THEN
        DateHelper dateHelper = new DateHelper();
        verify(props).setProperty("Runned", String.valueOf(10));
        verify(props).setProperty("Failed", String.valueOf(2));
        verify(props).setProperty("Passed", String.valueOf(8));
        verify(props).setProperty("TCdummy", String.valueOf(0));
        verify(props).setProperty("TCnotapplicable", String.valueOf(0));
        verify(props).setProperty("Time", "<b>" + expectedMinuteDuration + "</b> minutes and <b>" + nf.format(40.0) + "</b> seconds");
        verify(props).setProperty("SecondsTime", "" + expectedDuration);
        verify(props).setProperty("DateTime", dateHelper.getShortStringFromDate(cal));
        verify(props).setProperty("Version", APPLICATION_VERSION);
        verify(props).setProperty("TEID", "Unknown");
        verify(props).setProperty("TCUsed", Integer.toString(5));
        verify(props).setProperty("TCNumber", Integer.toString(0)); //no tests were added
        verify(props).setProperty("ResultURLHTML", "");
    }

    @Test
    public void testFinalizeTheReportCheckPropertySettingsWithoutApplicationVersion() {
        //GIVEN
        long elapsedTime = 100000L;
        Calendar cal = Calendar.getInstance();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(0);
        nf.setMaximumFractionDigits(0);
        int expectedMinuteDuration = 1;
        double expectedDuration = 100;
        //WHEN
        underTest.finalizeTheReport(gSuite, allTestResults, null, elapsedTime, props);
        //THEN
        DateHelper dateHelper = new DateHelper();
        verify(props).setProperty("Runned", String.valueOf(10));
        verify(props).setProperty("Failed", String.valueOf(2));
        verify(props).setProperty("Passed", String.valueOf(8));
        verify(props).setProperty("TCdummy", String.valueOf(0));
        verify(props).setProperty("TCnotapplicable", String.valueOf(0));
        verify(props).setProperty("Time", "<b>" + expectedMinuteDuration + "</b> minutes and <b>" + nf.format(40.0) + "</b> seconds");
        verify(props).setProperty("SecondsTime", "" + expectedDuration);
        verify(props).setProperty("DateTime", dateHelper.getShortStringFromDate(cal));
        verify(props).setProperty("Version", "undetected");
        verify(props).setProperty("TEID", "Unknown");
        verify(props).setProperty("TCUsed", Integer.toString(5));
        verify(props).setProperty("TCNumber", Integer.toString(0)); //no tests were added
        verify(props).setProperty("ResultURLHTML", "");
    }

    @Test
    public void testFinalizeTheReportCheckPropertySettingsWithResultHtmlUrl() {
        //GIVEN
        long elapsedTime = 100000L;
        Calendar cal = Calendar.getInstance();
        environment.setProperty(Environment.GEPARD_PUBLIC_ENABLED, "true");
        environment.setProperty(Environment.GEPARD_PUBLIC_PATH, "test");
        environment.setProperty(Environment.GEPARD_PUBLIC_RESULT, "path");
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(0);
        nf.setMaximumFractionDigits(0);
        int expectedMinuteDuration = 1;
        double expectedDuration = 100;
        //WHEN
        underTest.finalizeTheReport(gSuite, allTestResults, APPLICATION_VERSION, elapsedTime, props);
        //THEN
        DateHelper dateHelper = new DateHelper();
        verify(props).setProperty("Runned", String.valueOf(10));
        verify(props).setProperty("Failed", String.valueOf(2));
        verify(props).setProperty("Passed", String.valueOf(8));
        verify(props).setProperty("TCdummy", String.valueOf(0));
        verify(props).setProperty("TCnotapplicable", String.valueOf(0));
        verify(props).setProperty("Time", "<b>" + expectedMinuteDuration + "</b> minutes and <b>" + nf.format(40.0) + "</b> seconds");
        verify(props).setProperty("SecondsTime", "" + expectedDuration);
        verify(props).setProperty("DateTime", dateHelper.getShortStringFromDate(cal));
        verify(props).setProperty("Version", APPLICATION_VERSION);
        verify(props).setProperty("TEID", "Unknown");
        verify(props).setProperty("TCUsed", Integer.toString(5));
        verify(props).setProperty("TCNumber", Integer.toString(0)); //no tests were added
        verify(props).setProperty("ResultURLHTML", "Test Results will be available <a href=test/path>here.</a><br/>");
    }
}
