package com.epam.wilma.stubconfig.initializer.condition;
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
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.domain.stubconfig.TemporaryStubResourceHolder;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.stubconfig.condition.checker.general.header.HeaderParameterChecker;
import com.epam.wilma.stubconfig.condition.checker.json.JsonSchemaChecker;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;
import com.epam.wilma.stubconfig.initializer.support.ExternalInitializer;

/**
 * Provides unit tests for the class {@link ConditionCheckerInitializer}.
 * @author Tunde_Kovacs
 *
 */
public class ConditionCheckerInitializerTest {

    private static final String PATH = "config/condition-checkers";
    private static final String CHECKER_CLASS = "HeaderParameterChecker";
    private List<ConditionChecker> conditionCheckers;
    private ConditionChecker conditionChecker;

    @Mock
    private ApplicationContext appContext;
    @Mock
    private ExternalInitializer externalInitializer;
    @Mock
    private StubResourcePathProvider stubResourcePathProvider;
    @Mock
    private TemporaryStubResourceHolder stubResourceHolder;
    @InjectMocks
    private ConditionCheckerInitializer underTest;

    @BeforeMethod
    public void setUp() {
        underTest = Mockito.spy(new ConditionCheckerInitializer());
        MockitoAnnotations.initMocks(this);
        conditionCheckers = new ArrayList<>();
        given(stubResourceHolder.getConditionCheckers()).willReturn(conditionCheckers);
    }

    @Test
    public void testGetConditionCheckerShouldReturnConditionChecker() {
        //GIVEN
        given(appContext.getBean(CHECKER_CLASS, ConditionChecker.class)).willThrow(new NoSuchBeanDefinitionException(""));
        conditionChecker = new HeaderParameterChecker();
        conditionCheckers.add(conditionChecker);
        //WHEN
        ConditionChecker actual = underTest.getExternalClassObject(CHECKER_CLASS);
        //THEN
        assertEquals(actual, conditionChecker);
    }

    @Test
    public void testGetConditionCheckerShouldReturnConditionCheckerWhenTheClassIsManagedBySpring() {
        //GIVEN
        given(appContext.getBean(CHECKER_CLASS, ConditionChecker.class)).willReturn(conditionChecker);
        //WHEN
        ConditionChecker actual = underTest.getExternalClassObject(CHECKER_CLASS);
        //THEN
        assertEquals(actual, conditionChecker);
    }

    @Test
    public void testGetConditionCheckerWhenInternalExistsShouldReturnConditionChecker() {
        //GIVEN
        conditionChecker = new HeaderParameterChecker();
        conditionCheckers.add(conditionChecker);
        //WHEN
        ConditionChecker actual = underTest.getExternalClassObject(CHECKER_CLASS);
        //THEN
        assertEquals(actual, conditionChecker);
    }

    @Test
    public void testGetConditionCheckerWhenMoreInternalsExistShouldReturnConditionChecker() {
        //GIVEN
        conditionCheckers.clear();
        conditionChecker = new JsonSchemaChecker();
        conditionCheckers.add(conditionChecker);
        conditionChecker = new HeaderParameterChecker();
        conditionCheckers.add(conditionChecker);
        //WHEN
        ConditionChecker actual = underTest.getExternalClassObject(CHECKER_CLASS);
        //THEN
        assertEquals(actual, conditionChecker);
    }

    @Test
    public void testGetConditionCheckerWhenExternalExistsShouldReturnConditionChecker() {
        //GIVEN
        given(stubResourcePathProvider.getConditionCheckerPathAsString()).willReturn(PATH);
        conditionChecker = new HeaderParameterChecker();
        given(externalInitializer.loadExternalClass(CHECKER_CLASS, PATH, ConditionChecker.class)).willReturn(conditionChecker);
        //WHEN
        ConditionChecker actual = underTest.getExternalClassObject(CHECKER_CLASS);
        //THEN
        assertEquals(actual.getClass(), conditionChecker.getClass());
    }

    @Test(expectedExceptions = DescriptorValidationFailedException.class)
    public void testGetConditionCheckerWhenItDoesNotExistShouldThrowException() {
        //GIVEN
        given(stubResourcePathProvider.getConditionCheckerPathAsString()).willReturn(PATH);
        given(externalInitializer.loadExternalClass(CHECKER_CLASS, PATH, ConditionChecker.class)).willThrow(
                new DescriptorValidationFailedException(CHECKER_CLASS));
        //WHEN
        underTest.getExternalClassObject(CHECKER_CLASS);
        //THEN it should throw exception
    }
}
