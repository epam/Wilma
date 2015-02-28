package com.epam.wilma.domain.stubconfig;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateFormatter;
import com.epam.wilma.domain.stubconfig.interceptor.RequestInterceptor;
import com.epam.wilma.domain.stubconfig.interceptor.ResponseInterceptor;
import com.epam.wilma.domain.stubconfig.sequence.handler.SequenceHandler;

/**
 * Contains stub configuration resources (templates, template formatters, condition checkers,
 * interceptors and the previously parsed XML document).
 * @author Tamas_Bihari
 * @author Tunde_Kovacs
 * @author Balazs_Berkes
 */
@Component
public class StubResourceHolder {

    private Map<String, byte[]> templates;
    private List<ConditionChecker> conditionCheckers;
    private List<TemplateFormatter> templateFormatters;
    private List<RequestInterceptor> requestInterceptors;
    private List<ResponseInterceptor> responseInterceptors;
    private final Map<String, Document> stubConfigDocuments;
    private List<SequenceHandler> sequenceHandlers;

    /**
     * Default constructor for {@link StubResourceHolder} creation which initializes templates map.
     */
    public StubResourceHolder() {
        templates = new HashMap<String, byte[]>();
        stubConfigDocuments = new HashMap<>();
    }

    public void setConditionChekers(final List<ConditionChecker> conditionCheckers) {
        this.conditionCheckers = conditionCheckers;
    }

    public List<ConditionChecker> getConditionCheckers() {
        return conditionCheckers;
    }

    public void setTemplateFormatters(final List<TemplateFormatter> templateFormatters) {
        this.templateFormatters = templateFormatters;
    }

    public List<TemplateFormatter> getTemplateFormatters() {
        return templateFormatters;
    }

    public void setTemplates(final Map<String, byte[]> templates) {
        this.templates = templates;
    }

    public Map<String, byte[]> getTemplates() {
        return templates;
    }

    public List<RequestInterceptor> getRequestInterceptors() {
        return requestInterceptors;
    }

    public void setRequestInterceptors(final List<RequestInterceptor> requestInterceptors) {
        this.requestInterceptors = requestInterceptors;
    }

    public List<ResponseInterceptor> getResponseInterceptors() {
        return responseInterceptors;
    }

    public void setResponseInterceptors(final List<ResponseInterceptor> responseInterceptors) {
        this.responseInterceptors = responseInterceptors;
    }

    public List<SequenceHandler> getSequenceHandlers() {
        return sequenceHandlers;
    }

    public void setSequenceHandlers(final List<SequenceHandler> sequenceHandlers) {
        this.sequenceHandlers = sequenceHandlers;
    }

    /**
     * Put the document of stub configuration into a Map with the give groupName.
     * @param groupName is the groupname of the selected stub configuration
     * @param document is the xml document of the selected stub configuration
     */
    public void setActualStubConfigDocument(final String groupName, final Document document) {
        stubConfigDocuments.put(groupName, document);
    }

    /**
     * Get the document of stub configuration from a Map.
     * @param groupName is the groupname of the selected stub configuration
     * @return the document of the selected stub configuration
     */
    public Document getActualStubConfigDocument(final String groupName) {
        return stubConfigDocuments.get(groupName);
    }

}
