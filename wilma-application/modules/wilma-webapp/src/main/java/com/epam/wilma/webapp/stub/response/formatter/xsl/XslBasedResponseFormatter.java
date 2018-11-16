package com.epam.wilma.webapp.stub.response.formatter.xsl;
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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.FileFactory;
import com.epam.wilma.common.helper.FileUtils;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.webapp.domain.exception.ResponseFormattingFailedException;

import javax.servlet.http.HttpServletResponse;

/**
 * Creates a response based on an XSL file and template XML using the request body.
 * The XSL file name always has to be defined as a response formatter param in the
 * stub configuration!!!
 * @author Tamas_Bihari
 *
 */
@Component
public class XslBasedResponseFormatter implements ResponseFormatter {

    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private FileFactory fileFactory;
    @Autowired
    private StubResourcePathProvider stubResourcePathProvider;

    @Autowired
    private XslResponseGenerator xslResponseGenerator;

    @Override
    public byte[] formatResponse(final WilmaHttpRequest wilmaRequest, final HttpServletResponse resp,
                                 final byte[] templateResource, final ParameterList params) throws Exception {
        String xslResourcePath = checkAndGetXslResourcePath(params);
        byte[] xslResource = readXslResourceFromFileSystem(xslResourcePath);
        byte[] requestXmlResource = wilmaRequest.getBody().getBytes(StandardCharsets.UTF_8);
        return xslResponseGenerator.generateResponse(requestXmlResource, xslResource, templateResource);
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
        List<Parameter> paramList = params.getAllParameters();
        String xslResourceName = "";
        if (paramList.size() == 1) {
            xslResourceName = paramList.get(0).getValue();
        } else {
            throw new ResponseFormattingFailedException(
                    "The XslBasedResponseFormatter must have one parameter in stub configuration to define necessary xsl template file.");
        }
        return (stubResourcePathProvider.getTemplatesPathAsString() + "/" + xslResourceName).replace("\\", "/");
    }
}
