package com.epam.wilma.message.search.engine.bootstrap;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Properties;

import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanCreationException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.message.search.domain.exception.SystemException;
import com.epam.wilma.message.search.engine.bootstrap.helper.SystemExceptionSelector;
import com.epam.wilma.message.search.engine.properties.PropertyLoader;
import com.epam.wilma.message.search.web.WebAppServer;

/**
 * Unit tests for the class {@link MessageSearchBootstrap}.
 * @author Tunde_Kovacs
 *
 */
public class MessageSearchBootstrapTest {

    private static final String[] ARGS = {"message.search.conf.properties"};
    private Properties properties;
    @Mock
    private SystemExceptionSelector systemExceptionSelector;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private BeanCreationException beanCreationException;
    @Mock
    private Logger logger;
    @Mock
    private WebAppServer webAppServer;
    @Mock
    private SystemException systemException;
    @Mock
    private PropertyLoader propertyLoader;

    @InjectMocks
    private MessageSearchBootstrap underTest;

    @BeforeMethod
    public void setUp() {
        underTest = Mockito.spy(new MessageSearchBootstrap());
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "systemExceptionSelector", systemExceptionSelector);
        Whitebox.setInternalState(underTest, "propertyLoader", propertyLoader);
        Whitebox.setInternalState(underTest, "logger", logger);
        properties = new Properties();
        given(propertyLoader.loadProperties(ARGS[0])).willReturn(properties);
    }

    @Test
    public void testBootstrapShouldCallStart() {
        //GIVEN
        properties.setProperty("webapp.port", "8080");
        doReturn(webAppServer).when(underTest).createWebAppServer();
        //WHEN
        underTest.bootstrap(ARGS);
        //THEN
        verify(webAppServer).start();
    }

    @Test
    public void testBootstrapShouldLogError() {
        //GIVEN
        properties.setProperty("webapp.port", "8080");
        BeanCreationException exception = new BeanCreationException("exception");
        doReturn(webAppServer).when(underTest).createWebAppServer();
        willThrow(exception).given(webAppServer).createServer(8080);
        given(systemExceptionSelector.getSystemException(exception)).willReturn(systemException);
        //WHEN
        underTest.bootstrap(ARGS);
        //THEN
        verify(logger).error(Mockito.anyString());
    }

    @Test(expectedExceptions = BeanCreationException.class)
    public void testBootstrapShouldThrowException() {
        //GIVEN
        properties.setProperty("webapp.port", "8080");
        BeanCreationException exception = new BeanCreationException("exception");
        doReturn(webAppServer).when(underTest).createWebAppServer();
        willThrow(exception).given(webAppServer).createServer(8080);
        given(systemExceptionSelector.getSystemException(exception)).willReturn(null);
        //WHEN
        underTest.bootstrap(ARGS);
        //THEN it should throw exception
    }

    @Test
    public void testBootstrapWhenPortCannotBeParsedShouldThrowException() {
        //GIVEN
        properties.setProperty("webapp.port", "text");
        doReturn(webAppServer).when(underTest).createWebAppServer();
        //WHEN
        underTest.bootstrap(ARGS);
        //THEN
        verify(logger).error(Mockito.anyString());
    }
}
