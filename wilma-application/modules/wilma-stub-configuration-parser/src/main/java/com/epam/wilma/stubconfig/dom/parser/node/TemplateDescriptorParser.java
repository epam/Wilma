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

import java.util.LinkedHashSet;
import java.util.Set;

import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatter;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatterDescriptor;
import com.epam.wilma.stubconfig.initializer.template.ResponseFormatterInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.stubconfig.configuration.StubConfigurationAccess;
import com.epam.wilma.stubconfig.configuration.domain.PropertyDto;
import com.epam.wilma.stubconfig.dom.parser.NodeParser;
import com.epam.wilma.stubconfig.dom.parser.node.helper.StubConfigXPathEvaluator;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;

/**
 * Builds a set of {@link ResponseFormatter}s from a DOM node.
 * @author Tunde_Kovacs
 *
 */
@Component
public class TemplateDescriptorParser implements NodeParser<Set<ResponseFormatterDescriptor>> {

    private static final String TEMPLATE_FORMATTER_SET_INVOKER_TAG = "template-formatter-set-invoker";
    private static final String TEMPLATE_FORMATTER_TAG = "template-formatter";
    private Integer maxDepthOfXmlTree;

    @Autowired
    private StubConfigXPathEvaluator xPathEvaluator;
    @Autowired
    private ResponseFormatterInitializer formatterInitializer;
    @Autowired
    private StubConfigurationAccess configurationAccess;

    @Override
    public Set<ResponseFormatterDescriptor> parseNode(final Node node, final Document document) {
        //This number represents the depth of the subtree
        int depth = 0;
        return parse(node, document, depth);
    }

    private Set<ResponseFormatterDescriptor> parse(final Node node, final Document document, final int depth) {
        Set<ResponseFormatterDescriptor> templateFormatterSet = new LinkedHashSet<>();
        if (node.getChildNodes() != null) {
            NodeList templateFormatters = node.getChildNodes();
            for (int i = 0; i < templateFormatters.getLength(); i++) {
                if (templateFormatters.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) templateFormatters.item(i);
                    if (TEMPLATE_FORMATTER_TAG.equals(el.getTagName())) {
                        templateFormatterSet.add(parseTemplateFormatter(el));
                    } else if (TEMPLATE_FORMATTER_SET_INVOKER_TAG.equals(el.getTagName())) {
                        String invokerName = el.getAttribute("name");
                        int newDepth = validateDepth(depth, invokerName);
                        templateFormatterSet.addAll(parseTemplateFormatterSet(invokerName, document, newDepth));
                    }
                }
            }
        }
        return templateFormatterSet;
    }

    private Set<ResponseFormatterDescriptor> parseTemplateFormatterSet(final String templateFormatterSetName, final Document document, final int depth) {
        String expression = "/wilma:wilma-stub/wilma:template-descriptor/wilma:template-formatter-set[@name='" + templateFormatterSetName + "']";
        Element templateFormatterSet = xPathEvaluator.getElementByXPath(expression, document);
        return parse(templateFormatterSet, document, depth);
    }

    private ResponseFormatterDescriptor parseTemplateFormatter(final Element element) {
        String clazz = element.getAttribute("class");
        ParameterList params = parseTemplateFormatterParameters(element.getElementsByTagName("param"));
        ResponseFormatter templateFormatter = formatterInitializer.getExternalClassObject(clazz);
        return new ResponseFormatterDescriptor(templateFormatter, params);
    }

    private ParameterList parseTemplateFormatterParameters(final NodeList params) {
        ParameterList parameterList = new ParameterList();
        if (params != null) {
            for (int i = 0; i < params.getLength(); i++) {
                Element el = (Element) params.item(i);
                String name = el.getAttribute("name");
                String value = el.getAttribute("value");
                parameterList.addParameter(new Parameter(name, value));
            }
        }
        return parameterList;
    }

    private int validateDepth(final int depth, final String invokerName) {
        int newDepth = depth + 1;
        getMaxDepth();
        if (newDepth >= maxDepthOfXmlTree) {
            throw new DescriptorValidationFailedException(
                    "Validation of stub descriptor failed: Response-descriptor subtree is too deep or contains circles, error occurs at: <template-formatter-set-invoker name='"
                            + invokerName + "' ...>");
        }
        return newDepth;
    }

    private void getMaxDepth() {
        if (maxDepthOfXmlTree == null) {
            PropertyDto properties = configurationAccess.getProperties();
            maxDepthOfXmlTree = properties.getMaxDepthOfTree();
        }
    }
}
