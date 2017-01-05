package com.epam.wilma.browsermob.transformer.helper;
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

import com.epam.wilma.domain.http.WilmaHttpResponse;
import org.springframework.stereotype.Component;

/**
 * Factory for creating new instances of {@link WilmaHttpResponse}.
 *
 * @author Tunde_Kovacs
 */
@Component
public class WilmaResponseFactory {

    /**
     * Creates a new instance of {@link WilmaHttpResponse}.
     *
     * @param isVolatile determines if the response header/body can be updated/altered or not.
     * @return the new instance
     */
    public WilmaHttpResponse createNewWilmaHttpResponse(final boolean isVolatile) {
        return new WilmaHttpResponse(isVolatile);
    }
}
