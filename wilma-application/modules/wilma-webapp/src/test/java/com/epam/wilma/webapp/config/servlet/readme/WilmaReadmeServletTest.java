package com.epam.wilma.webapp.config.servlet.readme;
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

import com.epam.wilma.webapp.configuration.WebAppConfigurationAccess;
import com.epam.wilma.webapp.configuration.domain.PropertyDTO;
import com.epam.wilma.webapp.configuration.domain.Readme;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
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
 * Provides unit tests for the class {@link WilmaReadmeServlet}.
 *
 * @author Tunde_Kovacs
 */
public class WilmaReadmeServletTest {

    private Readme readme;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter writer;
    @Mock
    private WebAppConfigurationAccess webAppConfigurationAccess;
    @Mock
    private PropertyDTO properties;

    @InjectMocks
    private WilmaReadmeServlet underTest;

    @BeforeMethod
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        String readmeUrl = "url";
        String readmeText = "text";
        readme = new Readme(readmeUrl, readmeText);
        Whitebox.setInternalState(underTest, "webAppConfigurationAccess", webAppConfigurationAccess);
        given(response.getWriter()).willReturn(writer);
        given(webAppConfigurationAccess.getProperties()).willReturn(properties);
        given(properties.getReadme()).willReturn(readme);
    }

    @Test
    public void testDoGetShouldWriteUrlAndTextToResponse() throws ServletException, IOException {
        //GIVEN in setUp
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(writer).write("{\"readmeUrl\":\"" + readme.getUrl() + "\", \"readmeText\":\"" + readme.getText() + "\"}");
    }

    @Test
    public void testDoGetShouldSetResponseContentTypeToJson() throws ServletException, IOException {
        //GIVEN in setUp
        //WHEN
        underTest.doGet(request, response);
        //THEN
        verify(response).setContentType("application/json");
    }

    @Test
    public void testDoPostShouldWriteUrlAndTextToResponse() throws ServletException, IOException {
        //GIVEN in setUp
        //WHEN
        underTest.doPost(request, response);
        //THEN
        verify(writer).write("{\"readmeUrl\":\"" + readme.getUrl() + "\", \"readmeText\":\"" + readme.getText() + "\"}");
    }

}
