package com.epam.wilma.engine.bootstrap.helper;
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

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Wrapper class, that hides the creation of the ClassPathXmlApplicationContext.
 * @author Marton_Sereg
 *
 */
public class ApplicationContextFactory {

    private final Map<String, ClassPathXmlApplicationContext> applicationContexts = new HashMap<>();

    ApplicationContextFactory() {
    }

    public static ApplicationContextFactory getInstance() {
        return ApplicationContextFactoryHolder.APPLICATION_CONTEXT_FACTORY_INSTANCE;
    }

    /**
     * Creates a new {@link ClassPathXmlApplicationContext}.
     * @param path of the application context xml
     * @return the created ClassPathXmlApplicationContext
     */
    public ClassPathXmlApplicationContext getClassPathXmlApplicationContext(final String path) {
        applicationContexts.computeIfAbsent(path, k -> new ClassPathXmlApplicationContext(path));
        return applicationContexts.get(path);
    }

    private static class ApplicationContextFactoryHolder {
        public static final ApplicationContextFactory APPLICATION_CONTEXT_FACTORY_INSTANCE = new ApplicationContextFactory();
    }
}
