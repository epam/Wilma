package com.epam.wilma.common.helper;
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

import java.net.URL;

import org.springframework.stereotype.Component;

/**
 * Loads a resource with the {@link ClassLoader}.
 * @author Marton_Sereg
 *
 */
@Component
public class ResourceLoader {

    /**
     * Loads a resource URL with the {@link ClassLoader}.
     * @param name the name of the resource
     * @return the resource URL
     */
    public URL loadResource(final String name) {
        return this.getClass().getClassLoader().getResource(name);
    }

}
