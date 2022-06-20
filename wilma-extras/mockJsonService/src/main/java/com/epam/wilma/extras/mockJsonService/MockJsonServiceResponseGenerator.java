package com.epam.wilma.extras.mockJsonService;
/*==========================================================================
Copyright since 2022, EPAM Systems

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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatter;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateGenerator;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * This class will generate stub response by using the configured mock.
 *
 * @author tkohegyi
 */
public class MockJsonServiceResponseGenerator implements TemplateGenerator, ResponseFormatter {
    private final Logger logger = LoggerFactory.getLogger(MockJsonServiceResponseGenerator.class);

    @Override
    public byte[] generateTemplate() {
        return new byte[0];
    }

    @Override
    public byte[] formatResponse(WilmaHttpRequest wilmaHttpRequest, HttpServletResponse httpServletResponse,
                                 byte[] bytes, ParameterList parameterList) {
        byte[] newBody;
        String mockInformation = MockJsonServiceChecker.getMock(wilmaHttpRequest);
        if (mockInformation == null) {
            //ups, this may happen when the mock has been deleted after the request has been arrived...
            newBody = "{ \"error\" : \"Cannot find the suitable mock answer.\" }".getBytes(StandardCharsets.UTF_8);
            logger.error("MockJsonService: Response generator did not find proper response for request with code: " + wilmaHttpRequest.getUri().toString());
        } else {
            newBody = mockInformation.getBytes(StandardCharsets.UTF_8);
        }
        return newBody;
    }
}
