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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Returns the list of dialog descriptors of the actual stub configuration and their usage.
 *
 * @author Tunde_Kovacs
 */
@Component
public class StubDescriptorStatusServlet extends HttpServlet {

    private final RoutingService routingService;
    private final ExpirationTimeProvider expirationTimeProvider;

    /**
     * Constructor using spring framework to initialize the class.
     * @param routingService provides access to the routing service
     * @param expirationTimeProvider provides access to expiration info of the time based stub configurations
     */
    @Autowired
    public StubDescriptorStatusServlet(RoutingService routingService, ExpirationTimeProvider expirationTimeProvider) {
        this.routingService = routingService;
        this.expirationTimeProvider = expirationTimeProvider;
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Map<String, StubDescriptor> stubDescriptors = routingService.getStubDescriptors();
        writeAllDialogDescriptors(out, stubDescriptors);
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void writeAllDialogDescriptors(final PrintWriter out, final Map<String, StubDescriptor> stubDescriptors) {
        out.write("{\"configs\":[");
        Iterator<String> iterator = stubDescriptors.keySet().iterator();
        while (iterator.hasNext()) {
            out.write("{");
            String groupName = iterator.next();
            StubDescriptor stubDescriptor = stubDescriptors.get(groupName);
            writeDialogDescriptors(out, stubDescriptor);
            out.write(",");
            writeSequenceDescriptors(out, stubDescriptor);
            out.write(",");
            writeInterceptorDescriptors(out, stubDescriptor);
            out.write("}");
            if (iterator.hasNext()) {
                out.write(",");
            }
        }
        out.write("]}");
    }

    private void writeInterceptorDescriptors(PrintWriter out, StubDescriptor stubDescriptor) {
        out.write("\"interceptorDescriptors\":[");
        List<InterceptorDescriptor> interceptorDescriptors = stubDescriptor.getInterceptorDescriptors();
        Iterator<InterceptorDescriptor> iterator = interceptorDescriptors.iterator();
        while (iterator.hasNext()) {
            writeInterceptorName(out, iterator);
            if (iterator.hasNext()) {
                out.write(",");
            }
        }
        out.write("]");
    }

    private void writeSequenceDescriptors(final PrintWriter out, final StubDescriptor stubDescriptor) {
        out.write("\"sequenceDescriptors\":[");
        List<SequenceDescriptor> sequenceDescriptors = stubDescriptor.getSequenceDescriptors();
        Iterator<SequenceDescriptor> iterator = sequenceDescriptors.iterator();
        while (iterator.hasNext()) {
            writeSequenceName(out, iterator);
            if (iterator.hasNext()) {
                out.write(",");
            }
        }
        out.write("]");
    }

    private void writeDialogDescriptors(final PrintWriter out, final StubDescriptor stubDescriptor) {
        out.write("\"dialogDescriptors\":[");
        List<DialogDescriptor> dialogDescriptors = stubDescriptor.getDialogDescriptors();
        Iterator<DialogDescriptor> iterator = dialogDescriptors.iterator();
        while (iterator.hasNext()) {
            writeNameAndUsage(out, iterator);
            if (iterator.hasNext()) {
                out.write(",");
            }
        }
        StubDescriptorAttributes attributes = stubDescriptor.getAttributes();
        out.write("], \"groupname\": \"" + attributes.getGroupName() + "\", \"active\": \"" + attributes.isActive() + "\"");
    }

    private void writeInterceptorName(PrintWriter out, Iterator<InterceptorDescriptor> iterator) {
        InterceptorDescriptor interceptorDescriptor = iterator.next();
        String name = interceptorDescriptor.getName();
        out.write("{\"Name\": \"" + name + "\"}");
    }

    private void writeSequenceName(final PrintWriter out, final Iterator<SequenceDescriptor> iterator) {
        SequenceDescriptor sequenceDescriptor = iterator.next();
        String name = sequenceDescriptor.getName();
        out.write("{\"Name\": \"" + name + "\"}");
    }

    private void writeNameAndUsage(final PrintWriter out, final Iterator<DialogDescriptor> iterator) {
        DialogDescriptor dialogDescriptor = iterator.next();
        DialogDescriptorUsage usage = dialogDescriptor.getAttributes().getUsage();
        String name = dialogDescriptor.getAttributes().getName();
        out.write("{\"Name\":\"" + name + "\", \"Usage\":\"" + usage);
        if (usage == DialogDescriptorUsage.ALWAYS || usage == DialogDescriptorUsage.DISABLED) {
            out.write("\"}");
        } else {
            if (usage == DialogDescriptorUsage.HITCOUNT) {
                long hitCount = dialogDescriptor.getAttributes().getHitcount();
                writeHitcountUsage(out, hitCount);
            } else {
                long timeout = dialogDescriptor.getAttributes().getTimeout();
                writeTimeoutUsage(out, timeout);
            }
        }
    }

    private void writeHitcountUsage(final PrintWriter out, final long validityValue) {
        out.write(" -> " + validityValue + " hit(s) left\"}");
    }

    private void writeTimeoutUsage(final PrintWriter out, final long validityValue) {
        long expirationMinutes = expirationTimeProvider.getExpirationMinutes(validityValue);
        long expirationSeconds = expirationTimeProvider.getExpirationSeconds(validityValue);
        if (expirationMinutes <= 0 && expirationSeconds <= 0) {
            out.write(" -> expired\"}");
        } else {
            out.write(" -> expires in " + expirationMinutes + ":" + expirationSeconds + " (min:sec)\"}");
        }
    }

}
