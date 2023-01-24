package com.epam.wilma.message.search.web;
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

import com.epam.wilma.message.search.web.domain.exception.ServerException;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the class {@link WebAppServer}.
 *
 * @author Tunde_Kovacs
 */
public class WebAppServerTest {

    private static final String EXCEPTION_MESSAGE = "exception message";

    @Mock
    private Server server;

    private WebAppServer underTest;

    @BeforeEach
    public void setUp() {
        underTest = Mockito.spy(new WebAppServer());
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testStartShouldStartServer() throws Exception {
        //GIVEN
        doNothing().when(underTest).startJettyServer();
        //WHEN
        underTest.start();
        //THEN
        verify(underTest).startJettyServer();
    }

    @Test
    public void testStartShouldLogErrorWhenCannotStart() {
        Assertions.assertThrows(ServerException.class, () -> {
            //GIVEN
            doThrow(new Exception()).when(underTest).startJettyServer();
            //WHEN
            underTest.start();
            //THEN it should throw exception
        });
    }

    @Test
    public void testStopShouldThrowExceptionWhenWebAppCanNotBeStopped() {
        Assertions.assertThrows(ServerException.class, () -> {
            //GIVEN
            ReflectionTestUtils.setField(underTest, "server", server);
            given(server.isStarted()).willReturn(true);
            Exception e = new Exception(EXCEPTION_MESSAGE);
            doThrow(e).when(underTest).stopJettyServer();
            //WHEN
            underTest.stop();
            //THEN it should throw exception
        });
    }

    @Test
    public void testStopShouldCallStopJettyServer() throws Exception {
        //GIVEN
        ReflectionTestUtils.setField(underTest, "server", server);
        given(server.isStarted()).willReturn(true);
        doNothing().when(underTest).stopJettyServer();
        //WHEN
        underTest.stop();
        //THEN
        verify(underTest).stopJettyServer();
    }

    @Test
    public void testStopShouldDoNothingWhenServerIsNull() throws Exception {
        //GIVEN
        ReflectionTestUtils.setField(underTest, "server", null);
        //WHEN
        underTest.stop();
        //THEN
        verify(underTest, never()).stopJettyServer();
    }

    @Test
    public void testStopShouldDoNothingWhenServerIsNotStarted() throws Exception {
        //GIVEN
        ReflectionTestUtils.setField(underTest, "server", server);
        given(server.isStarted()).willReturn(false);
        //WHEN
        underTest.stop();
        //THEN
        verify(underTest, never()).stopJettyServer();
    }
}
