package com.epam.wilma.stubconfig.initializer.support.helper;

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

import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link PackageBasedClassFinder}.
 *
 * @author Adam_Csaba_Kiraly
 */
public class PackageBasedClassFinderTest {

    private static final String PACKAGE_NAME = "a.b.c";
    private static final String JAR_FOLDER_PATH = "";
    private static final Class<Object> INTERFACE_OR_CLASS = Object.class;

    private static final String MULTIPLE_CLASSES_FOUND_TEMPLATE = "Warning! Multiple classes found '%s' in package '%s' as subtype of '%s'.";

    @InjectMocks
    private PackageBasedClassFinder underTest;

    @Mock
    private Logger logger;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        FieldSetter.setField(underTest, underTest.getClass().getDeclaredField("logger"), logger);
    }

    @Ignore
    @Test
    public void testFindFirstOfShouldNotReturnNull() throws MalformedURLException {
        //GIVEN
        Set<Class<? extends Object>> value = new HashSet<>();
        value.add(Object.class);
        //WHEN
        Class<? extends Object> result = underTest.findClassInJar(JAR_FOLDER_PATH, INTERFACE_OR_CLASS, PACKAGE_NAME);
        //THEN
        assertNotNull(result);
    }

    @Ignore
    @Test(expected = DescriptorValidationFailedException.class)
    public void testFindClassInJarOfShouldThrowDescriptorValidationFailedExceptionExceptionWhenNoClassIsFound() throws ClassNotFoundException, MalformedURLException {
        //GIVEN
        Set<Class<? extends Object>> value = new HashSet<>();
        //WHEN
        underTest.findClassInJar(JAR_FOLDER_PATH, INTERFACE_OR_CLASS, PACKAGE_NAME);
        //THEN exception is thrown
    }

    @Ignore
    @Test
    public void testFindClassInJarShouldWarnIfMoreClassesAreFound() throws ClassNotFoundException, MalformedURLException {
        //GIVEN
        Set<Class<? extends Object>> value = new HashSet<>();
        value.add(Object.class);
        value.add(String.class);
        //WHEN
        underTest.findClassInJar(JAR_FOLDER_PATH, INTERFACE_OR_CLASS, PACKAGE_NAME);
        //THEN
        String expected = String.format(MULTIPLE_CLASSES_FOUND_TEMPLATE, value, PACKAGE_NAME, INTERFACE_OR_CLASS);
        verify(logger).info(expected);
    }
}
