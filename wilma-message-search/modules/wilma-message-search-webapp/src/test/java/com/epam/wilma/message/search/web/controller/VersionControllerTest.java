package com.epam.wilma.message.search.web.controller;
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

import static org.mockito.BDDMockito.given;
import static org.testng.AssertJUnit.assertEquals;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.message.search.web.support.VersionTitleProvider;

/**
 * Unit test for {@link VersionController}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class VersionControllerTest {

    @Mock
    private VersionTitleProvider titleProvider;

    @InjectMocks
    private VersionController underTest;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetVersionShouldReturnVersionResponse() {
        //GIVEN
        given(titleProvider.getVersionTitle()).willReturn("version");
        //WHEN
        ResponseEntity<String> result = underTest.getVersion();
        //THEN
        assertEquals(result.getStatusCode(), HttpStatus.CREATED);
        assertEquals("{\"messageSearchVersion\":\"version\"}", result.getBody());
        assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType());
    }

}
