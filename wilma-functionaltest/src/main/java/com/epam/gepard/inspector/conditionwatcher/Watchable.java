package com.epam.gepard.inspector.conditionwatcher;
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
 * This interface provides info/warning/error events that can be watched for.
 * @param <T> is the object that is watchable.
 */
public interface Watchable<T> {

    /**
     * This method is called to check whether the condition is true during waiting.
     *
     * @param t is the watched object
     * @return boolean true if the condition is true
     */
    boolean checkCondition(T t);

    /**
     * Returns the message for logging purposes.
     *
     * @return The message.
     */
    String getMessage();
}
