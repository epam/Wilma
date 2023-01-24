package com.epam.wilma.stubconfig.initializer.template;
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

import com.epam.wilma.common.helper.FileFactory;
import com.epam.wilma.common.helper.FileUtils;
import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.domain.stubconfig.TemporaryStubResourceHolder;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 * Tests for {@link TemplateFileReader} class.
 *
 * @author Tamas_Bihari
 */
public class TemplateFileReaderTest {
    private static final String RESOURCE_NAME = "resource-name";

    @Mock
    private FileFactory fileFactory;
    @Mock
    private FileUtils fileUtils;
    @Mock
    private File file;
    @Mock
    private TemporaryStubResourceHolder stubResourceHolder;
    @Mock
    private StubResourcePathProvider stubResourcePathProvider;
    @Mock
    private Map<String, byte[]> templatesMap;

    @InjectMocks
    private TemplateFileReader underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testReadTemplateShouldReturnWithResourceWhenStubResourceHolderContainsTemplate() {
        //GIVEN
        given(stubResourceHolder.getTemplates()).willReturn(templatesMap);
        given(templatesMap.containsKey(RESOURCE_NAME)).willReturn(true);
        given(templatesMap.get(RESOURCE_NAME)).willReturn(RESOURCE_NAME.getBytes());
        //WHEN
        byte[] actual = underTest.readTemplate(RESOURCE_NAME);
        //THEN
        String a = new String(actual);
        String expected = new String(RESOURCE_NAME.getBytes());
        assertEquals(a, expected);
    }

    @Test
    public void testReadTemplateShouldReturnWithResourceWhenStubResourceHolderDoesNotContainTemplateYet() throws IOException {
        //GIVEN
        given(stubResourceHolder.getTemplates()).willReturn(templatesMap);
        given(templatesMap.containsKey(RESOURCE_NAME)).willReturn(false);
        given(fileFactory.createFile(Mockito.anyString())).willReturn(file);
        given(fileUtils.getFileAsByteArray(file)).willReturn(RESOURCE_NAME.getBytes());
        //WHEN
        byte[] actual = underTest.readTemplate(RESOURCE_NAME);
        //THEN
        Mockito.verify(stubResourceHolder).addTemplate(RESOURCE_NAME, actual);
        String a = new String(actual);
        String expected = new String(RESOURCE_NAME.getBytes());
        assertEquals(a, expected);
    }

    @Test
    public void testReadTemplateShouldThrowExceptionWhenTheSpecifiedTemplateDoesNotExist() {
        Assertions.assertThrows(DescriptorValidationFailedException.class, () -> {
            //GIVEN
            given(stubResourceHolder.getTemplates()).willReturn(templatesMap);
            given(templatesMap.containsKey(RESOURCE_NAME)).willReturn(false);
            given(fileFactory.createFile(Mockito.anyString())).willReturn(file);
            given(fileUtils.getFileAsByteArray(file)).willThrow(new IOException());
            //WHEN
            underTest.readTemplate(RESOURCE_NAME);
            //THEN exception is thrown
        });
    }
}
