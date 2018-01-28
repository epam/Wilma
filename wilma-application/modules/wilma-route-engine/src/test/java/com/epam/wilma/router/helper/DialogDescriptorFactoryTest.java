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

import static org.mockito.Mockito.verify;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for {@link DialogDescriptorFactory}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class DialogDescriptorFactoryTest {

    @Mock
    private ResponseDescriptorFactory responseDescriptorFactory;
    @Mock
    private ConditionDescriptorFactory conditionDescriptorFactory;

    @InjectMocks
    private DialogDescriptorFactory underTest;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createDialogDescriptorForStubMode() {
        //GIVEN
        final String dialogDescriptorName = "ddname";
        //WHEN
        underTest.createDialogDescriptorForStubMode(dialogDescriptorName);
        //THEN
        verify(responseDescriptorFactory).createResponseDescriptorForStubMode();
        verify(conditionDescriptorFactory).createConditionDescriptorForStubMode();
    }

}
