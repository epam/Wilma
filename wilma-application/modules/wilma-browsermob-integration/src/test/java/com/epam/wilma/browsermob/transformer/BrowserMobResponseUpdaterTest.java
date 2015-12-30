package com.epam.wilma.browsermob.transformer;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import com.epam.wilma.domain.http.WilmaHttpResponse;
import net.lightbody.bmp.proxy.http.BrowserMobHttpResponse;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the class {@link BrowserMobResponseUpdater}.
 *
 * @author Tamas_Kohegyi
 */
public class BrowserMobResponseUpdaterTest {

    @Mock
    private WilmaHttpResponse wilmaHttpResponse;
    @Mock
    private BrowserMobHttpResponse browserMobHttpResponse;
    @Mock
    private HttpResponse httpResponse;
    @Mock
    private Header header;

    @InjectMocks
    private BrowserMobResponseUpdater underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateResponseShouldUpdateHeadersAddPart() throws URISyntaxException {
        //GIVEN
        Map<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("A", "B");
        URI uri = new URI("MOCK");
        given(browserMobHttpResponse.getRawResponse()).willReturn(httpResponse);
        String mockID = "WILMA-LOG-MOCK-ID";
        given(wilmaHttpResponse.getExtraHeaders()).willReturn(extraHeaders);
        given(wilmaHttpResponse.getWilmaMessageId()).willReturn(mockID);
        //WHEN
        underTest.updateResponse(browserMobHttpResponse, wilmaHttpResponse);
        //THEN
        verify(httpResponse).addHeader("A", "B");
    }

    @Test
    public void testUpdateResponseShouldUpdateHeadersRemovePart() throws URISyntaxException {
        //GIVEN
        Map<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("A", "B");
        given(browserMobHttpResponse.getRawResponse()).willReturn(httpResponse);
        given(httpResponse.getFirstHeader("A")).willReturn(header);
        given(header.getName()).willReturn("A");
        given(header.getValue()).willReturn("B");
        String mockID = "WILMA-LOG-MOCK-ID";
        given(wilmaHttpResponse.getExtraHeadersToRemove()).willReturn(extraHeaders);
        given(wilmaHttpResponse.getWilmaMessageId()).willReturn(mockID);
        //WHEN
        underTest.updateResponse(browserMobHttpResponse, wilmaHttpResponse);
        //THEN
        verify(httpResponse).removeHeader(header);
    }

    @Test
    public void testUpdateResponseShouldUpdateBodyPart() throws URISyntaxException, IOException {
        //GIVEN
        given(browserMobHttpResponse.getRawResponse()).willReturn(httpResponse);
        given(wilmaHttpResponse.getNewBody()).willReturn("NEW BODY");
        //WHEN
        underTest.updateResponse(browserMobHttpResponse, wilmaHttpResponse);
        //THEN
        verify(browserMobHttpResponse).setAnswer((byte[]) anyObject());
        verify(httpResponse).setEntity((HttpEntity) anyObject());
    }

}
