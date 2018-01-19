package com.epam.wilma.properties.validation;
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

import com.epam.wilma.properties.InvalidPropertyException;
import com.epam.wilma.properties.PropertyHolder;
import com.epam.wilma.safeguard.configuration.domain.SafeguardLimits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validates safeguard limits that were introduced for protecting Wilma
 * from performance problems.
 *
 * @author Tunde_Kovacs
 * @author Tamas_Bihari
 */
@Component
public class SafeguardLimitValidator {

    @Autowired
    private PropertyHolder propertyHolder;

    /**
     * Validates the {@link SafeguardLimits} against each other.
     * <ul>
     * <li>FIOffLimit must be larger than FIOnLimit</li>
     * <li>MWOnLimit must be larger than FIOnLimit</li>
     * <li>MWOnLimit must be larger than FIOffLimit</li>
     * <li>MWOffLimit must be larger than MWOnLimit</li>
     * </ul>
     */
    public void validate() {
        Long fiOffLimit = propertyHolder.getLong("safeguard.responseFIdecoder.OFFlimit");
        Long fiOnLimit = propertyHolder.getLong("safeguard.responseFIdecoder.ONlimit");
        Long mwOffLimit = propertyHolder.getLong("safeguard.responseMessageWriter.OFFlimit");
        Long mwOnLimit = propertyHolder.getLong("safeguard.responseMessageWriter.ONlimit");
        String jmxPort = propertyHolder.get("com.sun.management.jmxremote.port");
        checkLimits(new SafeguardLimits(fiOffLimit, fiOnLimit, mwOffLimit, mwOnLimit, jmxPort));
    }

    private void checkLimits(final SafeguardLimits limits) {
        if (limits.hasNullField()) {
            throw new InvalidPropertyException("Safeguard limit can't be null!");
        } else if (limits.getFiOnLimit() > limits.getFiOffLimit()) {
            throw new InvalidPropertyException("Safeguard fastinfoset decompression off limit must be greater than fastinfoset on limit!");
        } else if (limits.getFiOnLimit() > limits.getMwOnLimit()) {
            throw new InvalidPropertyException("Safeguard fastinfoset decompression on limit must be less than message writing on limit!");
        } else if (limits.getFiOffLimit() > limits.getMwOffLimit()) {
            throw new InvalidPropertyException("Safeguard fastinfoset decompression off limit must be less than message writing off limit!");
        } else if (limits.getMwOnLimit() > limits.getMwOffLimit()) {
            throw new InvalidPropertyException("Safeguard message writing on limit must be less than message writing off limit!");
        } else if (limits.getJmxPort() != null) {
            try {
                Integer.parseInt(limits.getJmxPort());
            } catch (NumberFormatException e) {
                throw new InvalidPropertyException("Specified JMX port is unusable!");
            }
        }
    }

}
