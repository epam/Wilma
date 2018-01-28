package com.epam.wilma.common.stream.helper;
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

import javax.xml.transform.stream.StreamSource;

import org.springframework.stereotype.Component;

/**
 * Factory for creating new instances of {@link StreamSource}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class StreamSourceFactory {

    /**
     * Creates a new {@link StreamSource} from an <tt>inputStream</tt>.
     * @param inputStream the {@link InputStream} used to create a new stream source
     * @return the new instance
     */
    public StreamSource createStreamSource(final InputStream inputStream) {
        return new StreamSource(inputStream);
    }

    /**
     * Creates a new {@link StreamSource} from a string.
     * @param input the string used to create a new stream source
     * @return the mew instance
     */
    public StreamSource createStreamSourceFromString(final String input) {
        return new StreamSource(new StringReader(input));
    }
}
