package com.epam.wilma.common.helper;

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
