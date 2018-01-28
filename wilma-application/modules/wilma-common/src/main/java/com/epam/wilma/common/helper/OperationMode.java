package com.epam.wilma.common.helper;
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
 * Represents a switch between different operation modes of the application.
 * <ul>
 * <li>STUB mode: route engine evaluates the request normally, and works normally,
 * but in case the message should be handled in proxy mode, wilma must respond with E404 (text/html/xml) message</li>
 * <li>PROXY mode: in this case route engine shall not evaluate anything, always should be routed to proxy path</li>
 * <li>WILMA mode: normal mode, default route engine evaluates the request, and based on the config, either the stub or proxy mode is used</li>
 * </ul>
 *
 * @author Tunde_Kovacs
 */
public enum OperationMode {
    STUB,
    PROXY,
    WILMA;
}
