package com.epam.wilma.router;

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

import com.epam.wilma.common.helper.OperationMode;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.StubDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptor;
import com.epam.wilma.router.command.StubDescriptorModificationCommand;
import com.epam.wilma.router.configuration.RouteEngineConfigurationAccess;
import com.epam.wilma.router.configuration.domain.PropertyDTO;
import com.epam.wilma.router.domain.ResponseDescriptorDTO;
import com.epam.wilma.router.evaluation.StubDescriptorEvaluator;
import com.epam.wilma.router.evaluation.StubModeEvaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the {@link RoutingService} class.
 *
 * @author Tunde_Kovacs
 */
public class RoutingServiceTest {

    private static final String MSG_ID = "msgid=\"00001\"";
    private OperationMode operationMode;
    private String requestBody;

    @Mock
    private WilmaHttpRequest request;
    @Mock
    private StubDescriptorEvaluator stubDescriptorEvaluator;
    @Mock
    private StubDescriptor stubDescriptor;
    @Mock
    private ResponseDescriptorDTO responseDescriptorDTO;
    @Mock
    private StubModeEvaluator stubModeEvaluator;
    @Mock
    private RouteEngineConfigurationAccess configurationAccess;
    @Mock
    private PropertyDTO properties;
    private Map<String, StubDescriptor> stubDescriptors;
    @Mock
    private StubDescriptorModificationCommand command;

    @InjectMocks
    private RoutingService underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        requestBody = "";
        stubDescriptors = new LinkedHashMap<>();
        stubDescriptors.put("test", stubDescriptor);
        ReflectionTestUtils.setField(underTest, "stubDescriptors", stubDescriptors);
        ReflectionTestUtils.setField(underTest, "operationMode", null);
    }

    @Test
    public void testRedirectRequestToStubShouldReturnTrueWhenNewResponseDescriptorIsCreated() {
        //GIVEN
        requestBody = MSG_ID;
        given(request.getBody()).willReturn(requestBody);
        given(stubDescriptorEvaluator.findResponseDescriptor(stubDescriptors, request)).willReturn(responseDescriptorDTO);
        //WHEN
        boolean actual = underTest.redirectRequestToStub(request);
        //THEN
        assertTrue(actual);
    }

    @Test
    public void testRedirectRequestToStubShouldReturnFalseWhenResponseDescriptorIsNull() {
        //GIVEN
        given(request.getBody()).willReturn(requestBody);
        given(stubDescriptorEvaluator.findResponseDescriptor(stubDescriptors, request)).willReturn(null);
        //WHEN
        boolean actual = underTest.redirectRequestToStub(request);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testRedirectRequestToStubShouldCallStubModeEvaluatorGetResponseDescriptorForStubModeMethod() {
        //GIVEN
        ReflectionTestUtils.setField(underTest, "operationMode", OperationMode.STUB);
        given(request.getBody()).willReturn(requestBody);
        given(stubDescriptorEvaluator.findResponseDescriptor(stubDescriptors, request)).willReturn(null);
        //WHEN
        underTest.redirectRequestToStub(request);
        //THEN
        verify(stubModeEvaluator).getResponseDescriptorForStubMode(request, OperationMode.STUB);
    }

    @Test
    public void testGetResponseDescriptorDTOAndRemoveShouldFindResponseDescriptorWithKey() {
        //GIVEN
        Map<String, ResponseDescriptorDTO> responseDescriptorMap = new HashMap<>();
        String key = "key";
        responseDescriptorMap.put(key, new ResponseDescriptorDTO(null, "", MSG_ID));
        ReflectionTestUtils.setField(underTest, "responseDescriptorMap", responseDescriptorMap);
        //WHEN
        ResponseDescriptorDTO actual = underTest.getResponseDescriptorDTOAndRemove(key);
        //THEN
        assertEquals(MSG_ID, actual.getRequestBody());
    }

    @Test
    public void testGetResponseDescriptorDTOAndRemoveShouldReturnNullWhenKeyNotFound() {
        //GIVEN
        Map<String, ResponseDescriptor> responseDescriptorMap = new HashMap<>();
        String key = "key";
        ReflectionTestUtils.setField(underTest, "responseDescriptorMap", responseDescriptorMap);
        //WHEN
        ResponseDescriptorDTO actual = underTest.getResponseDescriptorDTOAndRemove(key);
        //THEN
        assertNull(actual);
    }

    @Test
    public void testGetResponseDescriptorAndRemoveShouldNotDeleteEntryWhenKeyNotFound() {
        //GIVEN
        Map<String, ResponseDescriptorDTO> responseDescriptorMap = new HashMap<>();
        String key = "key";
        responseDescriptorMap.put("someOtherKey", new ResponseDescriptorDTO(null, "", MSG_ID));
        ReflectionTestUtils.setField(underTest, "responseDescriptorMap", responseDescriptorMap);
        //WHEN
        underTest.getResponseDescriptorDTOAndRemove(key);
        //THEN
        @SuppressWarnings("unchecked")
        Map<String, ResponseDescriptorDTO> newDescriptorMap = (Map<String, ResponseDescriptorDTO>) ReflectionTestUtils.getField(underTest,
                "responseDescriptorMap");

        assert newDescriptorMap != null;
        assertEquals(MSG_ID, newDescriptorMap.get("someOtherKey").getRequestBody());
    }

    @Test
    public void testGetResponseDescriptorAndRemoveShouldReturnWithEntry() {
        //GIVEN
        Map<String, ResponseDescriptorDTO> responseDescriptorMap = new HashMap<>();
        String key = "key";
        ResponseDescriptorDTO expected = new ResponseDescriptorDTO(null, "", MSG_ID);
        responseDescriptorMap.put(key, expected);
        ReflectionTestUtils.setField(underTest, "responseDescriptorMap", responseDescriptorMap);
        //WHEN
        ResponseDescriptorDTO actual = underTest.getResponseDescriptorDTOAndRemove(key);
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    public void testSetOperationModeShouldSetStubMode() {
        //GIVEN
        operationMode = OperationMode.STUB;
        given(configurationAccess.getProperties()).willReturn(properties);
        given(properties.getOperationMode()).willReturn(operationMode);
        //WHEN
        underTest.setOperationMode(operationMode);
        //THEN
        OperationMode result = (OperationMode) ReflectionTestUtils.getField(underTest, "operationMode");
        assertEquals(operationMode, result);
    }

    @Test
    public void testSetOperationModeShouldSetProxyMode() {
        //GIVEN
        operationMode = OperationMode.PROXY;
        given(configurationAccess.getProperties()).willReturn(properties);
        given(properties.getOperationMode()).willReturn(operationMode);
        //WHEN
        underTest.setOperationMode(operationMode);
        //THEN
        OperationMode result = (OperationMode) ReflectionTestUtils.getField(underTest, "operationMode");
        assertEquals(operationMode, result);
    }

    @Test
    public void testSetOperationModeShouldSetWilmaMode() {
        //GIVEN
        operationMode = OperationMode.WILMA;
        given(configurationAccess.getProperties()).willReturn(properties);
        given(properties.getOperationMode()).willReturn(operationMode);
        //WHEN
        underTest.setOperationMode(operationMode);
        //THEN
        OperationMode result = (OperationMode) ReflectionTestUtils.getField(underTest, "operationMode");
        assertEquals(operationMode, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testInitStubDescriptorWhenOperationModeIsStubShouldSwitchOnStubMode() throws ClassNotFoundException {
        //GIVEN
        operationMode = OperationMode.STUB;
        given(configurationAccess.getProperties()).willReturn(properties);
        given(properties.getOperationMode()).willReturn(operationMode);
        given(command.modify(stubDescriptors)).willReturn(stubDescriptors);
        //WHEN
        underTest.performModification(command);
        //THEN
        Map<String, StubDescriptor> actual = (Map<String, StubDescriptor>) ReflectionTestUtils.getField(underTest, "stubDescriptors");
        assertEquals(actual, stubDescriptors);
        OperationMode result = (OperationMode) ReflectionTestUtils.getField(underTest, "operationMode");
        assertEquals(operationMode, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testInitStubDescriptorWhenOperationModeIsNotStubShouldDoNothing() throws ClassNotFoundException {
        //GIVEN
        operationMode = OperationMode.WILMA;
        given(configurationAccess.getProperties()).willReturn(properties);
        given(properties.getOperationMode()).willReturn(operationMode);
        given(command.modify(stubDescriptors)).willReturn(stubDescriptors);
        //WHEN
        underTest.performModification(command);
        //THEN
        Map<String, StubDescriptor> actual = (Map<String, StubDescriptor>) ReflectionTestUtils.getField(underTest, "stubDescriptors");
        assertEquals(actual, stubDescriptors);
        OperationMode result = (OperationMode) ReflectionTestUtils.getField(underTest, "operationMode");
        assertEquals(operationMode, result);
    }

}
