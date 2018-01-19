package com.epam.wilma.sequence.service;
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
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;
import com.epam.wilma.domain.sequence.WilmaSequence;
import com.epam.wilma.domain.stubconfig.sequence.SequenceHandler;
import com.epam.wilma.router.helper.WilmaHttpRequestCloner;
import com.epam.wilma.sequence.evaluator.SequenceDescriptorEvaluator;
import com.epam.wilma.sequence.factory.SequenceFactory;
import com.epam.wilma.sequence.helper.SequenceConstants;
import com.epam.wilma.sequence.helper.SequenceDescriptorKeyUtil;
import com.epam.wilma.sequence.helper.SequenceHeaderUtil;
import com.epam.wilma.sequence.helper.SequenceIdUtil;
import com.epam.wilma.sequence.validator.HandlerKeyValidator;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the class {@link SequenceService}.
 * @author Tibor_Kovacs
 *
 */
public class SequenceServiceTest {
    private static final String SEQUENCE_DESCRIPTOR_GROUPNAME = "TestTeam";
    private static final String SEQUENCE_DESCRIPTOR_NAME = "sequenceOne";

    @Mock
    private WilmaHttpRequest clonedRequest;
    @Mock
    private WilmaHttpRequest request;
    @Mock
    private SequenceDescriptor sequenceDescriptor;
    @Mock
    private SequenceHandler handler;
    @Mock
    private SequenceDescriptorEvaluator sequenceEvaluator;
    @Mock
    private WilmaSequence sequenceFirst;
    @Mock
    private WilmaSequence sequenceSecond;
    @Mock
    private CurrentDateProvider dateProvider;
    @Mock
    private WilmaHttpRequestCloner requestCloner;
    @Mock
    private SequenceFactory sequenceFactory;
    @Mock
    private SequenceHeaderUtil headerUtil;
    @Mock
    private SequenceDescriptorKeyUtil sequenceDescriptorKeyUtil;
    @Mock
    private HandlerKeyValidator keyValidator;
    @Mock
    private SequenceIdUtil sequenceIdUtil;

    @InjectMocks
    private SequenceService underTest;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
        given(dateProvider.getCurrentTimeInMillis()).willReturn(1000L);
        given(requestCloner.cloneRequest(request)).willReturn(clonedRequest);
        given(request.getSequenceId()).willReturn(null);
        given(sequenceDescriptor.getGroupName()).willReturn(SEQUENCE_DESCRIPTOR_GROUPNAME);
        given(sequenceDescriptor.getName()).willReturn(SEQUENCE_DESCRIPTOR_NAME);
        given(sequenceDescriptorKeyUtil.createDescriptorKey(SEQUENCE_DESCRIPTOR_GROUPNAME, SEQUENCE_DESCRIPTOR_NAME)).willReturn(
                SEQUENCE_DESCRIPTOR_GROUPNAME + SequenceConstants.DESCRIPTOR_KEY_PART_SEPARATOR.getConstant() + SEQUENCE_DESCRIPTOR_NAME);
        given(sequenceDescriptor.getHandler()).willReturn(handler);
    }

    @Test
    public void testCheckRequestWhenEvaluationFalse() {
        //GIVEN
        String sequenceKeyFirst = "testKey1";
        given(sequenceEvaluator.evaluate(request, sequenceDescriptor)).willReturn(false);
        //WHEN
        underTest.checkRequest(request, sequenceDescriptor);
        //THEN
        verify(request, never()).addSequenceId(sequenceKeyFirst);
    }

    @Test
    public void testCheckRequestShouldAppendRequestToAnExistingSequence() {
        //GIVEN
        String sequenceKeyFirst = "testKey1";
        Map<String, WilmaSequence> sequences = new ConcurrentHashMap<>();
        sequences.put(sequenceKeyFirst, sequenceFirst);

        given(sequenceEvaluator.evaluate(request, sequenceDescriptor)).willReturn(true);
        given(sequenceDescriptor.getSequences()).willReturn(sequences);
        given(handler.getExistingSequence(request, sequences, null)).willReturn(sequenceKeyFirst);
        given(sequenceDescriptor.getSequence(sequenceKeyFirst)).willReturn(sequenceFirst);
        given(request.getWilmaMessageId()).willReturn("TestLoggerId");
        given(sequenceIdUtil.createSequenceId(sequenceKeyFirst, sequenceDescriptor)).willReturn("newID");
        given(request.getSequenceId()).willReturn(null);
        given(headerUtil.createSequenceHeader(null, "newID")).willReturn("newID");
        //WHEN
        underTest.checkRequest(request, sequenceDescriptor);
        //THEN
        verify(sequenceFirst).setTimeout(Mockito.any(Timestamp.class));
        verify(sequenceFirst).addPair(Mockito.eq("TestLoggerId"), Mockito.any(RequestResponsePair.class));
        verify(request).addSequenceId("newID");
    }

    @Test
    public void testCheckRequestShouldCreateANewSequence() {
        //GIVEN
        String sequenceKeyFirst = "testKey1";
        String sequenceKeySecond = "testKey2";
        Map<String, WilmaSequence> sequences = new ConcurrentHashMap<>();
        sequences.put(sequenceKeyFirst, sequenceFirst);
        given(sequenceEvaluator.evaluate(request, sequenceDescriptor)).willReturn(true);
        given(handler.getExistingSequence(request, sequences, null)).willReturn(null);
        given(handler.generateNewSequenceKey(request, null)).willReturn(sequenceKeySecond);
        given(sequenceDescriptor.getDefaultTimeout()).willReturn(1000L);
        given(sequenceFactory.createNewSequence(sequenceKeySecond, request, 1000L)).willReturn(sequenceSecond);
        given(sequenceSecond.getSequenceKey()).willReturn(sequenceKeySecond);
        given(sequenceIdUtil.createSequenceId(sequenceKeySecond, sequenceDescriptor)).willReturn("newID");
        given(request.getSequenceId()).willReturn(null);
        given(headerUtil.createSequenceHeader(null, "newID")).willReturn("newID");
        //WHEN
        underTest.checkRequest(request, sequenceDescriptor);
        //THEN
        verify(sequenceDescriptor).putIntoSequences(sequenceSecond);
        verify(request).addSequenceId("newID");
    }

    @Test
    public void testCheckRequestWhenTheMethodShouldConcatANewSequenceKeyToAnExistingHeader() {
        //GIVEN
        String sequenceKeyFirst = "testKey1";
        String sequenceKeySecond = "testKey2";
        Map<String, WilmaSequence> sequences = new ConcurrentHashMap<>();
        sequences.put(sequenceKeyFirst, sequenceFirst);
        given(sequenceEvaluator.evaluate(request, sequenceDescriptor)).willReturn(true);
        given(sequenceDescriptor.getHandler()).willReturn(handler);
        given(handler.getExistingSequence(request, sequences, null)).willReturn(null);
        given(handler.generateNewSequenceKey(request, null)).willReturn(sequenceKeySecond);
        given(sequenceDescriptor.getDefaultTimeout()).willReturn(1000L);
        given(sequenceFactory.createNewSequence(sequenceKeySecond, request, 1000L)).willReturn(sequenceSecond);
        given(sequenceIdUtil.createSequenceId(sequenceKeySecond, sequenceDescriptor)).willReturn("newID");
        given(request.getSequenceId()).willReturn(sequenceKeyFirst);
        given(headerUtil.createSequenceHeader(sequenceKeyFirst, "newID")).willReturn("TwoKey");
        //WHEN
        underTest.checkRequest(request, sequenceDescriptor);
        //THEN
        verify(sequenceDescriptor).putIntoSequences(sequenceSecond);
        verify(request).addSequenceId("TwoKey");
    }
}
