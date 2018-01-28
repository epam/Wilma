package com.epam.wilma.gepard.testclient;

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

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * Fill information about the received response into this class.
 */
public class ResponseHolder {
    private String responseMessage;
    private String responseCode;
    private PostMethod httpPost;
    private GetMethod httpGet;

    public void setResponseMessage(final String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(final String responseCode) {
        this.responseCode = responseCode;
    }

    public void setHttpPost(final PostMethod httpPost) {
        this.httpPost = httpPost;
    }

    public PostMethod getHttpPost() {
        return httpPost;
    }

    public void setHttpGet(final GetMethod httpGet) {
        this.httpGet = httpGet;
    }

    public GetMethod getHttpGet() {
        return httpGet;
    }
}
