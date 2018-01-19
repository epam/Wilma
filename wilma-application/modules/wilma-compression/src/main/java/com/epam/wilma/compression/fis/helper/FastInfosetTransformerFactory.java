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

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

import org.springframework.stereotype.Component;

/**
 * Factory for creating a new {@link Transformer} instance.
 * @author Tunde_Kovacs
 *
 */
@Component
public class FastInfosetTransformerFactory {

    /**
     * Creates a new instance of {@link Transformer}.
     * @return the new instance
     * @throws TransformerConfigurationException thrown if the implementation
     * is not available or cannot be instantiated.
     */
    public Transformer createTransformer() throws TransformerConfigurationException {
        return TransformerFactory.newInstance().newTransformer();
    }

}
