package com.epam.wilma.core.processor.entity;
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

import static org.mockito.Mockito.verify;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.sequence.SequenceManager;

/**
 * Provides unit tests for the class {@link SequenceRequestHandlingProcessor}.
 * @author Tibor_Kovacs
 *
 */
public class SequenceRequestHandlingProcessorTest {
    @Mock
    private SequenceManager manager;
    @Mock
    private WilmaHttpRequest request;

    @InjectMocks
    private SequenceRequestHandlingProcessor underTest;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessShouldCallManagerHandleRequest() throws ApplicationException {
        //GIVEN
        //WHEN
        underTest.process(request);
        //THEN
        verify(manager).handleRequest(request);
    }
}
