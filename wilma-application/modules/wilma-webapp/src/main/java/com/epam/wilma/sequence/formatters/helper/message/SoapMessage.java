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
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import com.epam.wilma.sequence.formatters.helper.converter.Converter;
import com.epam.wilma.sequence.formatters.helper.resolver.SoapMessageNameResolver;

/**
 * A SOAP request or response.
 * @author Adam_Csaba_Kiraly
 *
 */
public class SoapMessage implements Message {

    @Autowired
    private SoapMessageNameResolver soapMessageNameResolver;

    private final WilmaHttpEntity entity;
    private final Converter converter;

    /**
     * Constructor for {@link SoapMessage}.
     * @param entity the {@link WilmaHttpEntity} to wrap
     * @param converter the {@link Converter} to use for converting to the desired format
     */
    public SoapMessage(final WilmaHttpEntity entity, final Converter converter) {
        this.entity = entity;
        this.converter = converter;
    }

    @Override
    public String resolveName(final ParameterList parameters) {
        return soapMessageNameResolver.resolve(entity, parameters);
    }

    @Override
    public String convert(final String name) {
        return converter.convert(entity.getBody(), name);
    }

}
