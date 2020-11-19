package com.epam.wilma.browserup.transformer;
/*==========================================================================
Copyright since 2020, EPAM Systems

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
import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.domain.http.header.HttpHeaderChange;
import com.epam.wilma.domain.http.header.HttpHeaderToBeRemoved;
import com.epam.wilma.domain.http.header.HttpHeaderToBeUpdated;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the class {@link BrowserUpResponseUpdater}.
 *
 * @author Tamas_Kohegyi
 */
public class BrowserUpResponseUpdaterTest {

    @Mock
    HttpResponse response;
    @Mock
    HttpMessageContents contents;
    @Mock
    HttpMessageInfo messageInfo;
    @Mock
    HttpHeaders httpHeaders;
    @InjectMocks
    private BrowserUpResponseUpdater underTest;
    @Mock
    private WilmaHttpResponse wilmaHttpResponse;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateResponseShouldUpdateHeadersAddPart() {
        //GIVEN
        given(wilmaHttpResponse.isVolatile()).willReturn(true);
        given(wilmaHttpResponse.getNewBody()).willReturn(null);
        String mockID = "WILMA-LOG-MOCK-ID";
        Map<String, HttpHeaderChange> headerChanges = new HashMap<>();
        HttpHeaderToBeUpdated headerToBeUpdated = new HttpHeaderToBeUpdated("B");
        headerChanges.put("A", headerToBeUpdated);
        given(wilmaHttpResponse.getHeaderChanges()).willReturn(headerChanges);
        given(wilmaHttpResponse.getWilmaMessageId()).willReturn(mockID);
        given(response.headers()).willReturn(httpHeaders);
        given(httpHeaders.get("A")).willReturn("C");
        //WHEN
        underTest.updateResponse(response, contents, messageInfo, wilmaHttpResponse);
        //THEN
        verify(httpHeaders).add("A", "B");
    }

    @Test
    public void testUpdateResponseShouldUpdateHeadersRemovePart() {
        //GIVEN
        given(wilmaHttpResponse.isVolatile()).willReturn(true);
        given(wilmaHttpResponse.getNewBody()).willReturn(null);
        String mockID = "WILMA-LOG-MOCK-ID";
        Map<String, HttpHeaderChange> headerChanges = new HashMap<>();
        HttpHeaderToBeRemoved headerToBeRemoved = new HttpHeaderToBeRemoved();
        headerChanges.put("A", headerToBeRemoved);
        given(wilmaHttpResponse.getHeaderChanges()).willReturn(headerChanges);
        given(wilmaHttpResponse.getWilmaMessageId()).willReturn(mockID);
        given(response.headers()).willReturn(httpHeaders);
        given(httpHeaders.get("A")).willReturn("C");
        //WHEN
        underTest.updateResponse(response, contents, messageInfo, wilmaHttpResponse);
        //THEN
        verify(httpHeaders).remove("A");

    }

    @Test
    public void testUpdateResponseShouldUpdateBodyPart() {
        //GIVEN
        given(wilmaHttpResponse.isVolatile()).willReturn(true);
        given(wilmaHttpResponse.getNewBody()).willReturn("NEW BODY".getBytes());
        //WHEN
        underTest.updateResponse(response, contents, messageInfo, wilmaHttpResponse);
        //THEN
        verify(contents).setBinaryContents(anyObject());
        verify(contents).setTextContents(anyObject());
    }

}
