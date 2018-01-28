package com.epam.wilma.webapp.config.servlet.helper;
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

import org.springframework.stereotype.Component;

import com.epam.wilma.webapp.configuration.domain.MaintainerProperties;
import com.google.gson.Gson;

/**
 * Builds a Json response from a MaintainerProperties object.
 * @author Marton_Sereg
 *
 */
@Component
public class MaintainerPropertiesJsonBuilder {

    /**
     * Builds a Json response from a MaintainerProperties object.
     * @param maintainerProperties the object from which the Json is built.
     * @return the object as a Json
     */
    public String buildMaintainerPropertiesJson(final MaintainerProperties maintainerProperties) {
        Gson gson = new Gson();
        return gson.toJson(maintainerProperties);
    }

}
