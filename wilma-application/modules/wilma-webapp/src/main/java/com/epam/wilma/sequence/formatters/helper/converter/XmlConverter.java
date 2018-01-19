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
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Converts XML to other formats.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class XmlConverter implements Converter {

    private final Logger logger = LoggerFactory.getLogger(XmlConverter.class);

    /**
     * Converts the given XML to JSON format.
     * @param xml the given XML
     * @param name not used
     * @return json or an empty String if an error occurred
     */
    @Override
    public String convert(final String xml, final String name) {
        String json;
        try {
            json = XML.toJSONObject(xml).toString();
        } catch (JSONException e) {
            logger.debug("Couldn't convert to JSON the following XML: " + xml, e);
            json = "";
        }
        return json;
    }
}
