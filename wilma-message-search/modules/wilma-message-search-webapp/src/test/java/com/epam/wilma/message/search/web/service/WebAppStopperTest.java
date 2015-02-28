package com.epam.wilma.message.search.web.service;

/*==========================================================================
Copyright 2015 EPAM Systems

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

import static org.mockito.Mockito.verify;

import java.util.concurrent.ExecutorService;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.message.search.web.WebAppServer;

/**
 * Unit test for {@link WebAppStopper}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class WebAppStopperTest {

    @Mock
    private ExecutorService executorService;
    @Mock
    private WebAppServer webAppServer;

    private WebAppStopper underTest;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        underTest = new WebAppStopper(webAppServer, executorService);
    }

    @Test
    public void testStopAsyncShouldShutdownExecutor() {
        //GIVEN
        //WHEN
        underTest.stopAsync();
        //THEN
        verify(executorService).shutdown();
    }

    @Test
    public void testStopAsyncShouldStopWebAppServer() {
        //GIVEN
        ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
        //WHEN
        underTest.stopAsync();
        //THEN
        verify(executorService).execute(captor.capture());
        captor.getValue().run();
        verify(webAppServer).stop();
    }

}
