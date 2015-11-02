package com.epam.browsermob.messagemarker.idgenerator;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The purpose is to generate Wilma Message ID that is used to mark the messages.
 * Same ID is used for a request and response pairs. Usually it is a timestamp + a 4 digit number.
 * More than 4 digit is possible, but in theory only, as that would mean we have over 10K message pairs in a sec.
 *
 * @author Tamas_Kohegyi
 */
public class TimeStampBasedIdGenerator {

    private static final int NO_DIGITS = 4;
    private final AtomicInteger currentNumber = new AtomicInteger();
    private String previousSimpleDate;
    private String currentSimpleDate;

    private SimpleDateFormat fileSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public synchronized String nextIdentifier() {
        currentSimpleDate = getCurrentDateFormattedForFiles();
        checkPreviousDate(currentSimpleDate);
        return currentSimpleDate + "." + fourDigitString();
    }

    private String getCurrentDateFormattedForFiles() {
        return fileSimpleDateFormat.format(new Date());
    }

    private void checkPreviousDate(final String currentSimpleDate) {
        if (previousSimpleDate == null || !currentSimpleDate.equals(previousSimpleDate)) {
            previousSimpleDate = currentSimpleDate;
            currentNumber.set(0);
        }
    }

    private String fourDigitString() {
        String convertedNumber = String.valueOf(currentNumber.getAndIncrement());
        String zeros = createZeros(convertedNumber);
        return zeros + convertedNumber;
    }

    private String createZeros(final String convertedNumber) {
        String ret = "";
        for (int i = 0; i < NO_DIGITS - convertedNumber.length(); i++) {
            ret += "0";
        }
        return ret;
    }

}

