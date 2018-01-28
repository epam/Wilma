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

import java.io.ByteArrayOutputStream;

import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XsltTransformer;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.common.saxon.helper.SerializerFactory;
import com.epam.wilma.common.stream.helper.ByteArrayOutputStreamFactory;

/**
 * Provides unit tests for the class {@link XslOutputProvider}.
 * @author Tunde_Kovacs
 *
 */
public class XslOutputProviderTest {

    @Mock
    private ByteArrayOutputStreamFactory byteArrayOutputStreamFactory;
    @Mock
    private SerializerFactory serializerFactory;
    @Mock
    private XsltTransformer xsltTransformer;
    @Mock
    private Serializer serializer;
    @Mock
    private ByteArrayOutputStream byteArrayOutputStream;

    @InjectMocks
    private XslOutputProvider underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOutputShouldSetDestination() {
        //GIVEN
        given(serializerFactory.createSerializer()).willReturn(serializer);
        //WHEN
        underTest.getOutput(xsltTransformer);
        //THEN
        verify(xsltTransformer).setDestination(serializer);
    }

    @Test
    public void testGetOutputShouldReturnOutput() {
        //GIVEN
        given(serializerFactory.createSerializer()).willReturn(serializer);
        given(byteArrayOutputStreamFactory.createByteArrayOutputStream()).willReturn(byteArrayOutputStream);
        //WHEN
        ByteArrayOutputStream actual = underTest.getOutput(xsltTransformer);
        //THEN
        assertEquals(actual, byteArrayOutputStream);
    }
}
