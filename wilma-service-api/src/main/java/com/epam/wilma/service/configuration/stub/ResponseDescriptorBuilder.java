package com.epam.wilma.service.configuration.stub;
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

import com.epam.wilma.service.configuration.stub.helper.common.ConfigurationParameter;
import com.epam.wilma.service.configuration.stub.helper.common.StubConfigurationException;
import com.epam.wilma.service.configuration.stub.helper.response.Template;
import com.epam.wilma.service.configuration.stub.helper.response.TemplateFormatter;
import com.epam.wilma.service.configuration.stub.helper.response.TemplateType;
import com.epam.wilma.service.configuration.stub.request.RequestCondition;
import com.epam.wilma.service.configuration.stub.response.ResponseDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Builder class for building a complete WilmaStub Configuration.
 *
 * @author Tamas_Kohegyi
 */
public class ResponseDescriptorBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(ResponseDescriptorBuilder.class);
    private static final int STATUS_CODE_MAX = 600;
    private static final int STATUS_CODE_MIN = 100;

    private String groupName;
    private RequestCondition requestCondition;
    private String code = "200";
    private String delay = "0";
    private String mimeType = "text/plain";
    private Template template = new Template(TemplateType.TEXT, "Wilma default response");
    private List<TemplateFormatter> templateFormatters = new LinkedList<>();

    /**
     * Constructor of Response Descriptor builder.
     *
     * @param groupName        is the stub configuration group name
     * @param requestCondition is the Request descriptor/condition part of the configuration
     */
    public ResponseDescriptorBuilder(String groupName, RequestCondition requestCondition) {
        this.groupName = groupName;
        this.requestCondition = requestCondition;
    }

    /**
     * Set a plain text response, the response itself is the parameter.
     * Warning! When you call it, the mime type will be set to text/plain
     *
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
     *
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
     *
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
     *
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
     *
     * @param xmlFileName is the response file
     * @return with itself
     */
    public ResponseDescriptorBuilder xmlFileResponse(String xmlFileName) {
        mimeType = "application/xml";
        template = new Template(TemplateType.XMLFILE, xmlFileName);
        return this;
    }

    /**
     * Build method that finally builds the Response Descriptor object part of the stub configuration.
     *
     * @return with the built Response Descriptor object
     */
    public ResponseDescriptor buildResponseDescriptor() {
        return new ResponseDescriptor(delay, code, mimeType, template, templateFormatters);
    }

    /**
     * Build method of the Stub Configuration.
     * The group name, the request and the response descriptors are the main inputs.
     *
     * @return with the new WilmaStub object.
     * @throws StubConfigurationException then the stub configuration is not valid
     */
    public WilmaStub build() throws StubConfigurationException {
        WilmaStub wilmaStub = new WilmaStub(groupName, requestCondition, buildResponseDescriptor());
        LOG.debug("WilmaStub created, XML is:\n" + wilmaStub.toString());
        return wilmaStub;
    }

    /**
     * Sets the response status code. Must be between 100 and 600, otherwise, will throw exception.
     * By default the response status code is 200.
     *
     * @param i is the expected status code
     * @return with itself
     * @throws StubConfigurationException if the given status code is not acceptable
     */
    public ResponseDescriptorBuilder withStatus(int i) throws StubConfigurationException {
        if (i < STATUS_CODE_MIN || i > STATUS_CODE_MAX) {
            throw new StubConfigurationException("Given Response StatusCode (" + i + ") is invalid.");
        }
        code = String.valueOf(i);
        return this;
    }

    /**
     * Calls a template formatter class without any parameter.
     *
     * @param formatterClass is the template formatter class
     * @return with itself
     */
    public ResponseDescriptorBuilder applyFormatter(String formatterClass) {
        return applyFormatter(formatterClass, null);
    }

    /**
     * Calls a template formatter class with parameters.
     *
     * @param formatterClass          is the template formatter class
     * @param configurationParameters is the parameters of the formatter class
     * @return with itself
     */
    public ResponseDescriptorBuilder applyFormatter(String formatterClass, ConfigurationParameter[] configurationParameters) {
        TemplateFormatter templateFormatter = new TemplateFormatter(formatterClass, configurationParameters);
        templateFormatters.add(templateFormatter);
        return this;
    }

    /**
     * Generates the response, by using a response generator class.
     *
     * @param className is the name of the class that generates the response
     * @return with itself
     */
    public ResponseDescriptorBuilder generatedResponse(String className) {
        template = new Template(TemplateType.EXTERNAL, className);
        return this;
    }

    /**
     * Sets the delay of the stub response.
     * By default the delay is 0 (no delay).
     *
     * @param i is the used delay in milliseconds
     * @return with itself
     * @throws StubConfigurationException in case of negative value
     */
    public ResponseDescriptorBuilder withDelay(int i) throws StubConfigurationException {
        if (i < 0) {
            throw new StubConfigurationException("Given Response Delay (" + i + ") is invalid.");
        }
        delay = String.valueOf(i);
        return this;
    }

    /**
     * Sets the mime type of the response.
     * Deafult mime type is "text/plain"
     * Beware that *Response() methods sets the mime type accordingly,
     * so call this method only, if you would like to overwrite the default value.
     *
     * @param mimeType that should be used in the response
     * @return with itself
     */
    public ResponseDescriptorBuilder withMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }
}
