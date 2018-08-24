package com.epam.wilma.stubconfig;
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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Unit test for ensuring the proper content of the Stub Config Json Schema.
 *
 * @author Tamas_Kohegyi
 */
public class StubConfigJsonSchemaInterceptorTest extends StubConfigJsonSchemaTestBase {

    @BeforeClass
    public void setup() {
        setTestFilePath("schema/interceptors/");
        loadStubConfigJsonSchemaTest();
    }

    @Test
    public void testCheckValidInterceptors() throws IOException {
        String stubConfigRequest = givenStubConfigRequest("JsonWilmaStubTestInterceptorsValid.json");

        boolean matches = checkStubConfigValidity(stubConfigRequest);

        assertTrue(matches);
    }

    @Test
    public void testCheckValidInterceptorsMinimal() throws IOException {
        String stubConfigRequest = givenStubConfigRequest("JsonWilmaStubTestInterceptorsValidMinimal.json");

        boolean matches = checkStubConfigValidity(stubConfigRequest);

        assertTrue(matches);
    }

    @Test
    public void testCheckInValidInterceptorsMissingMandatoryName() throws IOException {
        String stubConfigRequest = givenStubConfigRequest("JsonWilmaStubTestInValidInterceptorsMissingMandatoryName.json");

        boolean matches = checkStubConfigValidity(stubConfigRequest);

        assertFalse(matches);
    }

    @Test
    public void testCheckInValidInterceptorsMissingMandatoryClass() throws IOException {
        String stubConfigRequest = givenStubConfigRequest("JsonWilmaStubTestInValidInterceptorsMissingMandatoryClass.json");

        boolean matches = checkStubConfigValidity(stubConfigRequest);

        assertFalse(matches);
    }

    @Test
    public void testCheckInValidInterceptorsMandatoryNameIsEmpty() throws IOException {
        String stubConfigRequest = givenStubConfigRequest("JsonWilmaStubTestInValidInterceptorsMandatoryNameIsEmpty.json");

        boolean matches = checkStubConfigValidity(stubConfigRequest);

        assertFalse(matches);
    }

    @Test
    public void testCheckInValidInterceptorsMandatoryClassIsEmpty() throws IOException {
        String stubConfigRequest = givenStubConfigRequest("JsonWilmaStubTestInValidInterceptorsMandatoryClassIsEmpty.json");

        boolean matches = checkStubConfigValidity(stubConfigRequest);

        assertFalse(matches);
    }

}
