package com.epam.wilma.sequence;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;
import com.epam.wilma.domain.sequence.WilmaSequence;
import com.epam.wilma.router.helper.WilmaHttpResponseCloner;
import com.epam.wilma.sequence.cleaner.SequenceCleaner;
import com.epam.wilma.sequence.handler.exception.SequenceHandlerKeyValidationException;
import com.epam.wilma.sequence.helper.SequenceHeaderUtil;
import com.epam.wilma.sequence.helper.SequenceIdUtil;
import com.epam.wilma.sequence.service.SequenceService;

/**
 * Provides unit tests for the class {@link SequenceManager}.
 * @author Tibor_Kovacs
 *
 */
public class SequenceManagerTest {

    private WilmaHttpRequest request;
    private WilmaHttpResponse response;
    private final boolean isVolatile = false;

    @Mock
    private SequenceService sequenceService;
    @Mock
    private SequenceDescriptor sequenceDescriptor;
    @Mock
    private SequenceCleaner cleaner;
    @Mock
    private WilmaHttpResponse clonedResponse;
    @Mock
    private WilmaHttpResponseCloner responseCloner;
    @Mock
    private WilmaSequence sequence;
    @Mock
    private SequenceHeaderUtil headerUtil;
    @Mock
    private SequenceIdUtil sequenceKeyResolver;
    @Mock
    private Logger logger;

    @InjectMocks
    private SequenceManager underTest;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
        given(responseCloner.cloneResponse(Mockito.any(WilmaHttpResponse.class))).willReturn(clonedResponse);
    }

    @Test
    public void testAddSequenceDescriptorWhenDescriptorsIsNotNull() {
        //GIVEN
        String descriptorKey = "TestTeam-fistDescriptor-TestHandler";
        //WHEN
        underTest.addSequenceDescriptor(descriptorKey, sequenceDescriptor);
        //THEN
        Map<String, SequenceDescriptor> result = (Map<String, SequenceDescriptor>) Whitebox.getInternalState(underTest, "descriptors");
        Assert.assertEquals(result.get(descriptorKey), sequenceDescriptor);
    }

    @Test
    public void testHandleRequestWhenSequenceDescriptorIsActive() {
        //GIVEN
        Map<String, SequenceDescriptor> descriptors = new ConcurrentHashMap<>();
        descriptors.put("TestTeam-fistDescriptor-TestHandler", sequenceDescriptor);
        Whitebox.setInternalState(underTest, "descriptors", descriptors);
        given(sequenceDescriptor.isActive()).willReturn(true);
        //WHEN
        underTest.handleRequest(request);
        //THEN
        verify(sequenceService).checkRequest(request, sequenceDescriptor);
    }

    @Test
    public void testHandleRequestWhenSequenceDescriptorIsNotActive() {
        //GIVEN
        Map<String, SequenceDescriptor> descriptors = new ConcurrentHashMap<>();
        descriptors.put("TestTeam-fistDescriptor-TestHandler", sequenceDescriptor);
        Whitebox.setInternalState(underTest, "descriptors", descriptors);
        given(sequenceDescriptor.isActive()).willReturn(false);
        //WHEN
        underTest.handleRequest(request);
        //THEN
        verify(sequenceService, never()).checkRequest(request, sequenceDescriptor);
    }

    @Test
    public void testHandleRequestWhenCheckRequestThrowValidationException() {
        //GIVEN
        Map<String, SequenceDescriptor> descriptors = new ConcurrentHashMap<>();
        descriptors.put("TestTeam-fistDescriptor-TestHandler", sequenceDescriptor);
        Whitebox.setInternalState(underTest, "descriptors", descriptors);
        Whitebox.setInternalState(underTest, "logger", logger);
        given(sequenceDescriptor.isActive()).willReturn(true);
        Mockito.doThrow(new SequenceHandlerKeyValidationException("Test message")).when(sequenceService).checkRequest(request, sequenceDescriptor);
        //WHEN
        underTest.handleRequest(request);
        //THEN
        verify(logger).error(Mockito.anyString());
    }

    @Test
    public void testCleanUpDescriptors() {
        //GIVEN
        Map<String, SequenceDescriptor> descriptors = new ConcurrentHashMap<>();
        descriptors.put("TestTeam-fistDescriptor-TestHandler", sequenceDescriptor);
        Whitebox.setInternalState(underTest, "descriptors", descriptors);
        //WHEN
        underTest.cleanUpDescriptors();
        //THEN
        verify(cleaner).cleanTheExpiredSequences(descriptors);
    }

    @Test
    public void testTryToSaveResponseIntoSequence() {
        //GIVEN
        response = new WilmaHttpResponse(isVolatile);
        String sequenceKey = "TestTeam-fistDescriptor-TestHandler|Sequence1";
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put(WilmaHttpEntity.WILMA_SEQUENCE_ID, sequenceKey);
        Map<String, SequenceDescriptor> descriptors = new ConcurrentHashMap<>();
        descriptors.put("TestTeam-fistDescriptor-TestHandler", sequenceDescriptor);
        Whitebox.setInternalState(response, "requestHeaders", requestHeaders);
        Whitebox.setInternalState(underTest, "descriptors", descriptors);
        given(headerUtil.resolveSequenceHeader(sequenceKey)).willReturn(new String[]{sequenceKey});
        given(sequenceKeyResolver.getDescriptorKey(sequenceKey)).willReturn("TestTeam-fistDescriptor-TestHandler");
        given(sequenceKeyResolver.getHandlerKey(sequenceKey)).willReturn("Sequence1");
        given(sequenceDescriptor.getSequence("Sequence1")).willReturn(sequence);
        //WHEN
        underTest.tryToSaveResponseIntoSequence(response);
        //THEN
        verify(sequence).addResponseToPair(clonedResponse);
    }

    @Test
    public void testTryToSaveResponseIntoSequenceWhenResponseDoesNotBelongsToSequence() {
        //GIVEN
        response = new WilmaHttpResponse(isVolatile);
        //WHEN
        underTest.tryToSaveResponseIntoSequence(response);
        //THEN
        verify(headerUtil, times(0)).resolveSequenceHeader(Mockito.anyString());
    }

    @Test
    public void testRemoveSequenceDescriptorsShouldRemoveDescriptor() {
        //GIVEN
        Map<String, SequenceDescriptor> descriptors = new ConcurrentHashMap<>();
        descriptors.put("TestTeam-fistDescriptor-TestHandler", sequenceDescriptor);
        Whitebox.setInternalState(underTest, "descriptors", descriptors);
        given(sequenceDescriptor.getGroupName()).willReturn("TestTeam");
        //WHEN
        underTest.removeSequenceDescriptors("TestTeam");
        //THEN
        Map<String, SequenceDescriptor> actualDescriptors = (Map<String, SequenceDescriptor>) Whitebox.getInternalState(underTest, "descriptors");
        Assert.assertEquals(actualDescriptors.size(), 0);
    }

    @Test
    public void testRemoveSequenceDescriptorsShouldNotRemoveDescriptor() {
        //GIVEN
        Map<String, SequenceDescriptor> descriptors = new ConcurrentHashMap<>();
        descriptors.put("TestTeam-fistDescriptor-TestHandler", sequenceDescriptor);
        Whitebox.setInternalState(underTest, "descriptors", descriptors);
        //WHEN
        underTest.removeSequenceDescriptors("something");
        //THEN
        Map<String, SequenceDescriptor> actualDescriptors = (Map<String, SequenceDescriptor>) Whitebox.getInternalState(underTest, "descriptors");
        Assert.assertEquals(actualDescriptors.size(), 1);
    }

    @Test
    public void testSetStatusOfDescriptors() {
        //GIVEN
        Map<String, SequenceDescriptor> descriptors = new ConcurrentHashMap<>();
        descriptors.put("TestTeam-fistDescriptor-TestHandler", sequenceDescriptor);
        Whitebox.setInternalState(underTest, "descriptors", descriptors);
        given(sequenceDescriptor.getGroupName()).willReturn("TestTeam");
        //WHEN
        underTest.setStatusOfDescriptors("TestTeam", false);
        //THEN
        verify(sequenceDescriptor).setActive(false);
        verify(sequenceDescriptor).dropAllSequences();
    }

    @Test
    public void testGetSequencesShouldReturnWithEmptyMap() {
        //GIVEN
        Map<String, SequenceDescriptor> descriptors = new ConcurrentHashMap<>();
        descriptors.put("TestTeam-fistDescriptor-TestHandler", sequenceDescriptor);
        Whitebox.setInternalState(underTest, "descriptors", descriptors);
        //WHEN
        Map<String, WilmaSequence> actual = underTest.getSequences("TestTeam");
        //THEN
        Assert.assertEquals(actual, Collections.EMPTY_MAP);
    }

    @Test
    public void testGetSequencesShouldReturnWithACollection() {
        //GIVEN
        Map<String, WilmaSequence> sequences = new ConcurrentHashMap<>();
        sequences.put("TestKey1", sequence);
        Map<String, SequenceDescriptor> descriptors = new ConcurrentHashMap<>();
        descriptors.put("TestTeam-fistDescriptor-TestHandler", sequenceDescriptor);
        Whitebox.setInternalState(underTest, "descriptors", descriptors);
        given(sequenceDescriptor.getSequences()).willReturn(sequences);
        //WHEN
        Map<String, WilmaSequence> actual = underTest.getSequences("TestTeam-fistDescriptor-TestHandler");
        //THEN
        Assert.assertEquals(actual, sequences);
    }
}
