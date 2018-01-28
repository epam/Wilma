package com.epam.wilma.core.processor;
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

import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpEntity;

/**
 * Interface for processing wilma requests/responses and perform operations on them.
 * @author Tunde_Kovacs
 *
 */
public interface WilmaEntityProcessorInterface {

    /**
     * Performs an operation on a {@link WilmaHttpEntity}.
     * @param entity the entity on which the modification is performed
     * @throws ApplicationException if an exception occurs during modification
     */
    void process(WilmaHttpEntity entity) throws ApplicationException;

}
