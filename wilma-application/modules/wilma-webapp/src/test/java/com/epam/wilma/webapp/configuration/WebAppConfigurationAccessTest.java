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

import com.epam.wilma.properties.PropertyHolder;
import com.epam.wilma.webapp.configuration.domain.PropertyDTO;
import com.epam.wilma.webapp.configuration.domain.ServerProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 * Tests for {@link WebAppConfigurationAccess}.
 *
 * @author Tamas_Bihari
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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
        assertEquals(PROXY_PORT, result.getProxyPort());
        assertEquals(REQUEST_BUFFER_SIZE, result.getRequestBufferSize());
        assertEquals(RESPONSE_BUFFER_SIZE, result.getResponseBufferSize());
    }

    @Test
    public void testLoadPropertiesShouldSetMaintainerProperties() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDTO actual = underTest.getProperties();
        assertEquals(EXPRESSION, actual.getMaintainerProperties().getCronExpression());
        assertEquals(MAINTAINER_METHOD, actual.getMaintainerProperties().getMaintainerMethod());
        assertEquals(FILE_LIMIT, actual.getMaintainerProperties().getFileLimit());
        assertEquals(EXPRESSION, actual.getMaintainerProperties().getTimeLimit());
    }

    @Test
    public void testLoadPropertiesShouldSetReadme() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDTO actual = underTest.getProperties();
        assertEquals(EXPRESSION, actual.getReadme().getUrl());
        assertEquals(EXPRESSION, actual.getReadme().getText());
    }

    @Test
    public void testLoadPropertiesShouldSetFileListJsonProperties() {
        //GIVEN in setUp
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDTO actual = underTest.getProperties();
        assertEquals(MAX_SIZE, actual.getFileListProperties().getMaximumCountOfMessages());
    }

}
