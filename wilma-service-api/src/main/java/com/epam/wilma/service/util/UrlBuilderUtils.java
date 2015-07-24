package com.epam.wilma.service.util;

/*==========================================================================
 Copyright 2015 EPAM Systems

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

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.apache.commons.collections.MapUtils.isNotEmpty;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Helper class for URL building.
 *
 * @author Tamas_Pinter
 *
 */
public final class UrlBuilderUtils {
    public static final String COLON = ":";
    public static final String AMPERSAND = "&";
    public static final String EQUAL = "=";
    public static final String QUESTION_MARK = "?";
    public static final String SEPARATOR = "/";
    public static final String PROTOCOL_SEPARATOR = "://";
    public static final String SCHEME_HTTP = "http";
    public static final String SCHEME_HTTPS = "https";
    public static final String ENCODE = "UTF-8";

    private UrlBuilderUtils() {
        // not intended to be used as it's just a utility class
    }

    /**
     * Build absolute url with multiple parameters.
     *
     * @param isSecure secure or not
     * @param host the server host
     * @param port the port
     * @param uri base uri
     * @param params parameters
     * @return url
     */
    //CHECKSTYLE.OFF
    public static String buildAbsoluteURL(boolean isSecure, String host, String port, String uri, Map<String, String> params) {
        //CHECKSTYLE.ON
        StringBuilder result = new StringBuilder();

        if (isSecure) {
            result.append(SCHEME_HTTPS);
        } else {
            result.append(SCHEME_HTTP);
        }
        result.append(PROTOCOL_SEPARATOR).append(host);
        if (!isNullOrEmpty(port)) {
            result.append(COLON).append(port);
        }
        if (!isNullOrEmpty(uri)) {
            if (!uri.startsWith(SEPARATOR)) {
                result.append(SEPARATOR);
            }
            result.append(uri);
            appendParams(result, params);
        }

        return result.toString();
    }

    private static void appendParam(StringBuilder url, String separator, String key, String value) {
        url.append(separator);
        url.append(key);
        url.append(EQUAL);
        if (!isNullOrEmpty(value)) {
            try {
                url.append(URLEncoder.encode(value, ENCODE));
            } catch (UnsupportedEncodingException e) {
                throw new WilmaServiceException("Unable to encode value: " + value, e);
            }
        }
    }

    private static void appendParams(StringBuilder url, Map<String, String> params) {
        if (isNotEmpty(params)) {
            String separator = QUESTION_MARK;
            for (Entry<String, String> entry : params.entrySet()) {
                appendParam(url, separator, entry.getKey(), entry.getValue());
                separator = AMPERSAND;
            }
        }
    }

}
