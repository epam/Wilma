package com.epam.wilma.service.unit;
/*==========================================================================
 Copyright 2013-2016 EPAM Systems

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

import com.epam.wilma.service.unit.helper.response.TemplateType;
import com.epam.wilma.service.unit.helper.common.ConfigurationParameter;
import com.epam.wilma.service.unit.helper.common.StubConfigurationException;
import com.epam.wilma.service.unit.helper.response.Template;
import com.epam.wilma.service.unit.helper.response.TemplateFormatter;
import com.epam.wilma.service.unit.request.RequestCondition;
import com.epam.wilma.service.unit.response.ResponseDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 * Builder class for building a complete Stub Configuration.
 *
 * @author Tamas_Kohegyi
 *
 */
public class ResponseDescriptorBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(ResponseDescriptorBuilder.class);

    private String groupName;
    private RequestCondition requestCondition;
    private String code = "200";
    private String delay = "0";
    private String mimeType = "text/plain";
    private Template template = new Template(TemplateType.TEXT, "Wilma default response");
    private LinkedList<TemplateFormatter> templateFormatters = new LinkedList<>();

    public ResponseDescriptorBuilder(String groupName, RequestCondition requestCondition) {
        this.groupName = groupName;
        this.requestCondition = requestCondition;
    }

    /**
     *
     * Warning! When you call it, the mime type will be set to text/plain
     * @param plainTextResponse
     * @return
     */
    public ResponseDescriptorBuilder plainTextResponse(String plainTextResponse) {
        mimeType = "text/plain";
        template = new Template(TemplateType.TEXT, plainTextResponse);
        return this;
    }

    /**
     *
     * Warning! When you call it, the mime type will be set to text/html
     * @param fileName
     * @return
     */
    public ResponseDescriptorBuilder htmlFileResponse(String fileName) {
        mimeType = "text/html";
        template = new Template(TemplateType.TEXTFILE, fileName);
        return this;
    }

    public ResponseDescriptor buildResponseDescriptor() {
        return new ResponseDescriptor(delay, code, mimeType, template, templateFormatters);
    }

    public Stub build() {
        //need to validate both the request condition, and the response descriptor
        Stub stub = new Stub(groupName, requestCondition, buildResponseDescriptor());
        LOG.debug("Stub created, XML is:\n" + stub.toString());
        return stub;
    }

    public ResponseDescriptorBuilder withStatus(int i) {
        if (i < 0) {
            throw new StubConfigurationException("Given Response StatusCode (" + i + ") is invalid.");
        }
        code = String.valueOf(i);
        return this;
    }

    public ResponseDescriptorBuilder applyFormatter(String formatterClass) {
        return applyFormatter(formatterClass, null);
    }

    public ResponseDescriptorBuilder applyFormatter(String formatterClass, ConfigurationParameter[] configurationParameters) {
        TemplateFormatter templateFormatter = new TemplateFormatter(formatterClass, configurationParameters);
        templateFormatters.add(templateFormatter);
        return this;
    }

    public ResponseDescriptorBuilder generatedResponse(String className) {
        template = new Template(TemplateType.EXTERNAL, className);
        return this;
    }

    public ResponseDescriptorBuilder withDelay(int i) {
        if (i < 0) {
            throw new StubConfigurationException("Given Response Delay (" + i + ") is invalid.");
        }
        delay = String.valueOf(i);
        return this;
    }

    public ResponseDescriptorBuilder withMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }
}
