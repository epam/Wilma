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

import com.gargoylesoftware.base.testing.TestFilter;

/**
 * Interface for expression-based test filters. Custom filter classes for Gepard has to implement this,
 * and extend com.gargoylesoftware.base.testing.AcceptAllTestFilter.
 *
 * @author Tamas Godan
 */
public interface ExpressionTestFilter extends TestFilter {
    /**
     * Sets the filter expression in the filter class to the specified string. This allows
     * creation of filter classes with an empty constructor and adding the filter expression later.
     *
     * @param filterExpr Filter expression
     * @throws Exception in case of any error, like filter expression is invalid
     */
    void setFilterExpression(String filterExpr) throws Exception;
}

