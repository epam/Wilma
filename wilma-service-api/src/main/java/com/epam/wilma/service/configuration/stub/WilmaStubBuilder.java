package com.epam.wilma.service.configuration.stub;
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

import com.epam.wilma.service.configuration.stub.helper.common.IdGenerator;

/**
 * Builder class for building a complete WilmaStub Configuration.
 *
 * @author Tamas_Kohegyi
 */
public class WilmaStubBuilder {
    private String groupName = "Default";

    /**
     * Constructor with the possibility of specifying the group name.
     *
     * @param groupName is the stub configuration group name, it it is known.
     */
    public WilmaStubBuilder(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Constructor that uses a self-generated group name.
     */
    public WilmaStubBuilder() {
        this.groupName = "service-api-" + IdGenerator.getNextGeneratedId();
    }

    /**
     * Init method of creating a stub configuration, by starting with a condition part for the request.
     *
     * @return with a builder for a request condition
     */
    public RequestConditionBuilder forRequestsLike() {
        return new RequestConditionBuilder(groupName);
    }

    /**
     * Init method of creating a stub configuration, by starting with a condition part for the request.
     *
     * @return with a builder for a response descriptor. After this, only addInterceptor has meaning to call.
     */
    public ResponseDescriptorBuilder forAnyRequest() {
        RequestConditionBuilder requestConditionBuilder = new RequestConditionBuilder(groupName);
        ResponseDescriptorBuilder responseDescriptorBuilder = requestConditionBuilder.condition("AlwaysFalseChecker")
                .willRespondWith().plainTextResponse("dummy");
        return responseDescriptorBuilder;
    }
}
