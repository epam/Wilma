package com.epam.wilma.message.search.domain;
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

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Component;

/**
 * Holds the status of the index files.
 *
 * @author Adam_Csaba_Kiraly
 */
@Component
public class IndexStatus {

    private final AtomicBoolean ready = new AtomicBoolean();

    /**
     * Sets the state representing the Lucene index.
     * @param ready true if the indexing is complete, false if it doesn't exist or it's in progress
     */
    public void setReady(final boolean ready) {
        this.ready.set(ready);
    }

    public boolean isReady() {
        return ready.get();
    }
}
