package com.epam.wilma.gepard.test.stubconfig;

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

import com.epam.gepard.annotations.TestClass;
import com.epam.wilma.gepard.WilmaTestCase;
import com.epam.wilma.gepard.testclient.RequestParameters;
import com.epam.wilma.gepard.testclient.ResponseHolder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Saves the actual stub configuration into a file.
 *
 * @author Tunde_Kovacs
 */
@TestClass(id = "StubConfig", name = "Save")
public class SaveStubConfig extends WilmaTestCase {

    private static final String FILE_DELETION_EXCEPTION_TEMPLATE = "Can't delete file: '%s'";
    private static final String EXAMPLE_2 = "resources/example2.xml";
    private static final String STUB_CONFIG_PATH = "resources/savedStubConfig.json";

    private final Logger logger = LoggerFactory.getLogger(SaveStubConfig.class);

    /**
     * Saves the actual Stub Config that belongs to the Default group.
     * This test runs as first test in the functional test pack, and the last test will restore this.
     *
     * @throws Exception in case of problem
     */
    @Test
    public void testSaveStubConfig() throws Exception {
        RequestParameters requestParameters = createRequestParameters();
        ResponseHolder result = callWilmaWithPostMethod(requestParameters);
        logComment("Actual Stub Config Arrived");
        saveInFile(result.getResponseMessage());
        setMessageMarkingTo("off");
    }

    /**
     * Prepares request parameter to get the default stub descriptor configuration from Wilma.
     * Note: using Get instead of Post is recommended.
     *
     * @return with the prepared request
     * @throws FileNotFoundException in case of error.
     */
    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = getWilmaInternalUrl() + "config/public/stub/stubconfig.json?groupname=Default";
        String wilmaHost = getTestClassExecutionData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getTestClassExecutionData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "json";
        String acceptHeader = "json";
        String contentEncoding = "";
        String acceptEncoding = "";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(false).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .requestInputStream(new FileInputStream(EXAMPLE_2)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding);
    }

    private void saveInFile(final String content) throws IOException {
        Path path = FileSystems.getDefault().getPath(STUB_CONFIG_PATH);
        logComment("Path used: " + path);
        try {
            Files.delete(path);
        } catch (NoSuchFileException e) {
            logger.debug(String.format(FILE_DELETION_EXCEPTION_TEMPLATE, path), e);
        }
        Files.write(path, content.getBytes(), StandardOpenOption.CREATE);

    }
}
