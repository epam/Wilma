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
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

import java.util.HashMap;
import java.util.Map;

/**
 * Condition checker class that is used for example: Mock Json Service.
 * Its main task is to determine if the request is mocked or not.
 * If mocked then the response is available already, then returns true (need to be stubbed).
 * If not mocked, then just return with false.
 *
 * @author Tamas_Kohegyi
 */
public class MockJsonServiceChecker implements ConditionChecker {

    private static final Map<String, String> MOCK_JSON_SERVICE_MAP = new HashMap<>();
    private static final Object GUARD = new Object();

    public static String getMock(final WilmaHttpRequest request) {
        String test = request.getUri().toString();
        String response = null;
        if (MOCK_JSON_SERVICE_MAP.containsKey(test)) {
            response = MOCK_JSON_SERVICE_MAP.get(test);
        }
        return response;
    }

    public static Map<String, String> getMockMap() {
        return MOCK_JSON_SERVICE_MAP;
    }

    public static void addMock(String mockUrl, String json) {
        synchronized (GUARD) {
            MOCK_JSON_SERVICE_MAP.put(mockUrl, json);
        }
    }

    @Override
    public boolean checkCondition(final WilmaHttpRequest request, final ParameterList parameters) {
        String test = request.getUri().toString();
        boolean isMocked; // = false
        synchronized (GUARD) {
            isMocked = MOCK_JSON_SERVICE_MAP.containsKey(test);
        }
        return isMocked;
    }
}
