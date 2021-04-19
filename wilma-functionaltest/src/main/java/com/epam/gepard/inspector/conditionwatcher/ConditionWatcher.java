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

import java.util.Map;
import java.util.Set;

/**
 * Interface class of Condition Watchers.
 *
 * @param <T> is the object the watcher is working with.
 */
public interface ConditionWatcher<T> {

    /**
     * Register a condition watcher.
     * @param watchable is an object that requires watching something
     * @param conditionSeverity is the severity value of the specified condition
     */
    void register(Watchable<T> watchable, ConditionSeverity conditionSeverity);

    /**
     * Register a list of Condition watcher at once.
     *
     * @param watchList is the list of watchers.
     */
    void registerAll(Map<Watchable<T>, ConditionSeverity> watchList);

    /**
     * Unregister a previously registered watchable object.
     * @param watchable that should be un-registered.
     */
    void unregister(Watchable<T> watchable);

    /**
     * Unregister a list of previously registered watchable objects.
     * @param watchables is the list of watchables those should be un-registered.
     */
    void unregisterAll(Set<Watchable<T>> watchables);

    /**
     * Remove all registered watchable from the list.
     */
    void reset();

    /**
     * Evaluate all the registered conditions and decide what to do.
     */
    void watchForConditions();
}
