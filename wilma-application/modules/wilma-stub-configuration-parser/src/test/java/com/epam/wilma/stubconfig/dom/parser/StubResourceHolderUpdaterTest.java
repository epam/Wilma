package com.epam.wilma.stubconfig.dom.parser;
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

import com.epam.wilma.domain.stubconfig.StubResourceHolder;
import com.epam.wilma.domain.stubconfig.TemporaryStubResourceHolder;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatter;
import com.epam.wilma.domain.stubconfig.helper.InternalResourceHolder;
import com.epam.wilma.domain.stubconfig.interceptor.RequestInterceptor;
import com.epam.wilma.domain.stubconfig.interceptor.ResponseInterceptor;
import com.epam.wilma.domain.stubconfig.sequence.SequenceHandler;
import org.json.JSONObject;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Provides unit tests for the class {@link StubResourceHolderUpdater}.
 *
 * @author Tunde_Kovacs
 */
public class StubResourceHolderUpdaterTest {

    private static final String DEFAULT_GROUPNAME = "test";
    @Mock
    private StubResourceHolder stubResourceHolder;
    @Mock
    private TemporaryStubResourceHolder temporaryStubResourceHolder;
    @Mock
    private InternalResourceHolder internalResourceHolder;
    @Mock
    private JSONObject jsonObject;

    @InjectMocks
    private StubResourceHolderUpdater underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateResourceHolderShouldSetResources() {
        //GIVEN
        List<ConditionChecker> conditionCheckers = new ArrayList<>();
        List<ResponseFormatter> templateFormatters = new ArrayList<>();
        Map<String, byte[]> templates = new HashMap<>();
        List<RequestInterceptor> requestInterceptors = new ArrayList<>();
        List<ResponseInterceptor> responseInterceptors = new ArrayList<>();
        List<SequenceHandler> sequenceHandlers = new ArrayList<>();
        given(temporaryStubResourceHolder.getConditionCheckers()).willReturn(conditionCheckers);
        given(temporaryStubResourceHolder.getResponseFormatters()).willReturn(templateFormatters);
        given(temporaryStubResourceHolder.getTemplates()).willReturn(templates);
        given(temporaryStubResourceHolder.getRequestInterceptors()).willReturn(requestInterceptors);
        given(temporaryStubResourceHolder.getResponseInterceptors()).willReturn(responseInterceptors);
        given(temporaryStubResourceHolder.getSequenceHandlers()).willReturn(sequenceHandlers);
        //WHEN
        underTest.updateResourceHolder();
        //THEN
        verify(stubResourceHolder).setConditionChekers(conditionCheckers);
        verify(stubResourceHolder).setResponseFormatters(templateFormatters);
        verify(stubResourceHolder).setTemplates(templates);
        verify(stubResourceHolder).setRequestInterceptors(requestInterceptors);
        verify(stubResourceHolder).setResponseInterceptors(responseInterceptors);
        verify(stubResourceHolder).setSequenceHandlers(sequenceHandlers);
    }

    @Test
    public void testUpdateResourceHolderWhenNoRequestInterceptorsShouldNotSetInterceptors() {
        //GIVEN
        given(temporaryStubResourceHolder.getRequestInterceptors()).willReturn(null);
        //WHEN
        underTest.updateResourceHolder();
        //THEN
        verify(stubResourceHolder, BDDMockito.never()).setRequestInterceptors(null);
    }

    @Test
    public void testUpdateResourceHolderWhenNoResponseInterceptorsShouldNotSetInterceptors() {
        //GIVEN
        given(temporaryStubResourceHolder.getResponseInterceptors()).willReturn(null);
        //WHEN
        underTest.updateResourceHolder();
        //THEN
        verify(stubResourceHolder, BDDMockito.never()).setRequestInterceptors(null);
    }

    @Test
    public void testClearTemporaryResourceHolderShouldCallTemporaryResourceHolder() {
        //GIVEN in setUp
        //WHEN
        underTest.clearTemporaryResourceHolder();
        //THEN
        verify(temporaryStubResourceHolder).clearConditionCheckers();
        verify(temporaryStubResourceHolder).clearResponseFormatters();
        verify(temporaryStubResourceHolder).clearTemplates();
        verify(temporaryStubResourceHolder).clearRequestInterceptors();
        verify(temporaryStubResourceHolder).clearResponseInterceptors();
        verify(temporaryStubResourceHolder).clearSequenceHandlers();
    }

    @Test
    public void testAddDocumentToResourceHolderShouldSetDocument() {
        //GIVEN in setUp
        //WHEN
        underTest.addDocumentToResourceHolder(DEFAULT_GROUPNAME, jsonObject);
        //THEN
        verify(stubResourceHolder).setActualStubConfigJsonObject(DEFAULT_GROUPNAME, jsonObject);
    }

    @Test
    public void testInitializeTemporaryResourceHolderShouldInitializeResources() {
        //GIVEN
        List<ConditionChecker> conditionCheckers = new ArrayList<>();
        List<ResponseFormatter> responseFormatters = new ArrayList<>();
        List<RequestInterceptor> requestInterceptors = new ArrayList<>();
        List<ResponseInterceptor> responseInterceptors = new ArrayList<>();
        List<SequenceHandler> sequenceHandlers = new ArrayList<>();
        given(internalResourceHolder.getConditionCheckers()).willReturn(conditionCheckers);
        given(internalResourceHolder.getResponseFormatters()).willReturn(responseFormatters);
        given(internalResourceHolder.getRequestInterceptors()).willReturn(requestInterceptors);
        given(internalResourceHolder.getResponseInterceptors()).willReturn(responseInterceptors);
        given(internalResourceHolder.getSequenceHandlers()).willReturn(sequenceHandlers);
        //WHEN
        underTest.initializeTemporaryResourceHolder();
        //THEN
        verify(temporaryStubResourceHolder).setConditionCheckers(conditionCheckers);
        verify(temporaryStubResourceHolder).setResponseFormatters(responseFormatters);
        verify(temporaryStubResourceHolder).setRequestInterceptors(requestInterceptors);
        verify(temporaryStubResourceHolder).setResponseInterceptors(responseInterceptors);
        verify(temporaryStubResourceHolder).setSequenceHandlers(sequenceHandlers);
    }

    @Test
    public void testInitializeTemporaryResourceHolderWhenNoInterceptorsShouldNotInitializeResources() {
        //GIVEN
        List<ConditionChecker> conditionCheckers = new ArrayList<>();
        List<ResponseFormatter> templateFormatters = new ArrayList<>();
        List<SequenceHandler> sequenceHandlers = new ArrayList<>();
        given(internalResourceHolder.getConditionCheckers()).willReturn(conditionCheckers);
        given(internalResourceHolder.getResponseFormatters()).willReturn(templateFormatters);
        given(internalResourceHolder.getRequestInterceptors()).willReturn(null);
        given(internalResourceHolder.getResponseInterceptors()).willReturn(null);
        given(internalResourceHolder.getSequenceHandlers()).willReturn(sequenceHandlers);
        //WHEN
        underTest.initializeTemporaryResourceHolder();
        //THEN
        verify(temporaryStubResourceHolder).setConditionCheckers(conditionCheckers);
        verify(temporaryStubResourceHolder).setResponseFormatters(templateFormatters);
        verify(temporaryStubResourceHolder, BDDMockito.never()).setRequestInterceptors(null);
        verify(temporaryStubResourceHolder, BDDMockito.never()).setResponseInterceptors(null);
        verify(temporaryStubResourceHolder).setSequenceHandlers(sequenceHandlers);
    }
}
