package com.epam.wilma.service.unit;
/*==========================================================================
 Copyright 2013-2016 EPAM Systems

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

import com.epam.wilma.service.unit.helper.ConfigurationParameter;
import org.testng.annotations.Test;

/**
 * Test class for Stub class.
 *
 * @author Tamas_Kohegyi
 */
public class StubTest {

    private Stub stub;

    @Test
    public void testCreateStubMinimal() {
        stub = new StubConfigurationBuilder()
                .forRequestsLike().comingFrom("localhost")
                .willResponseWith().plainTextResponse("blah")
                .build();
        stub.start();
        //do the test
        stub.stop();
        stub.drop();
    }

    @Test
    public void testCreateStubExtremelyComplex() {
        ConfigurationParameter[] formatterParameters = new ConfigurationParameter[1];
        formatterParameters[0] = new ConfigurationParameter("fromtext", "totext");
        stub = new StubConfigurationBuilder()
                .forRequestsLike()
                .notStart()
                .orStart()
                .andStart().withHeader("blah").withHeader("blah2").condition("AlwaysTrueChecker").andEnd()
                .comingFrom("localhost")
                .comingFrom("192.168.0.1")
                .negatedCondition("AlwaysFalseChecker")
                .orEnd()
                .notEnd()
                .willResponseWith().plainTextResponse("{ \"ERROR\":\"fromtext\" }").withStatus(404)
                .applyFormatter("StringReplaceTemplateFormatter", formatterParameters).applyFormatter("JsonTemplateFormatter")
                .build();
        stub.start();
        //do the test
        stub.stop();
        stub.drop();
    }

    @Test
    public void testCreateStubGeneratedResponse() {
        ConfigurationParameter[] configurationParameters = new ConfigurationParameter[1];
        configurationParameters[0] = new ConfigurationParameter("Content-Type", "text/plain");
        stub = new StubConfigurationBuilder()
                .forRequestsLike().condition("HeaderParameterChecker", configurationParameters)
                .willResponseWith().generatedResponse()
                .build();
        stub.start();
        //do the test
        stub.stop();
        stub.drop();
    }

    @Test
    public void testCreateStubGeneratedResponseConditionNegated() {
        ConfigurationParameter[] conditionParameters = new ConfigurationParameter[1];
        conditionParameters[0] = new ConfigurationParameter("Content-Type", "text/plain");
        stub = new StubConfigurationBuilder()
                .forRequestsLike().negatedCondition("HeaderParameterChecker", conditionParameters)
                .willResponseWith().generatedResponse()
                .build();
        stub.start();
        //do the test
        stub.stop();
        stub.drop();
    }

    @Test
    public void testCreateStubSimpleMock() {
        stub = new StubConfigurationBuilder()
                .forRequestsLike().textInUrl("/blah")
                .willResponseWith().plainTextResponse("body").withStatus(200)
                .build();
        stub.start();
        //do the test
        stub.stop();
        stub.drop();
    }

}
