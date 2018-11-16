package com.epam.wilma.stubconfig.dom.parser.node;
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
import com.epam.wilma.sequence.helper.SequenceDescriptorKeyUtil;
import com.epam.wilma.stubconfig.dom.parser.NodeParser;
import com.epam.wilma.stubconfig.dom.parser.node.helper.StubConfigXPathEvaluator;
import com.epam.wilma.domain.stubconfig.exception.DescriptorCannotBeParsedException;
import com.epam.wilma.stubconfig.initializer.template.TemplateFileReader;
import com.epam.wilma.stubconfig.initializer.template.TemplateGeneratorInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * Builds a new {@link ResponseDescriptor} from a DOM node.
 *
 * @author Tunde_Kovacs
 */
@Component
public class ResponseDescriptorParser implements NodeParser<ResponseDescriptor> {

    @Autowired
    private StubConfigXPathEvaluator xPathEvaluator;
    @Autowired
    @Qualifier("templateDescriptorParser")
    private NodeParser<Set<ResponseFormatterDescriptor>> templateDescriptorParser;
    @Autowired
    private TemplateFileReader templateFileReader;
    @Autowired
    private TemplateGeneratorInitializer templateGeneratorInitializer;
    @Autowired
    private SequenceDescriptorKeyUtil sequenceDescriptorKeyUtil;

    /**
     * Builds a new {@link ResponseDescriptor} from a DOM node.
     *
     * @param responseDescriptorNode the response descriptor node that is parsed
     * @param document               the DOM document that is parsed, needed to be able to find nodes outside
     *                               the <tt>responseDescriptorNode</tt>
     * @return a new {@link ResponseDescriptor}. If input parameter <tt>responseDescriptorNode</tt>
     * is null, it returns null.
     */
    @Override
    public ResponseDescriptor parseNode(final Node responseDescriptorNode, final Document document) {
        ResponseDescriptor responseDescriptor = null;
        if (responseDescriptorNode != null) {
            Element el = (Element) responseDescriptorNode;
            ResponseDescriptorAttributes attributes = getAttributes(el, document);
            Set<ResponseFormatterDescriptor> responseFormatters = templateDescriptorParser.parseNode(el, document);
            responseDescriptor = new ResponseDescriptor(attributes, responseFormatters);
        }
        return responseDescriptor;
    }

    private ResponseDescriptorAttributes getAttributes(final Element el, final Document document) {
        int delay = Integer.valueOf(el.getAttribute("delay"));
        String code = el.getAttribute("code");
        String mimeType = el.getAttribute("mimetype");
        Template template = getTemplate(el.getAttribute("template"), document);
        String groupname = document.getDocumentElement().getAttribute("groupname");
        String sequenceDescriptorName = el.getAttribute("sequenceDescriptorName");
        String sequenceDescriptorKey = sequenceDescriptorKeyUtil.createDescriptorKey(groupname, sequenceDescriptorName);
        ResponseDescriptorAttributes responseDescriptorAttributes = new ResponseDescriptorAttributes.Builder().delay(delay).code(code)
                .mimeType(mimeType).template(template).sequenceDescriptorKey(sequenceDescriptorKey).build();
        return responseDescriptorAttributes;
    }

    private Template getTemplate(final String templateName, final Document document) {
        Element element = xPathEvaluator.getElementByXPath(
                "/wilma:wilma-stub/wilma:template-descriptor/wilma:template[@name='" + templateName + "']", document);
        TemplateType templateType = getTemplateType(element.getAttribute("type"));
        String resourceName = element.getAttribute("resource");
        byte[] resource = initializeResource(templateType, resourceName);
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
