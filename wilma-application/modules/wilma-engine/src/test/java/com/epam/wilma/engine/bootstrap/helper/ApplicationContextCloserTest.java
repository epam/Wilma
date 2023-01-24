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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the class <tt>ContextCloseHandler</tt>.
 *
 * @author Tunde_Kovacs
 */
public class ApplicationContextCloserTest {

    @Mock
    private ThreadPoolTaskExecutor executor;
    @Mock
    private ThreadPoolTaskScheduler scheduler;
    @Mock
    private ClassPathXmlApplicationContext appContext;
    @InjectMocks
    private ApplicationContextCloser underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest.setApplicationContext(appContext);
    }

    @Test
    public void testCloseTriggersShouldShutdownScheduler() {
        //GIVEN in setUp
        //WHEN
        underTest.closeTriggers();
        //THEN
        verify(scheduler).shutdown();
    }

    @Test
    public void testCloseTriggersShouldShutdownExecutor() {
        //GIVEN in setUp
        //WHEN
        underTest.closeTriggers();
        //THEN
        verify(executor).shutdown();
    }

    @Test
    public void testCloseTriggersShouldCloseApplicationContext() {
        //GIVEN in setUp
        //WHEN
        underTest.closeTriggers();
        //THEN
        verify(appContext).close();
    }
}
