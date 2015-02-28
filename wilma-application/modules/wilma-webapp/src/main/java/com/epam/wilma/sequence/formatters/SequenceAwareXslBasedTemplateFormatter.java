package com.epam.wilma.sequence.formatters;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.FileFactory;
import com.epam.wilma.common.helper.FileUtils;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateFormatter;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.domain.stubconfig.sequence.RequestResponsePair;
import com.epam.wilma.domain.stubconfig.sequence.WilmaSequence;
import com.epam.wilma.sequence.formatters.helper.SequenceXmlTransformer;
import com.epam.wilma.sequence.formatters.xsl.SequenceAwareXslResponseGenerator;
import com.epam.wilma.webapp.domain.exception.TemplateFormattingFailedException;

/**
 * XSL based version of {@link SequenceAwareXslBasedTemplateFormatter}.
 * The xsl file should be passed with key {@link SequenceAwareXslBasedTemplateFormatter#XSL_FILE_PARAM_KEY}.
 * @author Balazs_Berkes
 */
@Component
public class SequenceAwareXslBasedTemplateFormatter implements TemplateFormatter {

    @Autowired
    private FileFactory fileFactory;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private StubResourcePathProvider stubResourcePathProvider;
    @Autowired
    private SequenceAwareXslResponseGenerator xslResponseGenerator;
    @Autowired
    private SequenceXmlTransformer sequenceXmlTransformer;

    @Override
    public byte[] formatTemplate(final WilmaHttpRequest wilmaRequest, final byte[] templateResource, final ParameterList params,
            final WilmaSequence sequence) throws Exception {
        Map<String, RequestResponsePair> message = sequence.getPairs();

        Map<String, String> nameToXml = sequenceXmlTransformer.transform(params, message);

        String xslResourcePath = checkAndGetXslResourcePath(params);
        byte[] xslResource = readXslResourceFromFileSystem(xslResourcePath);
        byte[] requestXmlResource = wilmaRequest.getBody().getBytes(StandardCharsets.UTF_8);
        return xslResponseGenerator.generateResponse(requestXmlResource, xslResource, templateResource, nameToXml);
    }

    private byte[] readXslResourceFromFileSystem(final String xslResourcePath) {
        File xslFile = fileFactory.createFile(xslResourcePath);
        byte[] result = null;
        try {
            result = fileUtils.getFileAsByteArray(xslFile);
        } catch (IOException e) {
            throw new TemplateFormattingFailedException(
                    "The previously defined xslFile='" + xslResourcePath + "' can not be read or does not exist.", e);
        }
        return result;
    }

    private String checkAndGetXslResourcePath(final ParameterList params) {
        List<Parameter> paramList = params.getAllParameters();
        String xslResourceName = "";
        if (!paramList.isEmpty()) {
            xslResourceName = paramList.get(0).getValue();
        } else {
            throw new TemplateFormattingFailedException(
                    "The XslBasedTemplateFormatter must have one parameter in stub configuration to define necessary xsl template file.");
        }
        return (stubResourcePathProvider.getTemplatesPathAsString() + "/" + xslResourceName).replace("\\", "/");
    }
}
