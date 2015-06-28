package com.epam.wilma.mock.client;

/*==========================================================================
 Copyright 2015 EPAM Systems

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

import static com.epam.wilma.mock.client.WilmaMock.WILMA_HOST_KEY;
import static com.epam.wilma.mock.client.WilmaMock.WILMA_PORT_KEY;
import static com.epam.wilma.mock.domain.MessageLoggingControl.OFF;
import static com.epam.wilma.mock.domain.MessageLoggingControl.ON;
import static com.epam.wilma.mock.domain.OperationMode.PROXY;
import static com.epam.wilma.mock.domain.OperationMode.STUB;
import static com.epam.wilma.mock.domain.OperationMode.WILMA;
import static com.epam.wilma.mock.domain.StubConfigOrder.DOWN;
import static com.epam.wilma.mock.domain.StubConfigOrder.UP;
import static com.epam.wilma.mock.domain.StubConfigStatus.DISABLED;
import static com.epam.wilma.mock.domain.StubConfigStatus.ENABLED;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.File;
import java.util.Properties;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.epam.wilma.mock.application.WilmaApplication;
import com.epam.wilma.mock.configuration.LocalhostBlockingConfiguration;
import com.epam.wilma.mock.configuration.MessageLoggingConfiguration;
import com.epam.wilma.mock.configuration.OperationConfiguration;
import com.epam.wilma.mock.configuration.StubConfiguration;
import com.epam.wilma.mock.domain.LocalhostControl;
import com.epam.wilma.mock.domain.MessageLoggingControl;
import com.epam.wilma.mock.domain.OperationMode;
import com.epam.wilma.mock.domain.StubConfigOrder;
import com.epam.wilma.mock.domain.StubConfigStatus;
import com.epam.wilma.mock.resource.Upload;

/**
 * Unit test for {@link WilmaMock}.
 *
 * @author Tamas_Pinter
 *
 */
public class WilmaMockTest {

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
    private Upload fileUpload;

    @Spy
    private Properties properties = createWilmaProperties();

    @InjectMocks
    private WilmaMock wilmaMock;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConfigIsMissing() {
        new WilmaMock(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConfigIsInvalid() {
        Properties properties = new Properties();
        properties.put(WILMA_HOST_KEY, HOST);
        properties.put(WILMA_PORT_KEY, Long.valueOf("1"));

        new WilmaMock(properties);
    }

    @Test
    public void testGetActualLoadInformation() {
        wilmaMock.getActualLoadInformation();

        verify(wilmaApplication).getActualLoadInformation();
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testShutdownApplication() {
        wilmaMock.shutdownApplication();

        verify(wilmaApplication).shutdownApplication();
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testGetMessageLoggingStatus() {
        wilmaMock.getMessageLoggingStatus();

        verify(messageLoggingConfiguration).getMessageLoggingStatus();
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @DataProvider(name = "messageLoggingControls")
    public static Object[][] messageLoggingControls() {
        return new Object[][] {{ON}, {OFF}};
    }

    @Test(dataProvider = "messageLoggingControls")
    public void testSetMessageLoggingStatus(MessageLoggingControl control) {
        wilmaMock.setMessageLoggingStatus(control);

        verify(messageLoggingConfiguration).setMessageLoggingStatus(control);
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testGetOperationMode() {
        wilmaMock.getOperationMode();

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
        wilmaMock.setOperationMode(mode);

        verify(operationConfiguration).setOperationMode(mode);
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testGetLocalhostBlockingStatus() {
        wilmaMock.getLocalhostBlockingStatus();

        verify(localhostBlockingConfiguration).getLocalhostBlockingStatus();
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @DataProvider(name = "localhostControls")
    public static Object[][] localhostControls() {
        return new Object[][] {{LocalhostControl.ON}, {LocalhostControl.OFF}};
    }

    @Test(dataProvider = "localhostControls")
    public void testSetOperationMode(LocalhostControl control) {
        wilmaMock.setLocalhostBlockingStatus(control);

        verify(localhostBlockingConfiguration).setLocalhostBlockingStatus(control);
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testGetStubConfigInformation() {
        wilmaMock.getStubConfigInformation();

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
        wilmaMock.changeStubConfigStatus(GROUP_NAME, status);

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
        wilmaMock.changeStubConfigOrder(GROUP_NAME, order);

        verify(stubConfiguration).setStubConfigOrder(GROUP_NAME, order);
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testDropStubConfig() {
        wilmaMock.dropStubConfig(GROUP_NAME);

        verify(stubConfiguration).dropStubConfig(GROUP_NAME);
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testPersistActualStubConfig() {
        wilmaMock.persistActualStubConfig();

        verify(stubConfiguration).persistActualStubConfig();
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testUploadConditionChecker() {
        wilmaMock.uploadConditionChecker(FILE_NAME, MOCK_FILE);

        verify(fileUpload).uploadConditionChecker(FILE_NAME, MOCK_FILE);
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testUploadTemplate() {
        wilmaMock.uploadTemplate(FILE_NAME, MOCK_FILE);

        verify(fileUpload).uploadTemplate(FILE_NAME, MOCK_FILE);
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testUploadTemplateFormatter() {
        wilmaMock.uploadTemplateFormatter(FILE_NAME, MOCK_FILE);

        verify(fileUpload).uploadTemplateFormatter(FILE_NAME, MOCK_FILE);
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    @Test
    public void testUploadStubConfiguration() {
        wilmaMock.uploadStubConfiguration(FILE_NAME, MOCK_FILE);

        verify(fileUpload).uploadStubConfiguration(FILE_NAME, MOCK_FILE);
        verifyNoMoreInteractions(wilmaApplication, messageLoggingConfiguration, operationConfiguration,
                localhostBlockingConfiguration, stubConfiguration, fileUpload);
    }

    private Properties createWilmaProperties() {
        Properties properties = new Properties();
        properties.put(WILMA_HOST_KEY, HOST);
        properties.put(WILMA_PORT_KEY, PORT);
        return properties;
    }

}
