package com.epam.wilma.gepard.test.helper;
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

import com.epam.gepard.exception.SimpleGepardException;

/**
 * Uploads Wilma resources.
 *
 * @author Tamas Kohegyi
 */
public class WilmaResourceUploaderDecorator extends ResourceUploaderDecorator {

    public static final String STUB_CONFIG_URL = "config/admin/stub/stubconfig";

    private static final String CONDITION_CHECKER_URL = "config/admin/stub/conditionchecker?fileName=";
    private static final String TEMPLATE_URL = "config/admin/stub/template?fileName=";
    private static final String INTERCEPTOR_URL = "config/admin/stub/interceptor?fileName=";
    private static final String JAR_URL = "config/admin/stub/jar?fileName=";

    /**
     * Gets the internal Wilma GUI url.
     *
     * @return with URL to internal wilma, like: http://localhost:1234/
     */
    public String getWilmaInternalUrl() {
        return String.format("http://%s:%s/", getTestClassExecutionData().getEnvironment().getProperty("wilma.host"),
                getTestClassExecutionData().getEnvironment().getProperty("wilma.port.internal"));
    }

    /**
     * Upload Stub Configuration file to Wilma.
     *
     * @param stubConfig is the file
     * @throws Exception if any problem occurs
     */
    public void uploadStubConfigToWilma(final String stubConfig) throws Exception {
        logStep("Upload Stub Configuration to Wilma.");
        uploadResource(getWilmaInternalUrl() + STUB_CONFIG_URL, stubConfig);
    }

    /**
     * Upload Condition Checker class to Wilma.
     *
     * @param fileName is the name how Wilma will store it
     * @param filePath is the file to be uploaded
     */
    public void uploadConditionCheckerToWilma(final String fileName, final String filePath) {
        logStep("Upload Condition Checker Class to Wilma.");
        uploadResourceToWilma(CONDITION_CHECKER_URL, fileName, filePath);
    }

    /**
     * Upload Template file to Wilma.
     *
     * @param fileName is the name how Wilma will store it
     * @param filePath is the file to be uploaded
     */
    public void uploadTemplateToWilma(final String fileName, final String filePath) {
        logStep("Upload Resource File to Wilma.");
        uploadResourceToWilma(TEMPLATE_URL, fileName, filePath);
    }

    /**
     * Upload Jar file to Wilma.
     *
     * @param fileName is the name how Wilma will store it
     * @param filePath is the file to be uploaded
     */
    public void uploadJarToWilma(final String fileName, final String filePath) {
        logStep("Upload Jar File to Wilma.");
        uploadResourceToWilma(JAR_URL, fileName, filePath);
    }

    /**
     * Upload Interceptor class to Wilma.
     *
     * @param fileName is the name how Wilma will store it
     * @param filePath is the file to be uploaded
     */
    public void uploadInterceptorToWilma(final String fileName, final String filePath) {
        logStep("Upload Interceptor Class to Wilma.");
        uploadResourceToWilma(INTERCEPTOR_URL, fileName, filePath);
    }

    /**
     * General purpose resource uploader to Wilma.
     *
     * @param url      is the resource url
     * @param fileName is the name how Wilma will store it
     * @param filePath is the file to be uploaded
     */
    private void uploadResourceToWilma(final String url, final String fileName, final String filePath) {
        try {
            uploadResource(getWilmaInternalUrl() + url + fileName, filePath);
        } catch (Exception e) {
            throw new SimpleGepardException(e.getMessage(), e);
        }
    }

}
