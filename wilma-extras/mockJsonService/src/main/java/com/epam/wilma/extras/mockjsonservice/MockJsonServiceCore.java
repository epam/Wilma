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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * The main class of this service.
 *
 * @author tkohegyi
 */
public class MockJsonServiceCore {

    private final Logger logger = LoggerFactory.getLogger(MockJsonServiceCore.class);

    protected String cleanMockContent(HttpServletResponse httpServletResponse) {
        //we should delete all
        MockJsonServiceChecker.clearMockSet();
        String response = getMockContent();
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        return response;
    }

    protected String handlePostMockRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String response = "{ \"status\": \"OK\" }";
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        try {
            String body = httpServletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            JSONObject object = new JSONObject(body);
            if (object.has("add")) { //add a new mock
                addMock(object.getJSONObject("add"), null);
            }
            if (object.has("saveJson")) { //save the mock service into the given resource
                saveJson(object.getString("saveJson"));
            }
            if (object.has("loadJson")) { //load the mock service from the given resource
                loadJson(object.getString("loadJson"));
            }
            if (object.has("saveWilmaConfiguration")) { //save the mock service in Wilma std format
                saveWilmaConfiguration(object.getString("saveWilmaConfiguration"), object.getString("groupName"));
            }
        } catch (JSONException | IOException e) {
            response = "{ \"status\": \"BAD REQUEST\" }";
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.warn("MockJsonService had an incorrect call.", e);
        }
        return response;
    }

    private void addMock(JSONObject object, Integer key) {
        String urlTest = null;
        if (object.has("mockUrl")) {
            urlTest = object.getString("mockUrl");
        }
        String bodyContentTest = null;
        if (object.has("mockBodyContent")) {
            bodyContentTest = object.getString("mockBodyContent");
        }
        String jsonPathTest = null;
        String jsonValueTest = null;
        if (object.has("mockJsonPath")) {
            JSONObject jsonPath = object.getJSONObject("mockJsonPath");
            jsonPathTest = jsonPath.getString("path");
            jsonValueTest = jsonPath.getString("value");
        }
        String json = object.getString("mockAnswer");
        JSONObject answer = new JSONObject(json); // test if it is a valid json
        //we have the data to be stored
        if (!hasProperStringValue(urlTest) && !hasProperStringValue(bodyContentTest) && !hasProperStringValue(jsonPathTest) ) {
            throw new JSONException("Mock condition is missing");
        }
        MockItem mockItem = new MockItem(urlTest, bodyContentTest, jsonPathTest, jsonValueTest, answer);
        MockJsonServiceChecker.addMock(mockItem, key);
    }

    private boolean hasProperStringValue(final String string) {
        return string != null && string.length() > 0;
    }

    protected String getMockContent() {
        JSONObject jsonResponse = new JSONObject("{}");
        JSONArray mockContent = MockJsonServiceChecker.getMockContent();
        jsonResponse.put("mockJsonService", mockContent);
        return jsonResponse.toString();
    }

    private void saveJson(String saveJson) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(saveJson))) {
            String content = getMockContent();
            out.write(content);
        } catch (IOException e) {
            throw e;
        }
    }

    private void loadJson(String loadJson) throws IOException, JSONException {
        String content = new String(Files.readAllBytes(Paths.get(loadJson)));
        JSONObject o = new JSONObject(content);
        JSONArray a = o.getJSONArray("mockJsonService");
        for (Object mock: a) {
            JSONObject m = (JSONObject) mock;
            addMock(m, m.getInt("mockKey"));
        }
    }

    private void saveWilmaConfiguration(String fileName, String groupName) throws IOException {
        JSONArray mockContent = MockJsonServiceChecker.getMockContent();
        JSONArray dialogDescriptors = new JSONArray();
        JSONArray templates = new JSONArray();
        for (Object o : mockContent) {
            JSONObject jo = (JSONObject) o;
            Integer key = jo.getInt("mockKey");
            String templateAnswer = jo.getString("mockAnswer");
            //build condition
            JSONArray and = new JSONArray();
            if (jo.has("mockUrl")) {
                String conditionBody = "\"class\":\"AndUrlPatternChecker\"," +
                        "\"parameters\": [{\"name\": \"URL check\", \"value\": \"" + jo.getString("mockUrl")+ "\"}]";
                JSONObject condition = new JSONObject("{ \"condition\": {" + conditionBody +" } }");
                and.put(condition);
            }
            if (jo.has("mockBodyContent")) {
                String conditionBody = "\"class\":\"AndBodyPatternChecker\"," +
                        "\"parameters\": [{\"name\": \"Body check\", \"value\": \"" + jo.getString("mockBodyContent")+ "\"}]";
                JSONObject condition = new JSONObject("{ \"condition\": {" + conditionBody +" } }");
                and.put(condition);
            }
            if (jo.has("mockJsonPath")) {
                String conditionBody = "\"class\":\"JsonPathChecker\"," +
                        "\"parameters\": [{\"name\": \"jsonPath\", \"value\": \"" +
                        jo.getJSONObject("mockJsonPath").getString("path") + "\"}," +
                        "{\"name\": \"expected\", \"value\": \"" +
                        jo.getJSONObject("mockJsonPath").getString("value") + "\"}]";
                JSONObject condition = new JSONObject("{ \"condition\": {" + conditionBody +" } }");
                and.put(condition);
            }
            JSONObject andCondition = new JSONObject();
            andCondition.put("and", and);
            JSONObject responseDescriptor = new JSONObject("{\"code\": 200, \"delay\": 0, \"templateName\": \"json-answer-" +
                    key + "\", \"mimeType\": \"application/json\"}");
            JSONObject dialogDescriptor = new JSONObject();
            dialogDescriptor.put("name", "Mock - " + key);
            dialogDescriptor.put("conditionDescriptor", andCondition);
            dialogDescriptor.put("responseDescriptor", responseDescriptor);
            dialogDescriptors.put(dialogDescriptor);
            JSONObject template = new JSONObject("{ \"type\": \"json\", \"name\": \"json-answer-" + key + "\"}");
            template.put("resource", jo.getString("mockAnswer"));
            templates.put(template);
        }
        JSONObject stubConfig = new JSONObject();
        if (!dialogDescriptors.isEmpty()) {
            stubConfig.put("dialogDescriptors", dialogDescriptors);
            stubConfig.put("templates", templates);
        }
        stubConfig.put("groupName", groupName);
        JSONObject finalConfig = new JSONObject();
        finalConfig.put("wilmaStubConfiguration", stubConfig);
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            String content = finalConfig.toString(2);
            out.write(content);
        } catch (IOException e) {
            throw e;
        }
    }

}
