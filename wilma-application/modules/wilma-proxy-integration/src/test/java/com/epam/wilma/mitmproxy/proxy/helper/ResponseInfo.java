package com.epam.wilma.mitmproxy.proxy.helper;

import org.apache.http.Header;

/**
 * This class is originated from project: https://github.com/tkohegyi/mitmJavaProxy
 *
 * @author Tamas_Kohegyi
 */
public class ResponseInfo {
    private final int statusCode;
    private final String body;
    private final Header contentEncodingHeader;

    public ResponseInfo(int statusCode, String body, Header contentEncodingHeader) {
        super();
        this.statusCode = statusCode;
        this.body = body;
        this.contentEncodingHeader = contentEncodingHeader;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((body == null) ? 0 : body.hashCode());
        result = prime * result + statusCode;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ResponseInfo other = (ResponseInfo) obj;
        if (body == null) {
            if (other.body != null)
                return false;
        } else if (!body.equals(other.body))
            return false;
        return statusCode == other.statusCode;
    }

    @Override
    public String toString() {
        return "ResponseInfo [statusCode=" + statusCode + ", body=" + body
                + "]";
    }

    public String getContentEncoding() {
        if (contentEncodingHeader != null) {
            return contentEncodingHeader.getValue();
        }
        return null;
    }

}
