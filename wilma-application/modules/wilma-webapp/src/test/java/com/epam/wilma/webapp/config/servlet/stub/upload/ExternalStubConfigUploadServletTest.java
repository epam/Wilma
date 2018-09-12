package com.epam.wilma.webapp.config.servlet.stub.upload;
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

import com.epam.wilma.domain.stubconfig.exception.DescriptorCannotBeParsedException;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptorHolder;
import com.epam.wilma.router.RoutingService;
import com.epam.wilma.stubconfig.StubDescriptorJsonFactory;
import com.epam.wilma.webapp.helper.UrlAccessLogMessageAssembler;
import com.epam.wilma.webapp.service.command.NewStubDescriptorCommand;
import com.epam.wilma.webapp.service.external.ServiceMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the class {@link ExternalStubConfigUploadServlet}.
 *
 * @author Tunde_Kovacs
 */
public class ExternalStubConfigUploadServletTest {

    private static final String FILE_NAME = "resource file";
    private static final String EXCEPTION_MESSAGE = "Could not upload external stub configuration";
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletInputStream inputStream;
    @Mock
    private PrintWriter writer;
    @Mock
    private StubDescriptorJsonFactory stubDescriptorJsonFactory;
    @Mock
    private RoutingService routingService;
    @Mock
    private ServiceMap serviceMap;
    @Mock
    private UrlAccessLogMessageAssembler urlAccessLogMessageAssembler;
    @Mock
    private SequenceDescriptorHolder sequenceDescriptorHolder;


    @InjectMocks
    private ExternalStubConfigUploadServlet underTest;

    @BeforeMethod
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "urlAccessLogMessageAssembler", urlAccessLogMessageAssembler);
        Whitebox.setInternalState(underTest, "sequenceDescriptorHolder", sequenceDescriptorHolder);
        Whitebox.setInternalState(underTest, "stubDescriptorJsonFactory", stubDescriptorJsonFactory);
        Whitebox.setInternalState(underTest, "routingService", routingService);
        Whitebox.setInternalState(underTest, "serviceMap", serviceMap);
        given(request.getInputStream()).willReturn(inputStream);
        given(request.getParameter("fileName")).willReturn(FILE_NAME);
        given(response.getWriter()).willReturn(writer);
    }

    @Test
    public void testDoGetShouldCallStubDescriptorConstructor() throws ServletException, IOException, ClassNotFoundException {
        //GIVEN
        given(request.getContentLength()).willReturn(1);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(routingService).performModification(Mockito.any(NewStubDescriptorCommand.class));
    }

    @Test
    public void testDoGetWhenRequestBodyIsEmptyNotShouldCallStubDescriptorConstructor() throws ServletException, IOException {
        //GIVEN
        given(request.getContentLength()).willReturn(0);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(writer).write("Please provide a non-empty stub configuration!");
    }

    @Test
    public void testDoGetWhenExceptionShouldWriteErrorToResponse() throws ServletException, IOException, ClassNotFoundException {
        //GIVEN
        given(request.getContentLength()).willReturn(1);
        willThrow(new DescriptorCannotBeParsedException(EXCEPTION_MESSAGE, new Throwable())).given(routingService).performModification(
                Mockito.any(NewStubDescriptorCommand.class));
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(writer).write(Mockito.anyString());
    }

    @Test
    public void testDoPostShouldCallDoGet() throws ServletException, IOException, ClassNotFoundException {
        //GIVEN
        given(request.getContentLength()).willReturn(1);
        //WHEN
        underTest.doPost(request, response);
        //THEN
        verify(routingService).performModification(Mockito.any(NewStubDescriptorCommand.class));
    }
}
