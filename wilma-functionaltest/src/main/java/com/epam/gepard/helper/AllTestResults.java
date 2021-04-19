package com.epam.gepard.helper;

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
 * Simple 'parameter object' for holding the aggregate test results.
 * @author Adam_Csaba_Kiraly
 */
public class AllTestResults {
    private int runned;
    private int passed;
    private int failed;
    private int notApplicable;
    private int dummy;

    public int getRunned() {
        return runned;
    }

    public int getPassed() {
        return passed;
    }

    public int getFailed() {
        return failed;
    }

    public int getNotApplicable() {
        return notApplicable;
    }

    public int getDummy() {
        return dummy;
    }

    /**
     * Adds the given parameter to runned.
     * @param runned the given parameter
     */
    public void increaseRunned(final int runned) {
        this.runned += runned;
    }

    /**
     * Adds the given parameter to passed.
     * @param countPassed the given parameter
     */
    public void increasePassed(final int countPassed) {
        passed += countPassed;
    }

    /**
     * Adds the given parameter to failed.
     * @param countFailed the given parameter
     */
    public void increaseFailed(final int countFailed) {
        failed += countFailed;
    }

    /**
     * Adds the given parameter to notApplicable.
     * @param countNA the given parameter
     */
    public void increaseNotApplicable(final int countNA) {
        notApplicable += countNA;
    }

    /**
     * Adds the given parameter to increaseDummy.
     * @param countDummy the given parameter
     */
    public void increaseDummy(final int countDummy) {
        dummy += countDummy;
    }
}
