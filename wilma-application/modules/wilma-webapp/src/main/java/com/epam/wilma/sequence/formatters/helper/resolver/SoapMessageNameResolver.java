package com.epam.wilma.sequence.formatters.helper.resolver;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Resolves the message name of the SOAP request or response.
 *
 * @author Balazs_Berkes
 */
@Component("sequenceSoapMessageNameResolver")
public class SoapMessageNameResolver implements MessageNameResolver {

    private static final String SOAP_TYPE_QUERY = "local-name(/*/*/*[1])";
    private final Logger logger = LoggerFactory.getLogger(SoapMessageNameResolver.class);

    @Qualifier("sequenceXmlXpathResolver")
    @Autowired
    private XmlXpathResolver xmlTypeResolver;

    @Override
    public String resolve(final WilmaHttpEntity request, final ParameterList parameters) {
        String name;
        try {
            name = xmlTypeResolver.getValue(SOAP_TYPE_QUERY, request.getBody());
        } catch (Exception e) {
            logger.debug("Invalid XPath expression: " + SOAP_TYPE_QUERY, e);
            name = "";
        }
        return name;
    }
}
