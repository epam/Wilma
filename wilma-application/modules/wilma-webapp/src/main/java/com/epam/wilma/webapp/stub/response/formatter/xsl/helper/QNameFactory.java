package com.epam.wilma.webapp.stub.response.formatter.xsl.helper;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

import net.sf.saxon.s9api.QName;

import org.springframework.stereotype.Component;

/**
 * Factory for creating new instances of {@link QName}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class QNameFactory {

    /**
     * Creates a new {@link QName} with a given parameter.
     * @param parameter used to create a new qname
     * @return the new instance
     */
    public QName createQName(final String parameter) {
        return new QName(parameter);
    }
}
