package com.epam.wilma.stubconfig.json.parser;
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

import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatter;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatterDescriptor;
import com.epam.wilma.domain.stubconfig.exception.DescriptorCannotBeParsedException;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.stubconfig.configuration.StubConfigurationAccess;
import com.epam.wilma.stubconfig.configuration.domain.PropertyDto;
import com.epam.wilma.stubconfig.initializer.template.ResponseFormatterInitializer;
import com.epam.wilma.stubconfig.json.parser.helper.ObjectParser;
import com.epam.wilma.stubconfig.json.parser.helper.ParameterListParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Builds a set of {@link ResponseFormatter}s from a JSON object.
 *
 * @author Tamas_Kohegyi
 */
@Component
public class ResponseFormatterDescriptorJsonParser implements ObjectParser<Set<ResponseFormatterDescriptor>> {

    private static final String RESPONSE_FORMATTER_SET_INVOKER_TAG = "responseFormatterSetInvoker";
    private static final String RESPONSE_FORMATTER_TAG = "responseFormatter";
    private Integer maxDepthOfJsonTree;

    @Autowired
    private ResponseFormatterInitializer formatterInitializer;
    @Autowired
    private StubConfigurationAccess configurationAccess;
    @Autowired
    private ParameterListParser parameterListParser;

    @Override
    public Set<ResponseFormatterDescriptor> parseObject(final JSONObject responseDescriptorObject, final JSONObject root) {
        //This number represents the depth of the subtree
        int depth = 0;
        return parse(responseDescriptorObject, root, depth);
    }

    private Set<ResponseFormatterDescriptor> parse(final JSONObject responseDescriptorObject, final JSONObject root, final int depth) {
        Set<ResponseFormatterDescriptor> responseFormatterDescriptorSet = new LinkedHashSet<>();
        if (responseDescriptorObject.has("responseFormatterSet")) {
            JSONArray responseFormatterSet = responseDescriptorObject.getJSONArray("responseFormatterSet");
            for (int i = 0; i < responseFormatterSet.length(); i++) {
                JSONObject formatter = responseFormatterSet.getJSONObject(i);
                if (formatter.has(RESPONSE_FORMATTER_TAG)) {
                    //response formatter
                    responseFormatterDescriptorSet.add(parseResponseFormatter(formatter.getJSONObject(RESPONSE_FORMATTER_TAG), root));
                } else {
                    if (formatter.has(RESPONSE_FORMATTER_SET_INVOKER_TAG)) {
                        //response formatter set invoker
                        String name = formatter.getString(RESPONSE_FORMATTER_SET_INVOKER_TAG);
                        int newDepth = validateDepth(depth, name);
                        responseFormatterDescriptorSet.addAll(parseResponseFormatterSetInvoker(name, root, newDepth));
                    }
                }
            }
        }
        return responseFormatterDescriptorSet;
    }

    private Set<ResponseFormatterDescriptor> parseResponseFormatterSetInvoker(final String responseFormatterSetName, final JSONObject root, final int depth) {
        //String expression = "/wilma:wilma-stub/wilma:template-descriptor/wilma:template-formatter-set[@name='" + templateFormatterSetName + "']";
        //Element templateFormatterSet = xPathEvaluator.getElementByXPath(expression, document);
        //return parse(templateFormatterSet, document, depth);
        JSONObject formatterSet = null;
        boolean found = false;
        if (root.has("responseFormatterSets")) {
            JSONArray responseFormatterSetArray = root.getJSONArray("responseFormatterSets");
            for (int i = 0; responseFormatterSetArray.length() > i; i++) {
                JSONObject responseFormatterSet = responseFormatterSetArray.getJSONObject(i);
                String name = responseFormatterSet.getString("name");
                if (name.contentEquals(responseFormatterSetName)) {
                    formatterSet = responseFormatterSet.getJSONObject("responseFormatterSet");
                    found = true;
                    break;
                }
            }
        } else {
            throw new DescriptorCannotBeParsedException("There is no Response Formatter Set defined.");
        }
        if (!found) {
            throw new DescriptorCannotBeParsedException("Cannot find Response Formatter Set with name: '" + responseFormatterSetName + "'.");
        }
        return parse(formatterSet, root, depth);
    }

    private ResponseFormatterDescriptor parseResponseFormatter(final JSONObject responseFormatterObject, final JSONObject root) {
        String clazz = responseFormatterObject.getString("class");
        ParameterList params = parameterListParser.parseObject(responseFormatterObject, root);
        ResponseFormatter responseFormatter = formatterInitializer.getExternalClassObject(clazz);
        return new ResponseFormatterDescriptor(responseFormatter, params);
    }

    private int validateDepth(final int depth, final String invokerName) {
        int newDepth = depth + 1;
        getMaxDepth();
        if (newDepth >= maxDepthOfJsonTree) {
            throw new DescriptorValidationFailedException(
                    "Validation of stub descriptor failed: Response-descriptor subtree is too deep or contains circles, error occurs at: responseFormatterSetInvoker = '"
                            + invokerName + "' ...>");
        }
        return newDepth;
    }

    private void getMaxDepth() {
        if (maxDepthOfJsonTree == null) {
            PropertyDto properties = configurationAccess.getProperties();
            maxDepthOfJsonTree = properties.getMaxDepthOfTree();
        }
    }
}
