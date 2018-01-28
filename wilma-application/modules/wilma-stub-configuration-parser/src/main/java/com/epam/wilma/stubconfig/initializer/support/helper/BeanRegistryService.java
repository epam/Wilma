package com.epam.wilma.stubconfig.initializer.support.helper;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Class used to register and get beans from the application context.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class BeanRegistryService {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private ConfigurableListableBeanFactory beanRegistry;

    /**
     * Registers an object as a singleton into the spring context, with the given name.
     * Note: only single registration is allowed, duplicated re-registration should be avoided.
     * Pls note that after the first registration it might be actively in use by Wilma.
     *
     * @param name the given name
     * @param object the object to register
     * @param <T> the type of the object
     */
    public <T> void register(final String name, final T object) {
        context.getAutowireCapableBeanFactory().autowireBean(object);
        beanRegistry.registerSingleton(name, object);
    }

    /**
     * Returns a bean with the given name and type, throws exception if not found.
     * @param name the given name
     * @param requiredType the type of the bean to search for
     * @param <T> the type of the returned object
     * @return the bean
     * @see ApplicationContext#getBean(String, Class)
     */
    public <T> T getBean(final String name, final Class<T> requiredType) {
        return context.getBean(name, requiredType);
    }
}
