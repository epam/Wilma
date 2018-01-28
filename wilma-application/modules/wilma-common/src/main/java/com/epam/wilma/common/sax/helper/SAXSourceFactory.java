package com.epam.wilma.common.sax.helper;
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

import javax.xml.transform.sax.SAXSource;

import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * Factory for creating new instances of {@link SAXSource}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class SAXSourceFactory {

    /**
     * Creates a new instance of {@link SAXSource}.
     * @param xmlReader the {@link XMLReader} used to read the <tt>inputSource</tt>
     * @param inputSource the {@link InputSource} the will be converted to a sax source
     * @return the new instance
     */
    public SAXSource createSAXSource(final XMLReader xmlReader, final InputSource inputSource) {
        return new SAXSource(xmlReader, inputSource);
    }

    /**
     * Creates a new instance of {@link SAXSource}.
     * @param inputSource the {@link InputSource} the will be converted to a sax source
     * @return the new instance
     */
    public SAXSource createSAXSource(final InputSource inputSource) {
        return new SAXSource(inputSource);
    }

}
