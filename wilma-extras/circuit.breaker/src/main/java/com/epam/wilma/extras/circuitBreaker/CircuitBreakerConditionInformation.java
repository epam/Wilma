package com.epam.wilma.extras.circuitBreaker;
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

import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class holds information about a single circuitBreaker.
 *
 * @author tkohegyi
 */
class CircuitBreakerConditionInformation {

    private final Logger logger = LoggerFactory.getLogger(CircuitBreakerConditionInformation.class);

    //input settings
    private String identifier;
    private boolean isValid;
    private String path;
    private Integer timeoutInSec;
    private Integer[] successCodes;
    private Integer maxErrorCount;
    //status
    private Integer actualErrorLevel;
    private boolean isActive;
    private Integer timeoutLeftInSec;

    /**
     * Creates a new response information, based on the original response, and specifying a timeout.
     * Timeout value is the system time, when this response become obsolete.
     */
    CircuitBreakerConditionInformation(final String identifier, final ParameterList parameters) {
        this.identifier = identifier;
        //get parameters
        isValid = true;
        //get path
        path = parameters.get("path");
        if (path == null) {
            logger.debug("Circuit Breaker: '"  + identifier + "' has missing 'path' parameter, pls fix.");
            isValid = false;
        }
        //get timeoutInSec
        timeoutInSec = 0;
        String timeoutInSecString = parameters.get("timeoutInSec");
        if (timeoutInSecString != null) {
            try {
                timeoutInSec = Integer.parseInt(timeoutInSecString);
            } catch (NumberFormatException e) {
                logger.debug("Circuit Breaker: '"  + identifier + "' has invalid 'timeoutInSec' parameter, pls fix.");
                isValid = false;
            }
        } else {
            logger.debug("Circuit Breaker: '"  + identifier + "' has missing 'timeoutInSec' parameter, pls fix.");
            isValid = false;

        }
        //get success codes
        parseSuccessCodes(parameters.get("successCodes"));
        //get max error count
        String maxErrorCountString = parameters.get("maxErrorCount");
        if (maxErrorCountString != null) {
            try {
                maxErrorCount = Integer.parseInt(maxErrorCountString);
            } catch (NumberFormatException e) {
                logger.debug("Circuit Breaker: '"  + identifier + "' has invalid 'maxErrorCount' parameter, pls fix.");
                isValid = false;
            }
        } else {
            logger.debug("Circuit Breaker: '"  + identifier + "' has missing 'maxErrorCount' parameter, pls fix.");
            isValid = false;
        }
        //init status
        actualErrorLevel = 0;
        isActive = false;
        timeoutLeftInSec = 0;
    }

    private void parseSuccessCodes(String successCodesString) {
        if (successCodesString != null) {
            String[] codeStrings = successCodesString.split(",");
            successCodes = new Integer[codeStrings.length];
            for (int i = 0; i < codeStrings.length; i++) {
                try {
                    successCodes[i] = Integer.parseInt(codeStrings[i].trim());
                } catch (NumberFormatException e) {
                    logger.debug("Circuit Breaker: '"  + identifier + "' has invalid 'successCodes' parameter, pls fix.");
                    isValid = false;
                }
            }
        } else {
            logger.debug("Circuit Breaker: '"  + identifier + "' has missing 'successCodes' parameter, pls fix.");
            isValid = false;
        }
    }

    public String toString() {
        String status;
        if (!isValid) {
            status = "{ \"isValid\": false }";
        } else { //valid
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{ ");
            stringBuilder.append("\"name\": \"").append(identifier).append("\",\n");
            stringBuilder.append("  \"settings\": {\n");
            stringBuilder.append("    \"path\": \"").append(path).append("\",\n");
            stringBuilder.append("    \"timeoutInSec\": ").append(timeoutInSec).append(",\n");
            stringBuilder.append("    \"successCodes\": [");
            for (int i = 0; i < successCodes.length; i++) {
                stringBuilder.append(successCodes[i]);
                if (i < successCodes.length - 1) {
                    stringBuilder.append(", ");
                }
            }
            stringBuilder.append("],\n");
            stringBuilder.append("    \"maxErrorCount\": ").append(maxErrorCount).append(" },\n");
            stringBuilder.append("  \"status\": {\n");
            stringBuilder.append("    \"isActive\": ").append(isActive).append(",\n");
            stringBuilder.append("    \"actualErrorLevel\": ").append(actualErrorLevel).append(",\n");
            stringBuilder.append("    \"timeoutLeftInSec\": ").append(timeoutLeftInSec).append(" }\n");
            stringBuilder.append("}");
            status = stringBuilder.toString();
        }
        return status;
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getPath() {
        return path;
    }

    public Integer[] getSuccessCodes() {
        return successCodes;
    }
}
