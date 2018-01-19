package com.epam.wilma.core.processor.response;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.epam.wilma.core.processor.entity.ProcessorBase;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpResponse;

/**
 * Provides unit tests for the class <tt>WilmaHttpResponseHandler</tt>.
 * @author Tunde_Kovacs
 *
 */
public class WilmaHttpResponseProcessorTest {

    @Mock
    private WilmaHttpResponse response;
    @Mock
    private ProcessorBase responseProcessor;

    private WilmaHttpResponseProcessor underTest;

    private List<ProcessorBase> processors;

    @BeforeMethod
    public void setUp() {
        underTest = new WilmaHttpResponseProcessor();
        MockitoAnnotations.initMocks(this);
        processors = new ArrayList<>();
        processors.add(responseProcessor);
        setInternalState(underTest, "processors", processors);
    }

    @Test
    public void testProcessResponse() throws ApplicationException {
        //GIVEN in setUp
        given(responseProcessor.isEnabled()).willReturn(true);
        //WHEN
        underTest.processResponse(response);
        //THEN
        verify(responseProcessor).process(response);
    }

    @Test
    public void testDisableProcessorShouldDisableProcessorWhenListContainsProcessor() {
        //GIVEN
        int initialSize = processors.size();
        //WHEN
        underTest.disableProcessor(responseProcessor);
        //THEN
        assertFalse(underTest.isProcessorEnabled(responseProcessor));
    }

    @Test
    public void testAddProcessorShouldAddProcessorWhenListDoesNotContainProcessor() {
        //GIVEN
        processors.remove(0);
        int initialSize = processors.size();
        //WHEN
        underTest.addProcessor(responseProcessor);
        //THEN
        assertEquals(processors.size(), initialSize + 1);
    }

    @Test
    public void testAddProcessorShouldNotAddProcessorAgainWhenListContainsProcessor() {
        //GIVEN
        int initialSize = processors.size();
        //WHEN
        underTest.addProcessor(responseProcessor);
        //THEN
        assertEquals(processors.size(), initialSize);
    }

    @Test
    public void testContainsProcessorShouldReturnTrueWhenProcessorIsInList() {
        //GIVEN in setup
        //WHEN
        boolean actual = underTest.containsProcessor(responseProcessor);
        //THEN
        assertTrue(actual);
    }

    @Test
    public void testContainsProcessorShouldReturnFalseWhenProcessorIsNotInList() {
        //GIVEN
        processors.remove(0);
        //WHEN
        boolean actual = underTest.containsProcessor(responseProcessor);
        //THEN
        assertFalse(actual);
    }

}
