package com.epam.wilma.core.processor.entity;

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

import com.epam.wilma.common.stream.helper.StreamResultFactory;
import com.epam.wilma.common.stream.helper.StreamSourceFactory;
import com.epam.wilma.core.processor.entity.helper.XmlTransformerFactory;
import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

/**
 * Indents a request/response body in order to make it readable.
 *
 * @author Tunde_Kovacs
 */
@Component
public class PrettyPrintProcessor extends ProcessorBase {

    private final Logger logger = LoggerFactory.getLogger(PrettyPrintProcessor.class);

    @Autowired
    private StreamResultFactory streamResultFactory;
    @Autowired
    private StreamSourceFactory streamSourceFactory;
    @Autowired
    private XmlTransformerFactory transformerFactory;

    @Override
    public void process(final WilmaHttpEntity entity) throws ApplicationException {
        String body = entity.getBody();
        if ((body == null) || (body.length() == 0)) {
            return; //actually we have nothing to print pretty :)
        }
        String contentTypeHeader = entity.getHeader("Content-Type");
        if (contentTypeHeader != null && contentTypeHeader.contains("xml") && !contentTypeHeader.contains("image/svg+xml")) {
            try {
                Transformer transformer = transformerFactory.createTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                StreamResult result = streamResultFactory.createStreamResult();
                StreamSource source = streamSourceFactory.createStreamSourceFromString(body);
                transformer.transform(source, result);
                String xmlString = result.getWriter().toString();
                entity.setBody(xmlString);
            } catch (TransformerException e) {
                logError(entity, e);
            }
        } else if (contentTypeHeader != null && contentTypeHeader.contains("json")) {
            handleJsonContent(entity);
        }

    }

    private void handleJsonContent(final WilmaHttpEntity entity) {
        String body = entity.getBody();
        if (body != null) {
            String jsonString = tryToParseJson(entity, body);
            entity.setBody(jsonString);
        }
    }

    private String tryToParseJson(final WilmaHttpEntity entity, final String body) {
        String result = body;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            JsonParser parser = new JsonParser();
            JsonReader reader = new JsonReader(new StringReader(body));
            reader.setLenient(true);
            JsonElement element = parser.parse(reader);
            result = gson.toJson(element);
        } catch (Exception e) {
            logError(entity, e);
        }
        return result;
    }

    private void logError(final WilmaHttpEntity entity, final Exception e) {
        logger.error("Error during pretty printing of message with Wilma Logger ID: "
                + entity.getWilmaMessageLoggerId() + "! Reason:" + e.getMessage());
    }
}
