package com.epam.wilma.router.evaluation;
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
import com.epam.wilma.domain.stubconfig.StubDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptor;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseDescriptorAttributes;
import com.epam.wilma.domain.stubconfig.dialog.response.template.Template;
import com.epam.wilma.router.domain.ResponseDescriptorDTO;
import com.epam.wilma.router.helper.DialogDescriptorFactory;
import com.epam.wilma.router.helper.ResponseDescriptorDtoFactory;

/**
 * Provides unit tests for the class {@link StubModeEvaluator}.
 * @author Tibor_Kovacs
 *
 */
public class StubModeEvaluatorTest {

    private static final String DEFAULT_GROUPNAME = "test";

    private static final String DIALOG_DESCRIPTOR_NAME_FOR_STUB_MODE = "dialog-descriptor-for-stub-mode";

    private byte[] resource;
    private Map<String, StubDescriptor> stubDescriptors;
    private StubDescriptorAttributes attributes;
    private ResponseDescriptorAttributes responseDescriptorAttributes;

    @Mock
    private DialogDescriptorFactory dialogDescriptorFactory;
    @Mock
    private ResponseDescriptorDtoFactory responseDescriptorDtoFactory;
    @Mock
    private StubDescriptor stubDescriptor;
    @Mock
    private ResponseDescriptorDTO responseDescriptorDTO;
    @Mock
    private DialogDescriptor dialogDescriptor;
    @Mock
    private WilmaHttpRequest request;
    @Mock
    private ResponseDescriptor responseDescriptor;
    @Mock
    private Template template;

    @InjectMocks
    private StubModeEvaluator underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        stubDescriptors = new LinkedHashMap<>();
        stubDescriptors.put(DEFAULT_GROUPNAME, stubDescriptor);
        attributes = new StubDescriptorAttributes(DEFAULT_GROUPNAME, true);
        given(stubDescriptor.getAttributes()).willReturn(attributes);
        responseDescriptorAttributes = new ResponseDescriptorAttributes.Builder().delay(0).mimeType("mimeType").template(template).build();
    }

    @Test
    public void testGetResponseDescriptorForStubModeShouldReturnTheResponseDescriptorWhenOperationModeIsStub() {
        //GIVEN
        given(responseDescriptorDtoFactory.createResponseDescriptorDTO(DEFAULT_GROUPNAME, dialogDescriptor)).willReturn(responseDescriptorDTO);
        given(responseDescriptorDTO.getResponseDescriptor()).willReturn(responseDescriptor);
        given(responseDescriptor.getAttributes()).willReturn(responseDescriptorAttributes);
        given(template.getResource()).willReturn(resource);
        given(request.getBody()).willReturn(DEFAULT_GROUPNAME);
        //WHEN
        ResponseDescriptorDTO result = underTest.getResponseDescriptorForStubMode(request, OperationMode.STUB);
        //THEN
        verify(responseDescriptorDtoFactory).modifyResponseDescriptorDTOForStubMode(request, responseDescriptorDTO);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetResponseDescriptorForStubModeShouldReturnTheResponseDescriptorWhenOperationModeIsStubAndAtFirstCall() {
        //GIVEN
        Whitebox.setInternalState(underTest, "dialogDescriptorForStub", null);
        given(dialogDescriptorFactory.createDialogDescriptorForStubMode(DIALOG_DESCRIPTOR_NAME_FOR_STUB_MODE)).willReturn(dialogDescriptor);
        given(responseDescriptorDtoFactory.createResponseDescriptorDTO(DEFAULT_GROUPNAME, dialogDescriptor)).willReturn(responseDescriptorDTO);
        given(responseDescriptorDTO.getResponseDescriptor()).willReturn(responseDescriptor);
        given(responseDescriptor.getAttributes()).willReturn(responseDescriptorAttributes);
        given(template.getResource()).willReturn(resource);
        given(request.getBody()).willReturn(DEFAULT_GROUPNAME);
        //WHEN
        ResponseDescriptorDTO result = underTest.getResponseDescriptorForStubMode(request, OperationMode.STUB);
        //THEN
        verify(responseDescriptorDtoFactory).modifyResponseDescriptorDTOForStubMode(request, responseDescriptorDTO);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetResponseDescriptorForStubModeShouldReturnNullWhenOperationModeIsWilma() {
        //GIVEN
        //WHEN
        ResponseDescriptorDTO result = underTest.getResponseDescriptorForStubMode(request, OperationMode.WILMA);
        //THEN
        Assert.assertNull(result);
    }

    @Test
    public void testGetResponseDescriptorForStubModeShouldReturnNullWhenOperationModeIsProxy() {
        //GIVEN
        //WHEN
        ResponseDescriptorDTO result = underTest.getResponseDescriptorForStubMode(request, OperationMode.PROXY);
        //THEN
        Assert.assertNull(result);
    }

}
