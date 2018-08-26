package com.epam.wilma.stubconfig.json.schema;
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

import com.epam.wilma.common.helper.ResourceLoader;
import com.epam.wilma.domain.stubconfig.exception.StubConfigJsonSchemaException;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Used to parse the stub config json schema.
 *
 * @author Tamas Kohegyi
 */
@Component
public class StubConfigJsonSchemaParser {

    @Autowired
    @Qualifier("stubConfigJsonSchemaLocation")
    private String stubConfigJsonSchemaLocation;

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * Loads the Json Schema of the stub config.
     *
     * @return the parsed Schema instance
     */
    public Schema parseSchema() {
        Schema result;
        try {
            URL schemaFile = resourceLoader.loadResource(stubConfigJsonSchemaLocation);
            InputStream inputStream = schemaFile.openStream();
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            result = SchemaLoader.load(rawSchema);
        } catch (IOException e) {
            throw new StubConfigJsonSchemaException("Cannot load: " + stubConfigJsonSchemaLocation
                    + " as Json Schema to check the Stub Configuration, pls notify Wilma maintainers.");
        } catch (JSONException e) {
            throw new StubConfigJsonSchemaException("Parsing of stub config Json Schema failed.", e);
        }
        return result;
    }

}
