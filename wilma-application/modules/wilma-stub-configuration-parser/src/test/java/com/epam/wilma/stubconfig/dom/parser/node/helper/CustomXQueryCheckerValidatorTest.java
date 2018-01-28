package com.epam.wilma.stubconfig.dom.parser.node.helper;
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

import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doReturn;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.domain.stubconfig.exception.ConditionEvaluationFailedException;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;

/**
 * Provides unit tests for the class {@link CustomXQueryCheckerValidator}.
 * @author Tunde_Kovacs
 *
 */
public class CustomXQueryCheckerValidatorTest {

    private static final String CUSTOM_BODY_CHECKER = "CustomXQueryBodyChecker";

    private ParameterList parameterList;

    @Mock
    private ConditionChecker conditionChecker;

    private CustomXQueryCheckerValidator underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = Mockito.spy(new CustomXQueryCheckerValidator());
        parameterList = new ParameterList();
    }

    @Test
    public void testValidateWhenXQueryIsSyntacticallyCorrectShouldPass() {
        //GIVEN
        parameterList.addParameter(new Parameter("check", "valid xquery"));
        doReturn(CUSTOM_BODY_CHECKER).when(underTest).getSimpleName(conditionChecker);
        //WHEN
        underTest.validate(conditionChecker, parameterList);
        //THEN it should not throw exception
    }

    @Test(expectedExceptions = DescriptorValidationFailedException.class)
    public void testValidateWhenXQueryIsSyntacticallyIncorrectShouldThrowException() {
        //GIVEN
        parameterList.addParameter(new Parameter("check", "invalid xquery"));
        doReturn(CUSTOM_BODY_CHECKER).when(underTest).getSimpleName(conditionChecker);
        willThrow(new ConditionEvaluationFailedException("exception")).given(conditionChecker).checkCondition(Mockito.any(WilmaHttpRequest.class),
                Mockito.eq(parameterList));
        //WHEN
        underTest.validate(conditionChecker, parameterList);
        //THEN it should throw exception
    }

    @Test
    public void testValidateWhenConditionCheckerIsNotCustomXQueryBodyCheckerShouldPass() {
        //GIVEN
        doReturn("another ckass").when(underTest).getSimpleName(conditionChecker);
        //WHEN
        underTest.validate(conditionChecker, parameterList);
        //THEN it should not throw exception
    }

    @Test
    public void testValidateWhenParameterMapSizeIsZeroShouldPass() {
        //GIVEN
        doReturn(CUSTOM_BODY_CHECKER).when(underTest).getSimpleName(conditionChecker);
        //WHEN
        underTest.validate(conditionChecker, parameterList);
        //THEN it should not throw exception
    }

    @Test
    public void testValidateWhenParameterMapSizeIsTwoShouldPass() {
        //GIVEN
        parameterList.addParameter(new Parameter("check", "valid xquery"));
        parameterList.addParameter(new Parameter("check1", "valid xquery"));
        doReturn(CUSTOM_BODY_CHECKER).when(underTest).getSimpleName(conditionChecker);
        //WHEN
        underTest.validate(conditionChecker, parameterList);
        //THEN it should not throw exception
    }
}
