package com.epam.wilma.message.search.lucene.helper;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import org.apache.lucene.index.Term;
import org.springframework.stereotype.Component;

/**
 * Factory for creating instances of {@link Term}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class TermFactory {

    /**
     * Constructs a Term with the given field and text.
     * Note that a null field or null text value results
     * in undefined behavior for most Lucene APIs that accept a Term parameter.
     * @param fieldName field name of the term
     * @param text text of the term
     * @return the new instance
     */
    public Term createTerm(final String fieldName, final String text) {
        return new Term(fieldName, text);
    }
}
