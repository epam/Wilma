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

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 * Unit test for {@link VersionTitleProvider}.
 *
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

    @Before
    public void setUp() {
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
