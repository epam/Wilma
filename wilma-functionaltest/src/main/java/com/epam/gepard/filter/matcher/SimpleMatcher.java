package com.epam.gepard.filter.matcher;

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
 * Simple matcher class.
 * @author unknown
 */
public class SimpleMatcher {
    private int pos; // = 0; // stores found joker position
    private int pos2; // = 0; // stores found joker position
    private int from1; // current index in filter string
    private int from2; // current index in className string
    private boolean wasJoker; // true if the last character was a joker in the previous loop
    private boolean result;
    private boolean resultFound;
    private final String joker;

    /**
     * Constructs a new instance of SimpleMatcher.
     * @param joker the joker character
     */
    public SimpleMatcher(final String joker) {
        this.joker = joker;
    }

    /**
     * Returns true if the filter expression matches the given expression.
     *
     * @param filter Filter expression
     * @param expr   Full name or testID of the class to check
     * @return true if the filter expression matches the given expression.
     */
    public boolean match(final String filter, final String expr) {
        String inFilter = filter.trim(); // trim unnecessary spaces
        while ((from1 < inFilter.length()) && (from2 < expr.length() && !resultFound)) {
            pos = filter.indexOf(joker, from1);
            if (noStar(pos)) {
                determineNoStarResult(expr, inFilter);
            } else {
                handleStar(expr, inFilter);
            }
        }
        if (!resultFound) {
            result = from1 == filter.length() && from2 == expr.length() || from1 == filter.length() && wasJoker;
        }
        return result;
    }

    private void handleStar(final String expr, final String inFilter) {
        if (wasJoker) {
            String firstk = checkForJokerResult(expr, inFilter);
            from1 = pos + 1;
            from2 = pos2 + firstk.length();
        } else {
            String firstk = checkForResult(expr, inFilter);
            from1 = pos + 1;
            from2 += firstk.length();
        }
        wasJoker = true;
    }

    private String checkForJokerResult(final String expr, final String inFilter) {
        String firstk = inFilter.substring(from1, pos);
        pos2 = expr.indexOf(firstk, from2);
        if (noStar(pos2)) {
            result = false;
            resultFound = true;
        }
        return firstk;
    }

    private String checkForResult(final String expr, final String inFilter) {
        String firstk = inFilter.substring(from1, pos);
        if (!expr.substring(from2).startsWith(firstk)) {
            result = false;
            resultFound = true;
        }
        return firstk;
    }

    private void determineNoStarResult(final String expr, final String inFilter) {
        if (wasJoker) {
            result = expr.substring(from2).endsWith(inFilter.substring(from1));
        } else {
            result = inFilter.substring(from1).equals(expr.substring(from2));
        }
        resultFound = true;
    }

    private boolean noStar(final int pos) {
        return pos == -1;
    }
}
