package com.epam.wilma.test.client;

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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bootstraps the client application by loading the properties file, and calling the business logic.
 * @author Marton_Sereg
 *
 */
public class TestClientBootstrap {

    private final Logger logger = LoggerFactory.getLogger(TestClientBootstrap.class);

    /**
     * Bootstraps the client application by loading the properties file, and calling the business logic.
     * @param args the program arguments
     * @param properties where the properties will be loaded
     * @param httpRequestSender the class that sends the request to the server
     */
    public void bootstrap(final String[] args, final Properties properties, final HttpRequestSender httpRequestSender) {
        if (args.length == 2) {
            try {
                if (!args[0].endsWith(".properties")) {
                    logger.error("First argument should be a properties file!");
                } else if ((!args[1].endsWith(".xml")) && (!args[1].endsWith(".fis") && (!args[1].endsWith(".json")) && (!args[1].endsWith(".txt")))) {
                    logger.error("Second argument should be an xml, json, fastinfoset or txt file!");
                } else {
                    properties.load(getFileInputStream(args[0]));
                    String testServerUrl = properties.getProperty("testserver.url");
                    Boolean useProxy = Boolean.valueOf(properties.getProperty("client.proxy"));
                    String wilmaHost = properties.getProperty("wilma.host");
                    Integer wilmaPort = Integer.parseInt(properties.getProperty("wilma.port"));
                    String contentType = properties.getProperty("content.type");
                    String acceptHeader = properties.getProperty("accept.header");
                    String contentEncoding = properties.getProperty("content.encoding");
                    String acceptEncoding = properties.getProperty("accept.encoding");
                    boolean isEndlessLoop = properties.getProperty("endless.loop").equalsIgnoreCase("true");
                    long pauseBetweenRequests = Long.parseLong(properties.getProperty("pause.time"));
                    Integer requestBufferSize = Integer.parseInt(properties.getProperty("http.socket.sendbuffer"));
                    Integer responseBufferSize = Integer.parseInt(properties.getProperty("http.socket.receivebuffer"));
                    do {
                        RequestParameters requestParameters = new RequestParameters().testServerUrl(testServerUrl).wilmaHost(wilmaHost)
                                .wilmaPort(wilmaPort).xmlIS(new FileInputStream(args[1])).contentType(contentType).acceptHeader(acceptHeader)
                                .contentEncoding(contentEncoding).acceptEncoding(acceptEncoding);
                        TestClientParameters clientParameters = new TestClientParameters().useProxy(useProxy).allowResponseLogging(!isEndlessLoop)
                                .requestBufferSize(requestBufferSize).responseBufferSize(responseBufferSize);
                        httpRequestSender.callWilmaTestServer(requestParameters, clientParameters);
                        try {
                            Thread.sleep(pauseBetweenRequests);
                        } catch (InterruptedException e) {
                            logger.error("InterruptedException raised in endless loop.", e);
                        }
                    } while (isEndlessLoop);
                }
            } catch (FileNotFoundException e) {
                logger.error("Specified property file not found!", e);
            } catch (NumberFormatException e) {
                logger.error("wilma.port property cannot be read. " + e.getMessage());
            } catch (IOException e) {
                logger.error("Property file cannot be read." + e.getMessage());
            } catch (SystemException e) {
                e.logStackTrace(logger);
            }
        } else {
            logger.error("Exactly two program arguments expected (properties file and the xml to send).");
        }

    }

    private InputStream getFileInputStream(final String filename) throws FileNotFoundException {
        return new FileInputStream(filename);
    }

}
