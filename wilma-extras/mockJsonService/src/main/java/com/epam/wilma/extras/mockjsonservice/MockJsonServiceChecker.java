package com.epam.wilma.extras.mockjsonservice;
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
import com.jayway.jsonpath.JsonPath;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Condition checker class that is used for example: Mock Json Service.
 * Its main task is to determine if the request is mocked or not.
 * If mocked then the response is available already, then returns true (need to be stubbed).
 * If not mocked, then just return with false.
 *
 * @author Tamas_Kohegyi
 */
public class MockJsonServiceChecker implements ConditionChecker {

    private static final Map<Integer,MockItem> MOCK_ITEM_MAP = new HashMap<>();
    private static Integer idCounter = 0;

    private static final Object GUARD = new Object();

    private static MockItem identifyMock(String url, String bodyContent) {
        MockItem mockItem = null;
        TreeSet<Integer> keySet = new TreeSet<>(MOCK_ITEM_MAP.keySet());
        for (Integer mockItemKey : keySet) {
            MockItem mock = MOCK_ITEM_MAP.get(mockItemKey);
            boolean urlTestPassed;
            if (mock.urlTest != null) {
                urlTestPassed = url.contains(mock.urlTest);
            } else {
                urlTestPassed = true;
            }
            boolean bodyContentTestPassed;
            if (mock.bodyContentTest != null) {
                bodyContentTestPassed = bodyContent.contains(mock.bodyContentTest);
            } else {
                bodyContentTestPassed = true;
            }
            boolean jsonPathTest;
            if (mock.jsonPathTest != null) {
                try {
                    String actualValue = JsonPath.read(bodyContent, mock.jsonPathTest);
                    jsonPathTest = mock.jsonValueTest.equals(actualValue);
                } catch (Exception e) {
                    jsonPathTest = false;
                }
            } else {
                jsonPathTest = true;
            }
            if (urlTestPassed && bodyContentTestPassed && jsonPathTest) {
                mockItem = mock;
                break;
            }
        }
        return mockItem;
    }
    public static String getMock(final WilmaHttpRequest request) {
        String urlTest = request.getUri().toString();
        String bodyContentTest = request.getBody();
        MockItem mockItem;
        synchronized (GUARD) {
            mockItem = identifyMock(urlTest, bodyContentTest);
        }
        String response = null;
        if (mockItem != null) {
            response = mockItem.answer.toString();
        }
        return response;
    }

    public static void addMock(MockItem mockItem, Integer key) {
        if (key != null) {
            idCounter = key;
        }
        synchronized (GUARD) {
            MOCK_ITEM_MAP.put(idCounter++, mockItem);
        }
    }

    public static void clearMockSet() {
        synchronized (GUARD) {
            MOCK_ITEM_MAP.clear();
        }
    }

    public static JSONArray getMockContent() {
        JSONArray array = new JSONArray();
        synchronized (GUARD) {
            if (!MOCK_ITEM_MAP.isEmpty()) {
                TreeSet<Integer> keySet = new TreeSet<>(MOCK_ITEM_MAP.keySet());
                for (Integer mockItemKey : keySet) {
                    MockItem mockItem = MOCK_ITEM_MAP.get(mockItemKey);
                    JSONObject o = mockItem.getJson(mockItemKey);
                    array.put(o);
                }
            }
        }
        return array;
    }

    @Override
    public boolean checkCondition(final WilmaHttpRequest request, final ParameterList parameters) {
        String urlTest = request.getUri().toString();
        String bodyContentTest = request.getBody();
        boolean isMocked; // = false
        synchronized (GUARD) {
            isMocked = identifyMock(urlTest, bodyContentTest) != null;
        }
        return isMocked;
    }

}
