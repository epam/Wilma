package com.epam.wilma.service.unit;
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

import com.epam.wilma.service.unit.request.RequestCondition;
import com.epam.wilma.service.unit.request.RequestConditionBase;
import com.epam.wilma.service.unit.response.ResponseDescriptor;

/**
 * Builder class for building a complete Stub Configuration.
 *
 * @author Tamas_Kohegyi
 *
 */
public class ResponseDescriptorBuilder {
    private RequestConditionBase requestConditionBase;

    public ResponseDescriptorBuilder(RequestCondition requestCondition) {
        this.requestConditionBase = requestCondition;
    }

    public ResponseDescriptorBuilder plainTextResponse(String plainTextResponse) {
        return this;
    }

    public ResponseDescriptor buildResponseDescriptor() {
        return new ResponseDescriptor();
    }

    public Stub build() {
        //need to validate both the request condition, and the response descriptor
        Stub stub = new Stub(requestConditionBase, buildResponseDescriptor());
        return stub;
    }

    public ResponseDescriptorBuilder withStatus(int i) {
        return this;
    }

    public ResponseDescriptorBuilder applyFormatter() {
        return this;
    }

    public ResponseDescriptorBuilder generatedResponse() {
        return this;
    }
}
