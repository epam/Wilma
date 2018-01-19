package com.epam.wilma.stubconfig.dom.validator.xsd.helper;
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

import javax.xml.transform.dom.DOMSource;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

/**
 * Creates a new {@link DOMSource} from a {@link Document}.
 * @author Marton_Sereg
 *
 */
@Component
public class DOMSourceFactory {

    /**
     * Creates a new {@link DOMSource} from a {@link Document}.
     * @param document the base of the {@link DOMSource}
     * @return the new {@link DOMSource}
     */
    public DOMSource newDOMSource(final Document document) {
        return new DOMSource(document);
    }

}
