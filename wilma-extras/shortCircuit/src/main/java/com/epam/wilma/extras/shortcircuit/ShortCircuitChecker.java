package com.epam.wilma.extras.shortcircuit;
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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Condition checker class that is used for example: Short Circuit.
 * Its main task is to determine if the request is cached already or not.
 * If cached and the response is available already, then returns true (need to be stubbed).
 * If not cached, then ensure that the request is cached, and prepare catching the response.
 *
 * @author Tamas_Kohegyi
 */
public class ShortCircuitChecker implements ConditionChecker {

    public static final String SHORT_CIRCUIT_HEADER = "Wilma-ShortCircuitId";
    private static final Map<String, ShortCircuitResponseInformation> SHORT_CIRCUIT_MAP = new HashMap<>();
    private static final Object GUARD = new Object();

    private final Logger logger = LoggerFactory.getLogger(ShortCircuitChecker.class);

    public static Map<String, ShortCircuitResponseInformation> getShortCircuitMap() {
        return SHORT_CIRCUIT_MAP;
    }

    public static Object getShortCircuitMapGuard() {
        return GUARD;
    }

    @Override
    public boolean checkCondition(final WilmaHttpRequest request, final ParameterList parameters) {
        boolean conditionResult = false;
        //get the key
        String hashCode = request.getHeaderUpdateValue(ShortCircuitChecker.SHORT_CIRCUIT_HEADER);
        if (hashCode != null) { //if the interceptor did not add the suitable header (or the interceptors are disabled), this can be null
            synchronized (GUARD) {
                //if the request-response pair is in the memory we might need stub response
                if (SHORT_CIRCUIT_MAP.containsKey(hashCode)) {
                    //we need stub answer if the response is arrived already - if not, we need to wait for the answer still
                    conditionResult = SHORT_CIRCUIT_MAP.get(hashCode) != null;
                } else { //we don't have even the request in the map, so put it there
                    SHORT_CIRCUIT_MAP.put(hashCode, null);
                    //CHECKSTYLE OFF - we must use "new String" here
                    String decodedEntryKey = new String(Base64.decodeBase64(hashCode)); //make it human readable
                    //CHECKSTYLE ON
                    logger.info("ShortCircuit: New request to be cached was detected, hash code: " + decodedEntryKey);
                }
            }
        }
        return conditionResult; //true only, if the response is stored, so we know what to answer
    }
}
