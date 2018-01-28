package com.epam.wilma.router.helper;
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
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.http.WilmaHttpRequest;

/**
 * Unit test for {@link LocalhostRequestChecker}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class LocalhostRequestCheckerTest {

    private LocalhostRequestChecker underTest;

    @Mock
    private WilmaHttpRequest request;

    private URI uri;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        underTest = new LocalhostRequestChecker();
    }

    @Test
    public void testCheckConditionShouldReturnTrueWhenRequestHasLocalhostIpUri() throws URISyntaxException {
        //GIVEN
        uri = new URI("http://127.0.0.1:1234/blabla");
        given(request.getUri()).willReturn(uri);
        //WHEN
        boolean actualResult = underTest.checkIfRequestTargetsLocalhost(request);
        //THEN

        assertTrue(actualResult);
    }

    @Test
    public void testCheckConditionShouldReturnTrueWhenRequestHasLocalhostNameUri() throws URISyntaxException {
        //GIVEN
        uri = new URI("http://localhost:1234/blabla");
        given(request.getUri()).willReturn(uri);
        //WHEN
        boolean actualResult = underTest.checkIfRequestTargetsLocalhost(request);
        //THEN
        assertTrue(actualResult);
    }

    @Test
    public void testCheckConditionShouldReturnFalseWhenRequestHasNonLocalhostUri() throws URISyntaxException {
        //GIVEN
        uri = new URI("http://somehostwhichisnotlocal:1234/blabla");
        given(request.getUri()).willReturn(uri);
        //WHEN
        boolean actualResult = underTest.checkIfRequestTargetsLocalhost(request);
        //THEN
        assertFalse(actualResult);
    }

}
