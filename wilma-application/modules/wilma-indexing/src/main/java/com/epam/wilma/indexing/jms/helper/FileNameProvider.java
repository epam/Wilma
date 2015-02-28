package com.epam.wilma.indexing.jms.helper;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.LogFilePathProvider;
import com.epam.wilma.common.helper.WilmaConstants;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.http.WilmaHttpResponse;

/**
 * Extracts the name of the file a {@link WilmaHttpRequest}/{@link WilmaHttpResponse} is
 * logged with.
 * @author Tunde_Kovacs
 *
 */
@Component
public class FileNameProvider {

    private static final String REQ_NAME_ENDING = "req.txt";
    private static final String RESP_NAME_ENDING = "resp.txt";

    @Autowired
    private LogFilePathProvider logFilePathProvider;

    /**
     * Extracts the name of the file a {@link WilmaHttpRequest} is logged with.
     * @param request the {@link WilmaHttpRequest} that's file name is returned
     * @return the name of the file with absolute path
     */
    public String getFileName(final WilmaHttpRequest request) {
        String wilmaLoggerId = request.getExtraHeader(WilmaConstants.WILMA_LOGGER_ID.getConstant());
        String path = getPath();
        return path + "/" + wilmaLoggerId + REQ_NAME_ENDING;
    }

    /**
     * Extracts the name of the file a {@link WilmaHttpResponse} is logged with.
     * @param response the {@link WilmaHttpResponse} that's file name is returned
     * @return the name of the file with absolute path
     */
    public String getFileName(final WilmaHttpResponse response) {
        String wilmaLoggerId = response.getRequestHeader(WilmaConstants.WILMA_LOGGER_ID.getConstant());
        String path = getPath();
        return path + "/" + wilmaLoggerId + RESP_NAME_ENDING;
    }

    private String getPath() {
        return logFilePathProvider.getLogFilePath().toAbsolutePath().toString();
    }
}
