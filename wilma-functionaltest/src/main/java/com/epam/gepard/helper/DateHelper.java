package com.epam.gepard.helper;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Helper class to handle date related information in tests.
 *
 * @author Tamas_Kohegyi, and from
 */
public class DateHelper {

    private static final long ONE_DAY_IN_MILLISECS = 1000 * 60 * 60 * 24;
    private static final long DAYS_IN_WEEK = 7;

    private List<SimpleDateFormat> knownPatterns;

    /**
     * Setup Date helper class, by preparing the understandable date formats.
     */
    public DateHelper() {
        knownPatterns = new ArrayList<>();
        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss"));
        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX"));
        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd"));
    }

    /**
     * Transfer String content to Date object.
     *
     * @param dateCandidate is the string to be converted.
     * @return with the new Date object, or null, if transfer was not successful.
     */
    public Date getDateFromString(final String dateCandidate) {

        for (SimpleDateFormat pattern : knownPatterns) {
            try {
                // Take a try
                return new Date(pattern.parse(dateCandidate).getTime());
                //CHECKSTYLE.OFF
            } catch (ParseException pe) {
                // Loop on
            }
            //CHECKSTYLE.ON
        }
        return null;
    }

    /**
     * Transfer Date object to String, using pattern: "yyyy-MM-dd'T'HH:mm:ss.SSSZ".
     *
     * @param date is the used date object
     * @return with string
     */
    public String getLongStringFromDate(final Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String dateString = dateFormat.format(date);
        return dateString;
    }

    /**
     * Transfer Date object to String, using pattern: "yyyy-MM-dd".
     *
     * @param date is the used date object
     * @return with string
     */
    public String getShortStringFromDate(final Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);
        return dateString;
    }

    /**
     * Format a Calendar object as date into YYYY-MM-DD string format.
     *
     * @param cal is the source of the calendar.
     * @return Date string
     */
    public String getShortStringFromDate(final Calendar cal) {
        NumberFormat df = DecimalFormat.getInstance();
        df.setMinimumIntegerDigits(2);
        df.setMaximumIntegerDigits(2);
        return cal.get(Calendar.YEAR) + "-" + df.format((long) cal.get(Calendar.MONTH) + 1) + "-" + df.format(cal.get(Calendar.DATE));
    }

    /**
     * Calculate number of days passed between two Dates, not counting Saturdays and Sundays.
     *
     * @param start is the starting date
     * @param end   is the ending date
     * @return with the diff in days
     */
    public long dayDiffsInWorkingDays(Date start, Date end) {
        //work with start date
        Calendar c1 = GregorianCalendar.getInstance();
        c1.setTime(start);
        int w1 = c1.get(Calendar.DAY_OF_WEEK);
        c1.add(Calendar.DAY_OF_WEEK, -w1 + 1);

        //work with end date
        Calendar c2 = GregorianCalendar.getInstance();
        c2.setTime(end);
        int w2 = c2.get(Calendar.DAY_OF_WEEK);
        c2.add(Calendar.DAY_OF_WEEK, -w2 + 1);

        //difference in days
        long days = (c2.getTimeInMillis() - c1.getTimeInMillis()) / ONE_DAY_IN_MILLISECS;

        //remove Saturdays and Sundays
        long daysWithoutSunday = days - (days * 2 / DAYS_IN_WEEK);

        if (w1 == Calendar.SUNDAY) {
            w1 = Calendar.MONDAY;
        }
        if (w2 == Calendar.SUNDAY) {
            w2 = Calendar.MONDAY;
        }
        return daysWithoutSunday - w1 + w2;
    }

    /**
     * Add N days to the existing Date object.
     *
     * @param d is the starting date
     * @param i number of days to add
     * @return with the new date object
     */
    public Date addDays(final Date d, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, i);  // number of days to add
        Date newDate = c.getTime();
        return newDate;
    }

}
