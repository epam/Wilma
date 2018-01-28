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

import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Provides unit tests for <tt>ApplicationContextFactory</tt>.
 */
public class ApplicationContextFactoryTest {

    private static final String SPRING_APP_CONTEXT_PATH = "test-application-context.xml";
    private ApplicationContextFactory underTest;

    @BeforeMethod
    public void setUp() {
        underTest = ApplicationContextFactory.getInstance();
    }

    @Test
    public void testGetClassPathXmlApplicationContextShouldCreateNewAppContext() {
        //GIVEN in setUp
        //WHEN
        ClassPathXmlApplicationContext actual = underTest.getClassPathXmlApplicationContext(SPRING_APP_CONTEXT_PATH);
        //THEN
        Assert.assertTrue(actual.containsBean("test"));
    }

    @Test
    public void testGetClassPathXmlApplicationContextShouldReturnTheSameAppContextForACertainPath() {
        //GIVEN
        ClassPathXmlApplicationContext expected = new ClassPathXmlApplicationContext(SPRING_APP_CONTEXT_PATH);
        Map<String, ClassPathXmlApplicationContext> appContexts = new HashMap<>();
        appContexts.put(SPRING_APP_CONTEXT_PATH, expected);
        Whitebox.setInternalState(underTest, "applicationContexts", appContexts);
        //WHEN
        ClassPathXmlApplicationContext actual = underTest.getClassPathXmlApplicationContext(SPRING_APP_CONTEXT_PATH);
        //THEN
        Assert.assertEquals(actual, expected);
    }
}
