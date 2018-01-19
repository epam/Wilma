package com.epam.wilma.sequence.formatters.helper.converter;

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

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Used to convert JSON to other formats.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class JsonConverter implements Converter {

    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private final Logger logger = LoggerFactory.getLogger(JsonConverter.class);

    /**
     * Converts the given JSON data to XML.
     * @param json the given json data
     * @param rootName the root name of the generated xml
     * @return xml or an empty String if an error occurred
     */
    @Override
    public String convert(final String json, final String rootName) {
        String xml;
        try {
            JSONObject jsonObject = new JSONObject(json);
            xml = buildXMLFrom(XML.toString(jsonObject), rootName);
        } catch (JSONException e) {
            logger.debug("Couldn't convert to XML the following JSON: " + json, e);
            xml = "";
        }
        return xml;
    }

    private String buildXMLFrom(final String xmlBody, final String xmlRootName) {
        StringBuilder sb = new StringBuilder(XML_HEADER).append('<').append(xmlRootName).append('>');
        sb.append(xmlBody);
        sb.append("</").append(xmlRootName).append('>');
        return sb.toString();
    }
}
