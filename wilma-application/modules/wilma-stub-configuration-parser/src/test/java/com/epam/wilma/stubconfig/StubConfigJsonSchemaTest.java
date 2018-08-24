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

import org.apache.commons.io.IOUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Unit test for ensuring the proper content of the Stub Config Json Schema.
 *
 * @author Tamas_Kohegyi
 */
public class StubConfigJsonSchemaTest {

    private String testFilePath;

    private Schema underTest;

    @BeforeClass
    public void setup() {
        testFilePath = "schema/interceptors/";
        //load main schema
        String jsonSchemaPath = ("../../../../StubConfig.json");
        InputStream inputStream = getClass().getResourceAsStream(jsonSchemaPath);
        JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
        underTest = SchemaLoader.load(rawSchema);
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

    private String givenStubConfigRequest(String jsonFileName) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(testFilePath + jsonFileName);
        return IOUtils.toString(inputStream);
    }

    private boolean checkStubConfigValidity(String stubConfigRequest) {
        boolean result = true;

        JSONObject jsonToBeValidated = null;
        try {
            InputStream stream = new ByteArrayInputStream(stubConfigRequest.getBytes(StandardCharsets.UTF_8.name()));
            jsonToBeValidated = new JSONObject(new JSONTokener(stream));
        } catch (JSONException | IOException e) {
            //it is not a valid Json file
            result = false;
        }
        if (result) {
            try {
                underTest.validate(jsonToBeValidated);
            } catch (ValidationException e) {
                //it is not a good Json file
                result = false;
            }
        }
        return result;
    }
}
