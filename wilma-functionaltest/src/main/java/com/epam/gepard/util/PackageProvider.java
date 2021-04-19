package com.epam.gepard.util;
/*==========================================================================
 Copyright 2004-2015 EPAM Systems

 This file is part of Gepard.

 Gepard is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Gepard is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Gepard.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

/**
 * Class used for getting the {@link Package} of an object.
 * Created by Tamas_Kohegyi on 2015-02-18.
 */
public class PackageProvider {

    /**
     * Returns the {@link Package} of the given object.
     * @param object the given object
     * @return the package of the given object
     */
    public Package getPackageOfObject(final Object object) {
        return object.getClass().getPackage();
    }

}
