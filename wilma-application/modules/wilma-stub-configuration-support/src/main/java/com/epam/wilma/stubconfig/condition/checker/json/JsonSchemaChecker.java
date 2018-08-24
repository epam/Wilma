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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Checks the message body as JSON with the given conditions.
 *
 * @author Tamas_Kohegyi
 */
@Component
public class JsonSchemaChecker implements ConditionChecker {

    //Parameters to be used for this Condition Checker
    //mandatory
    private static final String SCHEMA = "schema";
    // optional, default: false
    private static final String LOG_IF_VALIDATION_FAILED = "logIfValidationFailed";
    // optional, default: true - if it is false then the specific schema will be cached during Wilma's lifetime
    private static final String IS_SCHEMA_VOLATILE = "isSchemaVolatile";

    private static Map<String, Schema> schemaMap = new ConcurrentHashMap<>();

    @Autowired
    private StubResourcePathProvider stubResourcePathProvider;

    @Override
    public boolean checkCondition(final WilmaHttpRequest request, final ParameterList parameters) {
        boolean result = true;
        String schemaString = parameters.get(SCHEMA);
        String logIfValidationFailedString = parameters.get(LOG_IF_VALIDATION_FAILED);
        boolean logIfValidationFailed = logIfValidationFailedString != null && logIfValidationFailedString.compareToIgnoreCase("true") == 0;
        String isSchemaVolatileString = parameters.get(IS_SCHEMA_VOLATILE);
        boolean isSchemaVolatile = isSchemaVolatileString == null || isSchemaVolatileString.compareToIgnoreCase("true") == 0;

        Schema jsonSchema;
        try {
            if (!isSchemaVolatile && schemaMap.containsKey(schemaString)) {
                jsonSchema = schemaMap.get(schemaString);
            } else {  //either volatile or schema is not yet loaded
                jsonSchema = readTemplateAsJsonSchemaFromFileSystem(schemaString);
                if (!isSchemaVolatile) {
                    schemaMap.put(schemaString, jsonSchema);
                }
            }
        } catch (Exception e) {
            throw new ConditionEvaluationFailedException("Cannot load: " + schemaString + " as Json Schema to check the messages, pls fix the configuration.");
        }

        JSONObject jsonToBeValidated = null;
        try {
            InputStream stream = new ByteArrayInputStream(request.getBody().getBytes(StandardCharsets.UTF_8.name()));
            jsonToBeValidated = new JSONObject(new JSONTokener(stream));
        } catch (JSONException | IOException e) {
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
        String jsonSchemaPath = (stubResourcePathProvider.getTemplatesPathAsString() + jsonSchemaName).replace("\\", "/");
        InputStream inputStream = getClass().getResourceAsStream(jsonSchemaPath);
        JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
        Schema schema = SchemaLoader.load(rawSchema);
        return schema;
    }

}
