package com.epam.gepard.common.threads;

/*==========================================================================
Copyright 2004-2015 EPAM Systems

This file is part of Gepard.

Gepard is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Gepard is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Gepard.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import com.epam.gepard.common.threads.handler.KillCommandHandler;
import com.epam.gepard.common.threads.handler.RemoteControlHandler;
import com.epam.gepard.common.threads.helper.ServerSocketFactory;

/**
 * Unit test for {@link RemoteControlHandlerThread}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class RemoteControlHandlerThreadTest {

    @Mock
    private RemoteControlHandler handler;
    @Mock
    private ServerSocketFactory gepardServerSocketFactory;
    @Mock
    private ServerSocket serverSocket;
    @Mock
    private Socket socket;
    private InputStream inputStream;
    @Mock
    private OutputStream outputStream;
    @Mock
    private KillCommandHandler killCommandHandler;

    private RemoteControlHandlerThread underTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        underTest = new RemoteControlHandlerThread(handler, gepardServerSocketFactory, 1);
        underTest.setSleepTime(1);
        Whitebox.setInternalState(underTest, "killCommandHandler", killCommandHandler);
    }

    @Test
    public void testWhenExceptionOccursAtServerSocketCreationThenCommandShouldNotBeHandled() throws IOException {
        //GIVEN
        inputStream = new ByteArrayInputStream("kill\n".getBytes());
        given(gepardServerSocketFactory.create(1)).willThrow(new IOException());
        //WHEN
        underTest.run();
        //THEN
        verify(killCommandHandler, never()).handleCommand();
    }

    @Test
    public void testWhenClientSendsKillThenCommandHandlerShouldKillGepard() throws IOException {
        //GIVEN
        given(gepardServerSocketFactory.create(1)).willReturn(serverSocket);
        given(serverSocket.accept()).willReturn(socket);
        given(socket.getInputStream()).willReturn(inputStream);
        given(socket.getOutputStream()).willReturn(outputStream);
        inputStream = new ByteArrayInputStream("kill\n".getBytes());
        given(socket.getInputStream()).willReturn(inputStream);
        //WHEN
        underTest.run();
        //THEN
        verify(killCommandHandler).handleCommand();
    }

}
