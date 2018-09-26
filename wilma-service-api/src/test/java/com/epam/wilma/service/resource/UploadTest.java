package com.epam.wilma.service.resource;

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
import static org.testng.Assert.assertEquals;

import java.io.File;

import com.epam.wilma.service.configuration.stub.WilmaStub;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.service.domain.WilmaServiceConfig;
import com.epam.wilma.service.http.WilmaHttpClient;

/**
 * Unit test for {@link Upload}.
 *
 * @author Tamas_Pinter
 *
 */
public class UploadTest {

    private static final String HOST = "host";
    private static final Integer PORT = 1;
    private static final String CONDITION_CHECKER_UPLOAD_URL = "http://host:1/config/admin/stub/conditionchecker?fileName=testfile1";
    private static final String STUB_CONFIGURATION_UPLOAD_URL = "http://host:1/config/admin/stub/stubconfig?fileName=testfile1";
    private static final String STUB_CONFIGURATION_UPLOAD_URL_FOR_STRING = "http://host:1/config/admin/stub/stubconfig?fileName=directUpload";
    private static final String TEMPLATE_UPLOAD_URL = "http://host:1/config/admin/stub/templates?fileName=testfile1";
    private static final String RESPONSE_FORMATTER_UPLOAD_URL = "http://host:1/config/admin/stub/responseformatter?fileName=testfile1";
    private static final String FILENAME = "testfile1";

    @Mock
    private WilmaHttpClient client;

    @Mock
    private File testFile;

    @Mock
    private WilmaStub wilmaStub;

    private Upload fileUpload;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);

        WilmaServiceConfig config = createMockConfig();
        fileUpload = new Upload(config, client);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "config must not be null!")
    public void shouldThrowExceptionWhenConfigIsMissing() {
        new Upload(null);
    }

    @Test
    public void shouldCallTheProperConditionCheckerUploadUrlWithTheGivenFile() {
        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<File> file = ArgumentCaptor.forClass(File.class);

        fileUpload.uploadConditionChecker(FILENAME, testFile);

        verify(client).uploadFile(url.capture(), file.capture());

        assertEquals(url.getValue(), CONDITION_CHECKER_UPLOAD_URL);
        assertEquals(file.getValue(), testFile);
    }

    @Test
    public void shouldCallTheProperStubConfigurationUploadUrlWithTheGivenFile() {
        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<File> file = ArgumentCaptor.forClass(File.class);

        fileUpload.uploadStubConfiguration(FILENAME, testFile);

        verify(client).uploadFile(url.capture(), file.capture());

        assertEquals(url.getValue(), STUB_CONFIGURATION_UPLOAD_URL);
        assertEquals(file.getValue(), testFile);
    }

    @Test
    public void shouldCallTheProperStubConfigurationUploadUrlWithTheGivenStringResource() {
        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> resource = ArgumentCaptor.forClass(String.class);
        given(wilmaStub.toString()).willReturn(FILENAME);

        fileUpload.uploadStubConfiguration(wilmaStub);

        verify(client).uploadString(url.capture(), resource.capture());

        assertEquals(url.getValue(), STUB_CONFIGURATION_UPLOAD_URL_FOR_STRING);
        assertEquals(resource.getValue(), FILENAME);
    }

    @Test
    public void shouldCallTheProperTemplateUploadUploadUrlWithTheGivenFile() {
        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<File> file = ArgumentCaptor.forClass(File.class);

        fileUpload.uploadTemplate(FILENAME, testFile);

        verify(client).uploadFile(url.capture(), file.capture());

        assertEquals(url.getValue(), TEMPLATE_UPLOAD_URL);
        assertEquals(file.getValue(), testFile);
    }

    @Test
    public void shouldCallTheProperTemplateFormatterUploadUrlWithTheGivenFile() {
        ArgumentCaptor<String> url = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<File> file = ArgumentCaptor.forClass(File.class);

        fileUpload.uploadTemplateFormatter(FILENAME, testFile);

        verify(client).uploadFile(url.capture(), file.capture());

        assertEquals(url.getValue(), RESPONSE_FORMATTER_UPLOAD_URL);
        assertEquals(file.getValue(), testFile);
    }

    private WilmaServiceConfig createMockConfig() {
        return WilmaServiceConfig.getBuilder()
                .withHost(HOST)
                .withPort(PORT)
                .build();
    }

}
