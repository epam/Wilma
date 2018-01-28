package com.epam.wilma.common.saxon.helper;
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

import net.sf.saxon.Configuration;
import net.sf.saxon.s9api.Processor;

import org.springframework.stereotype.Component;

/**
 * Factory for creating new instances of {@link Processor}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class ProcessorFactory {

    /**
     * Creates a new {@link Processor} with a default {@link Configuration}.
     * @return the new instance
     */
    public Processor createProcessor() {
        return new Processor(new Configuration());
    }
}
