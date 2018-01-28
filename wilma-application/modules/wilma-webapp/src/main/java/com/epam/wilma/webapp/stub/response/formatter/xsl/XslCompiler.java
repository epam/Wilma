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

import java.io.InputStream;

import javax.xml.transform.Source;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.saxon.helper.SaxonCompilerErrorListener;
import com.epam.wilma.common.stream.helper.StreamSourceFactory;

/**
 * Compiles an xsl file.
 * @author Tunde_Kovacs
 *
 */
@Component
public class XslCompiler {

    @Autowired
    private StreamSourceFactory streamSourceFactory;
    @Autowired
    private SaxonCompilerErrorListener errorListener;

    /**
     * Compiles an xsl file.
     * @param inputStream the xsl file as {@link InputStream}
     * @param processor the {@link Processor} that provides the compiler
     * @return an {@link XsltExecutable} that will be used further in the xsl transformation process
     * @throws SaxonApiException it is thrown if an exception occurs during the compilation
     */
    public XsltExecutable compileXsl(final InputStream inputStream, final Processor processor) throws SaxonApiException {
        XsltCompiler xsltCompiler = processor.newXsltCompiler();
        Source xslSource = streamSourceFactory.createStreamSource(inputStream);
        xsltCompiler.setErrorListener(errorListener);
        XsltExecutable xsltExecutable = xsltCompiler.compile(xslSource);
        return xsltExecutable;
    }

}
