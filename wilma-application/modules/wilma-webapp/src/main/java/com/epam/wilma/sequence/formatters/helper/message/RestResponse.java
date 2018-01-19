package com.epam.wilma.sequence.formatters.helper.message;
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

import org.springframework.beans.factory.annotation.Autowired;

import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.sequence.formatters.helper.converter.Converter;
import com.epam.wilma.sequence.formatters.helper.resolver.RestUrlMappingMessageNameResolver;

/**
 * A REST response.
 * @author Adam_Csaba_Kiraly
 *
 */
public class RestResponse implements Message {

    private static final String POSTFIX = "Response";

    @Autowired
    private RestUrlMappingMessageNameResolver restUrlMappingMessageNameResolver;

    private final WilmaHttpEntity entity;
    private final Converter converter;
    private final WilmaHttpRequest request;

    /**
     * Constructor for {@link RestResponse}.
     * @param entity the {@link WilmaHttpEntity} to wrap
     * @param converter the {@link Converter} which will convert the entity body to the desired format
     * @param request {@link WilmaHttpRequest} the request pair of the wrapped response
     */
    public RestResponse(final WilmaHttpEntity entity, final Converter converter, final WilmaHttpRequest request) {
        this.entity = entity;
        this.converter = converter;
        this.request = request;
    }

    @Override
    public String resolveName(final ParameterList parameters) {
        String name = restUrlMappingMessageNameResolver.resolve(request, parameters);
        return name.isEmpty() ? "" : name + POSTFIX;
    }

    @Override
    public String convert(final String name) {
        return converter.convert(entity.getBody(), name);
    }

}
