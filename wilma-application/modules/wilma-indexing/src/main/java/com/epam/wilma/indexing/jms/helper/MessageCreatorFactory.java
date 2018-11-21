package com.epam.wilma.indexing.jms.helper;
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

import com.epam.wilma.indexing.domain.IndexMessage;
import org.springframework.stereotype.Component;

/**
 * Creates a new {@link JmsIndexMessageCreator} based on an {@link IndexMessage}.
 *
 * @author Tamas_Bihari
 */
@Component
public class MessageCreatorFactory {

    /**
     * Creates a new {@link JmsIndexMessageCreator} based on an {@link IndexMessage}.
     *
     * @param message the base of the {@link JmsIndexMessageCreator}
     * @return the created object
     */
    public JmsIndexMessageCreator createJmsMessageCreator(final IndexMessage message) {
        return new JmsIndexMessageCreator(message);
    }
}
