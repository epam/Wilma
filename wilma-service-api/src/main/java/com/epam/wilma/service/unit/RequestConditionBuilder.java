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

/**
 * A Request Condition class builder.
 *
 * @author Tamas_Kohegyi
 *
 */
public class RequestConditionBuilder {

    public RequestConditionBuilder andStart() {
        return this;
    }

    public RequestConditionBuilder andEnd() {
        return this;
    }

    public RequestConditionBuilder orStart() {
        return this;
    }

    public RequestConditionBuilder orEnd() {
        return this;
    }

    public RequestConditionBuilder not() {
        return this;
    }

    public RequestConditionBuilder condition() {
        return this;
    }

    public RequestConditionBuilder comingFrom(String localhost) {
        return this;
    }

    public ResponseDescriptorBuilder willResponseWith() {
        return new ResponseDescriptorBuilder(build());
    }

    public RequestCondition build() {
        return new RequestCondition();
    }

    public RequestConditionBuilder withHeader(String blah) {
        return this;
    }

    public RequestConditionBuilder textInUrl(String textInUrl) {
        return this;
    }
}
