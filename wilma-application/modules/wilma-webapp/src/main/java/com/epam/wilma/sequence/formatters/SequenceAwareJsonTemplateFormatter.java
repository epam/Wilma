package com.epam.wilma.sequence.formatters;

/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateFormatter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.domain.stubconfig.sequence.WilmaSequence;
import com.epam.wilma.sequence.formatters.helper.SequenceJsonTransformer;
import com.epam.wilma.webapp.stub.response.formatter.json.JsonTemplateFormatter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.jayway.jsonpath.JsonPath;

/**
 * Generates a JSON response from the given template resource by using
 * parameters from the request. Properties in the templates with JsonPath value
 * will be replaces with the evaluated expression value (e.g.
 * {@code"$.persons[0].name"}). This formatter has access previous messages of
 * the session. Those messages can be accessed like
 * {@code"$BalanceCheckRequest.card_details.card_number"}).
 *
 * @author Balazs_Berkes
 */
@Component
public class SequenceAwareJsonTemplateFormatter implements TemplateFormatter {

    @Autowired
    private SequenceJsonTransformer sequenceJsonTransformer;

    @Override
    public byte[] formatTemplate(final WilmaHttpRequest wilmaRequest, final byte[] templateResource, final ParameterList params,
            final WilmaSequence sequence) throws Exception {
        JsonElement response = new JsonParser().parse(IOUtils.toString(templateResource, StandardCharsets.UTF_8.name()));
        new SessionAwareJsonTreeEvaluator(wilmaRequest.getBody(), sequence, params).replaceAllNonRecursive(response);

        return response.toString().getBytes();
    }

    private final class SessionAwareJsonTreeEvaluator extends JsonTemplateFormatter.NonRecursiveJsonTreeEvaluator {

        private static final String SELF_REQUEST_REFFERENCE = "request";
        private static final String SIMPLE_JSON_PATH_PREFIX = "$.";
        private static final String SESSION_JSON_PATH_PREFIX = "$";

        private final WilmaSequence sequence;
        private final ParameterList params;

        private SessionAwareJsonTreeEvaluator(final String source, final WilmaSequence sequence, final ParameterList params) {
            super(source);
            this.sequence = sequence;
            this.params = params;
        }

        @Override
        protected void replaceJsonPath(final JsonObject parent, final String key, final JsonPrimitive value) {
            if (isSimpleExpression(value.getAsString())) {
                evaluateSimpleExpression(parent, key, value);
            } else if (isSessionExpression(value.getAsString())) {
                evaluateSessionExpression(parent, key, value);
            }
        }

        private void evaluateSessionExpression(final JsonObject parent, final String key, final JsonPrimitive value) {
            String linkedSource = getReferredSource(value.getAsString());
            String jsonQuery = rebuildQuery(value.getAsString());

            String requestValue = JsonPath.read(linkedSource, jsonQuery).toString();
            parent.add(key, new JsonPrimitive(requestValue));
        }

        private void evaluateSimpleExpression(final JsonObject parent, final String key, final JsonPrimitive value) {
            String requestValue = JsonPath.read(getSource(), value.getAsString());
            parent.add(key, new JsonPrimitive(requestValue));
        }

        private String rebuildQuery(final String query) {
            String[] parts = query.split("\\.");
            StringBuilder newQuery = new StringBuilder("$");
            for (int i = 1; i < parts.length; i++) {
                newQuery.append('.').append(parts[i]);
            }
            return newQuery.toString();
        }

        private String getReferredSource(final String expression) {
            String[] parts = expression.substring(1).split("\\.");
            String link = parts[0];
            String linkedContent;
            if (SELF_REQUEST_REFFERENCE.equals(link)) {
                linkedContent = getSource();
            } else {
                Map<String, String> map = sequenceJsonTransformer.transform(params, sequence.getPairs());
                linkedContent = map.get(link);
            }
            return linkedContent;
        }

        private boolean isSessionExpression(final String value) {
            return value.startsWith(SESSION_JSON_PATH_PREFIX);
        }

        private boolean isSimpleExpression(final String value) {
            return value.startsWith(SIMPLE_JSON_PATH_PREFIX);
        }
    }

}
