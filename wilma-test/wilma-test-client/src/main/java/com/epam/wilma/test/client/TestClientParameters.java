package com.epam.wilma.test.client;

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

/**
 * Holds the parameters of the connection and others.
 * @author Tibor_Kovacs
 */
public class TestClientParameters {
    private boolean useProxy;
    private boolean allowResponseToLog;
    private Integer requestBufferSize;
    private Integer responseBufferSize;

    /**
     * Adds the <tt>useProxy</tt> field to the client parameters. Part of the building process.
     * @param useProxy decides whether the proxy should be used when sending
     * the request to the server or not
     * @return the client parameter
     */
    public TestClientParameters useProxy(final boolean useProxy) {
        this.useProxy = useProxy;
        return this;
    }

    /**
     * Defines if the response should be logged to console ro not.
     * When endless loop is used, it is suggested to NOT allow to log the responses.
     * @param allowResponseToLog accept or not accept the logging
     * @return the client parameter
     */
    public TestClientParameters allowResponseLogging(final boolean allowResponseToLog) {
        this.allowResponseToLog = allowResponseToLog;
        return this;
    }

    /**
     * Defines the maximum request size.
     * @param requestBufferSize is the new size.
     * @return the client parameter
     */
    public TestClientParameters requestBufferSize(final Integer requestBufferSize) {
        this.requestBufferSize = requestBufferSize;
        return this;
    }

    /**
     * Defines the maximum response size.
     * @param responseBufferSize is the new size.
     * @return the client parameter
     */
    public TestClientParameters responseBufferSize(final Integer responseBufferSize) {
        this.responseBufferSize = responseBufferSize;
        return this;
    }

    public boolean isUseProxy() {
        return useProxy;
    }

    public boolean getAllowResponseLogging() {
        return allowResponseToLog;
    }

    public Integer getRequestBufferSize() {
        return requestBufferSize;
    }

    public Integer getResponseBufferSize() {
        return responseBufferSize;
    }
}
