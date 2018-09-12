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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.epam.wilma.domain.stubconfig.StubResourceHolder;
import com.epam.wilma.domain.stubconfig.TemporaryStubResourceHolder;
import com.epam.wilma.domain.stubconfig.helper.InternalResourceHolder;
import com.epam.wilma.domain.stubconfig.interceptor.RequestInterceptor;
import com.epam.wilma.domain.stubconfig.interceptor.ResponseInterceptor;

/**
 * Updates the {@link StubResourceHolder} from the {@link TemporaryStubResourceHolder}.
 * @author Tunde_Kovacs
 *
 */
@Component
public class StubResourceHolderUpdater {

    @Autowired
    private StubResourceHolder stubResourceHolder;
    @Autowired
    private TemporaryStubResourceHolder temporaryStubResourceHolder;
    @Autowired
    private InternalResourceHolder internalResourceHolder;

    /**
     * Copies the resources from the {@link TemporaryStubResourceHolder} to the {@link StubResourceHolder}.
     */
    public void updateResourceHolder() {
        stubResourceHolder.setConditionChekers(new ArrayList<>(temporaryStubResourceHolder.getConditionCheckers()));
        stubResourceHolder.setResponseFormatters(new ArrayList<>(temporaryStubResourceHolder.getResponseFormatters()));
        stubResourceHolder.setTemplates(new HashMap<>(temporaryStubResourceHolder.getTemplates()));
        updateRequestInterceptors();
        updateResponseInterceptors();
        stubResourceHolder.setSequenceHandlers(new ArrayList<>(temporaryStubResourceHolder.getSequenceHandlers()));
    }

    private void updateRequestInterceptors() {
        List<RequestInterceptor> requestInterceptors = temporaryStubResourceHolder.getRequestInterceptors();
        if (requestInterceptors != null) {
            stubResourceHolder.setRequestInterceptors(new ArrayList<>(requestInterceptors));
        }
    }

    private void updateResponseInterceptors() {
        List<ResponseInterceptor> responseInterceptors = temporaryStubResourceHolder.getResponseInterceptors();
        if (responseInterceptors != null) {
            stubResourceHolder.setResponseInterceptors(new ArrayList<>(responseInterceptors));
        }
    }

    /**
     * Clears the resources in the {@link TemporaryStubResourceHolder}.
     */
    public void clearTemporaryResourceHolder() {
        temporaryStubResourceHolder.clearConditionCheckers();
        temporaryStubResourceHolder.clearResponseFormatters();
        temporaryStubResourceHolder.clearTemplates();
        temporaryStubResourceHolder.clearRequestInterceptors();
        temporaryStubResourceHolder.clearResponseInterceptors();
        temporaryStubResourceHolder.clearSequenceHandlers();
    }

    /**
     * Add the XML document to {@link StubResourceHolder}.
     * @param groupName is the group name attribute of stub configuration
     * @param document is the XML document
     */
    public void addDocumentToResourceHolder(final String groupName, final Document document) {
        //stubResourceHolder.setActualStubConfigDocument(groupName, document);
    }

    /**
     * Add the JSON Object to {@link StubResourceHolder}.
     * @param groupName is the group name attribute of stub configuration
     * @param jsonObject is the json object
     */
    public void addDocumentToResourceHolder(final String groupName, final JSONObject jsonObject) {
        stubResourceHolder.setActualStubConfigJsonObject(groupName, jsonObject);
    }

    /**
     * Copies internal condition checker, template formatter classes
     * and request/response interceptors
     * into the {@link TemporaryStubResourceHolder}.
     */
    public void initializeTemporaryResourceHolder() {
        temporaryStubResourceHolder.setConditionCheckers(new ArrayList<>(internalResourceHolder.getConditionCheckers()));
        temporaryStubResourceHolder.setResponseFormatters(new ArrayList<>(internalResourceHolder.getResponseFormatters()));
        initializeRequestInterceptors();
        initializeResponseInterceptors();
        temporaryStubResourceHolder.setSequenceHandlers(new ArrayList<>(internalResourceHolder.getSequenceHandlers()));
    }

    private void initializeRequestInterceptors() {
        List<RequestInterceptor> requestInterceptors = internalResourceHolder.getRequestInterceptors();
        if (requestInterceptors != null) {
            temporaryStubResourceHolder.setRequestInterceptors(new ArrayList<>(requestInterceptors));
        }
    }

    private void initializeResponseInterceptors() {
        List<ResponseInterceptor> responseInterceptors = internalResourceHolder.getResponseInterceptors();
        if (responseInterceptors != null) {
            temporaryStubResourceHolder.setResponseInterceptors(new ArrayList<>(responseInterceptors));
        }
    }
}
