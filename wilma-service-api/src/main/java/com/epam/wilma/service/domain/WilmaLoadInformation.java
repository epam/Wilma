package com.epam.wilma.service.domain;
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

import org.json.JSONObject;

/**
 * This class hold information related to Wilma application load.
 *
 * @author Tamas_Kohegyi
 */
public class WilmaLoadInformation {
    private Integer deletedFilesCount;
    private Integer countOfMessages;
    private Integer responseQueueSize;
    private Integer loggerQueueSize;

    /**
     * Create a new WilmaLoadInformation object from the respective JSON object.
     * @param jsonObject is the respective JSON object
     */
    public WilmaLoadInformation(final JSONObject jsonObject) {
        deletedFilesCount = (Integer) jsonObject.get("deletedFilesCount");
        countOfMessages = (Integer) jsonObject.get("countOfMessages");
        responseQueueSize = (Integer) jsonObject.get("responseQueueSize");
        loggerQueueSize = (Integer) jsonObject.get("loggerQueueSize");
    }

    public Integer getDeletedFilesCount() {
        return deletedFilesCount;
    }

    public Integer getCountOfMessages() {
        return countOfMessages;
    }

    public Integer getResponseQueueSize() {
        return responseQueueSize;
    }

    public Integer getLoggerQueueSize() {
        return loggerQueueSize;
    }

}
