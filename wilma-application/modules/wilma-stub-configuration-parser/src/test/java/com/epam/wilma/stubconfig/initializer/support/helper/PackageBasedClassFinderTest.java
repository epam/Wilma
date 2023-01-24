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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * Unit test for {@link PackageBasedClassFinder}.
 *
 * @author Adam_Csaba_Kiraly
 */
public class PackageBasedClassFinderTest {

    private static final String PACKAGE_NAME = "a.b.c";
    private static final String JAR_FOLDER_PATH = "";
    private static final Class<Object> INTERFACE_OR_CLASS = Object.class;

    @InjectMocks
    private PackageBasedClassFinder underTest;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindClassInJarOfShouldThrowDescriptorValidationFailedExceptionExceptionWhenNoClassIsFound() {
        Assertions.assertThrows(DescriptorValidationFailedException.class, () -> {
            //GIVEN
            //WHEN
            underTest.findClassInJar(JAR_FOLDER_PATH, INTERFACE_OR_CLASS, PACKAGE_NAME);
            //THEN exception is thrown
        });
    }

}
