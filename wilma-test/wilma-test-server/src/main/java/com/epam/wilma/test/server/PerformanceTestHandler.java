package com.epam.wilma.test.server;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 * Handler that is able to handle requests coming to /perfexample with one of the given XMLs in the request body.
 * @author Tunde_Kovacs
 *
 */
public class PerformanceTestHandler extends AbstractHandler {
    private static final String PATH_TO_HANDLE = "/perfexample";

    private final Random random = new Random();

    private final byte[] xmlLargeAsBytes;
    private final byte[] xmlMaxAsBytes;
    private final byte[] xmlMinAsBytes;
    private final byte[] xmlSmallAsBytes;


    /**
     * Constructor reads xml from file.
     * @throws IOException when converting to byte array fails.
     */
    public PerformanceTestHandler() throws IOException {
        InputStream xmlLarge = getXmlFromFile("response_large.fis");
        xmlLargeAsBytes = IOUtils.toByteArray(xmlLarge, xmlLarge.available());

        InputStream xmlMax = getXmlFromFile("response_max.fis");
        xmlMaxAsBytes = IOUtils.toByteArray(xmlMax, xmlMax.available());

        InputStream xmlMin = getXmlFromFile("response_min.fis");
        xmlMinAsBytes = IOUtils.toByteArray(xmlMin, xmlMin.available());

        InputStream xmlSmall = getXmlFromFile("response_small.fis");
        xmlSmallAsBytes = IOUtils.toByteArray(xmlSmall, xmlSmall.available());

    }

    @Override
    public void handle(final String path, final Request baseRequest, final HttpServletRequest request, final HttpServletResponse response)
        throws IOException, ServletException {
        if (PATH_TO_HANDLE.equals(path)) {
            double rnd = random.nextDouble();
            byte[] responseBodyAsBytes;
            if (rnd > 0.98) {
                responseBodyAsBytes = xmlMaxAsBytes;
            } else if (rnd > 0.3) {
                responseBodyAsBytes = xmlLargeAsBytes;
            } else if (rnd > 0.1) {
                responseBodyAsBytes = xmlSmallAsBytes;
            } else {
                responseBodyAsBytes = xmlMinAsBytes;
            }
            response.setContentType("application/fastinfoset");
            response.setCharacterEncoding("UTF-8");
            response.getOutputStream().write(responseBodyAsBytes);
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
            response.getOutputStream().close();
        }
    }

    InputStream getXmlFromFile(final String filename) {
        return this.getClass().getClassLoader().getResourceAsStream(filename);
    }

}
