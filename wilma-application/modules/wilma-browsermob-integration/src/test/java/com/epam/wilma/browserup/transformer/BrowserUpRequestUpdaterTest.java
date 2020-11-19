package com.epam.wilma.browserup.transformer;
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

import com.browserup.bup.util.HttpMessageContents;
import com.browserup.bup.util.HttpMessageInfo;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.http.header.HttpHeaderChange;
import com.epam.wilma.domain.http.header.HttpHeaderToBeRemoved;
import com.epam.wilma.domain.http.header.HttpHeaderToBeUpdated;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
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
 * Provides unit tests for the class {@link BrowserUpRequestUpdater}.
 *
 * @author Tamas_Kohegyi
 */
public class BrowserUpRequestUpdaterTest {

    @Mock
    HttpRequest request;
    @Mock
    HttpMessageContents contents;
    @Mock
    HttpMessageInfo messageInfo;
    @Mock
    HttpHeaders httpHeaders;
    @Mock
    private WilmaHttpRequest wilmaHttpRequest;
    @Mock
    private HttpMethod requestBase;

    @InjectMocks
    private BrowserUpRequestUpdater underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateRequestShouldCallBrowserUpRequestsMethods() throws URISyntaxException {
        //GIVEN
        URI uri = new URI("MOCK");
        given(request.method()).willReturn(requestBase);
        given(wilmaHttpRequest.getUri()).willReturn(uri);
        String mockID = "WILMA-LOG-MOCK-ID";
        given(wilmaHttpRequest.getWilmaMessageId()).willReturn(mockID);
        //WHEN
        underTest.updateRequest(request, contents, messageInfo, wilmaHttpRequest);
        //THEN
        verify(request).setUri(uri.toString());
    }

    @Test
    public void testUpdateRequestShouldUpdateHeadersAddPart() throws URISyntaxException {
        //GIVEN
        URI uri = new URI("MOCK");
        given(wilmaHttpRequest.getUri()).willReturn(uri);
        String mockID = "WILMA-LOG-MOCK-ID";
        given(wilmaHttpRequest.getWilmaMessageId()).willReturn(mockID);
        Map<String, HttpHeaderChange> headerChanges = new HashMap<>();
        HttpHeaderToBeUpdated headerToBeUpdated = new HttpHeaderToBeUpdated("B");
        headerChanges.put("A", headerToBeUpdated);
        given(wilmaHttpRequest.getHeaderChanges()).willReturn(headerChanges);
        given(request.headers()).willReturn(httpHeaders);
        given(httpHeaders.get("A")).willReturn("C");
        //WHEN
        underTest.updateRequest(request, contents, messageInfo, wilmaHttpRequest);
        //THEN
        verify(httpHeaders).add("A", "B");
    }

    @Test
    public void testUpdateRequestShouldUpdateHeadersRemovePart() throws URISyntaxException {
        //GIVEN
        URI uri = new URI("MOCK");
        given(wilmaHttpRequest.getUri()).willReturn(uri);
        String mockID = "WILMA-LOG-MOCK-ID";
        Map<String, HttpHeaderChange> headerChanges = new HashMap<>();
        HttpHeaderToBeRemoved headerToBeRemoved = new HttpHeaderToBeRemoved();
        headerChanges.put("A", headerToBeRemoved);
        given(wilmaHttpRequest.getHeaderChanges()).willReturn(headerChanges);
        given(wilmaHttpRequest.getWilmaMessageId()).willReturn(mockID);
        given(request.headers()).willReturn(httpHeaders);
        given(httpHeaders.get("A")).willReturn("C");
        //WHEN
        underTest.updateRequest(request, contents, messageInfo, wilmaHttpRequest);
        //THEN
        verify(httpHeaders).remove("A");
    }

    @Test
    public void testUpdateRequestShouldUpdateBodyPart() throws URISyntaxException {
        //GIVEN
        URI uri = new URI("MOCK");
        String mockID = "WILMA-LOG-MOCK-ID";
        given(wilmaHttpRequest.getWilmaMessageId()).willReturn(mockID);
        given(wilmaHttpRequest.getNewBody()).willReturn("NEW BODY".getBytes());
        given(wilmaHttpRequest.getUri()).willReturn(uri);
        //WHEN
        underTest.updateRequest(request, contents, messageInfo, wilmaHttpRequest);
        //THEN
        verify(contents).setBinaryContents(Matchers.anyObject());
    }

}
