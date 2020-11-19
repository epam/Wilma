package com.epam.wilma.browserup.proxy.helper;

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class TimeStampBasedIdGenerator {
    private static final int NO_DIGITS = 4;
    private final AtomicInteger currentNumber = new AtomicInteger();
    private String previousSimpleDate;
    private CurrentDateProvider currentDateProvider = new CurrentDateProvider();
    private SimpleDateFormat fileSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public TimeStampBasedIdGenerator() {
    }

    public synchronized String nextIdentifier() {
        String currentSimpleDate = this.getCurrentDateFormattedForFiles();
        this.checkPreviousDate(currentSimpleDate);
        return currentSimpleDate + "." + this.fourDigitString();
    }

    private String getCurrentDateFormattedForFiles() {
        return this.fileSimpleDateFormat.format(this.currentDateProvider.getCurrentDate());
    }

    private void checkPreviousDate(String currentSimpleDate) {
        if (this.previousSimpleDate == null || !currentSimpleDate.equals(this.previousSimpleDate)) {
            this.previousSimpleDate = currentSimpleDate;
            this.currentNumber.set(0);
        }

    }

    private String fourDigitString() {
        String convertedNumber = String.valueOf(this.currentNumber.getAndIncrement());
        String zeros = this.createZeros(convertedNumber);
        return zeros + convertedNumber;
    }

    private String createZeros(String convertedNumber) {
        String ret = "";

        for(int i = 0; i < 4 - convertedNumber.length(); ++i) {
            ret = ret + "0";
        }

        return ret;
    }
}
