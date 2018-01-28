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

    private final Map<String, ClassPathXmlApplicationContext> applicationContexts = new HashMap<String, ClassPathXmlApplicationContext>();

    ApplicationContextFactory() {
    }

    public static ApplicationContextFactory getInstance() {
        return ApplicationContextFactoryHolder.APPLICATION_CONTEXT_FACTORY_INSTANCE;
    }

    /**
     * Creates a new ClassPathXmlApplicationContext.
     * @param path of the appContext.xml
     * @return the created ClassPathXmlApplicationContext
     */
    public ClassPathXmlApplicationContext getClassPathXmlApplicationContext(final String path) {
        ClassPathXmlApplicationContext applicationContext = applicationContexts.get(path);
        if (applicationContext == null) {
            applicationContext = new ClassPathXmlApplicationContext(path);
            applicationContexts.put(path, applicationContext);
        }
        return applicationContext;
    }

    /**
     * Creates a new instance of {@link ClassPathXmlApplicationContext}.
     * @param springAppContextPath the path of the application context xml
     * @return the new instance
     */
    public ClassPathXmlApplicationContext getApplicationContext(final String springAppContextPath) {
        return getClassPathXmlApplicationContext(springAppContextPath);
    }

    private static class ApplicationContextFactoryHolder {
        public static final ApplicationContextFactory APPLICATION_CONTEXT_FACTORY_INSTANCE = new ApplicationContextFactory();
    }
}
