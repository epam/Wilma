package com.epam.wilma.message.search.web.support;
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

import org.springframework.stereotype.Component;

/**
 * This class provides version title for Message-search.
 * @author Tibor_Kovacs
 *
 */
@Component
public class VersionTitleProvider {

    private static final String NOT_FOUND = "unknown (no manifest file)";

    /**
     * This method gets the actual version title from MANIFEST file.
     * @return with version title
     */
    public String getVersionTitle() {
        String versionTitle = getClass().getPackage().getImplementationTitle();
        return versionTitle != null ? versionTitle : NOT_FOUND;
    }
}
