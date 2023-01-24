package com.epam.wilma.proxy.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

public class TimeStampBasedIdGeneratorTest {

    @Mock
    CurrentDateProvider currentDateProvider;

    @InjectMocks
    private TimeStampBasedIdGenerator underTest;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        Instant i = Instant.EPOCH;
        Date d = Date.from(i);
        given(currentDateProvider.getCurrentDate()).willReturn(d);
        ReflectionTestUtils.setField(underTest, "currentDateProvider", currentDateProvider);
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
        assertEquals(10001, count);
        assertEquals(expected + 1, length);
    }
}