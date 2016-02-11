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
 * Builder class for building a complete StubConfiguration Configuration.
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
     * Set a plain text response, the response itself is the parameter.
     * Warning! When you call it, the mime type will be set to text/plain
     * @param plainTextResponse the response itself
     * @return with itself
     */
    public ResponseDescriptorBuilder plainTextResponse(String plainTextResponse) {
        mimeType = "text/plain";
        template = new Template(TemplateType.TEXT, plainTextResponse);
        return this;
    }

    /**
     * Set a text file as response.
     * Warning! When you call it, the mime type will be set to text/plain
     * @param textFileName is the response file
     * @return with itself
     */
    public ResponseDescriptorBuilder textFileResponse(String textFileName) {
        mimeType = "text/plain";
        template = new Template(TemplateType.TEXTFILE, textFileName);
        return this;
    }

    /**
     * Set an html file as response.
     * Warning! When you call it, the mime type will be set to text/html
     * @param htmlFileName is the response file
     * @return with itself
     */
    public ResponseDescriptorBuilder htmlFileResponse(String htmlFileName) {
        mimeType = "text/html";
        template = new Template(TemplateType.HTMLFILE, htmlFileName);
        return this;
    }

    /**
     * Set a json file as response.
     * Warning! When you call it, the mime type will be set to application/json
     * @param jsonFileName is the response file
     * @return with itself
     */
    public ResponseDescriptorBuilder jsonFileResponse(String jsonFileName) {
        mimeType = "application/json";
        template = new Template(TemplateType.JSONFILE, jsonFileName);
        return this;
    }

    /**
     * Set an xml file as response.
     * Warning! When you call it, the mime type will be set to application/xml
     * @param xmlFileName is the response file
     * @return with itself
     */
    public ResponseDescriptorBuilder xmlFileResponse(String xmlFileName) {
        mimeType = "application/xml";
        template = new Template(TemplateType.XMLFILE, xmlFileName);
        return this;
    }

    public ResponseDescriptor buildResponseDescriptor() {
        return new ResponseDescriptor(delay, code, mimeType, template, templateFormatters);
    }

    public StubConfiguration build() throws StubConfigurationException {
        StubConfiguration stubConfiguration = new StubConfiguration(groupName, requestCondition, buildResponseDescriptor());
        LOG.debug("StubConfiguration created, XML is:\n" + stubConfiguration.toString());
        return stubConfiguration;
    }

    public ResponseDescriptorBuilder withStatus(int i) throws StubConfigurationException {
        if (i < 100 || i > 600) {
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

    public ResponseDescriptorBuilder withDelay(int i) throws StubConfigurationException {
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
