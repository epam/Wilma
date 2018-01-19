package com.epam.wilma.router.evaluation;
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
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.helper.StackTraceToStringConverter;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.domain.stubconfig.StubDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.condition.Condition;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionDescriptor;
import com.epam.wilma.router.domain.ResponseDescriptorDTO;
import com.epam.wilma.router.evaluation.helper.DialogDescriptorService;
import com.epam.wilma.router.helper.ResponseDescriptorDtoFactory;
import com.epam.wilma.router.helper.WilmaHttpRequestCloner;

/**
 * Provides unit tests for the class {@link StubDescriptorEvaluator}.
 * @author Tunde_Kovacs
 *
 */
public class StubDescriptorEvaluatorTest {

    private static final String DIALOG_DESCRIPTOR_NAME = "dialog-descriptor";

    private static final String REQUEST_BODY = "body";
    private static final String DEFAULT_GROUPNAME = "test";
    private List<DialogDescriptor> dialogDescriptors;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private DialogDescriptor dialogDescriptor;
    @Mock
    private StubDescriptor stubDescriptor;
    @Mock
    private ConditionEvaluator conditionEvaluator;
    @Mock
    private ConditionDescriptor conditionDescriptor;
    @Mock
    private ResponseDescriptorDTO responseDescriptorDTO;
    @Mock
    private ResponseDescriptorDTO responseDescriptorDTOWithError;
    @Mock
    private ResponseDescriptorDtoFactory responseDescriptorDtoFactory;
    @Mock
    private Condition condition;
    @Mock
    private WilmaHttpRequest request;
    @Mock
    private Logger logger;
    @Mock
    private StackTraceToStringConverter stackTraceConverter;
    @Mock
    private WilmaHttpRequestCloner requestCloner;
    @Mock
    private WilmaHttpRequest clonedRequest;
    @Mock
    private DialogDescriptorService dialogDescriptorService;

    private Map<String, StubDescriptor> stubDescriptors;
    private StubDescriptorAttributes attributes;

    @InjectMocks
    private StubDescriptorEvaluator underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dialogDescriptors = new ArrayList<>();
        stubDescriptors = new LinkedHashMap<>();
        stubDescriptors.put(DEFAULT_GROUPNAME, stubDescriptor);
        dialogDescriptors.add(dialogDescriptor);
        attributes = new StubDescriptorAttributes(DEFAULT_GROUPNAME, true);
        given(stubDescriptor.getAttributes()).willReturn(attributes);
        given(requestCloner.cloneRequest(request)).willReturn(clonedRequest);
        given(dialogDescriptorService.isEnabled(dialogDescriptor)).willReturn(true);
        given(request.popEvaluationResult(dialogDescriptor)).willReturn(null);
    }

    @Test
    public void testFindResponseDescriptorShouldReturnNewDTO() {
        //GIVEN
        given(stubDescriptor.getDialogDescriptors()).willReturn(dialogDescriptors);
        given(dialogDescriptor.getConditionDescriptor()).willReturn(conditionDescriptor);
        given(conditionDescriptor.getCondition()).willReturn(condition);
        given(conditionEvaluator.evaluate(condition, clonedRequest)).willReturn(true);
        given(request.getBody()).willReturn(REQUEST_BODY);
        given(responseDescriptorDtoFactory.createResponseDescriptorDTO(REQUEST_BODY, dialogDescriptor)).willReturn(responseDescriptorDTO);
        //WHEN
        ResponseDescriptorDTO actual = underTest.findResponseDescriptor(stubDescriptors, request);
        //THEN
        assertEquals(actual, responseDescriptorDTO);
    }

    @Test
    public void testFindResponseDescriptorWhenConditionThrowsExceptionShouldLogError() {
        //GIVEN
        NullPointerException nullPointerException = new NullPointerException();
        given(stubDescriptor.getDialogDescriptors()).willReturn(dialogDescriptors);
        given(dialogDescriptor.getConditionDescriptor()).willReturn(conditionDescriptor);
        given(dialogDescriptor.getAttributes().getName()).willReturn(DIALOG_DESCRIPTOR_NAME);
        given(conditionDescriptor.getCondition()).willReturn(condition);
        given(conditionEvaluator.evaluate(condition, clonedRequest)).willThrow(nullPointerException);
        given(stackTraceConverter.getStackTraceAsString(nullPointerException)).willReturn("error");
        Whitebox.setInternalState(underTest, "logger", logger);
        //WHEN
        underTest.findResponseDescriptor(stubDescriptors, request);
        //THEN
        verify(logger).error(Mockito.eq("Error during condition evaluation in the dialog descriptor 'dialog-descriptor'!"),
                Mockito.any(NullPointerException.class));
    }

    @Test
    public void testFindResponseDescriptorWhenConditionThrowsExceptionShouldReturnRespDescDTOWithError() {
        //GIVEN
        NullPointerException nullPointerException = new NullPointerException();
        given(stubDescriptor.getDialogDescriptors()).willReturn(dialogDescriptors);
        given(dialogDescriptor.getConditionDescriptor()).willReturn(conditionDescriptor);
        given(dialogDescriptor.getAttributes().getName()).willReturn(DIALOG_DESCRIPTOR_NAME);
        given(conditionDescriptor.getCondition()).willReturn(condition);
        given(conditionEvaluator.evaluate(condition, clonedRequest)).willThrow(nullPointerException);
        given(request.getBody()).willReturn(REQUEST_BODY);
        given(stackTraceConverter.getStackTraceAsString(nullPointerException)).willReturn("error");
        given(responseDescriptorDtoFactory.createResponseDescriptorDTOWithError(dialogDescriptor, REQUEST_BODY, "error".getBytes())).willReturn(
                responseDescriptorDTOWithError);
        Whitebox.setInternalState(underTest, "logger", logger);
        //WHEN
        ResponseDescriptorDTO actual = underTest.findResponseDescriptor(stubDescriptors, request);
        //THEN
        assertEquals(actual, responseDescriptorDTOWithError);
    }

    @Test
    public void testFindResponseDescriptorWhenConditionIsFalseShouldReturnNull() {
        //GIVEN
        given(stubDescriptor.getDialogDescriptors()).willReturn(dialogDescriptors);
        given(dialogDescriptor.getConditionDescriptor()).willReturn(conditionDescriptor);
        given(conditionDescriptor.getCondition()).willReturn(condition);
        given(conditionEvaluator.evaluate(condition, clonedRequest)).willReturn(false);
        //WHEN
        ResponseDescriptorDTO actual = underTest.findResponseDescriptor(stubDescriptors, request);
        //THEN
        assertNull(actual);
    }

    @Test
    public void testFindResponseDescriptorWhenDialogDescriptorIsDisabledShouldReturnNull() {
        //GIVEN
        given(stubDescriptor.getDialogDescriptors()).willReturn(dialogDescriptors);
        given(dialogDescriptorService.isEnabled(dialogDescriptor)).willReturn(false);
        //WHEN
        ResponseDescriptorDTO actual = underTest.findResponseDescriptor(stubDescriptors, request);
        //THEN
        assertNull(actual);
    }

    @Test
    public void testFindResponseDescriptorShouldReturnNullBecauseTheOneStubDescriptorIsDisabled() {
        //GIVEN
        attributes = new StubDescriptorAttributes(DEFAULT_GROUPNAME, false);
        given(stubDescriptor.getAttributes()).willReturn(attributes);
        //WHEN
        ResponseDescriptorDTO actual = underTest.findResponseDescriptor(stubDescriptors, request);
        //THEN
        verify(stubDescriptor, never()).getDialogDescriptors();
    }

    @Test
    public void testFindResponseDescriptorShouldNotEvaluateDialogDescriptorWhichWasEvaluatedTrueBySequenceHandling() {
        //GIVEN
        given(request.popEvaluationResult(dialogDescriptor)).willReturn(Boolean.TRUE);
        //WHEN
        underTest.findResponseDescriptor(stubDescriptors, request);
        //THEN
        verify(conditionEvaluator, never()).evaluate(condition, clonedRequest);
    }

    @Test
    public void testFindResponseDescriptorShouldNotEvaluateDialogDescriptorWhichWasEvaluatedFalseBySequenceHandling() {
        //GIVEN
        given(request.popEvaluationResult(dialogDescriptor)).willReturn(Boolean.FALSE);
        //WHEN
        underTest.findResponseDescriptor(stubDescriptors, request);
        //THEN
        verify(conditionEvaluator, never()).evaluate(condition, clonedRequest);
    }

    @Test
    public void testFindResponseDescriptorShouldNotReturnNullWhenDialogDescriptorhWasEvaluatedTrueBySequenceHandling() {
        //GIVEN
        given(stubDescriptor.getDialogDescriptors()).willReturn(dialogDescriptors);
        given(request.popEvaluationResult(dialogDescriptor)).willReturn(Boolean.TRUE);
        given(request.getBody()).willReturn(REQUEST_BODY);
        given(responseDescriptorDtoFactory.createResponseDescriptorDTO(REQUEST_BODY, dialogDescriptor)).willReturn(responseDescriptorDTO);
        //WHEN
        ResponseDescriptorDTO actual = underTest.findResponseDescriptor(stubDescriptors, request);
        //THEN
        assertNotNull(actual);
    }

    @Test
    public void testFindResponseDescriptorShouldReturnNullWhenDialogDescriptorhWasEvaluatedFalseBySequenceHandling() {
        //GIVEN
        given(stubDescriptor.getDialogDescriptors()).willReturn(dialogDescriptors);
        given(request.popEvaluationResult(dialogDescriptor)).willReturn(Boolean.FALSE);
        given(request.getBody()).willReturn(REQUEST_BODY);
        given(responseDescriptorDtoFactory.createResponseDescriptorDTO(REQUEST_BODY, dialogDescriptor)).willReturn(responseDescriptorDTO);
        //WHEN
        ResponseDescriptorDTO actual = underTest.findResponseDescriptor(stubDescriptors, request);
        //THEN
        assertNull(actual);
    }
}
