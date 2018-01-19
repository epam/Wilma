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

import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.parameter.Parameter;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.regex.PatternSyntaxException;

/**
 * Resolves the message name of the given {@link WilmaHttpRequest} by the given parameters.
 * Definitions can be passed as {@link Parameter}:
 * <ul>
 * <li>The key: Should start with {@code type:} and continue with the name of the message or {@code typequery:} and the name will be the result of the following xpath query.</li>
 * <li>The value: Should start with {@code url:} and continue with the regexp of the URL which will be compared to the request URL.</li>
 * </ul>
 *
 * @author Balazs_Berkes
 */
@Component("sequenceRestUrlMappingMessageNameResolver")
public class RestUrlMappingMessageNameResolver implements MessageNameResolver {

    private static final String EMPTY = "";
    private static final String TYPE_PREFIX = "type:";
    private static final String XPATH_PREFIX = "typequery:";
    private static final String URL_PREFIX = "url:";

    private final Logger logger = LoggerFactory.getLogger(RestUrlMappingMessageNameResolver.class);

    @Qualifier("sequenceXmlXpathResolver")
    @Autowired
    private XmlXpathResolver xmlTypeResolver;

    @Override
    public String resolve(final WilmaHttpEntity entity, final ParameterList parameters) {
        String resolvedName;
        if (entity instanceof WilmaHttpRequest) {
            resolvedName = resolveRequest(entity, parameters);
        } else {
            resolvedName = EMPTY;
        }
        return resolvedName;
    }

    private String resolveRequest(final WilmaHttpEntity entity, final ParameterList parameters) {
        String resolvedName = EMPTY;
        WilmaHttpRequest request = (WilmaHttpRequest) entity;

        for (Parameter parameter : parameters.getAllParameters()) {
            if (resolverParameters(parameter)) {
                String target = getTargetUrl(parameter.getValue());
                String url = resolveUrlFromRequestLine(request);
                if (matches(url, target)) {
                    resolvedName = getType(parameter.getName(), entity);
                }
            }
        }
        return resolvedName;
    }

    private String resolveUrlFromRequestLine(final WilmaHttpRequest request) {
        return request.getRequestLine().split(" ")[1];
    }

    private boolean matches(final String url, final String target) {
        boolean matches;
        try {
            matches = url.matches(target);
        } catch (PatternSyntaxException ex) {
            logger.debug("Invalid regular expression: " + target, ex);
            matches = false;
        }
        return matches;
    }

    private String getTargetUrl(final String value) {
        return value.substring(URL_PREFIX.length());
    }

    private String getType(final String key, final WilmaHttpEntity entity) {
        String name;
        if (key.startsWith(TYPE_PREFIX)) {
            name = key.substring(TYPE_PREFIX.length());
        } else {
            name = evaluateQuery(key, entity);
        }

        return name;
    }

    private String evaluateQuery(final String key, final WilmaHttpEntity entity) {
        return xmlTypeResolver.getValue(key.substring(XPATH_PREFIX.length()), entity.getBody());
    }

    private boolean resolverParameters(final Parameter parameter) {
        return parameter.getValue().startsWith(URL_PREFIX)
                && (parameter.getName().startsWith(TYPE_PREFIX) || parameter.getName().startsWith(XPATH_PREFIX));
    }
}
