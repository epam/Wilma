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

import static org.testng.Assert.assertTrue;

/**
 * Unit test for ensuring the proper content of the Stub Config Json Schema.
 *
 * @author Tamas_Kohegyi
 */
public class StubConfigJsonSchemaTest extends StubConfigJsonSchemaTestBase {

    @BeforeClass
    public void setup() {
        setTestFilePath("");
        loadStubConfigJsonSchemaTest();
    }

    @Test
    public void testCheckValid() throws IOException {
        String stubConfigRequest = givenStubConfigRequest("JsonWilmaStubTestValid.json");

        boolean matches = checkStubConfigValidity(stubConfigRequest);

        assertTrue(matches);
    }

    @Test
    public void testCheckValidInterceptorsMinimal() throws IOException {
        String stubConfigRequest = givenStubConfigRequest("JsonWilmaStubTestValidMinimal.json");

        boolean matches = checkStubConfigValidity(stubConfigRequest);

        assertTrue(matches);
    }

}
