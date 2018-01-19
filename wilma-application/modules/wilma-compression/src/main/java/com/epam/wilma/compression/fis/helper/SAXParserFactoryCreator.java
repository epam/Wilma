package com.epam.wilma.compression.fis.helper;
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

import org.springframework.stereotype.Component;

import javax.xml.parsers.SAXParserFactory;

/**
 * Factory for creating a new instance of {@link SAXParserFactory}.
 *
 * @author Tunde_Kovacs
 */
@Component
public class SAXParserFactoryCreator {

    /**
     * Creates a new instance of {@link SAXParserFactory}.
     *
     * @return the new instance
     */
    public SAXParserFactory createSAXParserFactory() {
        return SAXParserFactory.newInstance();
    }
}
