package com.epam.wilma.stubconfig.json.parser.helper;
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

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.xml.transform.TransformerException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * Class for transforming a {@link org.json.JSONObject} to byte array.
 *
 * @author Tamas_Kohegyi
 */
@Component
public class JsonBasedObjectTransformer {

    private static final int INDENT_FACTOR = 4;

    /**
     * Transforms a {@link org.json.JSONObject} to byte array.
     *
     * @param object will be transformed
     * @return with the json as byte array
     */
    public byte[] transform(final JSONObject object) {
        String objectString = object.toString(INDENT_FACTOR);
        byte[] b = objectString.getBytes(StandardCharsets.UTF_8);
        return b;
    }

    /**
     * Transforms a {@link JSONObject} into a new File.
     *
     * @param jsonObject   will be transformed
     * @param path         will be the path of the new file
     * @param actualStatus is the is the actual status of this stub descriptor
     * @throws TransformerException is thrown when unrecoverable error occurs during the course of the transformation
     */
    public void transformToFile(final JSONObject jsonObject, final String path, final boolean actualStatus) throws TransformerException {
        try {
            if (jsonObject.getJSONObject("wilmaStubConfiguration").has("active")) {
                jsonObject.getJSONObject("wilmaStubConfiguration").put("active", String.valueOf(actualStatus));
            }
            Writer output;
            String text = jsonObject.toString(INDENT_FACTOR);
            File file = new File(path);
            output = new BufferedWriter(new FileWriter(file));
            output.write(text);
            output.close();
        } catch (IOException e) {
            throw new TransformerException(e);
        }
    }
}
