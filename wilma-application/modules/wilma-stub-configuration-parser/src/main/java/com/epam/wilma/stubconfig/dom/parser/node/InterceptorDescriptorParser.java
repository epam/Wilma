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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.epam.wilma.domain.stubconfig.interceptor.InterceptorDescriptor;
import com.epam.wilma.domain.stubconfig.interceptor.RequestInterceptor;
import com.epam.wilma.domain.stubconfig.interceptor.ResponseInterceptor;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.stubconfig.dom.parser.NodeParser;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;
import com.epam.wilma.domain.stubconfig.exception.InterfaceValidationFailedException;
import com.epam.wilma.stubconfig.initializer.interceptor.RequestInterceptorInitializer;
import com.epam.wilma.stubconfig.initializer.interceptor.ResponseInterceptorInitializer;

/**
 * Builds a new {@link InterceptorDescriptor} from a DOM node.
 * @author Tunde_Kovacs, Tamas_Kohegyi
 *
 */
@Component
public class InterceptorDescriptorParser implements NodeParser<InterceptorDescriptor> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InterceptorDescriptorParser.class);

    @Autowired
    private RequestInterceptorInitializer requestInterceptorInitializer;
    @Autowired
    private ResponseInterceptorInitializer responseInterceptorInitializer;

    @Override
    public InterceptorDescriptor parseNode(final Node interceptorNode, final Document document) {
        InterceptorDescriptor interceptorDescriptor = null;
        if (interceptorNode != null) {
            Element el = (Element) interceptorNode;
            String name = el.getAttribute("name");
            String clazz = el.getAttribute("class");
            //check for dummy problem of not giving class attribute, or giving 'class' to the end of the class name
            if (clazz == null) {
                throw new DescriptorValidationFailedException("Validation of stub descriptor failed - Class name is missing.");
            }
            if (clazz.toLowerCase().endsWith(".class")) {
                throw new DescriptorValidationFailedException("Validation of stub descriptor failed - Class name '" + clazz
                        + "' should not contain 'class' at its end.");
            }
            ParameterList params = parseInterceptorParameters(el.getElementsByTagName("param"));
            RequestInterceptor requestInterceptor = null;
            ResponseInterceptor responseInterceptor = null;
            try {
                requestInterceptor = requestInterceptorInitializer.getExternalClassObject(clazz);
            } catch (InterfaceValidationFailedException e) {
                // don't worry
                LOGGER.debug("No Request interceptor in class:" + clazz, e);
            }
            try {
                responseInterceptor = responseInterceptorInitializer.getExternalClassObject(clazz);
            } catch (InterfaceValidationFailedException e) {
                //don't worry
                LOGGER.debug("No Response interceptor in class:" + clazz, e);
            }
            if (requestInterceptor == null && responseInterceptor == null) {
                throw new DescriptorValidationFailedException("Validation of stub descriptor failed - Class '" + clazz
                        + "' does not implement any of the necessary interfaces.");
            }
            interceptorDescriptor = new InterceptorDescriptor(name, requestInterceptor, responseInterceptor, params);
        }
        return interceptorDescriptor;
    }

    private ParameterList parseInterceptorParameters(final NodeList params) {
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
}
