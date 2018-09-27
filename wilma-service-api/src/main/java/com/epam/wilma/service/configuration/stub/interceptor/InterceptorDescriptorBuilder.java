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

import com.epam.wilma.service.configuration.stub.ResponseDescriptorBuilder;
import com.epam.wilma.service.configuration.stub.WilmaStub;
import com.epam.wilma.service.configuration.stub.helper.common.ConfigurationParameterArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Builder class for building a complete WilmaStub Configuration.
 *
 * @author Tamas_Kohegyi
 */
public class InterceptorDescriptorBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(InterceptorDescriptorBuilder.class);

    private String groupName;
    private List<Interceptor> interceptors = new LinkedList<>();
    private ResponseDescriptorBuilder responseDescriptorBuilder;


    public InterceptorDescriptorBuilder(String groupName) {
        this.groupName = groupName;
    }
    /**
     * Add an interceptor to the stub configuration.
     *
     * @param interceptorName  name of the interceptor
     * @param interceptorClass the class name of the interceptor
     * @return with itself
     */
    public InterceptorDescriptorBuilder addInterceptor(String interceptorName, String interceptorClass) {
        return addInterceptor(interceptorName, interceptorClass, null);
    }

    /**
     * Add an interceptor that has parameters, to the stub configuration.
     *
     * @param interceptorName     name of the interceptor
     * @param interceptorClass    the class name of the interceptor
     * @param configurationParameterArray are the parameters of the interceptor
     * @return with itself
     */
    public InterceptorDescriptorBuilder addInterceptor(String interceptorName, String interceptorClass, ConfigurationParameterArray configurationParameterArray) {
        interceptors.add(new Interceptor(interceptorName, interceptorClass, configurationParameterArray));
        return this;
    }

    public WilmaStub build() {
        InterceptorDescriptor interceptorDescriptor = new InterceptorDescriptor(interceptors);
        WilmaStub wilmaStub;
        if (responseDescriptorBuilder != null) {
            wilmaStub = responseDescriptorBuilder.build(interceptorDescriptor);
        } else {
            wilmaStub = new WilmaStub(groupName, null, null, interceptorDescriptor);
            LOG.debug("WilmaStub created, JSON is:\n" + wilmaStub.toString());
        }
        return wilmaStub;
    }

    /**
     * Add an interceptor to the stub configuration.
     *
     * @param interceptorName  name of the interceptor
     * @param interceptorClass the class name of the interceptor
     * @return with itself
     */
    public InterceptorDescriptorBuilder addInterceptor(ResponseDescriptorBuilder responseDescriptorBuilder, String interceptorName, String interceptorClass) {
        this.responseDescriptorBuilder = responseDescriptorBuilder;
        return addInterceptor(interceptorName, interceptorClass);
    }

    /**
     * Add an interceptor that has parameters, to the stub configuration.
     *
     * @param interceptorName     name of the interceptor
     * @param interceptorClass    the class name of the interceptor
     * @param configurationParameterArray are the parameters of the interceptor
     * @return with itself
     */
    public InterceptorDescriptorBuilder addInterceptor(ResponseDescriptorBuilder responseDescriptorBuilder, String interceptorName, String interceptorClass, ConfigurationParameterArray configurationParameterArray) {
        this.responseDescriptorBuilder = responseDescriptorBuilder;
        interceptors.add(new Interceptor(interceptorName, interceptorClass, configurationParameterArray));
        return this;
    }

}
