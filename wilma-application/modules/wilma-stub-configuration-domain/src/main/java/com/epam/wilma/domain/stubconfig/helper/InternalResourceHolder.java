package com.epam.wilma.domain.stubconfig.helper;
/*==========================================================================
Copyright 2013-2016 EPAM Systems

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

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateFormatter;
import com.epam.wilma.domain.stubconfig.interceptor.RequestInterceptor;
import com.epam.wilma.domain.stubconfig.interceptor.ResponseInterceptor;
import com.epam.wilma.domain.stubconfig.sequence.handler.SequenceHandler;

/**
 * Holds internal condition checker classes, template formatters and request/response interceptors.
 * @author Tunde_Kovacs
 *
 */
@Component
public class InternalResourceHolder {
    @Autowired
    private List<ConditionChecker> conditionCheckers;
    @Autowired
    private List<TemplateFormatter> templateFormatters;
    @Autowired(required = false)
    private List<RequestInterceptor> requestInterceptors;
    @Autowired(required = false)
    private List<ResponseInterceptor> responseInterceptors;
    @Autowired(required = false)
    private List<SequenceHandler> sequenceHandlers;

    public List<ConditionChecker> getConditionCheckers() {
        return conditionCheckers;
    }

    public List<TemplateFormatter> getTemplateFormatters() {
        return templateFormatters;
    }

    public List<RequestInterceptor> getRequestInterceptors() {
        return requestInterceptors;
    }

    public List<ResponseInterceptor> getResponseInterceptors() {
        return responseInterceptors;
    }

    /**
     * Gets the list of internal sequence handlers.
     * @return the list of sequence handlers
     */
    public List<SequenceHandler> getSequenceHandlers() {
        if (sequenceHandlers == null) {
            sequenceHandlers = Collections.emptyList();
        }
        return sequenceHandlers;
    }

}
