package com.epam.wilma.webapp.config.servlet.logging;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.webapp.config.servlet.helper.MaintainerPropertiesJsonBuilder;
import com.epam.wilma.webapp.configuration.WebAppConfigurationAccess;
import com.epam.wilma.webapp.configuration.domain.MaintainerProperties;
import com.epam.wilma.webapp.configuration.domain.PropertyDTO;

/**
 * Test class for {@link MaintainerPropertiesServlet}.
 * @author Marton_Sereg
 *
 */
public class MaintainerPropertiesServletTest {

    private MaintainerProperties maintainerProperties;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private MaintainerPropertiesJsonBuilder maintainerPropertiesJsonBuilder;
    @Mock
    private WebAppConfigurationAccess webAppConfigurationAccess;
    @Mock
    private PropertyDTO properties;

    @InjectMocks
    private MaintainerPropertiesServlet underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(underTest, "maintainerPropertiesJsonBuilder", maintainerPropertiesJsonBuilder);
        Whitebox.setInternalState(underTest, "webAppConfigurationAccess", webAppConfigurationAccess);
        given(webAppConfigurationAccess.getProperties()).willReturn(properties);
        given(properties.getMaintainerProperties()).willReturn(maintainerProperties);
    }

    @Test
    public final void testDoGetShouldCallJsonBuilderAndWriteResponse() throws IOException, ServletException {
        // GIVEN
        given(properties.getMaintainerProperties()).willReturn(maintainerProperties);
        given(response.getWriter()).willReturn(printWriter);
        given(maintainerPropertiesJsonBuilder.buildMaintainerPropertiesJson(maintainerProperties)).willReturn("json");
        // WHEN
        underTest.doGet(request, response);
        // THEN
        verify(response).setContentType("application/json");
        verify(printWriter).write("json");
        verify(printWriter).flush();
        verify(printWriter).close();
    }

    @Test
    public final void testDoPostShouldCallJsonBuilderAndWriteResponse() throws Exception {
        // GIVEN
        given(properties.getMaintainerProperties()).willReturn(maintainerProperties);
        given(response.getWriter()).willReturn(printWriter);
        given(maintainerPropertiesJsonBuilder.buildMaintainerPropertiesJson(maintainerProperties)).willReturn("json");
        // WHEN
        underTest.doPost(request, response);
        // THEN
        verify(response).setContentType("application/json");
        verify(printWriter).write("json");
        verify(printWriter).flush();
        verify(printWriter).close();
    }

}
