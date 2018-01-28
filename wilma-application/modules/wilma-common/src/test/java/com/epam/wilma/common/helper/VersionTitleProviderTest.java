package com.epam.wilma.common.helper;
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
import static org.testng.AssertJUnit.assertEquals;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for {@link VersionTitleProvider}.
 * @author Adam_Csaba_Kiraly
 */
public class VersionTitleProviderTest {

    private static final String NOT_FOUND = "unknown (no manifest file)";

    @Mock
    private PackageProvider packageProvider;
    @InjectMocks
    private VersionTitleProvider underTest;

    @Mock
    private Package packageOfUnderTest;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWhenManifestsExistsVersionTitleShouldBeReturned() {
        //GIVEN
        given(packageProvider.getPackageOfObject(underTest)).willReturn(packageOfUnderTest);
        given(packageOfUnderTest.getImplementationTitle()).willReturn("something");
        //WHEN
        String result = underTest.getVersionTitle();
        //THEN
        assertEquals("something", result);
    }

    @Test
    public void testWhenManifestsNotFoundTextShouldBeReturned() {
        //GIVEN
        given(packageProvider.getPackageOfObject(underTest)).willReturn(packageOfUnderTest);
        given(packageOfUnderTest.getImplementationTitle()).willReturn(null);
        //WHEN
        String result = underTest.getVersionTitle();
        //THEN
        assertEquals(NOT_FOUND, result);
    }

}
