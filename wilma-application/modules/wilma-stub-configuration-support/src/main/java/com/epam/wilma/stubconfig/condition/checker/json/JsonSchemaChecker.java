package com.epam.wilma.stubconfig.condition.checker.json;
/*==========================================================================
Copyright 2013-2018 EPAM Systems

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
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.exception.ConditionEvaluationFailedException;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Checks the message body as JSON with the given conditions.
 *
 * @author Tamas_Kohegyi
 */
@Component
public class JsonSchemaChecker implements ConditionChecker {

    private static final String SCHEMA = "schema";
    private static final String LOG_IF_VALIDATION_FAILED = "logIfValidationFailed";
    @Autowired
    private StubResourcePathProvider stubResourcePathProvider;

    @Override
    public boolean checkCondition(final WilmaHttpRequest request, final ParameterList parameters) {
        boolean result = true;
        String schemaString = parameters.get(SCHEMA);
        String logIfValidationFailedString = parameters.get(LOG_IF_VALIDATION_FAILED);
        boolean logIfValidationFailed = (logIfValidationFailedString != null && logIfValidationFailedString.compareToIgnoreCase("true") == 0);

        Schema jsonSchema;
        try {
            jsonSchema = readTemplateAsJsonSchemaFromFileSystem(schemaString);
        } catch (Exception e) {
            //TODO: note, this should be loaded one time instead of loading for every message - will be implemented such feature later
            throw new ConditionEvaluationFailedException("Cannot load: " + schemaString + " as Json Schema to check the messages, pls fix the configuration.");
        }

        JSONObject jsonToBeValidated = null;
        try {
            InputStream stream = new ByteArrayInputStream(request.getBody().getBytes(StandardCharsets.UTF_8.name()));
            jsonToBeValidated = new JSONObject(new JSONTokener(stream));
        } catch (IOException e) {
            //it is not a valid Json file
            result = false;
            if (logIfValidationFailed) {
                throw new ConditionEvaluationFailedException("Message: " + request.getWilmaMessageLoggerId() + " failed schema validation: invalid JSON content.");
            }
        }

        if (result) {
            try {
                jsonSchema.validate(jsonToBeValidated);
            } catch (ValidationException e) {
                //it is not a good Json file
                result = false;
                if (logIfValidationFailed) {
                    throw new ConditionEvaluationFailedException("Message: " + request.getWilmaMessageLoggerId() + " failed schema validation: " + schemaString);
                }

            }
        }
        return result;
    }

    private Schema readTemplateAsJsonSchemaFromFileSystem(final String jsonSchemaName) {
        String jsonSchemaPath = (stubResourcePathProvider.getTemplatesPathAsString() + "/" + jsonSchemaName).replace("\\", "/");
        InputStream inputStream = getClass().getResourceAsStream(jsonSchemaPath);
        JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
        Schema schema = SchemaLoader.load(rawSchema);
        return schema;
    }

}
