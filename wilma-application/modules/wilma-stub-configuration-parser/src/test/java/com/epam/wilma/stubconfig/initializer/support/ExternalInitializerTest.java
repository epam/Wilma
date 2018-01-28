package com.epam.wilma.stubconfig.initializer.support;
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
import static org.testng.AssertJUnit.assertNotNull;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;

/**
 * Unit test for {@link ExternalInitializer}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class ExternalInitializerTest {

    private static final String EXTERNAL_CLASS_NAME = "ExternalClass";
    private static final String CLASS_PATH = "config/something";
    private static final Class<Object> INTERFACE_TO_CAST = Object.class;

    @Mock
    private ExternalClassInitializer externalClassInitializer;
    @Mock
    private ExternalJarClassInitializer externalJarClassInitializer;
    @Mock
    private StubResourcePathProvider stubResourcePathProvider;

    @InjectMocks
    private ExternalInitializer underTest;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadExternalClassShouldLoadFromExternalClassInitializerFirst() {
        //GIVEN
        given(externalClassInitializer.loadExternalClass(EXTERNAL_CLASS_NAME, CLASS_PATH, INTERFACE_TO_CAST)).willReturn(new Object());
        //WHEN
        Object result = underTest.loadExternalClass(EXTERNAL_CLASS_NAME, CLASS_PATH, INTERFACE_TO_CAST);
        //THEN
        assertNotNull(result);
        verify(externalJarClassInitializer, never()).loadExternalClass(EXTERNAL_CLASS_NAME, CLASS_PATH, INTERFACE_TO_CAST);
    }

    @Test
    public void testWhenExternalClassInitializerCannotLoadClassThenExternalJarClassInitializerShouldTryToLoadClass() {
        //GIVEN
        given(externalClassInitializer.loadExternalClass(EXTERNAL_CLASS_NAME, CLASS_PATH, INTERFACE_TO_CAST)).willThrow(
                new DescriptorValidationFailedException(""));
        given(stubResourcePathProvider.getJarPathAsString()).willReturn("path/jar");
        //WHEN
        underTest.loadExternalClass(EXTERNAL_CLASS_NAME, CLASS_PATH, INTERFACE_TO_CAST);
        //THEN
        verify(externalJarClassInitializer).loadExternalClass(EXTERNAL_CLASS_NAME, "path/jar", INTERFACE_TO_CAST);
    }

}
