package com.epam.wilma.mitmproxy.transformer;
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

import com.epam.wilma.domain.http.WilmaHttpResponse;
import com.epam.wilma.domain.http.header.HttpHeaderChange;
import com.epam.wilma.domain.http.header.HttpHeaderToBeRemoved;
import com.epam.wilma.domain.http.header.HttpHeaderToBeUpdated;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import website.magyar.mitm.proxy.http.MitmJavaProxyHttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the class {@link MitmProxyResponseUpdater}.
 *
 * @author Tamas_Kohegyi
 */
public class MitmProxyResponseUpdaterTest {

    @Mock
    private WilmaHttpResponse wilmaHttpResponse;
    @Mock
    private MitmJavaProxyHttpResponse browserMobHttpResponse;
    @Mock
    private HttpResponse httpResponse;
    @Mock
    private Header header;

    @InjectMocks
    private MitmProxyResponseUpdater underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateResponseShouldUpdateHeadersAddPart() {
        //GIVEN
        given(browserMobHttpResponse.getRawResponse()).willReturn(httpResponse);
        String mockID = "WILMA-LOG-MOCK-ID";
        Map<String, HttpHeaderChange> headerChanges = new HashMap<>();
        HttpHeaderToBeUpdated headerToBeUpdated = new HttpHeaderToBeUpdated("B");
        headerChanges.put("A", headerToBeUpdated);
        given(wilmaHttpResponse.getHeaderChanges()).willReturn(headerChanges);
        given(wilmaHttpResponse.getWilmaMessageId()).willReturn(mockID);
        given(wilmaHttpResponse.isVolatile()).willReturn(true);
        //WHEN
        underTest.updateResponse(browserMobHttpResponse, wilmaHttpResponse);
        //THEN
        verify(httpResponse).addHeader("A", "B");
    }

    @Test
    public void testUpdateResponseShouldUpdateHeadersRemovePart() {
        //GIVEN
        given(browserMobHttpResponse.getRawResponse()).willReturn(httpResponse);
        given(httpResponse.getFirstHeader("A")).willReturn(header);
        given(header.getName()).willReturn("A");
        given(header.getValue()).willReturn("B");
        String mockID = "WILMA-LOG-MOCK-ID";
        Map<String, HttpHeaderChange> headerChanges = new HashMap<>();
        HttpHeaderToBeRemoved headerToBeRemoved = new HttpHeaderToBeRemoved();
        headerChanges.put("A", headerToBeRemoved);
        given(wilmaHttpResponse.getHeaderChanges()).willReturn(headerChanges);
        given(wilmaHttpResponse.getWilmaMessageId()).willReturn(mockID);
        given(wilmaHttpResponse.isVolatile()).willReturn(true);
        //WHEN
        underTest.updateResponse(browserMobHttpResponse, wilmaHttpResponse);
        //THEN
        verify(httpResponse).removeHeader(header);
    }

    @Test
    public void testUpdateResponseShouldUpdateBodyPart() throws IOException {
        //GIVEN
        given(browserMobHttpResponse.getRawResponse()).willReturn(httpResponse);
        given(wilmaHttpResponse.getNewBody()).willReturn("NEW BODY".getBytes());
        given(wilmaHttpResponse.isVolatile()).willReturn(true);
        //WHEN
        underTest.updateResponse(browserMobHttpResponse, wilmaHttpResponse);
        //THEN
        verify(browserMobHttpResponse).setBody(any());
    }

    @Test
    public void testUpdateResponseShouldUpdateStatusCodePart() {
        //GIVEN
        given(browserMobHttpResponse.getRawResponse()).willReturn(httpResponse);
        given(wilmaHttpResponse.getStatusCode()).willReturn(0);
        given(wilmaHttpResponse.isVolatile()).willReturn(true);
        //WHEN
        underTest.updateResponse(browserMobHttpResponse, wilmaHttpResponse);
        //THEN
        verify(browserMobHttpResponse.getRawResponse()).setStatusCode(0);
    }

}
