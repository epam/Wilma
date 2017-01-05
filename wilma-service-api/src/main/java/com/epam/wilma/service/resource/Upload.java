package com.epam.wilma.service.resource;

/*==========================================================================
 Copyright 2013-2017 EPAM Systems

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
import java.util.Map;

import com.epam.wilma.service.configuration.stub.WilmaStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.wilma.service.domain.WilmaServiceConfig;
import com.epam.wilma.service.http.WilmaHttpClient;
import com.epam.wilma.service.util.UrlBuilderUtils;
import com.google.common.collect.ImmutableMap;

/**
 * Collects the resource upload commands.
 *
 * @author Tamas_Pinter
 *
 */
public class Upload {
    private static final Logger LOG = LoggerFactory.getLogger(Upload.class);

    private static final String CONDITION_CHECKER_UPLOAD_URL_POSTFIX = "config/admin/stub/conditionchecker";
    private static final String STUB_CONFIGURATION_UPLOAD_URL_POSTFIX = "config/admin/stub/stubconfig";
    private static final String TEMPLATE_UPLOAD_URL_POSTFIX = "config/admin/stub/templates";
    private static final String TEMPLATE_FORMATTER_UPLOAD_URL_POSTFIX = "config/admin/stub/templateformatter";
    private static final String FILE_NAME = "fileName";
    private static final String FILE_FROM_STRING = "directUpload";

    private WilmaHttpClient wilmaClient;
    private WilmaServiceConfig config;

    /**
     * Constructor.
     *
     * @param config the Wilma server configuration
     */
    public Upload(WilmaServiceConfig config) {
        this(config, null);
    }

    /**
     * Constructor.
     *
     * @param config the Wilma server configuration
     * @param client the Wilma HTTP client
     */
    public Upload(WilmaServiceConfig config, WilmaHttpClient client) {
        checkArgument(config != null, "config must not be null!");
        this.config = config;
        this.wilmaClient = client == null ? new WilmaHttpClient() : client;
    }

    /**
     * Uploads the given condition checker file.
     *
     * @param fileName name of the given file
     * @param file the condition checker file
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean uploadConditionChecker(String fileName, File file) {
        LOG.debug("Call condition checker upload API.");

        String url = buildUrl(CONDITION_CHECKER_UPLOAD_URL_POSTFIX, ImmutableMap.of(FILE_NAME, fileName));

        return callFileUploadMethod(url, file);
    }

    /**
     * Uploads the given template file.
     *
     * @param fileName name of the given file
     * @param file the template file
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean uploadTemplate(String fileName, File file) {
        LOG.debug("Call template upload API.");

        String url = buildUrl(TEMPLATE_UPLOAD_URL_POSTFIX, ImmutableMap.of(FILE_NAME, fileName));

        return callFileUploadMethod(url, file);
    }

    /**
     * Uploads the given template formatter file.
     *
     * @param fileName name of the given file
     * @param file the template formatter file
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean uploadTemplateFormatter(String fileName, File file) {
        LOG.debug("Call template formatter upload API.");

        String url = buildUrl(TEMPLATE_FORMATTER_UPLOAD_URL_POSTFIX, ImmutableMap.of(FILE_NAME, fileName));

        return callFileUploadMethod(url, file);
    }

    /**
     * Uploads the given stub configuration file.
     *
     * @param fileName name of the given file
     * @param file the stub configuration file
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean uploadStubConfiguration(String fileName, File file) {
        LOG.debug("Call stub configuration upload API.");

        String url = buildUrl(STUB_CONFIGURATION_UPLOAD_URL_POSTFIX, ImmutableMap.of(FILE_NAME, fileName));

        return callFileUploadMethod(url, file);
    }

    /**
     * Uploads the given stub configuration in a form of WilmaStub object.
     *
     * @param resource is the WilmaStub object that holds the stub configuration
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean uploadStubConfiguration(WilmaStub resource) {
        LOG.debug("Call stub configuration upload API.");

        String url = buildUrl(STUB_CONFIGURATION_UPLOAD_URL_POSTFIX, ImmutableMap.of(FILE_NAME, FILE_FROM_STRING));

        return callStringUploadMethod(url, resource.toString());
    }

    private boolean callFileUploadMethod(String url, File file) {
        LOG.debug("Send file upload request to: " + url);

        return wilmaClient.uploadFile(url, file);
    }

    private boolean callStringUploadMethod(String url, String resource) {
        LOG.debug("Send resource upload request to: " + url);

        return wilmaClient.uploadString(url, resource);
    }

    private String buildUrl(String postfix, Map<String, String> params) {
        return UrlBuilderUtils.buildAbsoluteURL(false, config.getHost(), config.getPort().toString(), postfix, params);
    }

}
