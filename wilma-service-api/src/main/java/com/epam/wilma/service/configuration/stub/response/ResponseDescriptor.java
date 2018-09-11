package com.epam.wilma.service.configuration.stub.response;
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

import com.epam.wilma.service.configuration.stub.helper.other.Interceptor;
import com.epam.wilma.service.configuration.stub.helper.response.Template;
import com.epam.wilma.service.configuration.stub.helper.response.ResponseFormatter;

import java.util.List;

/**
 * A Response Descriptor class.
 *
 * @author Tamas_Kohegyi
 */
public class ResponseDescriptor {
    private String delay;
    private String code;
    private String mimeType;
    private Template template;
    private List<RsponseFormatter> responseFormatters;
    private List<Interceptor> interceptors;

    /**
     * Creates a response descriptor with given parameters.
     *
     * @param delay              is the delay of the response
     * @param code               is the status code of the response
     * @param mimeType           is the mime type of the response
     * @param template           is the template that is used as response
     * @param responseFormatters are the formatters to be applied on the template before sending the response back
     * @param interceptors       are the interceptor list to be added to the stub configuration
     */
    public ResponseDescriptor(String delay, String code, String mimeType, Template template,
                              List<ResponseFormatter> responseFormatters, List<Interceptor> interceptors) {
        this.delay = delay;
        this.code = code;
        this.mimeType = mimeType;
        this.template = template;
        this.responseFormatters = responseFormatters;
        this.interceptors = interceptors;
    }

    /**
     * Generates the response descriptor part of the stub configuration.
     *
     * @return with the string representation of the response descriptor
     */
    public String responseDescriptorToString() {
        String responseDescriptor = "<response-descriptor delay=\"" + delay + "\" "
                + "code=\"" + code + "\" mimetype=\"" + mimeType + "\" template=\"" + template.getName() + "\" >\n";
        for (TemplateFormatter templateFormatter : templateFormatters) {
            responseDescriptor += templateFormatter.toString();
        }
        responseDescriptor += "</response-descriptor>\n";
        return responseDescriptor;
    }

    /**
     * Generates the interceptor part of the stub configuration.
     *
     * @return with the string representation of the interceptors, or empty string
     */
    public String interceptorsToString() {
        String interceptorDescriptor = "";
        if (interceptors != null && !interceptors.isEmpty()) {
            interceptorDescriptor += "<interceptors>\n";
            for (Interceptor interceptor : interceptors) {
                interceptorDescriptor += interceptor.toString();
            }
            interceptorDescriptor += "</interceptors>\n";
        }
        return interceptorDescriptor;
    }

    /**
     * Returns with the string representation of the used template.
     *
     * @return with the template string
     */
    public String templateToString() {
        return template.toString();
    }

}
