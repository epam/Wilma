package com.epam.wilma.sequence.formatters;
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

import com.epam.wilma.common.helper.FileFactory;
import com.epam.wilma.common.helper.FileUtils;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.sequence.RequestResponsePair;
import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.sequence.formatters.helper.SequenceXmlTransformer;
import com.epam.wilma.sequence.formatters.xsl.SequenceAwareXslResponseGenerator;
import com.epam.wilma.webapp.domain.exception.ResponseFormattingFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * XSL based version of {@link SequenceAwareXslBasedResponseFormatter}.
 * The xsl file should be passed with key {@link com.epam.wilma.sequence.formatters.xsl.SequenceAwareXslTransformer#REQUEST_PARAMETER_NAME}.
 *
 * @author Balazs_Berkes
 */
@Component
public class SequenceAwareXslBasedResponseFormatter implements ResponseFormatter {

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
    public byte[] formatResponse(final WilmaHttpRequest wilmaRequest, final HttpServletResponse resp,
                                 final byte[] templateResource, final ParameterList params) {
        Map<String, RequestResponsePair> message = wilmaRequest.getSequence().getPairs();

        Map<String, String> nameToXml = sequenceXmlTransformer.transform(params, message);

        String xslResourcePath = checkAndGetXslResourcePath(params);
        byte[] xslResource = readXslResourceFromFileSystem(xslResourcePath);
        byte[] requestXmlResource = wilmaRequest.getBody().getBytes(StandardCharsets.UTF_8);
        return xslResponseGenerator.generateResponse(requestXmlResource, xslResource, templateResource, nameToXml);
    }

    private byte[] readXslResourceFromFileSystem(final String xslResourcePath) {
        File xslFile = fileFactory.createFile(xslResourcePath);
        byte[] result;
        try {
            result = fileUtils.getFileAsByteArray(xslFile);
        } catch (IOException e) {
            throw new ResponseFormattingFailedException(
                    "The previously defined xslFile='" + xslResourcePath + "' can not be read or does not exist.", e);
        }
        return result;
    }

    private String checkAndGetXslResourcePath(final ParameterList params) {
        String xslResourceName;
        if (params == null || params.get("xslFile") == null) {
            throw new ResponseFormattingFailedException(
                    "The SequenceAwareXslBasedResponseFormatter must have 'xslFile' parameter in stub configuration to define necessary xsl template file.");
        } else {
            xslResourceName = params.get("xslFile");
        }
        return (stubResourcePathProvider.getTemplatesPathAsString() + "/" + xslResourceName).replace("\\", "/");
    }
}
