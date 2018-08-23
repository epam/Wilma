package com.epam.wilma.stubconfig.condition.checker.json;
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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import org.apache.commons.io.IOUtils;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Unit test for {@link JsonSchemaChecker}.
 *
 * @author Tamas_Kohegyi
 */
public class WilmaStubSchemaTest {

    private static final String SCHEMA = "schema";
    private static final String LOG_IF_VALIDATION_FAILED = "logIfValidationFailed";

    private WilmaHttpRequest request;
    private ParameterList parameters;

    private JsonSchemaChecker underTest;

    @BeforeClass
    public void setup() {
        underTest = new JsonSchemaChecker();
        StubResourcePathProvider stubResourcePathProvider = new StubResourcePathProvider();
        stubResourcePathProvider.setTemplatesPath("");
        Whitebox.setInternalState(underTest, "stubResourcePathProvider", stubResourcePathProvider);
    }

    @Test
    public void testCheckValidInterceptors() throws IOException {
        givenExpectations("WilmaStubConfig.json", "false");
        givenWilmaRequest("JsonWilmaStubTestInterceptorsValid.json");

        boolean matches = underTest.checkCondition(request, parameters);

        assertTrue(matches);
    }

    @Test
    public void testCheckValidInterceptorsMinimal() throws IOException {
        givenExpectations("WilmaStubConfig.json", "false");
        givenWilmaRequest("JsonWilmaStubTestInterceptorsValidMinimal.json");

        boolean matches = underTest.checkCondition(request, parameters);

        assertTrue(matches);
    }


    @Test
    public void testCheckInValidInterceptorsMissingMandatoryName() throws IOException {
        givenExpectations("WilmaStubConfig.json", "false");
        givenWilmaRequest("JsonWilmaStubTestInValidInterceptorsMissingMandatoryName.json");

        boolean matches = underTest.checkCondition(request, parameters);

        assertFalse(matches);
    }

    @Test
    public void testCheckInValidInterceptorsMissingMandatoryClass() throws IOException {
        givenExpectations("WilmaStubConfig.json", "false");
        givenWilmaRequest("JsonWilmaStubTestInValidInterceptorsMissingMandatoryClass.json");

        boolean matches = underTest.checkCondition(request, parameters);

        assertFalse(matches);
    }

    @Test
    public void testCheckInValidInterceptorsMandatoryNameIsEmpty() throws IOException {
        givenExpectations("WilmaStubConfig.json", "false");
        givenWilmaRequest("JsonWilmaStubTestInValidInterceptorsMandatoryNameIsEmpty.json");

        boolean matches = underTest.checkCondition(request, parameters);

        assertFalse(matches);
    }

    @Test
    public void testCheckInValidInterceptorsMandatoryClassIsEmpty() throws IOException {
        givenExpectations("WilmaStubConfig.json", "false");
        givenWilmaRequest("JsonWilmaStubTestInValidInterceptorsMandatoryClassIsEmpty.json");

        boolean matches = underTest.checkCondition(request, parameters);

        assertFalse(matches);
    }

    private void givenWilmaRequest(String jsonFileName) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(jsonFileName);
        request = new WilmaHttpRequest();
        request.setBody(IOUtils.toString(inputStream));
    }

    private void givenExpectations(String schemaFile, String logValidationFailure) {
        parameters = new ParameterList();
        parameters.addParameter(new Parameter(SCHEMA, schemaFile));
        parameters.addParameter(new Parameter(LOG_IF_VALIDATION_FAILED, logValidationFailure));
    }

}
