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

import java.io.InputStream;
import java.io.StringReader;

import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

/**
 * Factory for creating new instances of {@link InputSource}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class InputSourceFactory {

    /**
     * Creates a new {@link InputSource} from an <tt>inputStream</tt>.
     * @param inputStream the {@link InputStream} used to create a new input source
     * @return the new instance
     */
    public InputSource createInputSource(final InputStream inputStream) {
        return new InputSource(inputStream);
    }

    /**
     * Creates a new {@link InputSource} from an <tt>input</tt>.
     * @param input the string input used to create a new input source
     * @return the new instance
     */
    public InputSource createInputSource(final String input) {
        return new InputSource(new StringReader(input));
    }

}
