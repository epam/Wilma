package com.epam.wilma.webapp.config.servlet.service;
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

import com.epam.wilma.webapp.service.external.ServiceMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link ServiceServlet}.
 * @author Tamas KOhegyi
 *
 */
public class ServiceServletTest {

    @InjectMocks
    private ServiceServlet underTest;

    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private ServiceMap serviceMap;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoGetShouldReturnWithDefaultIfEmptyServiceGet() throws ServletException, IOException {
        //GIVEN
        given(resp.getWriter()).willReturn(printWriter);
        given(req.getRequestURI()).willReturn("/public/service");
        given(req.getMethod()).willReturn("get");
        given(serviceMap.getMapAsResponse()).willReturn("blah");
        //WHEN
        underTest.doGet(req, resp);
        //THEN
        verify(resp).setContentType("application/json");
        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
        String response = "blah";
        verify(printWriter).write(response);
        verify(printWriter).flush();
        verify(printWriter).close();
    }

    @Test
    public void testDoGetShouldReturnWithServiceListForEmptyServicePost() throws ServletException, IOException {
        //GIVEN
        given(resp.getWriter()).willReturn(printWriter);
        given(req.getRequestURI()).willReturn("/public/services/");
        given(req.getMethod()).willReturn("post");
        given(serviceMap.getMapAsResponse()).willReturn("blah");
        //WHEN
        underTest.doGet(req, resp);
        //THEN
        verify(resp).setContentType("application/json");
        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
        String response = "blah";
        verify(printWriter).write(response);
        verify(printWriter).flush();
        verify(printWriter).close();
    }

    @Test
    public void testDoGetShouldReturnWithServiceListForEmptyServiceGet() throws ServletException, IOException {
        //GIVEN
        given(resp.getWriter()).willReturn(printWriter);
        given(req.getRequestURI()).willReturn("/public/services/");
        given(req.getMethod()).willReturn("get");
        given(serviceMap.getMapAsResponse()).willReturn("blah");
        //WHEN
        underTest.doGet(req, resp);
        //THEN
        verify(resp).setContentType("application/json");
        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
        String response = "blah";
        verify(printWriter).write(response);
        verify(printWriter).flush();
        verify(printWriter).close();
    }

    @Test
    public void testDoGetShouldReturnWithDefaultIfUnknownService() throws ServletException, IOException {
        //GIVEN
        given(resp.getWriter()).willReturn(printWriter);
        given(req.getRequestURI()).willReturn("/public/services/unknown");
        given(req.getMethod()).willReturn("get");
        //WHEN
        underTest.doGet(req, resp);
        //THEN
        verify(resp).setContentType("application/json");
        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
        String response = "{ \"unknownServiceCall\": \"get unknown\" }";
        verify(printWriter).write(response);
        verify(printWriter).flush();
        verify(printWriter).close();
    }

    @Test
    public void testDoGetShouldReturnWithWilmaUniqueId() throws ServletException, IOException {
        //GIVEN
        given(resp.getWriter()).willReturn(printWriter);
        given(req.getRequestURI()).willReturn("/public/services/UniqueIdGenerator/uniqueId");
        given(req.getMethod()).willReturn("get");
        //WHEN
        underTest.doGet(req, resp);
        //THEN
        verify(resp).setContentType("application/json");
        verify(resp).setStatus(HttpServletResponse.SC_OK);
    }

}
