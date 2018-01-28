package com.epam.wilma.sequence.formatters.helper;
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

import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.http.WilmaHttpResponse;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Unit test for {@link WilmaHttpEntityUtils}.
 *
 * @author Balazs_Berkes
 */
public class WilmaHttpEntityUtilsTest {

    private WilmaHttpEntityUtils underTest;

    @BeforeMethod
    public void setup() {
        underTest = new WilmaHttpEntityUtils();
    }

    @Test
    public void testIsJsonMessage() throws Exception {
        //GIVEN
        //WHEN
        boolean jsonResult = underTest.isJsonMessage(requestOf("{\"property\":\"value\"}"));
        boolean jsonArrayResult = underTest.isJsonMessage(requestOf("{\"list\":[{\"key\":\"value\"},{\"key\":\"value\"}]}"));
        boolean xmlResult = underTest.isJsonMessage(requestOf("<root><child></child></root>"));
        boolean emptyResult = underTest.isJsonMessage(requestOf(""));
        boolean textResult = underTest.isJsonMessage(requestOf("simple text"));
        //THEN
        assertTrue(jsonResult);
        assertTrue(jsonArrayResult);
        assertFalse(xmlResult);
        assertFalse(emptyResult);
        assertFalse(textResult);
    }

    @Test
    public void testIsXmlMessage() throws Exception {
        //GIVEN
        //WHEN
        boolean xmlResult = underTest.isXmlMessage(requestOf("<?xml version=\"1.0\"?><root><child></child></root>"));
        boolean soapResult = underTest.isXmlMessage(requestOf(soapMessage()));
        boolean jsonResult = underTest.isXmlMessage(requestOf("{\"property\":\"value\"}"));
        //THEN
        assertTrue(xmlResult);
        assertTrue(soapResult);
        assertFalse(jsonResult);
    }

    @Test
    public void testIsSoapMessage() throws Exception {
        //GIVEN
        //WHEN
        boolean soapResult = underTest.isSoapMessage(requestOf(soapMessage()));
        boolean jsonResult = underTest.isSoapMessage(requestOf("{\"property\":\"value\"}"));
        boolean xmlResult = underTest.isSoapMessage(requestOf("<root><child></child></root>"));
        //THEN
        assertTrue(soapResult);
        assertFalse(jsonResult);
        assertFalse(xmlResult);
    }

    private String soapMessage() {
        return "<?xml version=\"1.0\"?>"
                + "<soap:Envelope xmlns:soap=\"http://www.w3.org/2001/12/soap-envelope\" soap:encodingStyle=\"http://www.w3.org/2001/12/soap-encoding\">"
                + "<soap:Body xmlns:m=\"http://www.example.org/stock\"><m:GetStockPrice><m:StockName>IBM</m:StockName></m:GetStockPrice>"
                + "</soap:Body></soap:Envelope>";
    }

    private WilmaHttpEntity requestOf(final String body) {
        WilmaHttpEntity entity = new WilmaHttpEntity();
        entity.setBody(body);
        return entity;
    }

    @Test
    public void testGetWilmaMessageIdAtRequest() throws Exception {
        //test the request part
        WilmaHttpRequest request = new WilmaHttpRequest();
        request.setWilmaMessageId("test");
        assertEquals(request.getWilmaMessageId(), "test");
        assertEquals(request.getWilmaMessageLoggerId(), "testreq");
    }

    @Test
    public void testGetWilmaMessageIdAtResponse() throws Exception {
        //test the response part
        WilmaHttpResponse response = new WilmaHttpResponse(false);
        response.setWilmaMessageId("test");
        assertEquals(response.getWilmaMessageId(), "test");
        assertEquals(response.getWilmaMessageLoggerId(), "testresp");
    }

}
