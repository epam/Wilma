package com.epam.wilma.stubconfig;
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

import com.epam.wilma.domain.stubconfig.StubDescriptor;

import java.io.InputStream;

/**
 * Access point to the stub-configuration module. This interface is used to build up the object model of a {@link StubDescriptor}
 * based on an {@link InputStream}.
 *
 * @author Tamas_Kohegyi
 */
public interface StubDescriptorJsonFactory {

    /**
     * Loads the stub descriptor from an {@link InputStream}.
     *
     * @param inputStream the stream that contains the stub descriptor.
     * @return the newly built {@link StubDescriptor}
     */
    StubDescriptor buildStubDescriptor(InputStream inputStream);

}
