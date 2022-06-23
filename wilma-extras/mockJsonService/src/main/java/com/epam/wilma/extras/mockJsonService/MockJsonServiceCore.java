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

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The main class of this service.
 *
 * @author tkohegyi
 */
public class MockJsonServiceCore {
    private static final Map<String, String> MOCK_JSON_SERVICE_MAP = MockJsonServiceChecker.getMockMap();

    protected String cleanMockContent(HttpServletResponse httpServletResponse) {
        //we should delete all
        //invalidate map (remove all from map) (circuits + delete)
        MOCK_JSON_SERVICE_MAP.clear();
        String response = getMockContent(httpServletResponse);
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        return response;
    }

    protected String addMock(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String response = "{ \"status\": \"OK\" }";
        try {
            String body = httpServletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            JSONObject object = new JSONObject(body);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            String mockUrl = object.getString("mockUrl");
            String json = object.getString("mockAnswer");
            new JSONObject(json); // test if it is a valid json
            if (mockUrl == null || mockUrl.length() == 0) {
                throw new JSONException("MockUrl is invalid");
            }
            MockJsonServiceChecker.addMock(mockUrl, json);
        } catch (JSONException | IOException e) {
            response = "{ \"status\": \"BAD REQUEST\" }";
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return response;
    }

    protected String getMockContent(HttpServletResponse httpServletResponse) {
        StringBuilder response = new StringBuilder("{\n  \"mockJsonService\": [\n");
        if (!MOCK_JSON_SERVICE_MAP.isEmpty()) {
            String[] keySet = MOCK_JSON_SERVICE_MAP.keySet().toArray(new String[MOCK_JSON_SERVICE_MAP.size()]);
            for (int i = 0; i < keySet.length; i++) {
                String entryKey = keySet[i];
                String info = escape(MOCK_JSON_SERVICE_MAP.get(entryKey));
                response.append("    { \"mockUrl\": \"").append(entryKey)
                        .append("\", \"mockAnswer\": \"").append(info)
                        .append("\" }");
                if (i < keySet.length - 1) {
                    response.append(",");
                }
                response.append("\n");
            }
        }
        response.append("  ]\n}\n");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        return response.toString();
    }

    /**
     * escape()
     *
     * Escape a give String to make it safe to be printed or stored.
     *
     * @param s The input String.
     * @return The output String.
     **/
    private String escape(String s) {
        return s.replace("\\", "\\\\")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\f", "\\f")
                .replace("\'", "\\'")
                .replace("\"", "\\\"");
    }
}
