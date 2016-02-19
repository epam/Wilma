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
import com.epam.wilma.service.domain.WilmaLoadInformation;
import com.epam.wilma.service.domain.WilmaServiceConfig;
import com.epam.wilma.service.resource.Upload;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * The main class of Wilma mock project.
 *
 * @author Tamas_Pinter
 *
 */
public final class WilmaService {
    public static final String WILMA_HOST_KEY = "wilma.host";
    public static final String WILMA_INTERNAL_PORT_KEY = "wilma.port.internal";  //this is the Wilma internal WebApp port
    //public static final String WILMA_EXTERNAL_PORT_KEY = "wilma.port.external";  //this is the Wilma Proxy port

    private static final Logger LOG = LoggerFactory.getLogger(WilmaService.class);

    private WilmaApplication wilmaApplication;
    private MessageLoggingConfiguration messageLoggingConfiguration;
    private OperationConfiguration operationConfiguration;
    private LocalhostBlockingConfiguration localhostBlockingConfiguration;
    private StubConfiguration stubConfiguration;
    private Upload fileUpload;

    /**
     * <p>
     *   Constructor.
     *   The given properties object has to contain the Wilma server
     *   configuration:
     * </p>
     *   <table summary="">
     *     <tr>
     *       <th>Key</th>
     *       <th>Class Type</th>
     *     </tr>
     *     <tr>
     *       <td align="center">wilma.host</td>
     *       <td align="center"><pre>String</pre></td>
     *     </tr>
     *     <tr>
     *       <td align="center">wilma.port</td>
     *       <td align="center"><pre>String|Integer</pre></td>
     *     </tr>
     *   </table>
     * <p>
     *   For properties key you can use {@code WilmaMock.WILMA_HOST_KEY} and
     *   {@code WilmaMock.WILMA_INTERNAL_PORT_KEY} constants.
     * </p>
     *
     * @param properties the Wilma server configuration
     */
    public WilmaService(Properties properties) {
        LOG.debug("Initialize Wilma service.");

        WilmaServiceConfig config = initializeWilmaServiceConfig(properties);
        this.wilmaApplication = new WilmaApplication(config);
        this.messageLoggingConfiguration = new MessageLoggingConfiguration(config);
        this.operationConfiguration = new OperationConfiguration(config);
        this.localhostBlockingConfiguration = new LocalhostBlockingConfiguration(config);
        this.stubConfiguration = new StubConfiguration(config);
        this.fileUpload = new Upload(config);
    }

    /**
     * Gets the version of Wilma.
     *
     * @return with the version information, or null in case of communication problem.
     */
    public String getVersionInformation() {
        LOG.debug("Request application's version information.");

        return wilmaApplication.getVersionInformation();
    }

    /**
     * Gets the actual load information of the application.
     *
     * @return actual load information, or null in case of communication problem.
     */
    public WilmaLoadInformation getActualLoadInformation() {
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
     * @return message logging status, or null in case of communication problem.
     */
    public MessageLoggingControlStatus getMessageLoggingStatus() {
        LOG.debug("Request message logging status.");

        return messageLoggingConfiguration.getMessageLoggingStatus();
    }

    /**
     * Turns on/off the message logging status.
     *
     * @param control on/off
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean setMessageLoggingStatus(MessageLoggingControlStatus control) {
        LOG.debug("Set message logging status to: " + control);

        return messageLoggingConfiguration.setMessageLoggingStatus(control);
    }

    /**
     * Gets the operation mode status.
     *
     * @return operation mode status, or null in case of communication problem.
     */
    public OperationMode getOperationMode() {
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
    public LocalhostControlStatus getLocalhostBlockingStatus() {
        LOG.debug("Request localhost blocking status.");

        return localhostBlockingConfiguration.getLocalhostBlockingStatus();
    }

    /**
     * Turns on/off the localhost blocking.
     *
     * @param control on/off
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean setLocalhostBlockingStatus(LocalhostControlStatus control) {
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
     * Enable/disable the given group.
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

    /**
     * Uploads stub configuration.
     *
     * @param resource the WilmaStub resource itself that is considered as stub configuration
     * @return <tt>true</tt> if the upload request is successful, otherwise return <tt>false</tt>
     */
    public boolean uploadStubConfiguration(WilmaStub resource) {
        LOG.debug("Upload stub configuration.");

        return fileUpload.uploadStubConfiguration(resource);
    }

    /**
     * Calls WIlma special service with get method.
     *
     * @param queryString the query string part of the url
     * @return with an optional JSONObject
     */
    public JSONObject callGetService(String queryString) {
        LOG.debug("Call Wilma special service.");

        return wilmaApplication.callGetService(queryString);
    }

    public void setWilmaApplication(WilmaApplication wilmaApplication) {
        this.wilmaApplication = wilmaApplication;
    }

    public void setMessageLoggingConfiguration(MessageLoggingConfiguration messageLoggingConfiguration) {
        this.messageLoggingConfiguration = messageLoggingConfiguration;
    }

    public void setOperationConfiguration(OperationConfiguration operationConfiguration) {
        this.operationConfiguration = operationConfiguration;
    }

    public void setLocalhostBlockingConfiguration(LocalhostBlockingConfiguration localhostBlockingConfiguration) {
        this.localhostBlockingConfiguration = localhostBlockingConfiguration;
    }

    public void setStubConfiguration(StubConfiguration stubConfiguration) {
        this.stubConfiguration = stubConfiguration;
    }

    public void setFileUpload(Upload fileUpload) {
        this.fileUpload = fileUpload;
    }

    private WilmaServiceConfig initializeWilmaServiceConfig(Properties properties) {
        checkArgument(properties != null, "properties must not be null!");
        return WilmaServiceConfig.getBuilder()
                .withHost(properties.getProperty(WILMA_HOST_KEY))
                .withPort(getWilmaPort(properties))
                .build();
    }

    private Integer getWilmaPort(Properties properties) {
        Integer port = null;

        Object object = properties.get(WILMA_INTERNAL_PORT_KEY);
        if (object instanceof Integer) {
            port = (Integer) object;
        } else if (object instanceof String) {
            port = Integer.valueOf((String) object);
        } else {
            throw new IllegalArgumentException("Wilma port must be String or Integer value.");
        }

        return port;
    }

}
