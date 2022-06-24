package com.epam.wilma.extras.mockjsonservice;

import org.json.JSONObject;

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
public class MockItem {
    public final String urlTest;
    public final String bodyContentTest;
    public final String jsonPathTest;
    public final String jsonValueTest;
    public final JSONObject answer;

    public MockItem(final String urlTest, final String bodyContentTest, final String jsonPathTest, final String jsonValueTest, final JSONObject answer) {
        this.urlTest = urlTest;
        this.bodyContentTest = bodyContentTest;
        this.jsonPathTest = jsonPathTest;
        this.jsonValueTest = jsonValueTest;
        this.answer = answer;
    }

    public JSONObject getJson(Integer key) {
        JSONObject mock = new JSONObject();
        if (urlTest != null) {
            mock.put("mockUrl", urlTest);
        }
        if (bodyContentTest != null) {
            mock.put("mockBodyContent", bodyContentTest);
        }
        if (jsonPathTest != null) {
            JSONObject j = new JSONObject();
            j.put("path", jsonPathTest);
            j.put("value", jsonValueTest);
            mock.put("mockJsonPath", j);
        }
        mock.put("mockAnswer", answer.toString());
        mock.put("mockKey", key);
        return mock;
    }
}
