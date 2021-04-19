package com.epam.gepard.common.threads;

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
 * This class is used to store information about the blocker string.
 */
public class BlockingInfo {
    private boolean blockerInUse; // = false;         //whether the blocker is in use or not
    private String actualClass; // = null; //stores the class name who actually use the block
    private int overload; // = 0;          //stores information on how many classes are using this block

    boolean isBlockerInUse() {
        return blockerInUse;
    }

    void setBlockerInUse(final boolean b) {
        this.blockerInUse = b;
    }

    String getActualClass() {
        return actualClass;
    }

    void setActualClass(final String actualClass) {
        this.actualClass = actualClass;
    }

    int getOverload() {
        return overload;
    }

    void setOverload(final int overload) {
        this.overload = overload;
    }
}
