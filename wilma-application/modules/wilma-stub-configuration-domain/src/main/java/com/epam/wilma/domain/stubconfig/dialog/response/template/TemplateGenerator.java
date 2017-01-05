package com.epam.wilma.domain.stubconfig.dialog.response.template;
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

/**
 * Interface for external template generation.
 * @author Tamas_Bihari
 *
 */
public interface TemplateGenerator {

    /**
     * Generates a stub template, which can be use for response generation in stub module of Wilma.
     * @return with the generated template as byte array
     */
    byte[] generateTemplate();
}
