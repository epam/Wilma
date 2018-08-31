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

import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;
import com.epam.wilma.domain.stubconfig.exception.InterfaceValidationFailedException;
import com.epam.wilma.domain.stubconfig.interceptor.InterceptorDescriptor;
import com.epam.wilma.domain.stubconfig.interceptor.RequestInterceptor;
import com.epam.wilma.domain.stubconfig.interceptor.ResponseInterceptor;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.stubconfig.initializer.interceptor.RequestInterceptorInitializer;
import com.epam.wilma.stubconfig.initializer.interceptor.ResponseInterceptorInitializer;
import com.epam.wilma.stubconfig.json.parser.helper.ObjectParser;
import com.epam.wilma.stubconfig.json.parser.helper.ParameterListParser;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Builds a new {@link InterceptorDescriptor} from a JSON object.
 *
 * @author Tamas_Kohegyi
 */
@Component
public class InterceptorDescriptorJsonParser implements ObjectParser<InterceptorDescriptor> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InterceptorDescriptorJsonParser.class);

    @Autowired
    private RequestInterceptorInitializer requestInterceptorInitializer;
    @Autowired
    private ResponseInterceptorInitializer responseInterceptorInitializer;
    @Autowired
    private ParameterListParser parameterListParser;

    @Override
    public InterceptorDescriptor parseObject(final JSONObject interceptorObject, final JSONObject root) {
        InterceptorDescriptor interceptorDescriptor;
        String name = interceptorObject.getString("name");
        String clazz = interceptorObject.getString("class");
        //check for dummy problem of not giving class attribute, or giving 'class' to the end of the class name
        if (clazz == null) {
            throw new DescriptorValidationFailedException("Validation of stub descriptor failed - Class name is missing.");
        }
        if (clazz.toLowerCase().endsWith(".class")) {
            throw new DescriptorValidationFailedException("Validation of stub descriptor failed - Class name '" + clazz
                    + "' should not contain 'class' at its end.");
        }
        ParameterList params = parameterListParser.parseObject(interceptorObject, root);
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

        return interceptorDescriptor;
    }

}
