package com.epam.wilma.messagemarker;
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
import com.epam.wilma.messagemarker.configuration.MessageMarkerConfigurationAccess;
import com.epam.wilma.messagemarker.configuration.domain.MessageMarkerRequest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Test class for {@link PropertyBasedMessageMarker}.
 * @author Marton_Sereg
 */
public class PropertyBasedMessageMarkerTest {

    @InjectMocks
    private PropertyBasedMessageMarker underTest;

    @Mock
    private MessageMarkerConfigurationAccess configurationAccess;

    @Mock
    private WilmaHttpRequest request;

    @Mock
    private MessageMarkerRequest messageMarkerRequest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMarkMessageHeaderWhenRequested() {
        // GIVEN
        given(configurationAccess.getProperties()).willReturn(messageMarkerRequest);
        given(messageMarkerRequest.getNeedMessageMarker()).willReturn(true);
        given(request.getWilmaMessageId()).willReturn("blah");
        // WHEN
        underTest.markMessageHeader(request);
        // THEN
        verify(request).addHeaderUpdate(WilmaHttpEntity.WILMA_LOGGER_ID, "blah");
    }

    @Test
    public void testDontMarkMessageHeaderWhenNotRequested() {
        // GIVEN
        given(configurationAccess.getProperties()).willReturn(messageMarkerRequest);
        given(messageMarkerRequest.getNeedMessageMarker()).willReturn(false);
        given(request.getWilmaMessageId()).willReturn("blah");
        // WHEN
        underTest.markMessageHeader(request);
        // THEN
        verifyZeroInteractions(request);
    }

}
