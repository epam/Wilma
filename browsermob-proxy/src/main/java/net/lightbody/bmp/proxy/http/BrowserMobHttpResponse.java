package net.lightbody.bmp.proxy.http;

import net.lightbody.bmp.core.har.HarEntry;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BrowserMobHttpResponse {
    private HarEntry entry;
    private HttpRequestBase method;
    private HttpResponse response;
    private boolean contentMatched;
    private String verificationText;
    private String errorMessage;
    private String body;
    private String contentType;
    private String charSet;
    private int status;
    private ByteArrayOutputStream bos;
    private OutputStream os;

    public BrowserMobHttpResponse(int status, HarEntry entry, HttpRequestBase method, HttpResponse response, boolean contentMatched, String verificationText, String errorMessage, String body, String contentType, String charSet, ByteArrayOutputStream bos, OutputStream os) {
        this.entry = entry;
        this.method = method;
        this.response = response;
        this.contentMatched = contentMatched;
        this.verificationText = verificationText;
        this.errorMessage = errorMessage;
        this.body = body;
        this.contentType = contentType;
        this.charSet = charSet;
        this.status = status;
        this.bos = bos;
        this.os = os;
    }

    public boolean isContentMatched() {
        return contentMatched;
    }

    public String getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }

    public String getCharSet() {
        return charSet;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getHeader(String name) {
        Header header = response.getFirstHeader(name);
        if (header == null) {
            return null;
        }

        return header.getValue();
    }

    public Header[] getHeaders() {
        return response.getAllHeaders();
    }

    public Header[] getRequestHeaders() {
        return method.getAllHeaders();
    }
    public int getStatus() {
        return status;
    }

    public HttpResponse getRawResponse() {
        return response;
    }

    public void checkContentMatched(String info) {
        if (!isContentMatched()) {
            throw new RuntimeException("Content match failure. Expected '" + verificationText + "'." + (info != null ? " " + info : ""));
        }
    }

    public HarEntry getEntry() {
        return entry;
    }

    public void doAnswer() {
        //from bos write to os
        byte[] answer = bos.toByteArray();
        try {
            os.write(answer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(os);
        }
    }

    public byte[] getAnswer() {
        return bos.toByteArray();
    }

    public void setAnswer(byte[] bytes) throws IOException {
        IOUtils.closeQuietly(bos);
        bos = new ByteArrayOutputStream(bytes.length);
        IOUtils.write(bytes, bos);
    }
}
