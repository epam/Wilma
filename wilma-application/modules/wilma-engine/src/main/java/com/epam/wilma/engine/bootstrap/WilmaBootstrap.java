package com.epam.wilma.engine.bootstrap;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.epam.wilma.domain.exception.SystemException;
import com.epam.wilma.engine.bootstrap.helper.ApplicationContextFactory;
import com.epam.wilma.engine.bootstrap.helper.SystemExceptionSelector;
import com.epam.wilma.engine.bootstrap.helper.WilmaServiceListener;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * Bootstrap class, that initializes the Spring context, and starts the application engine.
 * @author Marton_Sereg
 *
 */
public class WilmaBootstrap {

    private static final String SPRING_APP_CONTEXT_PATH = "conf/spring/engine-application-context.xml";
    private final Logger logger = LoggerFactory.getLogger(WilmaBootstrap.class);
    private final ApplicationContextFactory applicationContextFactory;
    private final SystemExceptionSelector systemExceptionSelector;

    /**
     * ApplicationContextFactory is injected here.
     * @param applicationContextFactory creates the Spring application context.
     */
    public WilmaBootstrap(final ApplicationContextFactory applicationContextFactory) {
        this.applicationContextFactory = applicationContextFactory;
        systemExceptionSelector = new SystemExceptionSelector();
    }

    /**
     * Initializes Spring context, and starts the application engine. Closes application context if exception is catched.
     * Loads properties from configuration file and sets them into the application context.
     *
     */
    public void bootstrap() {
        ClassPathXmlApplicationContext applicationContext = null;
        try {
            applicationContext = getApplicationContext();
            WilmaEngine wilmaEngine = applicationContext.getBean(WilmaEngine.class);
            WilmaServiceListener wilmaServiceListener = applicationContext.getBean(WilmaServiceListener.class);
            wilmaEngine.addListener(wilmaServiceListener, MoreExecutors.directExecutor());
            wilmaEngine.start();
        } catch (Exception e) {
            logErrorByTypeOfException(e);
            closeApplicationContext(applicationContext);
        }
    }

    ClassPathXmlApplicationContext getApplicationContext() {
        return applicationContextFactory.getClassPathXmlApplicationContext(SPRING_APP_CONTEXT_PATH);
    }

    private void closeApplicationContext(final ClassPathXmlApplicationContext applicationContext) {
        if (applicationContext != null) {
            applicationContext.close();
        }
    }

    private void logErrorByTypeOfException(final Exception e) {
        SystemException ex = systemExceptionSelector.getSystemException(e);
        if (ex != null) {
            logError(ex.getMessage(), ex);
        } else {
            logError("", e);
        }
    }

    private void logError(final String message, final Exception e) {
        logger.error("Wilma cannot be started. " + message, e);
    }
}
