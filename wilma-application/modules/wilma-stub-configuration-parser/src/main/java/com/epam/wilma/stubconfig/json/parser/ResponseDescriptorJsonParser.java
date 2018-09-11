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

import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatterDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.response.template.Template;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateGenerator;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateType;
import com.epam.wilma.domain.stubconfig.exception.DescriptorCannotBeParsedException;
import com.epam.wilma.sequence.helper.SequenceDescriptorKeyUtil;
import com.epam.wilma.stubconfig.initializer.template.TemplateFileReader;
import com.epam.wilma.stubconfig.initializer.template.TemplateGeneratorInitializer;
import com.epam.wilma.stubconfig.json.parser.helper.ObjectParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * Builds a new {@link ResponseDescriptor} from a JSON object.
 *
 * @author Tamas_Kohegyi
 */
@Component
public class ResponseDescriptorJsonParser implements ObjectParser<ResponseDescriptor> {

    @Autowired
    @Qualifier("responseFormatterDescriptorJsonParser")
    private ObjectParser<Set<ResponseFormatterDescriptor>> responseFormatterDescriptorJsonParser;
    @Autowired
    private TemplateFileReader templateFileReader;
    @Autowired
    private TemplateGeneratorInitializer templateGeneratorInitializer;
    @Autowired
    private SequenceDescriptorKeyUtil sequenceDescriptorKeyUtil;

    /**
     * Builds a new {@link ResponseDescriptor} from a JSON object.
     *
     * @param responseDescriptorObject the response descriptor object that is parsed
     * @param root                     the JSONObject that is parsed, needed to be able to find objects outside
     *                                 the <tt>responseDescriptorNode</tt>
     * @return a new {@link ResponseDescriptor}. If input parameter <tt>responseDescriptorObject</tt>
     * is null, it returns null.
     */
    @Override
    public ResponseDescriptor parseObject(final JSONObject responseDescriptorObject, final JSONObject root) {
        ResponseDescriptor responseDescriptor = null;
        if (responseDescriptorObject != null) {
            ResponseDescriptorAttributes attributes = getAttributes(responseDescriptorObject, root);
            Set<ResponseFormatterDescriptor> responseFormatters = responseFormatterDescriptorJsonParser.parseObject(responseDescriptorObject, root);
            responseDescriptor = new ResponseDescriptor(attributes, responseFormatters);
        }
        return responseDescriptor;
    }

    private ResponseDescriptorAttributes getAttributes(final JSONObject responseDescriptorObject, final JSONObject root) {
        int delay = 0;
        if (responseDescriptorObject.has("delay")) {
            delay = responseDescriptorObject.getInt("delay");
        }
        String code = "200";
        if (responseDescriptorObject.has("code")) {
            code = Integer.toString(responseDescriptorObject.getInt("code"));
        }

        String mimeType = responseDescriptorObject.getString("mimeType");
        Template template = getTemplate(responseDescriptorObject.getString("templateName"), root);
        String groupName = "Default";
        if (root.has("groupName")) {
            groupName = root.getString("groupName");
        }
        String sequenceDescriptorName = null;
        if (responseDescriptorObject.has("sequenceDescriptorName")) {
            sequenceDescriptorName = responseDescriptorObject.getString("sequenceDescriptorName");
        }
        String sequenceDescriptorKey = sequenceDescriptorKeyUtil.createDescriptorKey(groupName, sequenceDescriptorName);
        ResponseDescriptorAttributes responseDescriptorAttributes;
        responseDescriptorAttributes = new ResponseDescriptorAttributes.Builder().delay(delay).code(code)
                .mimeType(mimeType).template(template).sequenceDescriptorKey(sequenceDescriptorKey).build();
        return responseDescriptorAttributes;
    }

    private Template getTemplate(final String templateName, final JSONObject root) {
        String name;
        String type = null;
        String resourceString = null;
        boolean found = false;
        if (root.has("templates")) {
            JSONArray templateArray = root.getJSONArray("templates");
            for (int i = 0; templateArray.length() > i; i++) {
                JSONObject template = templateArray.getJSONObject(i);
                name = template.getString("name");
                if (name.contentEquals(templateName)) {
                    type = template.getString("type");
                    resourceString = template.getString("resource");
                    found = true;
                    break;
                }
            }
        } else {
            throw new DescriptorCannotBeParsedException("There is no Template defined.");
        }
        if (!found) {
            throw new DescriptorCannotBeParsedException("Cannot find template with name: '" + templateName + "'.");
        }
        TemplateType templateType = getTemplateType(type);
        byte[] resource = initializeResource(templateType, resourceString);
        return new Template(templateName, templateType, resource);
    }

    private byte[] initializeResource(final TemplateType templateType, final String resource) {
        byte[] result;
        if (templateType == TemplateType.HTML || templateType == TemplateType.TEXT || templateType == TemplateType.XML) {
            result = resource.getBytes(StandardCharsets.UTF_8);
        } else if (templateType == TemplateType.EXTERNAL) {
            result = getTemplateFromTemplateGenerator(resource);
        } else {
            result = templateFileReader.readTemplate(resource);
        }
        return result;
    }

    /**
     * Loads external template formatter class.
     *
     * @param resource is the template generator class resource name
     * @return with the template, generated by the template generator class
     */
    private byte[] getTemplateFromTemplateGenerator(final String resource) {
        byte[] result;
        TemplateGenerator templateGenerator = templateGeneratorInitializer.getTemplateGenerator(resource);
        try {
            result = templateGenerator.generateTemplate();
        } catch (Exception e) {
            throw new DescriptorCannotBeParsedException("Descriptor can not be validated, because template generation failed at: "
                    + templateGenerator.getClass().getName() + ": " + e.getMessage(), e);
        }
        return result;
    }

    private TemplateType getTemplateType(final String type) {
        return TemplateType.valueOf(type.toUpperCase());
    }
}
