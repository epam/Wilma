package com.epam.wilma.service.configuration.stub.interceptor;
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

import java.util.List;

/**
 * An Interceptor Descriptor class.
 *
 * @author Tamas_Kohegyi
 */
public class InterceptorDescriptor {
    private List<Interceptor> interceptors;

    /**
     * Creates an interceptor descriptor with given parameters.
     *
     * @param interceptors       are the interceptor list to be added to the stub configuration
     */
    public InterceptorDescriptor(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }

    /**
     * Generates the interceptor part of the stub configuration.
     *
     * @return with the string representation of the interceptors, or empty string
     */
    @Override
    public String toString() {
        String interceptorDescriptor = "";
        if (interceptors != null && !interceptors.isEmpty()) {
            interceptorDescriptor += ",\n  \"interceptors\": [\n";
            int i = 0;
            for (Interceptor interceptor : interceptors) {
                if (i > 0) {
                    interceptorDescriptor += ",\n";
                }
                interceptorDescriptor += interceptor.toString();
                i++;
            }
            interceptorDescriptor += "\n]";
        }
        return interceptorDescriptor;
    }

}
