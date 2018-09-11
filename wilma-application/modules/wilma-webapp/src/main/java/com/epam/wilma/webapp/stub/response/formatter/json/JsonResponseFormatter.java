package com.epam.wilma.webapp.stub.response.formatter.json;
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

import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatter;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.jayway.jsonpath.JsonPath;

import javax.servlet.http.HttpServletResponse;

/**
 * Generates a JSON response from the given template resource by using
 * parameters from the request. Properties in the templates with JsonPath value
 * will be replaces with the evaluated expression value (e.g.
 * {@code"$.persons[0].name"}).
 *
 * @author Balazs_Berkes
 */
@Component
public class JsonResponseFormatter implements ResponseFormatter {

    @Override
    public byte[] formatResponse(final WilmaHttpRequest wilmaRequest, final HttpServletResponse resp,
                                 final byte[] templateResource, final ParameterList params) {
        JsonElement response = new JsonParser().parse(new String(templateResource, StandardCharsets.UTF_8));

        new NonRecursiveJsonTreeEvaluator(wilmaRequest.getBody()).replaceAllNonRecursive(response);

        return response.toString().getBytes();
    }

    /**
     * Iterate through all JSON tree element.
     */
    public static class NonRecursiveJsonTreeEvaluator {

        private static final String JSON_PATH_PREFIX = "$";

        private final String source;
        private final Deque<JsonElement> stack;

        /**
         * Creates an evaluator which will iterate through all JSON tree element an perform custom steps.
         * @param source String representation of JSON object.
         */
        public NonRecursiveJsonTreeEvaluator(final String source) {
            this.source = source;
            this.stack = new ArrayDeque<>();
        }

        /**
         * Calls replaceJsonPath() method for this node and all its children.
         * @param node root node
         */
        public void replaceAllNonRecursive(final JsonElement node) {
            stack.push(node);

            while (!stack.isEmpty()) {
                JsonElement current = stack.pop();

                if (current.isJsonArray()) {
                    pushAllElements(current.getAsJsonArray());
                } else if (current.isJsonObject()) {
                    exploreObject(current.getAsJsonObject());
                }
            }
        }

        /**
         * Replaces the current node value if needed.
         * @param parent parent node
         * @param key name of the current node
         * @param value the current node
         */
        protected void replaceJsonPath(final JsonObject parent, final String key, final JsonPrimitive value) {
            if (isExpression(value.getAsString())) {
                String requestValue = JsonPath.read(source, value.getAsString());
                parent.add(key, new JsonPrimitive(requestValue));
            }
        }

        protected String getSource() {
            return source;
        }

        protected Deque<JsonElement> getStack() {
            return stack;
        }

        private void exploreObject(final JsonObject object) {
            for (Map.Entry<String, JsonElement> property : object.entrySet()) {
                if (property.getValue().isJsonPrimitive()) {
                    replaceJsonPath(object, property.getKey(), property.getValue().getAsJsonPrimitive());
                } else {
                    stack.push(property.getValue());
                }
            }
        }

        private void pushAllElements(final JsonArray array) {
            for (JsonElement element : array) {
                stack.push(element);
            }
        }

        private boolean isExpression(final String value) {
            return value.startsWith(JSON_PATH_PREFIX);
        }
    }
}
