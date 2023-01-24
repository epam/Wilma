package com.epam.wilma.engine.configuration.parser;
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

import com.epam.wilma.core.configuration.domain.WilmaAdminHostsDTO;
import com.epam.wilma.properties.PropertyHolder;
import com.epam.wilma.webapp.config.servlet.helper.BufferedReaderFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link WilmaAdminHostsFileParser}.
 *
 * @author Adam_Csaba_Kiraly
 */
public class WilmaAdminHostsFileParserTest {
    private static final String WILMA_ADMIN_HOSTS_FILE = "wilma.admin.hosts.file";
    @Mock
    private WilmaAdminHostsDTO wilmaAdminHostsDTO;
    @Mock
    private PropertyHolder propertyHolder;
    @Mock
    private BufferedReaderFactory bufferedReaderFactory;

    @InjectMocks
    private WilmaAdminHostsFileParser underTest;

    @Mock
    private BufferedReader bufferedReader;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testWhenNoFileIsGivenShouldSetWilmaAdminHostsToEmpty() {
        //GIVEN
        List<String> emptyList = new ArrayList<>();
        given(propertyHolder.get(WILMA_ADMIN_HOSTS_FILE)).willReturn("");
        //WHEN
        underTest.parseFile();
        //THEN
        verify(wilmaAdminHostsDTO).setWilmaAdminHosts(emptyList);
    }

    @Test
    public void testWhenNoFileIsGivenShouldDisableSecurity() {
        //GIVEN
        given(propertyHolder.get(WILMA_ADMIN_HOSTS_FILE)).willReturn("");
        //WHEN
        underTest.parseFile();
        //THEN
        verify(wilmaAdminHostsDTO).setSecurityEnabled(false);
    }

    @Test
    public void testWhenPropertyIsMissingShouldDisableSecurity() {
        //GIVEN
        given(propertyHolder.get(WILMA_ADMIN_HOSTS_FILE)).willReturn(null);
        //WHEN
        underTest.parseFile();
        //THEN
        verify(wilmaAdminHostsDTO).setSecurityEnabled(false);
    }

    @Test
    public void testWhenAnExistingFileIsGivenShouldEnableSecurity() throws IOException {
        //GIVEN
        String filePath = "file.txt";
        given(propertyHolder.get(WILMA_ADMIN_HOSTS_FILE)).willReturn(filePath);
        given(bufferedReaderFactory.createBufferedReaderFromFilePath(filePath)).willReturn(bufferedReader);
        given(bufferedReader.ready()).willReturn(false);
        //WHEN
        underTest.parseFile();
        //THEN
        verify(wilmaAdminHostsDTO).setSecurityEnabled(true);
    }

    @Test
    public void testWhenGivenFileDoesNotExistShouldThrowCannotParseExternalResourceException() {
        Assertions.assertThrows(CannotParseExternalResourceException.class, () -> {
            //GIVEN
            String filePath = "file.txt";
            given(propertyHolder.get(WILMA_ADMIN_HOSTS_FILE)).willReturn(filePath);
            given(bufferedReaderFactory.createBufferedReaderFromFilePath(filePath)).willThrow(
                    new CannotParseExternalResourceException("error", new IOException()));
            //WHEN
            underTest.parseFile();
            //THEN
            verify(wilmaAdminHostsDTO, never()).setSecurityEnabled(Mockito.anyBoolean());
            verify(wilmaAdminHostsDTO, never()).setWilmaAdminHosts(anyList());
        });
    }

    @Test
    public void testWhenAnExistingFileIsGivenShouldSetAdmins() throws IOException {
        //GIVEN
        List<String> wilmaAdminHosts = new ArrayList<>();
        wilmaAdminHosts.add("127.0.0.1");
        wilmaAdminHosts.add("otherhost");
        String filePath = "file.txt";
        given(propertyHolder.get(WILMA_ADMIN_HOSTS_FILE)).willReturn(filePath);
        given(bufferedReaderFactory.createBufferedReaderFromFilePath(filePath)).willReturn(bufferedReader);
        given(bufferedReader.ready()).willReturn(true).willReturn(true).willReturn(false);
        given(bufferedReader.readLine()).willReturn("127.0.0.1").willReturn("otherhost");
        //WHEN
        underTest.parseFile();
        //THEN
        verify(wilmaAdminHostsDTO).setWilmaAdminHosts(wilmaAdminHosts);
    }
}
