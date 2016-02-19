package com.epam.wilma.service.client;

/*==========================================================================
 Copyright 2013-2016 EPAM Systems

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

import com.epam.wilma.service.application.WilmaApplication;
import com.epam.wilma.service.configuration.LocalhostBlockingConfiguration;
import com.epam.wilma.service.configuration.MessageLoggingConfiguration;
import com.epam.wilma.service.configuration.OperationConfiguration;
import com.epam.wilma.service.configuration.StubConfiguration;
import com.epam.wilma.service.configuration.stub.WilmaStub;
import com.epam.wilma.service.domain.LocalhostControlStatus;
import com.epam.wilma.service.domain.MessageLoggingControlStatus;
import com.epam.wilma.service.domain.OperationMode;
import com.epam.wilma.service.domain.StubConfigOrder;
import com.epam.wilma.service.domain.StubConfigStatus;
import com.epam.wilma.service.resource.Upload;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Properties;

import static com.epam.wilma.service.client.WilmaService.WILMA_HOST_KEY;
import static com.epam.wilma.service.client.WilmaService.WILMA_INTERNAL_PORT_KEY;
import static com.epam.wilma.service.domain.MessageLoggingControlStatus.OFF;
import static com.epam.wilma.service.domain.MessageLoggingControlStatus.ON;
import static com.epam.wilma.service.domain.OperationMode.PROXY;
import static com.epam.wilma.service.domain.OperationMode.STUB;
import static com.epam.wilma.service.domain.OperationMode.WILMA;
import static com.epam.wilma.service.domain.StubConfigOrder.DOWN;
import static com.epam.wilma.service.domain.StubConfigOrder.UP;
import static com.epam.wilma.service.domain.StubConfigStatus.DISABLED;
import static com.epam.wilma.service.domain.StubConfigStatus.ENABLED;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Unit test for {@link WilmaService}.
 *
 * @author Tamas_Pinter
 *
 */
public class WilmaServiceTest {

    private static final String HOST = "host";
    private static final String PORT = "1";
    private static final String GROUP_NAME = "testGroup";
    private static final String FILE_NAME = "testFile";
    private static final File MOCK_FILE = mock(File.class);

    @Mock
    private WilmaApplication wilmaApplication;

    @Mock
    private MessageLoggingConfiguration messageLoggingConfiguration;

    @Mock
    private OperationConfiguration operationConfiguration;

    @Mock
    private LocalhostBlockingConfiguration localhostBlockingConfiguration;

    @Mock
    private StubConfiguration stubConfiguration;

    @Mock
    private WilmaStub wilmaStub;

    @Mock
    private Upload fileUpload;

    @Spy
    private Properties properties = createWilmaProperties();

    @InjectMocks
    private WilmaService wilmaService;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConfigIsMissing() {
        new WilmaService(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConfigIsInvalid() {
        Properties properties = new Properties();
        properties.put(WILMA_HOST_KEY, HOST);
        properties.put(WILMA_INTERNAL_PORT_KEY, Long.valueOf("1"));

        new WilmaService(properties);
    }

    @Test
    public void testGetActualLoadInformation() {
        wilmaService.getActualLoadInformation();

        verify(wilmaApplication).getActualLoadInformation();
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testShutdownApplication() {
        wilmaService.shutdownApplication();

        verify(wilmaApplication).shutdownApplication();
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testGetMessageLoggingStatus() {
        wilmaService.getMessageLoggingStatus();

        verify(messageLoggingConfiguration).getMessageLoggingStatus();
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @DataProvider(name = "messageLoggingControls")
    public static Object[][] messageLoggingControls() {
        return new Object[][] {{ON}, {OFF}};
    }

    @Test(dataProvider = "messageLoggingControls")
    public void testSetMessageLoggingStatus(MessageLoggingControlStatus control) {
        wilmaService.setMessageLoggingStatus(control);

        verify(messageLoggingConfiguration).setMessageLoggingStatus(control);
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testGetOperationMode() {
        wilmaService.getOperationMode();

        verify(operationConfiguration).getOperationMode();
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @DataProvider(name = "operationModes")
    public static Object[][] operationModes() {
        return new Object[][] {{WILMA}, {STUB}, {PROXY}};
    }

    @Test(dataProvider = "operationModes")
    public void testSetOperationMode(OperationMode mode) {
        wilmaService.setOperationMode(mode);

        verify(operationConfiguration).setOperationMode(mode);
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testGetLocalhostBlockingStatus() {
        wilmaService.getLocalhostBlockingStatus();

        verify(localhostBlockingConfiguration).getLocalhostBlockingStatus();
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @DataProvider(name = "localhostControls")
    public static Object[][] localhostControls() {
        return new Object[][] {{LocalhostControlStatus.ON}, {LocalhostControlStatus.OFF}};
    }

    @Test(dataProvider = "localhostControls")
    public void testSetOperationMode(LocalhostControlStatus control) {
        wilmaService.setLocalhostBlockingStatus(control);

        verify(localhostBlockingConfiguration).setLocalhostBlockingStatus(control);
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testGetStubConfigInformation() {
        wilmaService.getStubConfigInformation();

        verify(stubConfiguration).getStubConfigInformation();
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @DataProvider(name = "stubConfigStatus")
    public static Object[][] stubConfigStatus() {
        return new Object[][] {{ENABLED}, {DISABLED}};
    }

    @Test(dataProvider = "stubConfigStatus")
    public void testChangeStubConfigStatus(StubConfigStatus status) {
        wilmaService.changeStubConfigStatus(GROUP_NAME, status);

        verify(stubConfiguration).setStubConfigStatus(GROUP_NAME, status);
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @DataProvider(name = "stubConfigOrders")
    public static Object[][] stubConfigOrders() {
        return new Object[][] {{UP}, {DOWN}};
    }

    @Test(dataProvider = "stubConfigOrders")
    public void testChangeStubConfigStatus(StubConfigOrder order) {
        wilmaService.changeStubConfigOrder(GROUP_NAME, order);

        verify(stubConfiguration).setStubConfigOrder(GROUP_NAME, order);
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testDropStubConfig() {
        wilmaService.dropStubConfig(GROUP_NAME);

        verify(stubConfiguration).dropStubConfig(GROUP_NAME);
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testPersistActualStubConfig() {
        wilmaService.persistActualStubConfig();

        verify(stubConfiguration).persistActualStubConfig();
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testGetVersionInformation() {
        wilmaService.getVersionInformation();

        verify(wilmaApplication).getVersionInformation();
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testCallGetService() {
        wilmaService.callGetService("blah");

        verify(wilmaApplication).callGetService("blah");
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testUploadConditionChecker() {
        wilmaService.uploadConditionChecker(FILE_NAME, MOCK_FILE);

        verify(fileUpload).uploadConditionChecker(FILE_NAME, MOCK_FILE);
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testUploadTemplate() {
        wilmaService.uploadTemplate(FILE_NAME, MOCK_FILE);

        verify(fileUpload).uploadTemplate(FILE_NAME, MOCK_FILE);
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testUploadTemplateFormatter() {
        wilmaService.uploadTemplateFormatter(FILE_NAME, MOCK_FILE);

        verify(fileUpload).uploadTemplateFormatter(FILE_NAME, MOCK_FILE);
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testUploadStubConfigurationFile() {
        wilmaService.uploadStubConfiguration(FILE_NAME, MOCK_FILE);

        verify(fileUpload).uploadStubConfiguration(FILE_NAME, MOCK_FILE);
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testUploadStubConfigurationString() {
        given(wilmaStub.toString()).willReturn(FILE_NAME);
        wilmaService.uploadStubConfiguration(wilmaStub);

        verify(fileUpload).uploadStubConfiguration(wilmaStub);
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    private Properties createWilmaProperties() {
        Properties properties = new Properties();
        properties.put(WILMA_HOST_KEY, HOST);
        properties.put(WILMA_INTERNAL_PORT_KEY, PORT);
        return properties;
    }

}
