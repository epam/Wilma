package com.epam.wilma.core;
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

import com.epam.wilma.router.domain.ResponseDescriptorDTO;

/**
 * Provides access to the response descriptor generated for each incoming request.
 * @author Tunde_Kovacs
 *
 */
public interface ResponseDescriptorAccess {

    /**
     * Retrieves a {@link ResponseDescriptorDTO} based on a unique key a request has.
     * @param key the unique identifier each request has
     * @return a response descriptor associated to a request
     */
    ResponseDescriptorDTO getResponseDescriptor(String key);
}
