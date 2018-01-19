package com.epam.wilma.webapp.stub.response.support;

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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.sequence.WilmaSequence;
import com.epam.wilma.webapp.configuration.WebAppConfigurationAccess;
import com.epam.wilma.webapp.configuration.domain.PropertyDTO;
import com.epam.wilma.webapp.configuration.domain.SequenceResponseGuardProperties;
import com.epam.wilma.webapp.stub.response.exception.ResponseTimeoutException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link SequenceResponseGuard}.
 * @author Adam_Csaba_Kiraly
 */
public class SequenceResponseGuardTest {

    private static final String LOGGER_ID = "id";

    @Mock
    private WebAppConfigurationAccess configurationAccess;
    @Mock
    private PropertyDTO propertyDto;

    @Mock
    private WilmaHttpRequest stubbedRequest;
    @Mock
    private WilmaSequence actualSequence;

    @InjectMocks
    private SequenceResponseGuard underTest;

    @BeforeMethod
    public void setUp() throws Exception {
        SequenceResponseGuardProperties properties = new SequenceResponseGuardProperties(3, 1);
        MockitoAnnotations.initMocks(this);
        given(stubbedRequest.getWilmaMessageId()).willReturn(LOGGER_ID);
        given(configurationAccess.getProperties()).willReturn(propertyDto);
        given(propertyDto.getSequenceResponseGuardProperties()).willReturn(properties);
    }

    @Test(expectedExceptions = ResponseTimeoutException.class)
    public void testWhenResponsesCannotArriveInTimeThenExceptionIsThrown() throws InterruptedException {
        //GIVEN
        given(actualSequence.checkIfAllResponsesArrived(LOGGER_ID)).willReturn(false);
        //WHEN
        underTest.waitForResponses(stubbedRequest, actualSequence);
        //THEN exception is thrown
    }

    @Test
    public void testWaitForResponses() throws InterruptedException {
        //GIVEN
        given(actualSequence.checkIfAllResponsesArrived(LOGGER_ID)).willReturn(false, false, false, true);
        //WHEN
        underTest.waitForResponses(stubbedRequest, actualSequence);
        //THEN
        verify(actualSequence, times(4)).checkIfAllResponsesArrived(LOGGER_ID);
    }
}
