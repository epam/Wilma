package com.epam.wilma.router.helper;
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

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.stubconfig.dialog.condition.ConditionDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.condition.SimpleCondition;

/**
 * Unit test for {@link ConditionDescriptorFactory}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class ConditionDescriptorFactoryTest {

    private ConditionDescriptorFactory underTest;

    @BeforeMethod
    public void setUp() throws Exception {
        underTest = new ConditionDescriptorFactory();
    }

    @Test
    public void testCreateConditionDescriptorForStubMode() {
        //GIVEN nothing special
        //WHEN
        ConditionDescriptor actualConditionDescriptor = underTest.createConditionDescriptorForStubMode();
        //THEN
        SimpleCondition actualSimpleCondition = (SimpleCondition) actualConditionDescriptor.getCondition();
        assertTrue(actualSimpleCondition.getConditionChecker() instanceof StubModeConditionChecker);
        assertFalse(actualSimpleCondition.isNegate());
        assertTrue(actualSimpleCondition.getParameters().isEmpty());

    }

}
