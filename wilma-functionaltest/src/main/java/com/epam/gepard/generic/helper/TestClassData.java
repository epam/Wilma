package com.epam.gepard.generic.helper;

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
 * Simple 'parameter object' class holding data for a test class.
 * @author Adam_Csaba_Kiraly
 */
public class TestClassData {

    private final Class<?> classOfTestClass;
    private final int count;
    private final String blocker;

    /**
     * Constructs a new instance of {@link TestClassData}.
     * @param classOfTestClass          is the TC class to be added
     * @param count        is the data multiplier - how many times this class should be added
     * @param blocker      is the class blocker-string (for parallel execution)
     */
    public TestClassData(final Class<?> classOfTestClass, final int count, final String blocker) {
        this.classOfTestClass = classOfTestClass;
        this.count = count;
        this.blocker = blocker;
    }

    public Class<?> getClassOfTestClass() {
        return classOfTestClass;
    }

    public int getCount() {
        return count;
    }

    public String getBlocker() {
        return blocker;
    }
}
