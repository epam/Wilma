package com.epam.wilma.extras.reverseProxy;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class holds information about the reverse proxy.
 * <p>
 *
 * @author tkohegyi
 */
public class ReverseProxyInformation {
    private final Logger logger = LoggerFactory.getLogger(ReverseProxyInformation.class);

    private String identifier;
    private String originalTarget;
    private String realTarget;
    private boolean isValid;

    /**
     * Creates a new Reverse Proxy information.
     */
    ReverseProxyInformation(final String identifier, final String originalTarget, final String realTarget) {
        this.identifier = identifier;
        this.originalTarget = originalTarget;
        this.realTarget = realTarget;
        isValid = true;
        if ((identifier == null) || (identifier.length() == 0)) {
            isValid = false;
            logger.error("Reverse Proxy information cannot be created. Error-nous parameter arrived: identifier is not acceptable");
        } else {
            if ((originalTarget == null) || (originalTarget.length() == 0)) {
                isValid = false;
                logger.error("Reverse Proxy information cannot be created. Error-nous originalTarget parameter arrived with identifier: " + identifier);
            }
            if ((realTarget == null) || (realTarget.length() == 0)) {
                isValid = false;
                logger.error("Reverse Proxy information cannot be created. Error-nous realTarget parameter arrived with identifier: " + identifier);
            }
        }
    }

    /**
     * Converts a single ReverseProxyInformation class information to JSON string.
     * @return with the string representation of the Reverse Proxy map in JSON format
     */
    public String toString() {
        String status;
        if (!isValid) {
            status = "{ \"isValid\": false }";
        } else { //valid
            //and the real toString starts here
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("    {\n");
            stringBuilder.append("      \"identifier\": \"").append(identifier).append("\",\n");
            stringBuilder.append("      \"originalTarget\": \"").append(originalTarget).append("\",\n");
            stringBuilder.append("      \"realTarget\": \"").append(realTarget).append("\"\n");
            stringBuilder.append("    }");
            status = stringBuilder.toString();
        }
        return status;
    }

    boolean isValid() {
        return isValid;
    }

    String getIdentifier() {
        return identifier;
    }

    String getOriginalTarget() {
        return originalTarget;
    }

    String getRealTarget() {
        return realTarget;
    }

}
