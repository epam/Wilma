package com.epam.wilma.sequence.formatters.xsl;
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

import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.webapp.domain.exception.ResponseFormattingFailedException;
import com.epam.wilma.webapp.stub.servlet.helper.ByteArrayInputStreamFactory;

/**
 * Generates an output xml from an input xml based on a given XSLT.
 *
 * @author Balazs_Berkes
 */
@Component
public class SequenceAwareXslResponseGenerator {

    @Autowired
    private SequenceAwareXslTransformer transformer;
    @Autowired
    private ByteArrayInputStreamFactory inputStreamFactory;

    /**
     * Generates an output xml based on an xls transformation.
     * @param requestXmlResource the input xml that will be transformed
     * @param xsl the xsl file that will be used to format the response template
     * @param template the response template that will be formatted
     * @param nameToXml nameToXml the names identifying a request or response and their bodies
     * @throws ResponseFormattingFailedException it is thrown if an exceptional condition occures during the transformation process
     * @return the output xml
     */
    public byte[] generateResponse(final byte[] requestXmlResource, final byte[] xsl, final byte[] template, final Map<String, String> nameToXml) {
        byte[] result;
        InputStream requestInputStream = inputStreamFactory.createByteArrayInputStream(requestXmlResource);
        InputStream xslInputStream = inputStreamFactory.createByteArrayInputStream(xsl);
        InputStream templateInputStream = inputStreamFactory.createByteArrayInputStream(template);
        try {
            result = transformer.transform(xslInputStream, requestInputStream, templateInputStream, nameToXml);
        } catch (Exception e) {
            throw new ResponseFormattingFailedException("Template formatting failed with xslt:" + xsl, e);
        }
        return result;
    }

}
