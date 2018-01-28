package com.epam.wilma.webapp.config.servlet.stub;
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

import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.domain.stubconfig.StubDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptorUsage;
import com.epam.wilma.domain.stubconfig.interceptor.InterceptorDescriptor;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;
import com.epam.wilma.router.RoutingService;
import com.epam.wilma.webapp.config.servlet.stub.helper.ExpirationTimeProvider;
import org.mockito.Answers;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;

/**
 * Provides unit tests for the class {@link StubDescriptorStatusServlet}.
 *
 * @author Tunde_Kovacs
 */
public class StubDescriptorStatusServletTest {

    private static final String TEST_GROUPNAME = "test";
    private static final String NAME = "dialog-descriptor";
    private static final Long VALIDITY_VALUE = 2L;

    private List<InterceptorDescriptor> interceptorDescriptors;
    private List<SequenceDescriptor> sequenceDescriptors;
    private List<DialogDescriptor> dialogDescriptors;

    @Mock
    private RoutingService routingService;
    @Mock
    private StubDescriptor stubDescriptor;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private DialogDescriptor dialogDescriptor;
    @Mock
    private ExpirationTimeProvider expirationTimeProvider;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter out;
    @Mock
    private SequenceDescriptor sequenceDescriptor;
    @Mock
    private InterceptorDescriptor interceptorDescriptor;

    @InjectMocks
    private StubDescriptorStatusServlet underTest;

    private StubDescriptorAttributes stubDescriptorAttributes = new StubDescriptorAttributes(TEST_GROUPNAME);

    @BeforeMethod
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "routingService", routingService);
        Whitebox.setInternalState(underTest, "expirationTimeProvider", expirationTimeProvider);
        sequenceDescriptors = new ArrayList<>();
        dialogDescriptors = new ArrayList<>();
        interceptorDescriptors = new ArrayList<>();
        Map<String, StubDescriptor> stubDescriptors = new LinkedHashMap<>();
        stubDescriptors.put(TEST_GROUPNAME, stubDescriptor);
        Whitebox.setInternalState(routingService, "stubDescriptors", stubDescriptors);
        given(routingService.getStubDescriptors()).willReturn(stubDescriptors);
        given(stubDescriptor.getAttributes()).willReturn(stubDescriptorAttributes);
        given(stubDescriptor.getDialogDescriptors()).willReturn(dialogDescriptors);
        given(stubDescriptor.getSequenceDescriptors()).willReturn(sequenceDescriptors);
        given(stubDescriptor.getInterceptorDescriptors()).willReturn(interceptorDescriptors);
        given(response.getWriter()).willReturn(out);
        Whitebox.setInternalState(stubDescriptor, "attributes", stubDescriptorAttributes);
    }

    @Test
    public void testDoGetWhenThereAreNoSequenceDescriptorsWriteAnEmptyList() throws ServletException, IOException {
        //GIVEN in setUp
        //WHEN
        underTest.doGet(request, response);
        //THEN
        InOrder order = Mockito.inOrder(out);
        order.verify(out).write("\"sequenceDescriptors\":[");
        order.verify(out).write("]");
        order.verify(out).write(",");
    }

    @Test
    public void testDoGetWhenThereAreNoInterceptorDescriptorsWriteAnEmptyList() throws ServletException, IOException {
        //GIVEN in setUp
        //WHEN
        underTest.doGet(request, response);
        //THEN
        InOrder order = Mockito.inOrder(out);
        order.verify(out).write("\"interceptorDescriptors\":[");
        order.verify(out).write("]");
    }

    @Test
    public void testDoGetWhenThereIsOneSequenceDescriptors() throws ServletException, IOException {
        //GIVEN in setUp
        sequenceDescriptors.add(sequenceDescriptor);
        given(sequenceDescriptor.getName()).willReturn("SequenceDescriptor1");
        //WHEN
        underTest.doGet(request, response);
        //THEN
        InOrder order = Mockito.inOrder(out);
        order.verify(out).write("\"sequenceDescriptors\":[");
        order.verify(out).write("{\"Name\": \"SequenceDescriptor1\"}");
        order.verify(out).write("]");
        order.verify(out).write(",");
    }

    @Test
    public void testDoGetWhenThereIsOneInterceptorDescriptors() throws ServletException, IOException {
        //GIVEN in setUp
        interceptorDescriptors.add(interceptorDescriptor);
        given(interceptorDescriptor.getName()).willReturn("InterceptorDescriptor1");
        //WHEN
        underTest.doGet(request, response);
        //THEN
        InOrder order = Mockito.inOrder(out);
        order.verify(out).write("\"interceptorDescriptors\":[");
        order.verify(out).write("{\"Name\": \"InterceptorDescriptor1\"}");
        order.verify(out).write("]");
    }

    @Test
    public void testDoGetWhenThereAreNoDialogDescriptorsWriteAnEmptyList() throws ServletException, IOException {
        //GIVEN in setUp
        //WHEN
        underTest.doGet(request, response);
        //THEN
        InOrder order = Mockito.inOrder(out);
        order.verify(out).write("\"dialogDescriptors\":[");
        order.verify(out).write("], \"groupname\": \"test\", \"active\": \"true\"");
    }

    @Test
    public void testDoGetWhenThereIsSequenceDescriptorAndDialogDescriptorAndInterceptorDescriptorInStubDescriptor() throws ServletException, IOException {
        //GIVEN
        dialogDescriptors.add(dialogDescriptor);
        sequenceDescriptors.add(sequenceDescriptor);
        interceptorDescriptors.add(interceptorDescriptor);
        given(interceptorDescriptor.getName()).willReturn("InterceptorDescriptor1");
        given(sequenceDescriptor.getName()).willReturn("SequenceDescriptor1");
        given(dialogDescriptor.getAttributes().getName()).willReturn(NAME);
        given(dialogDescriptor.getAttributes().getUsage()).willReturn(DialogDescriptorUsage.ALWAYS);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        InOrder order = Mockito.inOrder(out);
        order.verify(out).write("\"dialogDescriptors\":[");
        order.verify(out).write("{\"Name\":\"" + NAME + "\", \"Usage\":\"" + DialogDescriptorUsage.ALWAYS);
        order.verify(out).write("\"}");
        order.verify(out).write("], \"groupname\": \"test\", \"active\": \"true\"");
        order.verify(out).write(",");
        order.verify(out).write("\"sequenceDescriptors\":[");
        order.verify(out).write("{\"Name\": \"SequenceDescriptor1\"}");
        order.verify(out).write("]");
        order.verify(out).write(",");
        order.verify(out).write("\"interceptorDescriptors\":[");
        order.verify(out).write("{\"Name\": \"InterceptorDescriptor1\"}");
    }

    @Test
    public void testDoGetWhenUsageIsAlwaysShouldWriteAlwaysInResponse() throws ServletException, IOException {
        //GIVEN
        dialogDescriptors.add(dialogDescriptor);
        given(dialogDescriptor.getAttributes().getName()).willReturn(NAME);
        given(dialogDescriptor.getAttributes().getUsage()).willReturn(DialogDescriptorUsage.ALWAYS);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        InOrder order = Mockito.inOrder(out);
        order.verify(out).write("{\"Name\":\"" + NAME + "\", \"Usage\":\"" + DialogDescriptorUsage.ALWAYS);
        order.verify(out).write("\"}");
    }

    @Test
    public void testDoGetWhenUsageIsDisabledShouldWriteDisabledInResponse() throws ServletException, IOException {
        //GIVEN
        dialogDescriptors.add(dialogDescriptor);
        given(dialogDescriptor.getAttributes().getName()).willReturn(NAME);
        given(dialogDescriptor.getAttributes().getUsage()).willReturn(DialogDescriptorUsage.DISABLED);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        InOrder order = Mockito.inOrder(out);
        order.verify(out).write("{\"Name\":\"" + NAME + "\", \"Usage\":\"" + DialogDescriptorUsage.DISABLED);
        order.verify(out).write("\"}");
    }

    @Test
    public void testDoGetWhenUsageIsHitcountShouldWriteHitcountInResponse() throws ServletException, IOException {
        //GIVEN
        dialogDescriptors.add(dialogDescriptor);
        given(dialogDescriptor.getAttributes().getName()).willReturn(NAME);
        given(dialogDescriptor.getAttributes().getUsage()).willReturn(DialogDescriptorUsage.HITCOUNT);
        given(dialogDescriptor.getAttributes().getHitcount()).willReturn(VALIDITY_VALUE);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        InOrder order = Mockito.inOrder(out);
        order.verify(out).write("{\"Name\":\"" + NAME + "\", \"Usage\":\"" + DialogDescriptorUsage.HITCOUNT);
        order.verify(out).write(" -> " + VALIDITY_VALUE + " hit(s) left\"}");
    }

    @Test
    public void testDoGetWhenUsageIsTimeoutShouldWriteTimeoutInResponse() throws ServletException, IOException {
        //GIVEN
        long expirationMinutes = 2L;
        long expirationSeconds = 31L;
        dialogDescriptors.add(dialogDescriptor);
        given(dialogDescriptor.getAttributes().getName()).willReturn(NAME);
        given(dialogDescriptor.getAttributes().getUsage()).willReturn(DialogDescriptorUsage.TIMEOUT);
        given(dialogDescriptor.getAttributes().getTimeout()).willReturn(VALIDITY_VALUE);
        given(expirationTimeProvider.getExpirationMinutes(VALIDITY_VALUE)).willReturn(expirationMinutes);
        given(expirationTimeProvider.getExpirationSeconds(VALIDITY_VALUE)).willReturn(expirationSeconds);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        InOrder order = Mockito.inOrder(out);
        order.verify(out).write("{\"Name\":\"" + NAME + "\", \"Usage\":\"" + DialogDescriptorUsage.TIMEOUT);
        order.verify(out).write(" -> expires in " + expirationMinutes + ":" + expirationSeconds + " (min:sec)\"}");
    }

    @Test
    public void testDoGetWhenUsageIsTimeoutMinuteExpiredShouldWriteTimeoutInResponse() throws ServletException, IOException {
        //GIVEN
        long expirationMinutes = 0L;
        long expirationSeconds = 2L;
        dialogDescriptors.add(dialogDescriptor);
        given(dialogDescriptor.getAttributes().getName()).willReturn(NAME);
        given(dialogDescriptor.getAttributes().getUsage()).willReturn(DialogDescriptorUsage.TIMEOUT);
        given(dialogDescriptor.getAttributes().getTimeout()).willReturn(VALIDITY_VALUE);
        given(expirationTimeProvider.getExpirationMinutes(VALIDITY_VALUE)).willReturn(expirationMinutes);
        given(expirationTimeProvider.getExpirationSeconds(VALIDITY_VALUE)).willReturn(expirationSeconds);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        InOrder order = Mockito.inOrder(out);
        order.verify(out).write("{\"Name\":\"" + NAME + "\", \"Usage\":\"" + DialogDescriptorUsage.TIMEOUT);
        order.verify(out).write(" -> expires in " + expirationMinutes + ":" + expirationSeconds + " (min:sec)\"}");
    }

    @Test
    public void testDoGetWhenUsageIsTimeoutAndExpiredShouldWriteTimeoutInResponse() throws ServletException, IOException {
        //GIVEN
        long expirationMinutes = 0L;
        long expirationSeconds = -1L;
        dialogDescriptors.add(dialogDescriptor);
        given(dialogDescriptor.getAttributes().getName()).willReturn(NAME);
        given(dialogDescriptor.getAttributes().getUsage()).willReturn(DialogDescriptorUsage.TIMEOUT);
        given(dialogDescriptor.getAttributes().getTimeout()).willReturn(VALIDITY_VALUE);
        given(expirationTimeProvider.getExpirationMinutes(VALIDITY_VALUE)).willReturn(expirationMinutes);
        given(expirationTimeProvider.getExpirationSeconds(VALIDITY_VALUE)).willReturn(expirationSeconds);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        InOrder order = Mockito.inOrder(out);
        order.verify(out).write("{\"Name\":\"" + NAME + "\", \"Usage\":\"" + DialogDescriptorUsage.TIMEOUT);
        order.verify(out).write(" -> expired\"}");
    }

    @Test
    public void testDoGetWhenThereAreMultipleDDsShouldWriteAlwaysInResponse() throws ServletException, IOException {
        //GIVEN
        dialogDescriptors.add(dialogDescriptor);
        dialogDescriptors.add(dialogDescriptor);
        given(dialogDescriptor.getAttributes().getName()).willReturn(NAME);
        given(dialogDescriptor.getAttributes().getUsage()).willReturn(DialogDescriptorUsage.ALWAYS);
        //WHEN
        underTest.doGet(request, response);
        //THEN
        InOrder order = Mockito.inOrder(out);
        order.verify(out).write("{\"Name\":\"" + NAME + "\", \"Usage\":\"" + DialogDescriptorUsage.ALWAYS);
        order.verify(out).write("\"}");
        order.verify(out).write(",");
        order.verify(out).write("{\"Name\":\"" + NAME + "\", \"Usage\":\"" + DialogDescriptorUsage.ALWAYS);
        order.verify(out).write("\"}");
    }

    @Test
    public void testDoPostWhenThereAreMultipleDDsShouldWriteAlwaysInResponse() throws ServletException, IOException {
        //GIVEN
        dialogDescriptors.add(dialogDescriptor);
        dialogDescriptors.add(dialogDescriptor);
        given(dialogDescriptor.getAttributes().getName()).willReturn(NAME);
        given(dialogDescriptor.getAttributes().getUsage()).willReturn(DialogDescriptorUsage.ALWAYS);
        //WHEN
        underTest.doPost(request, response);
        //THEN
        InOrder order = Mockito.inOrder(out);
        order.verify(out).write("{\"Name\":\"" + NAME + "\", \"Usage\":\"" + DialogDescriptorUsage.ALWAYS);
        order.verify(out).write("\"}");
        order.verify(out).write(",");
        order.verify(out).write("{\"Name\":\"" + NAME + "\", \"Usage\":\"" + DialogDescriptorUsage.ALWAYS);
        order.verify(out).write("\"}");
    }
}
