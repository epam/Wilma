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

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;
import java.util.Properties;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.epam.wilma.mock.domain.WilmaMockConfig;
import com.epam.wilma.mock.resource.Upload;

public final class WilmaMock {
    private static final Logger LOG = LoggerFactory.getLogger(WilmaMock.class);

    private static final String WILMA_HOST_KEY = "wilma.host";
    private static final String WILMA_PORT_KEY = "wilma.port";

    private WilmaMockConfig wilmaConfig;
    private WilmaApplication wilmaApplication;
    private MessageLoggingConfiguration messageLoggingConfiguration;
    private OperationConfiguration operationConfiguration;
    private LocalhostBlockingConfiguration localhostBlockingConfiguration;
    private StubConfiguration stubConfiguration;
    private Upload fileUpload;

    public WilmaMock(Properties properties) {
        LOG.debug("Initialize Wilma mock.");

        this.wilmaConfig = initializeMockConfig(properties);
        this.wilmaApplication = new WilmaApplication(wilmaConfig);
        this.messageLoggingConfiguration = new MessageLoggingConfiguration(wilmaConfig);
        this.operationConfiguration = new OperationConfiguration(wilmaConfig);
        this.localhostBlockingConfiguration = new LocalhostBlockingConfiguration(wilmaConfig);
        this.stubConfiguration = new StubConfiguration(wilmaConfig);
        this.fileUpload = new Upload(wilmaConfig);
    }

    /**
     * Gets the actual load information of the application.
     *
     * @return actual load information
     */
    public JSONObject getActualLoadInformation() {
        LOG.debug("Request application's actual load information.");

        return wilmaApplication.getActualLoadInformation();
    }

    /**
     * Shutdown the Wilma application.
     *
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean shutdownApplication() {
        LOG.debug("Shutdown the application.");

        return wilmaApplication.shutdownApplication();
    }

    /**
     * Gets the message logging status.
     *
     * @return message logging status
     */
    public JSONObject getMessageLoggingStatus() {
        LOG.debug("Request message logging status.");

        return messageLoggingConfiguration.getMessageLoggingStatus();
    }

    /**
     * Turns on/off the message logging status.
     *
     * @param control on/off
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean setMessageLoggingStatus(MessageLoggingControl control) {
        LOG.debug("Set message logging status to: " + control);

        return messageLoggingConfiguration.setMessageLoggingStatus(control);
    }

    /**
     * Gets the operation mode status.
     *
     * @return operation mode status
     */
    public JSONObject getOperationMode() {
        LOG.debug("Request operation mode status.");

        return operationConfiguration.getOperationMode();
    }

    /**
     * Switch the operation mode.
     *
     * @param mode wilma/stub/proxy
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean setOperationMode(OperationMode mode) {
        LOG.debug("Set operation mode to: " + mode);

        return operationConfiguration.setOperationMode(mode);
    }

    /**
     * Gets the localhost blocking status.
     *
     * @return localhost blocking status
     */
    public JSONObject getLocalhostBlockingStatus() {
        LOG.debug("Request localhost blocking status.");

        return localhostBlockingConfiguration.getLocalhostBlockingStatus();
    }

    /**
     * Turns on/off the localhost blocking.
     *
     * @param control on/off
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean setLocalhostBlockingStatus(LocalhostControl control) {
        LOG.debug("Set localhost blocking status to: " + control);

        return localhostBlockingConfiguration.setLocalhostBlockingStatus(control);
    }

    /**
     * Gets the stub configuration information.
     *
     * @return stub configuration
     */
    public JSONObject getStubConfigInformation() {
        LOG.debug("Request stub configuration information.");

        return stubConfiguration.getStubConfigInformation();
    }

    /**
     * 
     * @param groupName name of the stub configuration group
     * @param status the new status
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean changeStubConfigStatus(String groupName, StubConfigStatus status) {
        LOG.debug("Set stub configuration status to: {} for: {}", status, groupName);

        return stubConfiguration.setStubConfigStatus(groupName, status);
    }

    /**
     * Sets new order for the given group.
     *
     * @param groupName name of the stub configuration group
     * @param order the new order
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean changeStubConfigOrder(String groupName, StubConfigOrder order) {
        LOG.debug("Set stub configuration order to: {} for: {}", order, groupName);

        return stubConfiguration.setStubConfigOrder(groupName, order);
    }

    /**
     * Drops the given stub configuration.
     *
     * @param groupName name of the stub configuration group
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean dropStubConfig(String groupName) {
        LOG.debug("Drop stub configuration: {}", groupName);

        return stubConfiguration.dropStubConfig(groupName);
    }

    /**
     * Persists the actual stub configuration.
     *
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean persistActualStubConfig() {
        LOG.debug("Persist actual stub configuration.");

        return stubConfiguration.persistActualStubConfig();
    }

    /**
     * Uploads condition checker configuration.
     *
     * @param fileName the name of the file
     * @param file to upload
     * @return <tt>true</tt> if the upload request is successful, otherwise return <tt>false</tt>
     */
    public boolean uploadConditionChecker(String fileName, File file) {
        LOG.debug("Upload condition checker configuration.");

        return fileUpload.uploadConditionChecker(fileName, file);
    }

    /**
     * Uploads template.
     *
     * @param fileName the name of the file
     * @param file to upload
     * @return <tt>true</tt> if the upload request is successful, otherwise return <tt>false</tt>
     */
    public boolean uploadTemplate(String fileName, File file) {
        LOG.debug("Upload template.");

        return fileUpload.uploadTemplate(fileName, file);
    }

    /**
     * Uploads template formatter.
     *
     * @param fileName the name of the file
     * @param file to upload
     * @return <tt>true</tt> if the upload request is successful, otherwise return <tt>false</tt>
     */
    public boolean uploadTemplateFormatter(String fileName, File file) {
        LOG.debug("Upload template formatter.");

        return fileUpload.uploadTemplateFormatter(fileName, file);
    }

    /**
     * Uploads stub configuration.
     *
     * @param fileName the name of the file
     * @param file to upload
     * @return <tt>true</tt> if the upload request is successful, otherwise return <tt>false</tt>
     */
    public boolean uploadStubConfiguration(String fileName, File file) {
        LOG.debug("Upload stub configuration.");

        return fileUpload.uploadStubConfiguration(fileName, file);
    }

    private WilmaMockConfig initializeMockConfig(Properties properties) {
        checkArgument(properties != null, "properties must not be null!");
        return WilmaMockConfig.getBuilder()
                .withHost(properties.getProperty(WILMA_HOST_KEY))
                .withPort(Integer.parseInt(properties.getProperty(WILMA_PORT_KEY)))
                .build();
    }

}
