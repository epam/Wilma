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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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

/**
 * Provides unit tests for the {@link RoutingService} class.
 * @author Tunde_Kovacs
 *
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

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        requestBody = "";
        stubDescriptors = new LinkedHashMap<>();
        stubDescriptors.put("test", stubDescriptor);
        Whitebox.setInternalState(underTest, "stubDescriptors", stubDescriptors);
        Whitebox.setInternalState(underTest, "operationMode", null);
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
        assertEquals(actual, true);
    }

    @Test
    public void testRedirectRequestToStubShouldReturnFalseWhenResponseDescriptorIsNull() {
        //GIVEN
        given(request.getBody()).willReturn(requestBody);
        given(stubDescriptorEvaluator.findResponseDescriptor(stubDescriptors, request)).willReturn(null);
        //WHEN
        boolean actual = underTest.redirectRequestToStub(request);
        //THEN
        assertEquals(actual, false);
    }

    @Test
    public void testRedirectRequestToStubShouldCallStubModeEvaluatorGetResponseDescriptorForStubModeMethod() {
        //GIVEN
        Whitebox.setInternalState(underTest, "operationMode", OperationMode.STUB);
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
        Whitebox.setInternalState(underTest, "responseDescriptorMap", responseDescriptorMap);
        //WHEN
        ResponseDescriptorDTO actual = underTest.getResponseDescriptorDTOAndRemove(key);
        //THEN
        assertEquals(actual.getRequestBody(), MSG_ID);
    }

    @Test
    public void testGetResponseDescriptorDTOAndRemoveShouldReturnNullWhenKeyNotFound() {
        //GIVEN
        Map<String, ResponseDescriptor> responseDescriptorMap = new HashMap<>();
        String key = "key";
        Whitebox.setInternalState(underTest, "responseDescriptorMap", responseDescriptorMap);
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
        Whitebox.setInternalState(underTest, "responseDescriptorMap", responseDescriptorMap);
        //WHEN
        underTest.getResponseDescriptorDTOAndRemove(key);
        //THEN
        @SuppressWarnings("unchecked")
        Map<String, ResponseDescriptorDTO> newDescriptorMap = (Map<String, ResponseDescriptorDTO>) Whitebox.getInternalState(underTest,
                "responseDescriptorMap");

        assertEquals(newDescriptorMap.get("someOtherKey").getRequestBody(), MSG_ID);
    }

    @Test
    public void testGetResponseDescriptorAndRemoveShouldReturnWithEntry() {
        //GIVEN
        Map<String, ResponseDescriptorDTO> responseDescriptorMap = new HashMap<>();
        String key = "key";
        ResponseDescriptorDTO expected = new ResponseDescriptorDTO(null, "", MSG_ID);
        responseDescriptorMap.put(key, expected);
        Whitebox.setInternalState(underTest, "responseDescriptorMap", responseDescriptorMap);
        //WHEN
        ResponseDescriptorDTO actual = underTest.getResponseDescriptorDTOAndRemove(key);
        //THEN
        assertEquals(actual, expected);
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
        OperationMode result = (OperationMode) Whitebox.getInternalState(underTest, "operationMode");
        Assert.assertEquals(result, operationMode);
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
        OperationMode result = (OperationMode) Whitebox.getInternalState(underTest, "operationMode");
        Assert.assertEquals(result, operationMode);
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
        OperationMode result = (OperationMode) Whitebox.getInternalState(underTest, "operationMode");
        Assert.assertEquals(result, operationMode);
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
        Map<String, StubDescriptor> actual = (Map<String, StubDescriptor>) Whitebox.getInternalState(underTest, "stubDescriptors");
        assertEquals(actual, stubDescriptors);
        OperationMode result = (OperationMode) Whitebox.getInternalState(underTest, "operationMode");
        Assert.assertEquals(result, operationMode);
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
        Map<String, StubDescriptor> actual = (Map<String, StubDescriptor>) Whitebox.getInternalState(underTest, "stubDescriptors");
        assertEquals(actual, stubDescriptors);
        OperationMode result = (OperationMode) Whitebox.getInternalState(underTest, "operationMode");
        Assert.assertEquals(result, operationMode);
    }

}
