package com.epam.wilma.message.search.jms;
/*==========================================================================
Copyright 2013-2016 EPAM Systems

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
 * Determines whether a message should be added to an index, or should be deleted from it.
 * @author Tunde_Kovacs
 *
 */
public enum IndexingType {

    ADD("add"),
    DELETE("delete");

    private String name;

    private IndexingType(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
