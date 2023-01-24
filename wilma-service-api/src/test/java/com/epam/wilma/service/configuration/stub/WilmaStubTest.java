package com.epam.wilma.service.configuration.stub;
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

import com.epam.wilma.service.configuration.stub.helper.common.ConfigurationParameter;
import com.epam.wilma.service.configuration.stub.helper.common.ConfigurationParameterArray;
import com.epam.wilma.service.configuration.stub.helper.common.StubConfigurationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class for WilmaStub class.
 * The key point is to create a valid Stub Configuration.
 * If it is composed incorrectly, the build() method will throws StubConfigurationException
 *
 * @see StubConfigurationException
 *
 * @author Tamas_Kohegyi
 */
public class WilmaStubTest {

    private WilmaStub wilmaStub;

    @Test
    public void testCreateStubAbsoluteMinimal() throws StubConfigurationException {
        //given
        wilmaStub = new WilmaStubBuilder().build();
        //when
        //then
        //if we are here, we are fine
    }

    @Test
    public void testCreateStubMinimal() throws StubConfigurationException {
        //given
        wilmaStub = new WilmaStubBuilder("myGroup")
                .forRequestsLike().comingFrom("localhost")
                .willRespondWith().plainTextResponse("blah")
                .build();
        //when
        //then
        //if we are here, we are fine
    }

    @Test
    public void testResponseFormatterBasic() throws StubConfigurationException {
        //given, when and then
        ConfigurationParameter[] formatterParameters = new ConfigurationParameter[1];
        formatterParameters[0] = new ConfigurationParameter("fromtext", "totext");
        ConfigurationParameterArray configurationParameterArray = new ConfigurationParameterArray(formatterParameters);
        wilmaStub = new WilmaStubBuilder()
                .forAnyRequest()
                .plainTextResponse("{ \"ERROR\":\"fromtext\" }").withStatus(404).withDelay(1000)
                .applyFormatter("JsonTemplateFormatter")
                .build();
    }

    @Test
    public void testResponseFormatter() throws StubConfigurationException {
        //given, when and then
        ConfigurationParameter[] formatterParameters = new ConfigurationParameter[1];
        formatterParameters[0] = new ConfigurationParameter("fromtext", "totext");
        ConfigurationParameterArray configurationParameterArray = new ConfigurationParameterArray(formatterParameters);
        wilmaStub = new WilmaStubBuilder()
                .forAnyRequest()
                .plainTextResponse("{ \"ERROR\":\"fromtext\" }").withStatus(404).withDelay(1000)
                .applyFormatter("StringReplaceResponseFormatter", configurationParameterArray)
                .applyFormatter("JsonTemplateFormatter")
                .build();
    }

    @Test
    public void testCreateStubExtremelyComplexA() throws StubConfigurationException {
        //given, when and then
        ConfigurationParameter[] formatterParameters = new ConfigurationParameter[1];
        formatterParameters[0] = new ConfigurationParameter("fromtext", "totext");
        ConfigurationParameterArray configurationParameterArray = new ConfigurationParameterArray(formatterParameters);
        wilmaStub = new WilmaStubBuilder()
                .forRequestsLike()
                .orStart()
                  .comingFrom("localhost")
                .orEnd()
                .willRespondWith().plainTextResponse("{ \"ERROR\":\"fromtext\" }").withStatus(404).withDelay(1000)
                .applyFormatter("StringReplaceTemplateFormatter", configurationParameterArray).applyFormatter("JsonTemplateFormatter")
                .build();
    }

    @Test
    public void testCreateStubExtremelyComplexB() throws StubConfigurationException {
        //given, when and then
        ConfigurationParameter[] formatterParameters = new ConfigurationParameter[1];
        formatterParameters[0] = new ConfigurationParameter("fromtext", "totext");
        ConfigurationParameterArray configurationParameterArray = new ConfigurationParameterArray(formatterParameters);
        wilmaStub = new WilmaStubBuilder()
                .forRequestsLike()
                .orStart()
                  .comingFrom("localhost")
                  .comingFrom("192.168.0.1")
                .orEnd()
                .willRespondWith().plainTextResponse("{ \"ERROR\":\"fromtext\" }").withStatus(404).withDelay(1000)
                .applyFormatter("StringReplaceTemplateFormatter", configurationParameterArray)
                .applyFormatter("JsonTemplateFormatter")
                .build();
    }

    @Test
    public void testCreateStubExtremelyComplexC() throws StubConfigurationException {
        //given, when and then
        ConfigurationParameter[] formatterParameters = new ConfigurationParameter[1];
        formatterParameters[0] = new ConfigurationParameter("fromtext", "totext");
        ConfigurationParameterArray configurationParameterArray = new ConfigurationParameterArray(formatterParameters);
        wilmaStub = new WilmaStubBuilder()
                .forRequestsLike()
                .notStart()
                  .orStart()
                    .comingFrom("localhost")
                    .comingFrom("192.168.0.1")
                  .orEnd()
                .notEnd()
                .willRespondWith().plainTextResponse("{ \"ERROR\":\"fromtext\" }").withStatus(404).withDelay(1000)
                .applyFormatter("StringReplaceTemplateFormatter", configurationParameterArray)
                .applyFormatter("JsonTemplateFormatter")
                .build();
    }

    @Test
    public void testCreateStubExtremelyComplexD() throws StubConfigurationException {
        //given, when and then
        ConfigurationParameter[] formatterParameters = new ConfigurationParameter[1];
        formatterParameters[0] = new ConfigurationParameter("fromtext", "totext");
        ConfigurationParameterArray configurationParameterArray = new ConfigurationParameterArray(formatterParameters);
        wilmaStub = new WilmaStubBuilder()
                .forRequestsLike()
                .notStart()
                  .orStart()
                    .andStart().withTextInHeader("blah").withHeader("blah2", "blah2").condition("AlwaysTrueChecker").andEnd()
                    .comingFrom("localhost")
                    .comingFrom("192.168.0.1")
                    .withTextInBody("somethingInBody")
                    .negatedCondition("AlwaysFalseChecker")
                  .orEnd()
                .notEnd()
                .willRespondWith().plainTextResponse("{ \"ERROR\":\"fromtext\" }").withStatus(404).withDelay(1000)
                .applyFormatter("StringReplaceTemplateFormatter", configurationParameterArray)
                .applyFormatter("JsonTemplateFormatter")
                .build();
    }

    @Test
    public void testCreateStubGeneratedResponse() throws StubConfigurationException {
        //given, when and then
        ConfigurationParameter[] configurationParameters = new ConfigurationParameter[1];
        configurationParameters[0] = new ConfigurationParameter("Content-Type", "text/plain");
        ConfigurationParameterArray configurationParameterArray = new ConfigurationParameterArray(configurationParameters);
        wilmaStub = new WilmaStubBuilder()
                .forRequestsLike().condition("HeaderParameterChecker", configurationParameterArray)
                .willRespondWith().generatedResponse("dummy.class").withMimeType("application/xml")
                .build();
    }

    @Test
    public void testCreateStubGeneratedResponseConditionNegated() throws StubConfigurationException {
        //given, when and then
        ConfigurationParameter[] conditionParameters = new ConfigurationParameter[1];
        conditionParameters[0] = new ConfigurationParameter("Content-Type", "text/plain");
        ConfigurationParameterArray configurationParameterArray = new ConfigurationParameterArray(conditionParameters);
        wilmaStub = new WilmaStubBuilder()
                .forRequestsLike().negatedCondition("HeaderParameterChecker", configurationParameterArray)
                .willRespondWith().generatedResponse("dummy.class")
                .build();
    }

    @Test
    public void testCreateStubOrPatternCheckerWithBasicParameter() throws StubConfigurationException {
        //given, when and then
        ConfigurationParameter[] conditionParameters = new ConfigurationParameter[1];
        conditionParameters[0] = new ConfigurationParameter("somewhereinheader");
        ConfigurationParameterArray configurationParameterArray = new ConfigurationParameterArray(conditionParameters);
        wilmaStub = new WilmaStubBuilder()
                .forRequestsLike().condition("OrPatternChecker", configurationParameterArray)
                .willRespondWith().generatedResponse("dummy.class")
                .build();
    }

    @Test
    public void testCreateStubSimpleMock() throws StubConfigurationException {
        //given, when and then
        wilmaStub = new WilmaStubBuilder()
                .forRequestsLike().textInUrl("/blah")
                .willRespondWith().plainTextResponse("body").withStatus(200)
                .build();
    }

    @Test
    public void testCreateStubSimpleMockFromHtmlFile() throws StubConfigurationException {
        //given, when
        wilmaStub = new WilmaStubBuilder()
                .forRequestsLike().textInUrl("/blah")
                .willRespondWith().htmlFileResponse("filename").withStatus(200)
                .build();
        //then
        Assertions.assertTrue(wilmaStub.toString().contains("text/html"), "Bad mime type was set.");
    }

    @Test
    public void testCreateStubSimpleMockFromTextFile() throws StubConfigurationException {
        //given, when
        wilmaStub = new WilmaStubBuilder()
                .forRequestsLike().textInUrl("/blah")
                .willRespondWith().textFileResponse("filename").withStatus(200)
                .build();
        //then
        Assertions.assertTrue(wilmaStub.toString().contains("text/plain"), "Bad mime type was set.");
    }

    @Test
    public void testCreateStubSimpleMockFromJsonFile() throws StubConfigurationException {
        //given, when
        wilmaStub = new WilmaStubBuilder()
                .forRequestsLike().textInUrl("/blah")
                .willRespondWith().jsonFileResponse("filename").withStatus(200)
                .build();
        //then
        Assertions.assertTrue(wilmaStub.toString().contains("application/json"), "Bad mime type was set.");
    }

    @Test
    public void testCreateStubSimpleMockFromXmlFile() throws StubConfigurationException {
        //given, when
        wilmaStub = new WilmaStubBuilder()
                .forRequestsLike().textInUrl("/blah")
                .willRespondWith().xmlFileResponse("filename").withStatus(200)
                .build();
        //then
        Assertions.assertTrue(wilmaStub.toString().contains("application/xml"), "Bad mime type was set.");
        Assertions.assertTrue(!wilmaStub.toString().contains("interceptor"), "Should not contain any interceptor related entry.");
    }

    @Test
    public void testInvalidConfiguration() {
        Assertions.assertThrows(StubConfigurationException.class, () -> {
            //given, when and then
            wilmaStub = new WilmaStubBuilder()
                    .forRequestsLike().textInUrl("/blah")
                    .andStart()
                    .willRespondWith().plainTextResponse("body").withStatus(200)
                    .build();
            //then
            Assertions.fail("We should not reach this.");
        });
    }

    @Test
    public void testCreateStubOfInterceptorA() throws StubConfigurationException {
        //given, when
        ConfigurationParameter[] conditionParameters = new ConfigurationParameter[1];
        conditionParameters[0] = new ConfigurationParameter("somewhereinheader");
        ConfigurationParameterArray configurationParameterArray = new ConfigurationParameterArray(conditionParameters);
        wilmaStub = new WilmaStubBuilder()
                .forRequestsLike().textInUrl("/blah")
                .willRespondWith().xmlFileResponse("filename").withStatus(200)
                .addInterceptor("interceptorName", "interceptorClass")
                .addInterceptor("interceptorName", "interceptorClass", configurationParameterArray)
                .build();
        //then
        Assertions.assertTrue(wilmaStub.toString().contains("application/xml"), "Bad mime type was set.");
    }

    @Test
    public void testCreateStubOfInterceptorB() throws StubConfigurationException {
        //given, when
        ConfigurationParameter[] conditionParameters = new ConfigurationParameter[1];
        conditionParameters[0] = new ConfigurationParameter("somewhereinheader");
        ConfigurationParameterArray configurationParameterArray = new ConfigurationParameterArray(conditionParameters);
        wilmaStub = new WilmaStubBuilder()
                .forRequestsLike().textInUrl("/blah")
                .willRespondWith().xmlFileResponse("filename").withStatus(200)
                .addInterceptor("interceptorName", "interceptorClass", configurationParameterArray)
                .addInterceptor("interceptorName", "interceptorClass")
                .build();
        //then
        Assertions.assertTrue(wilmaStub.toString().contains("application/xml"), "Bad mime type was set.");
    }

    @Test
    public void testCreateStubOfInterceptorMinimalA() throws StubConfigurationException {
        //given, when
        wilmaStub = new WilmaStubBuilder()
                .addInterceptor("interceptorName", "interceptorClass")
                .build();
        //then
        Assertions.assertTrue(wilmaStub.toString().contains("interceptorName"), "Interceptor is missing.");
    }

    @Test
    public void testCreateStubOfInterceptorMinimalB() throws StubConfigurationException {
        //given
        ConfigurationParameter[] conditionParameters = new ConfigurationParameter[1];
        conditionParameters[0] = new ConfigurationParameter("somewhereinheader");
        ConfigurationParameterArray configurationParameterArray = new ConfigurationParameterArray(conditionParameters);
        //when
        wilmaStub = new WilmaStubBuilder()
                .addInterceptor("interceptorName", "interceptorClass", configurationParameterArray)
                .build();
        //then
        Assertions.assertTrue(wilmaStub.toString().contains("interceptorName"), "Interceptor is missing.");
    }

    @Test
    public void testAccessToGroupName() throws StubConfigurationException {
        //given, when
        String groupName = "anything";
        wilmaStub = new WilmaStubBuilder(groupName)
                .forAnyRequest()
                .build();
        //then
        Assertions.assertTrue(wilmaStub.toString().contains(groupName), "Group name is missing");
        Assertions.assertTrue(wilmaStub.getGroupName().contentEquals(groupName), "Getting Group name failed");
    }

    @Test
    public void testStatusMustBeCorrect() {
        Assertions.assertThrows(StubConfigurationException.class, () -> {
            //given, when and then
            wilmaStub = new WilmaStubBuilder()
                    .forAnyRequest()
                    .withStatus(8000)
                    .build();
            //then
            Assertions.fail("We should not reach this.");
        });
    }

    @Test
    public void testDelayMustBePositive() {
        Assertions.assertThrows(StubConfigurationException.class, () -> {
            //given, when and then
            wilmaStub = new WilmaStubBuilder()
                    .forAnyRequest()
                    .withDelay(-1)
                    .build();
            //then
            Assertions.fail("We should not reach this.");
        });
    }

    @Test
    public void testMethodUsage() throws StubConfigurationException {
        //given, when
        wilmaStub = new WilmaStubBuilder()
                .forRequestsLike()
                .orStart()
                .getMethod()
                .putMethod()
                .postMethod()
                .deleteMethod()
                .headMethod()
                .optionsMethod()
                .orEnd()
                .willRespondWith().plainTextResponse("body").withStatus(200)
                .build();
        //then
        //if we are here, we are fine
    }

}
