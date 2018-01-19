package com.epam.wilma.common.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

/**
 * Class used for getting the version title from the manifest file.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class VersionTitleProvider {

    private static final String NOT_FOUND = "unknown (no manifest file)";
    @Autowired
    private PackageProvider packageProvider;

    /**
     * Returns the version title.
     * @return the version title if the manifest is found
     */
    public String getVersionTitle() {
        String versionTitle = packageProvider.getPackageOfObject(this).getImplementationTitle();
        return versionTitle != null ? versionTitle : NOT_FOUND;
    }
}
