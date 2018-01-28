package com.epam.wilma.sequence.factory;
/*==========================================================================
Copyright since 2013, EPAM Systems

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

import com.epam.wilma.common.helper.CurrentDateProvider;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.sequence.RequestResponsePair;
import com.epam.wilma.domain.sequence.WilmaSequence;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Timestamp;

import static org.mockito.BDDMockito.given;

/**
 * Provides unit tests for the class {@link SequenceFactory}.
 * @author Tibor_Kovacs
 *
 */
public class SequenceFactoryTest {

    @Mock
    private WilmaHttpRequest request;
    @Mock
    private CurrentDateProvider dateProvider;

    @InjectMocks
    private SequenceFactory underTest;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
        given(dateProvider.getCurrentTimeInMillis()).willReturn(1000L);
    }

    @Test
    public void testCreateNewSequence() {
        //GIVEN
        String testKey = "TestKey";
        String logggerId = "testId";
        given(request.getWilmaMessageId()).willReturn(logggerId);
        //WHEN
        WilmaSequence result = underTest.createNewSequence(testKey, request, 1000L);
        //THEN
        Assert.assertEquals(result.getSequenceKey(), testKey);
        Assert.assertFalse(result.isExpired(new Timestamp(1000L)));
        RequestResponsePair firstPair = result.getPairs().get(logggerId);
        Assert.assertEquals(firstPair.getRequest(), request);
    }
}
