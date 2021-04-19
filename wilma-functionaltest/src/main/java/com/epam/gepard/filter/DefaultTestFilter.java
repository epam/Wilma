package com.epam.gepard.filter;

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

import com.epam.gepard.filter.matcher.SimpleMatcher;
import com.gargoylesoftware.base.testing.AcceptAllTestFilter;

/**
 * This class is used to do filtering on the available tests.
 */
public class DefaultTestFilter extends AcceptAllTestFilter implements ExpressionTestFilter {
    private static final String JOKER = "?";
    private static final String DELIMITER = ",";
    private String classFilter;

    /**
     * This constructor sets the filter to accept all classes.
     */
    public DefaultTestFilter() {
        super();
        this.classFilter = JOKER;
    }

    /**
     * This constructor sets the filter.
     *
     * @param classFilter filter expression
     */
    public DefaultTestFilter(final String classFilter) {
        super();
        this.classFilter = classFilter;
    }

    /**
     * Sets the filter expression to the specified String.
     *
     * @param filterExpr Filter expression
     */
    @Override
    public void setFilterExpression(final String filterExpr) {
        this.classFilter = filterExpr;
    }

    /**
     * Returns true is the filter expression matches the given expression.
     *
     * @param filter Filter expression
     * @param expr   Full name or testID of the class to check
     */
    private boolean isMatchingSimple(final String filter, final String expr) {
        SimpleMatcher simpleMatcher = new SimpleMatcher(JOKER);
        return simpleMatcher.match(filter, expr);
    }

    /**
     * Returns true is the filter expression matches the given expression.
     *
     * @param filter One or more filter expressions separated by commas
     * @param expr   Full name or testID of the class to check
     */
    private boolean isMatching(final String filter, final String expr) {
        boolean result = false;
        String[] parts = filter.split(DELIMITER);
        int i = 0;
        while (i < parts.length && !result) {
            String part = parts[i];
            if (isMatchingSimple(part, expr)) {
                result = true;
            }
            i++;
        }
        return result;
    }

    /**
     * Called by the test runner to determine if a given class is to be tested.
     *
     * @param clazz The class to check
     * @return True, if the class should be included in the tests, false if not
     */
    @Override
    public boolean accept(final Class clazz) {
        boolean needToInclude = false;
        // classname checking
        String className = clazz.getName();
        if (isMatching(this.classFilter, className)) {
            needToInclude = true;
        }
        // does not match
        return needToInclude;
    }
}
