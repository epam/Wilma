package com.epam.wilma.browsermob.transformer;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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
import com.epam.wilma.domain.http.header.HttpHeaderChange;
import com.epam.wilma.domain.http.header.HttpHeaderToBeRemoved;
import com.epam.wilma.domain.http.header.HttpHeaderToBeUpdated;
import net.lightbody.bmp.proxy.http.BrowserMobHttpRequest;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the class {@link BrowserMobRequestUpdater}.
 *
 * @author Tunde_Kovacs, Tamas_Kohegyi
 */
public class BrowserMobRequestUpdaterTest {

    @Mock
    private HttpEntityEnclosingRequestBase enclosingRequest;
    @Mock
    private WilmaHttpRequest wilmaHttpRequest;
    @Mock
    private BrowserMobHttpRequest browserMobHttpRequest;
    @Mock
    private HttpRequestBase requestBase;
    @Mock
    private Header header;

    @InjectMocks
    private BrowserMobRequestUpdater underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateRequestShouldCallBrowsermobRequestsMethods() throws URISyntaxException {
        //GIVEN
        URI uri = new URI("MOCK");
        given(browserMobHttpRequest.getMethod()).willReturn(requestBase);
        given(wilmaHttpRequest.getUri()).willReturn(uri);
        String mockID = "WILMA-LOG-MOCK-ID";
        given(wilmaHttpRequest.getWilmaMessageId()).willReturn(mockID);
        //WHEN
        underTest.updateRequest(browserMobHttpRequest, wilmaHttpRequest);
        //THEN
        verify(requestBase).setURI(uri);
    }

    @Test
    public void testUpdateRequestShouldUpdateHeadersAddPart() throws URISyntaxException {
        //GIVEN
        URI uri = new URI("MOCK");
        given(browserMobHttpRequest.getMethod()).willReturn(requestBase);
        given(wilmaHttpRequest.getUri()).willReturn(uri);
        String mockID = "WILMA-LOG-MOCK-ID";
        Map<String, HttpHeaderChange> headerChanges = new HashMap<>();
        HttpHeaderToBeUpdated headerToBeUpdated = new HttpHeaderToBeUpdated("B");
        headerChanges.put("A", headerToBeUpdated);
        given(wilmaHttpRequest.getHeaderChanges()).willReturn(headerChanges);
        given(wilmaHttpRequest.getWilmaMessageId()).willReturn(mockID);
        //WHEN
        underTest.updateRequest(browserMobHttpRequest, wilmaHttpRequest);
        //THEN
        verify(requestBase).addHeader("A", "B");
    }

    @Test
    public void testUpdateRequestShouldUpdateHeadersRemovePart() throws URISyntaxException {
        //GIVEN
        URI uri = new URI("MOCK");
        given(browserMobHttpRequest.getMethod()).willReturn(requestBase);
        given(requestBase.getFirstHeader("A")).willReturn(header);
        given(header.getName()).willReturn("A");
        given(header.getValue()).willReturn("B");
        given(wilmaHttpRequest.getUri()).willReturn(uri);
        String mockID = "WILMA-LOG-MOCK-ID";
        Map<String, HttpHeaderChange> headerChanges = new HashMap<>();
        HttpHeaderToBeRemoved headerToBeRemoved = new HttpHeaderToBeRemoved();
        headerChanges.put("A", headerToBeRemoved);
        given(wilmaHttpRequest.getHeaderChanges()).willReturn(headerChanges);
        given(wilmaHttpRequest.getWilmaMessageId()).willReturn(mockID);
        //WHEN
        underTest.updateRequest(browserMobHttpRequest, wilmaHttpRequest);
        //THEN
        verify(requestBase).removeHeader(header);
    }

    @Test
    public void testUpdateRequestShouldUpdateBodyPart() throws URISyntaxException {
        //GIVEN
        URI uri = new URI("MOCK");
        given(browserMobHttpRequest.getMethod()).willReturn(enclosingRequest);
        String mockID = "WILMA-LOG-MOCK-ID";
        given(wilmaHttpRequest.getWilmaMessageId()).willReturn(mockID);
        given(wilmaHttpRequest.getNewBody()).willReturn("NEW BODY".getBytes());
        given(wilmaHttpRequest.getUri()).willReturn(uri);
        //WHEN
        underTest.updateRequest(browserMobHttpRequest, wilmaHttpRequest);
        //THEN
        verify(enclosingRequest).setEntity((HttpEntity) Matchers.anyObject());
    }

}
