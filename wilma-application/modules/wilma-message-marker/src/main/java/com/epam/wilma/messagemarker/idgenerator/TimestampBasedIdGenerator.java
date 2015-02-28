package com.epam.wilma.messagemarker.idgenerator;
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
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.CurrentDateProvider;
import com.epam.wilma.domain.exception.TooManyRequestsException;
import com.epam.wilma.messagemarker.abortcounter.AbortedMessageCounter;
import com.epam.wilma.messagemarker.configuration.MessageMarkerConfigurationAccess;
import com.epam.wilma.messagemarker.configuration.domain.RequestLimits;

/**
 * Class for creating a new identifier. The ID is based on the current timestamp (seconds), and a 4 digit number that increments from 0 to 9999
 * @author Marton_Sereg
 *
 */
@Component
@Qualifier("timestampSimpleNumber")
public class TimestampBasedIdGenerator implements IdGenerator, ApplicationListener<ContextRefreshedEvent> {

    private static final int NO_DIGITS = 4;
    private final Logger logger = LoggerFactory.getLogger(TimestampBasedIdGenerator.class);
    private final AtomicInteger currentNumber = new AtomicInteger();
    private String previousSimpleDate;
    private String currentsimpleDate;
    private Integer warningLimit;
    private Integer errorLimit;

    @Autowired
    @Qualifier("simpleDateFormatterForFiles")
    private SimpleDateFormat fileSimpleDateFormat;

    @Autowired
    @Qualifier("simpleDateFormatterForAlerts")
    private SimpleDateFormat alertSimpleDateFormat;

    @Autowired
    private CurrentDateProvider currentDateProvider;

    @Autowired
    private AbortedMessageCounter abortedMessageCounter;

    @Autowired
    private MessageMarkerConfigurationAccess configurationAccess;

    @Override
    public synchronized String nextIdentifier() throws TooManyRequestsException {
        currentsimpleDate = getCurrentDateFormattedForFiles();
        checkPreviousDate(currentsimpleDate);
        return currentsimpleDate + "." + fourDigitString();
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        RequestLimits requestLimits = configurationAccess.getProperties();
        warningLimit = requestLimits.getWarningLimit();
        errorLimit = requestLimits.getErrorLimit();
    }

    private String getCurrentDateFormattedForFiles() {
        return fileSimpleDateFormat.format(currentDateProvider.getCurrentDate());
    }

    private String getCurrentDateFormattedForAlerts() {
        return alertSimpleDateFormat.format(currentDateProvider.getCurrentDate());
    }

    private void checkPreviousDate(final String currentsimpleDate) {
        if (previousSimpleDate == null || !currentsimpleDate.equals(previousSimpleDate)) {
            previousSimpleDate = currentsimpleDate;
            currentNumber.set(0);
        }
    }

    private String fourDigitString() throws TooManyRequestsException {
        String convertedNumber = String.valueOf(currentNumber.getAndIncrement());
        if (reachedWarningLimit()) {
            logger.warn("ALERT: Message load close to its limit! Timestamp: @" + getCurrentDateFormattedForAlerts() + ".");
        } else if (reachedErrorLimit()) {
            String timeStamp = getCurrentDateFormattedForAlerts();
            abortedMessageCounter.incrementAbortedMessages(timeStamp);
            throw new TooManyRequestsException("ALERT: Messages overloaded, message was dropped!", timeStamp);
        } else {
            abortedMessageCounter.logNumberOfAbortedMessages();
        }
        String zeros = createZeros(convertedNumber);
        return zeros + convertedNumber;
    }

    private boolean reachedWarningLimit() {
        return currentNumber.intValue() == warningLimit;
    }

    private boolean reachedErrorLimit() {
        return currentNumber.intValue() > errorLimit;
    }

    private String createZeros(final String convertedNumber) {
        String ret = "";
        for (int i = 0; i < NO_DIGITS - convertedNumber.length(); i++) {
            ret += "0";
        }
        return ret;
    }

}
