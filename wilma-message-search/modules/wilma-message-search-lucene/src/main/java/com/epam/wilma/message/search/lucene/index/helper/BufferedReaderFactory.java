package com.epam.wilma.message.search.lucene.index.helper;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Component;

/**
 * Factory for creating new instances of {@link BufferedReader}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class BufferedReaderFactory {

    /**
     * Creates a new instance of {@link BufferedReader}.
     * @param fis the {@link FileInputStream} used to create the reader
     * @return the new instance
     * @throws UnsupportedEncodingException  If the named charset is not supported
     */
    public BufferedReader createReader(final FileInputStream fis) throws UnsupportedEncodingException {
        return new BufferedReader(new InputStreamReader(fis, "UTF-8"));
    }
}
