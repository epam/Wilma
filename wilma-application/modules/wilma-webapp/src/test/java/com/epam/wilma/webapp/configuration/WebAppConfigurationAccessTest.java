package com.epam.wilma.webapp.configuration;

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
import static org.testng.Assert.assertEquals;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.properties.PropertyHolder;
import com.epam.wilma.webapp.configuration.domain.PropertyDTO;
import com.epam.wilma.webapp.configuration.domain.ServerProperties;

/**
 * Tests for {@link WebAppConfigurationAccess}.
 * @author Tamas_Bihari
 *
 */
public class WebAppConfigurationAccessTest {

    private static final String MAINTAINER_METHOD = "fileLimit";
    private static final String EXPRESSION = "STRING_EXPRESSION";
    private static final Integer FILE_LIMIT = 0;

    private static final int PROXY_PORT = 0;
    private static final int REQUEST_BUFFER_SIZE = 0;
    private static final int RESPONSE_BUFFER_SIZE = 0;
    private static final int MAX_SIZE = 10;

    @InjectMocks
    private WebAppConfigurationAccess underTest;

    @Mock
    private PropertyHolder propertyHolder;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(propertyHolder.getInt("internal.wilma.port")).willReturn(PROXY_PORT);
        given(propertyHolder.get("log.maintainer.cron")).willReturn(EXPRESSION);
        given(propertyHolder.get("log.maintainer.method")).willReturn(MAINTAINER_METHOD);
        given(propertyHolder.getInt("log.maintainer.file.limit")).willReturn(FILE_LIMIT);
        given(propertyHolder.get("log.maintainer.time.limit")).willReturn(EXPRESSION);
        given(propertyHolder.get("wilma.readme.url")).willReturn(EXPRESSION);
        given(propertyHolder.get("wilma.readme.text")).willReturn(EXPRESSION);
        given(propertyHolder.getInt("internal.wilma.request.buffer.size")).willReturn(REQUEST_BUFFER_SIZE);
        given(propertyHolder.getInt("internal.wilma.response.buffer.size")).willReturn(RESPONSE_BUFFER_SIZE);
        given(propertyHolder.getInt("message.log.UI.maxsize")).willReturn(MAX_SIZE);
    }

    @Test
    public void testLoadPropertiesShouldSetJettyServerProperties() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDTO actual = underTest.getProperties();
        ServerProperties result = actual.getServerProperties();
        assertEquals(result.getProxyPort(), PROXY_PORT);
        assertEquals(result.getRequestBufferSize(), REQUEST_BUFFER_SIZE);
        assertEquals(result.getResponseBufferSize(), RESPONSE_BUFFER_SIZE);
    }

    @Test
    public void testLoadPropertiesShouldSetMaintainerProperties() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDTO actual = underTest.getProperties();
        assertEquals(actual.getMaintainerProperties().getCronExpression(), EXPRESSION);
        assertEquals(actual.getMaintainerProperties().getMaintainerMethod(), MAINTAINER_METHOD);
        assertEquals(actual.getMaintainerProperties().getFileLimit(), FILE_LIMIT);
        assertEquals(actual.getMaintainerProperties().getTimeLimit(), EXPRESSION);
    }

    @Test
    public void testLoadPropertiesShouldSetReadme() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDTO actual = underTest.getProperties();
        assertEquals(actual.getReadme().getUrl(), EXPRESSION);
        assertEquals(actual.getReadme().getText(), EXPRESSION);
    }

    @Test
    public void testLoadPropertiesShouldSetFileListJsonProperties() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDTO actual = underTest.getProperties();
        assertEquals(actual.getFileListProperties().getMaximumCountOfMessages(), MAX_SIZE);
    }

}
