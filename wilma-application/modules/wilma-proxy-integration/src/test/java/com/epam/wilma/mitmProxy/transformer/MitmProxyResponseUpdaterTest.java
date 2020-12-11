package com.epam.wilma.mitmProxy.transformer;
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
import net.lightbody.bmp.proxy.http.BrowserMobHttpResponse;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
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
    private BrowserMobHttpResponse browserMobHttpResponse;
    @Mock
    private HttpResponse httpResponse;
    @Mock
    private Header header;

    @InjectMocks
    private MitmProxyResponseUpdater underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateResponseShouldUpdateHeadersAddPart() throws URISyntaxException {
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
    public void testUpdateResponseShouldUpdateHeadersRemovePart() throws URISyntaxException {
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
    public void testUpdateResponseShouldUpdateBodyPart() throws URISyntaxException, IOException {
        //GIVEN
        given(browserMobHttpResponse.getRawResponse()).willReturn(httpResponse);
        given(wilmaHttpResponse.getNewBody()).willReturn("NEW BODY".getBytes());
        given(wilmaHttpResponse.isVolatile()).willReturn(true);
        //WHEN
        underTest.updateResponse(browserMobHttpResponse, wilmaHttpResponse);
        //THEN
        verify(browserMobHttpResponse).setAnswer((byte[]) anyObject());
        verify(httpResponse).setEntity((HttpEntity) anyObject());
    }

}
