package com.epam.wilma.sequence.evaluator;
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

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.condition.Condition;
import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionDescriptor;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;
import com.epam.wilma.router.evaluation.ConditionEvaluator;
import com.epam.wilma.router.evaluation.helper.DialogDescriptorService;
import com.epam.wilma.router.helper.WilmaHttpRequestCloner;
import com.epam.wilma.sequence.helper.SequenceDescriptorKeyUtil;

/**
 * Provides unit tests for the class {@link SequenceDescriptorEvaluator}.
 * @author Tibor_Kovacs
 *
 */
public class SequenceDescriptorEvaluatorTest {

    @Mock
    private DialogDescriptor dialogDescriptor;
    @Mock
    private ConditionDescriptor conditionDescriptor;
    @Mock
    private Condition condition;
    @Mock
    private WilmaHttpRequest clonedRequest;
    @Mock
    private ConditionEvaluator conditionEvaluator;
    @Mock
    private WilmaHttpRequestCloner requestCloner;
    @Mock
    private DialogDescriptorService dialogDescriptorService;
    @Mock
    private WilmaHttpRequest request;
    @Mock
    private SequenceDescriptor sequenceDescriptor;
    @Mock
    private Logger logger;
    @Mock
    private SequenceDescriptorKeyUtil sequenceDescriptorKeyUtil;

    @InjectMocks
    private SequenceDescriptorEvaluator underTest;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
        given(requestCloner.cloneRequest(request)).willReturn(clonedRequest);
        given(dialogDescriptorService.isEnabled(dialogDescriptor)).willReturn(true);
        given(sequenceDescriptorKeyUtil.createDescriptorKey("TestTeam", "test")).willReturn("TestTeam_test");
    }

    @Test
    public void testEvaluateWhenThereIsNotAnyConditionAndDialogDescriptor() {
        //GIVEN
        given(sequenceDescriptor.getConditionDescriptors()).willReturn(new ArrayList<ConditionDescriptor>());
        given(sequenceDescriptor.getDialogDescriptors()).willReturn(new ArrayList<DialogDescriptor>());
        //WHEN
        boolean result = underTest.evaluate(request, sequenceDescriptor);
        //THEN
        Assert.assertFalse(result);
    }

    @Test
    public void testEvaluateWhenAConditionIsTrue() {
        //GIVEN
        given(dialogDescriptor.getConditionDescriptor()).willReturn(conditionDescriptor);
        given(conditionDescriptor.getCondition()).willReturn(condition);
        given(conditionEvaluator.evaluate(condition, clonedRequest)).willReturn(true);

        List<ConditionDescriptor> cds = new ArrayList<>();
        cds.add(conditionDescriptor);
        given(sequenceDescriptor.getConditionDescriptors()).willReturn(cds);
        List<DialogDescriptor> dds = new ArrayList<>();
        dds.add(dialogDescriptor);
        given(sequenceDescriptor.getDialogDescriptors()).willReturn(dds);
        //WHEN
        boolean result = underTest.evaluate(request, sequenceDescriptor);
        //THEN
        Assert.assertTrue(result);
    }

    @Test
    public void testEvaluateWhenOccurredAnExceptionInConditionEvaluation() {
        //GIVEN
        Exception e = new RuntimeException();
        given(sequenceDescriptor.getName()).willReturn("test");
        given(dialogDescriptor.getConditionDescriptor()).willReturn(conditionDescriptor);
        given(conditionDescriptor.getCondition()).willReturn(condition);
        given(conditionEvaluator.evaluate(condition, clonedRequest)).willThrow(e);

        List<ConditionDescriptor> cds = new ArrayList<>();
        cds.add(conditionDescriptor);
        given(sequenceDescriptor.getConditionDescriptors()).willReturn(cds);
        given(sequenceDescriptor.getDialogDescriptors()).willReturn(new ArrayList<DialogDescriptor>());
        //WHEN
        boolean result = underTest.evaluate(request, sequenceDescriptor);
        //THEN
        Assert.assertFalse(result);
    }

    @Test
    public void testEvaluateWhenADialogDescriptorIsTrue() {
        //GIVEN
        given(dialogDescriptor.getConditionDescriptor()).willReturn(conditionDescriptor);
        given(conditionDescriptor.getCondition()).willReturn(condition);
        given(conditionEvaluator.evaluate(condition, clonedRequest)).willReturn(false).willReturn(true);

        List<ConditionDescriptor> cds = new ArrayList<>();
        cds.add(conditionDescriptor);
        given(sequenceDescriptor.getConditionDescriptors()).willReturn(cds);
        List<DialogDescriptor> dds = new ArrayList<>();
        dds.add(dialogDescriptor);
        given(sequenceDescriptor.getDialogDescriptors()).willReturn(dds);
        //WHEN
        boolean result = underTest.evaluate(request, sequenceDescriptor);
        //THEN
        Assert.assertTrue(result);
    }

    @Test
    public void testEvaluateWhenDialogDescriptorIsNotEnabled() {
        //GIVEN
        given(dialogDescriptorService.isEnabled(dialogDescriptor)).willReturn(false);
        given(dialogDescriptor.getConditionDescriptor()).willReturn(conditionDescriptor);
        given(conditionDescriptor.getCondition()).willReturn(condition);
        given(conditionEvaluator.evaluate(condition, clonedRequest)).willReturn(false).willReturn(true);

        List<ConditionDescriptor> cds = new ArrayList<>();
        cds.add(conditionDescriptor);
        given(sequenceDescriptor.getConditionDescriptors()).willReturn(cds);
        List<DialogDescriptor> dds = new ArrayList<>();
        dds.add(dialogDescriptor);
        given(sequenceDescriptor.getDialogDescriptors()).willReturn(dds);
        //WHEN
        boolean result = underTest.evaluate(request, sequenceDescriptor);
        //THEN
        Assert.assertFalse(result);
    }

}
