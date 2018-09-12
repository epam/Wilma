package com.epam.wilma.domain.stubconfig;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epam.wilma.domain.stubconfig.dialog.response.ResponseFormatter;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.interceptor.RequestInterceptor;
import com.epam.wilma.domain.stubconfig.interceptor.ResponseInterceptor;
import com.epam.wilma.domain.stubconfig.sequence.SequenceHandler;

/**
 * Contains temporary stub configuration resources (templates, response formatters, condition checkers, interceptors).
 * This is needed in order to keep consistent state of the resource holder. During the stub config parsing,
 * inconsistent state of resources would be produced if some external classes would be loaded directly into
 * the resource holder object while some others are not yet validated.
 * @author Tunde_Kovacs
 *
 */
@Component
public class TemporaryStubResourceHolder {

    private Map<String, byte[]> templates;
    private List<ConditionChecker> conditionCheckers;
    private List<ResponseFormatter> responseFormatters;
    private List<RequestInterceptor> requestInterceptors;
    private List<ResponseInterceptor> responseInterceptors;
    private List<SequenceHandler> sequenceHandlers;

    /**
     * Default constructor for {@link StubResourceHolder} creation which initializes templates map.
     */
    public TemporaryStubResourceHolder() {
        templates = new HashMap<>();
    }

    /**
     * Adds a new <tt>conditionChecker</tt> to the list of condition checkers.
     * @param conditionChecker the {@link ConditionChecker} that will be added to the list
     */
    public void addConditionChecker(final ConditionChecker conditionChecker) {
        conditionCheckers.add(conditionChecker);
    }

    public void setConditionCheckers(final List<ConditionChecker> conditionCheckers) {
        this.conditionCheckers = conditionCheckers;
    }

    public List<ConditionChecker> getConditionCheckers() {
        return conditionCheckers;
    }

    /**
     * Adds a new <tt>responseFormatter</tt> to the list of response formatters.
     * @param responseFormatter the {@link ResponseFormatter} that will be added to the list
     */
    public void addResponseFormatter(final ResponseFormatter responseFormatter) {
        responseFormatters.add(responseFormatter);
    }

    public void setResponseFormatters(final List<ResponseFormatter> responseFormatters) {
        this.responseFormatters = responseFormatters;
    }

    public List<ResponseFormatter> getResponseFormatters() {
        return responseFormatters;
    }

    /**
     * Adds a new template to the map of templates with filename as key.
     * @param key is the file name
     * @param resource is the template as byte array
     */
    public void addTemplate(final String key, final byte[] resource) {
        templates.put(key, resource);
    }

    public void setTemplates(final Map<String, byte[]> templates) {
        this.templates = templates;
    }

    public Map<String, byte[]> getTemplates() {
        return templates;
    }

    /**
     * Adds a new <tt>requestInterceptor</tt> to the list of request interceptors.
     * @param requestInterceptor the {@link RequestInterceptor} that will be added to the list
     */
    public void addRequestInterceptor(final RequestInterceptor requestInterceptor) {
        if (requestInterceptors != null) {
            requestInterceptors.add(requestInterceptor);
        }
    }

    public List<RequestInterceptor> getRequestInterceptors() {
        return requestInterceptors;
    }

    public void setRequestInterceptors(final List<RequestInterceptor> requestInterceptors) {
        this.requestInterceptors = requestInterceptors;
    }

    /**
     * Adds a new <tt>responseInterceptor</tt> to the list of response interceptors.
     * @param responseInterceptor the {@link ResponseInterceptor} that will be added to the list
     */
    public void addResponseInterceptor(final ResponseInterceptor responseInterceptor) {
        if (responseInterceptors != null) {
            responseInterceptors.add(responseInterceptor);
        }
    }

    public List<ResponseInterceptor> getResponseInterceptors() {
        return responseInterceptors;
    }

    public void setResponseInterceptors(final List<ResponseInterceptor> responseInterceptors) {
        this.responseInterceptors = responseInterceptors;
    }

    /**
     * Adds a new <tt>sequenceHandler</tt> to the list of sequence handlers.
     * @param sequenceHandler the {@link SequenceHandler} that will be added to the list
     */
    public void addSequenceHandler(final SequenceHandler sequenceHandler) {
        if (sequenceHandlers != null) {
            sequenceHandlers.add(sequenceHandler);
        }
    }

    public List<SequenceHandler> getSequenceHandlers() {
        return sequenceHandlers;
    }

    public void setSequenceHandlers(final List<SequenceHandler> sequenceHandlers) {
        this.sequenceHandlers = sequenceHandlers;
    }

    /**
     * Empties the condition checker list.
     */
    public void clearConditionCheckers() {
        conditionCheckers.clear();
    }

    /**
     * Empties the response formatter list.
     */
    public void clearResponseFormatters() {
        responseFormatters.clear();
    }

    /**
     * Empties the template list.
     */
    public void clearTemplates() {
        templates.clear();
    }

    /**
     * Empties the request interceptor list.
     */
    public void clearRequestInterceptors() {
        if (requestInterceptors != null) {
            requestInterceptors.clear();
        }
    }

    /**
     * Empties the response interceptor list.
     */
    public void clearResponseInterceptors() {
        if (responseInterceptors != null) {
            responseInterceptors.clear();
        }
    }

    /**
     * Empties the sequence handler list.
     */
    public void clearSequenceHandlers() {
        if (sequenceHandlers != null) {
            sequenceHandlers.clear();
        }
    }

}
