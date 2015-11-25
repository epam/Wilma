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

import com.epam.wilma.domain.http.WilmaHttpRequest;
import net.lightbody.bmp.proxy.http.BrowserMobHttpRequest;
import org.apache.http.client.methods.HttpRequestBase;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the class {@link BrowserMobRequestUpdater}.
 * @author Tunde_Kovacs
 *
 */
public class BrowserMobRequestUpdaterTest {

    @Mock
    private WilmaHttpRequest wilmaHttpRequest;
    @Mock
    private BrowserMobHttpRequest browserMobHttpRequest;
    @Mock
    private HttpRequestBase requestBase;

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

}
