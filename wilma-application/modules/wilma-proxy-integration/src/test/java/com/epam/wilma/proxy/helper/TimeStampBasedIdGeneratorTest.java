package com.epam.wilma.proxy.helper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import java.time.Instant;
import java.util.Date;

import static org.mockito.BDDMockito.given;

public class TimeStampBasedIdGeneratorTest {

    @Mock
    CurrentDateProvider currentDateProvider;

    @InjectMocks
    private TimeStampBasedIdGenerator underTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Instant i = Instant.EPOCH;
        Date d = Date.from(i);
        given(currentDateProvider.getCurrentDate()).willReturn(d);
        Whitebox.setInternalState(underTest, "currentDateProvider", currentDateProvider);
    }

    @Test
    public void nextIdentifier() {
        int expected = "yyyyMMddHHmmss.0000".length();
        int count = 0;
        int length = expected;
        while (length == expected) {
            count++;
            var actual = underTest.nextIdentifier();
            length = actual.length();
        }
        Assert.assertEquals(10001, count);
        Assert.assertEquals(expected + 1, length);
    }
}