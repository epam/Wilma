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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import java.io.InputStream;

import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.saxon.helper.SaxonCompilerErrorListener;
import com.epam.wilma.common.stream.helper.StreamSourceFactory;

/**
 * Provides unit tests for the class {@link XslCompiler}.
 * @author Tunde_Kovacs
 *
 */
public class XslCompilerTest {

    @Mock
    private InputStream inputStream;
    @Mock
    private Processor processor;
    @Mock
    private XsltExecutable xsltExecutable;
    @Mock
    private StreamSource source;
    @Mock
    private XsltCompiler xsltCompiler;
    @Mock
    private StreamSourceFactory streamSourceFactory;
    @Mock
    private SaxonCompilerErrorListener errorListener;

    @InjectMocks
    private XslCompiler underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCompileXslShouldCompileSource() throws SaxonApiException {
        //GIVEN
        given(processor.newXsltCompiler()).willReturn(xsltCompiler);
        given(streamSourceFactory.createStreamSource(inputStream)).willReturn(source);
        //WHEN
        underTest.compileXsl(inputStream, processor);
        //THEN
        verify(xsltCompiler).compile(source);
    }

    @Test
    public void testCompileXslShouldReturnXsltExecutable() throws SaxonApiException {
        //GIVEN
        given(processor.newXsltCompiler()).willReturn(xsltCompiler);
        given(streamSourceFactory.createStreamSource(inputStream)).willReturn(source);
        given(xsltCompiler.compile(source)).willReturn(xsltExecutable);
        //WHEN
        XsltExecutable actual = underTest.compileXsl(inputStream, processor);
        //THEN
        assertEquals(actual, xsltExecutable);
    }
}
