package com.epam.wilma.domain.stubconfig.parameter;
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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for storing stub configuration parameter elements in a list.
 * @author Tamas_Bihari
 *
 */
public class ParameterList {
    private final List<Parameter> parameters;

    /**
     * Constructs a {@link ParameterList} and instantiates it's fields.
     */
    public ParameterList() {
        parameters = new LinkedList<>();
    }

    /**
     * Add a {@link Parameter} to the list.
     * @param param is the {@link Parameter}
     */
    public void addParameter(final Parameter param) {
        parameters.add(param);
    }

    /**
     * Appends all of the elements of a ParameterList to the list.
     * @param list is the list of {@link Parameter}s that will be added
     */
    public void addAll(final ParameterList list) {
        parameters.addAll(list.getAllParameters());
    }

    /**
     * Return with all {@link Parameter}.
     * @return with an unmodifiable {@link List} of {@link Parameter}s.
     */
    public List<Parameter> getAllParameters() {
        return Collections.unmodifiableList(parameters);
    }

    /**
     * Gets the parameter value of the given key.
     * @param key the key of the parameter.
     * @return the value of the parameter if presented otherwise {@code null}.
     */
    public String get(String key) {
        String value = null;
        for (Parameter p : parameters) {
            if (p.getName().equals(key)) {
                value = p.getValue();
            }
        }
        return value;
    }

    public boolean isEmpty() {
        return parameters.isEmpty();
    }

    @Override
    public String toString() {
        return "ParameterList [parameters=" + parameters + "]";
    }

}
