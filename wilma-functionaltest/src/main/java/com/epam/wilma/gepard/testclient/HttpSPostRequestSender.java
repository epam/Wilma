package com.epam.wilma.gepard.testclient;
/*==========================================================================
Copyright since 2021, EPAM Systems

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

import com.epam.wilma.gepard.WilmaTestCase;
import com.epam.wilma.gepard.testclient.compression.fis.FastInfosetCompressor;
import com.epam.wilma.gepard.testclient.compression.gzip.GzipCompressor;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.xml.sax.SAXException;

import javax.net.ssl.SSLContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map.Entry;

/**
 * Sends a new HTTPS request to a server through a proxy.
 *
 * @author Tamas Kohegyi
 */
public class HttpSPostRequestSender {

    private final FastInfosetCompressor fastInfosetCompressor = new FastInfosetCompressor();
    private final GzipCompressor gzipCompressor = new GzipCompressor();

    /**
     * Sends a new HTTPS request to a server through a proxy. Also logs request and response with gepard framework.
     *
     * @param tc                is the caller Test Case.
     * @param requestParameters a set of parameters that will set the content of the request
     *                          and specify the proxy it should go through
     * @return with Response Holder class.
     * @throws IOException                  in case error occurs
     * @throws ParserConfigurationException in case error occurs
     * @throws SAXException                 in case error occurs
     */
    public ResponseHolder callWilmaTestServer(final WilmaTestCase tc, final RequestParameters requestParameters) throws Exception {
        String responseCode;
        ResponseHolder responseMessage;

        HttpHost httpsWebHost = new HttpHost("127.0.0.1", 8443, "https");
        tc.logPostRequestEvent(requestParameters); //this dumps the request

        try (CloseableHttpClient httpClient = buildHttpClient(requestParameters.isUseProxy(), requestParameters.getWilmaPort())) {
            final HttpPost request = new HttpPost(requestParameters.getTestServerUrl());
            createRequest(requestParameters, request);

            final HttpResponse response = httpClient.execute(httpsWebHost, request);

            int statusCode = response.getStatusLine().getStatusCode();
            responseCode = "status code: " + statusCode + "\n";
            responseMessage = createResponse(response);
            responseMessage.setResponseCode(responseCode);
            tc.setActualResponseCode(statusCode);
            HttpEntity resEntity = response.getEntity();
            Header contentTypeHeader = resEntity.getContentType();
            if (contentTypeHeader != null) {
                tc.setActualResponseContentType(contentTypeHeader.getValue());
            }
            Header sequenceHeader = response.getFirstHeader("Wilma-Sequence");
            if (sequenceHeader != null) {
                tc.setActualDialogDescriptor(sequenceHeader.getValue());
            }
            tc.logResponseEvent(responseMessage); //this dumps the response

            return responseMessage;
        } catch (Exception e) {
            throw e;
        }

    }

    //TODO seems this does not work well
    private void createRequest(final RequestParameters requestParameters, final HttpPost httpPost) throws IOException,
            ParserConfigurationException, SAXException {
        InputStream inputStream = requestParameters.getInputStream();
        if (requestParameters.getContentType().contains("fastinfoset")) {
            inputStream = fastInfosetCompressor.compress(inputStream);
        }
        if (requestParameters.getContentEncoding().contains("gzip")) {
            inputStream = gzipCompressor.compress(inputStream);
            httpPost.addHeader("Content-Encoding", requestParameters.getContentEncoding());
        }
        final InputStreamEntity entity = new InputStreamEntity(inputStream);
        entity.setChunked(true);
        httpPost.setEntity(entity);

        httpPost.addHeader("Accept", requestParameters.getAcceptHeader());
        httpPost.addHeader("Accept-Encoding", requestParameters.getAcceptEncoding());
        if (requestParameters.getSpecialHeader() != null) {
            httpPost.addHeader("Special-Header", requestParameters.getSpecialHeader());
        }
        for (Entry<String, String> header : requestParameters.getCustomHeaders()) {
            httpPost.addHeader(header.getKey(), header.getValue());
        }
    }

    private ResponseHolder createResponse(final HttpResponse response) throws IOException {
        ResponseHolder responseHolder = new ResponseHolder();
        String responseMessage;
        final HttpEntity resEntity = response.getEntity();
        InputStream responseBodyAsStream = resEntity.getContent();
        if (resEntity.getContentEncoding() != null && resEntity.getContentEncoding().getValue().contains("gzip")) {
            responseBodyAsStream = gzipCompressor.decompress(responseBodyAsStream);
        }
        if (resEntity.getContentType() != null && resEntity.getContentType().getValue().contains("fastinfoset")) {
            try {
                responseMessage = fastInfosetCompressor.decompress(responseBodyAsStream);
            } catch (TransformerException e) {
                responseMessage = EntityUtils.toString(resEntity);
            }
        } else {
            responseMessage = IOUtils.toString(responseBodyAsStream, "UTF-8");
        }
        responseHolder.setResponseMessage(responseMessage);
        responseHolder.setHttpPost(null);
        return responseHolder;
    }

    /**
     * Creates a DefaultHttpClient instance.
     *
     * @param isProxied if the request must go through proxy or not
     * @param port      is the proxy port
     * @return instance of DefaultHttpClient
     * @throws Exception is something wrong happens
     */
    public static CloseableHttpClient buildHttpClient(boolean isProxied, int port) throws Exception {
//        TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;  //checkstyle cannot handle this, so using a bit more complex code below
        TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        };
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(acceptingTrustStrategy)
                .build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("https", sslsf)
                        .register("http", new PlainConnectionSocketFactory())
                        .build();

        BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);

        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .setConnectionManager(connectionManager);

        if (isProxied) {
            HttpHost proxy = new HttpHost("127.0.0.1", port);
            httpClientBuilder.setProxy(proxy);
        }

        CloseableHttpClient httpClient = httpClientBuilder.build();
        return httpClient;
    }

}
