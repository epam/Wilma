package com.epam.wilma.browsermob.transformer.helper;
/*==========================================================================
Copyright 2013-2016 EPAM Systems

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

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.exception.ApplicationException;

/**
 * Provides conversion from input stream to string. Also keeps the
 * input stream in its initial state, so it can be reread.
 * @author Tamas_Bihari
 * @author Tunde_Kovacs
 */
@Component
public class InputStreamConverter {

    private static final int BUFFER_SIZE = 1024;

    /**
     * Converts an inputStream to a String with Apache Commons' IOUtils.
     * @param inputStream InputStream to convert
     * @return converted stream
     * @throws ApplicationException when IOUtils or mark/reset fails
     */
    public String getStringFromStream(final InputStream inputStream) throws ApplicationException {
        String result = "";
        if (inputStream != null) {
            try {
                int extendedReadLimit = inputStream.available() + BUFFER_SIZE;
                inputStream.mark(extendedReadLimit);
                result = IOUtils.toString(inputStream);
                inputStream.reset();
            } catch (IOException e) {
                throw new ApplicationException("Could not transform request input stream into string!", e);
            }
        }
        return result;
    }
}
