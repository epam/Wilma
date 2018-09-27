package com.epam.wilma.service.configuration.stub.helper.common;
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

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * This validator class is used to check the generated config is valid or not.
 *
 * @author tkohegyi
 */
public class StubConfigurationValidator {
    static final String WILMA_SCHEMA = "StubConfig.json";

    /**
     * Validates the specified string as stub configuration Json against the Json Schema.
     *
     * @param candidateConfiguration is the candidate
     * @throws StubConfigurationException when the (structure) validation fails
     */
    public void validate(String candidateConfiguration) throws StubConfigurationException {
        Schema jsonSchema;
        try {
            URL schemaFile = loadResource(WILMA_SCHEMA);
            InputStream inputStream = schemaFile.openStream();
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            jsonSchema = SchemaLoader.load(rawSchema);
            //load the json file
            JSONObject jsonStubDescriptor = new JSONObject(new JSONTokener(new ByteArrayInputStream(candidateConfiguration.getBytes("utf-8"))));
            //validate against schema
            jsonSchema.validate(jsonStubDescriptor);
        } catch (IOException e) {
            throw new StubConfigurationException("Cannot load: " + WILMA_SCHEMA
                    + " as Json Schema to check the generated Stub Configuration, please notify Wilma maintainers.");
        } catch (JSONException e) {
            throw new StubConfigurationException("Parsing of stub config Json Schema failed, please notify Wilma maintainers.", e);
        }
    }

    /**
     * Loads a resource URL with the {@link ClassLoader}.
     *
     * @param name the name of the resource
     * @return the resource URL
     */
    public URL loadResource(final String name) {
        return this.getClass().getClassLoader().getResource(name);
    }

}
