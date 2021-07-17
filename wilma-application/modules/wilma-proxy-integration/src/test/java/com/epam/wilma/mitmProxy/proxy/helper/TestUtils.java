package com.epam.wilma.mitmProxy.proxy.helper;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TestUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);

    /**
     * Creates and starts an embedded web server on JVM-assigned HTTP and HTTPS ports.
     * Each response has a body that contains the specified contents.
     *
     * @param enableHttps if true, an HTTPS connector will be added to the web server
     * @param content     The response the server will return
     * @return Instance of Server
     */
    public static Server startWebServerWithResponse(boolean enableHttps, final byte[] content) {
        final Server httpServer = new Server(0);
        httpServer.setHandler(new AbstractHandler() {
            public void handle(String target,
                               Request baseRequest,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
                if (request.getRequestURI().contains("stub")) {
                    LOGGER.info("STUB found in request");
                }
                long numberOfBytesRead = 0;
                try (InputStream in = new BufferedInputStream(request.getInputStream())) {
                    while (in.read() != -1) {
                        numberOfBytesRead += 1;
                    }
                }
                LOGGER.info("Done reading # of bytes from request: {}", numberOfBytesRead);
                //prepare response
                byte[] finalContent = content; //by default
                String encoding = request.getHeader("Accept-Encoding");
                BodyCompressor bodyCompressor = new BodyCompressor();
                if (encoding.contains(ContentEncoding.GZIP.getValue())) {
                    //need gzip encoding
                    finalContent = bodyCompressor.compressGzip(new ByteArrayInputStream(content)).toByteArray();
                    response.addHeader("Content-Encoding", "gzip");
                } else {
                    if (encoding.contains(ContentEncoding.DEFLATE.getValue())) {
                        //need deflate encoding
                        finalContent = bodyCompressor.compressDeflate(new ByteArrayInputStream(content)).toByteArray();
                        response.addHeader("Content-Encoding", "deflate");
                    } else {
                        if (encoding.contains(ContentEncoding.BROTLI.getValue())) {
                            //need brotli encoding
                            finalContent = bodyCompressor.compressBrotli(new ByteArrayInputStream(content)).toByteArray();
                            response.addHeader("Content-Encoding", "br");
                        }
                    }
                }

                //finish response
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);

                response.addHeader("Content-Length", Integer.toString(finalContent.length));
                response.getOutputStream().write(finalContent);
            }
        });

        if (enableHttps) {
            // Add SSL connector
            SslContextFactory sslContextFactory = new SslContextFactory.Server.Server();

            SelfSignedSslEngineSource contextSource = new SelfSignedSslEngineSource();
            SSLContext sslContext = contextSource.getSslContext();

            sslContextFactory.setSslContext(sslContext);

            sslContextFactory.setIncludeProtocols("SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3");

            ServerConnector connector = new ServerConnector(httpServer, sslContextFactory);
            connector.setPort(0);
            connector.setIdleTimeout(0);
            httpServer.addConnector(connector);
        }

        try {
            httpServer.start();
        } catch (Exception e) {
            throw new RuntimeException("Error starting Jetty web server", e);
        }

        return httpServer;
    }

    /**
     * Finds the port the specified server is listening for HTTP connections on.
     *
     * @param webServer started web server
     * @return HTTP port, or -1 if no HTTP port was found
     */
    public static int findLocalHttpPort(Server webServer) {
        for (Connector connector : webServer.getConnectors()) {
            if (!Objects.equals(connector.getDefaultConnectionFactory().getProtocol(), "SSL")) {
                return ((ServerConnector) connector).getLocalPort();
            }
        }

        return -1;
    }

    /**
     * Finds the port the specified server is listening for HTTPS connections on.
     *
     * @param webServer started web server
     * @return HTTP port, or -1 if no HTTPS port was found
     */
    public static int findLocalHttpsPort(Server webServer) {
        for (Connector connector : webServer.getConnectors()) {
            if (Objects.equals(connector.getDefaultConnectionFactory().getProtocol(), "SSL")) {
                return ((ServerConnector) connector).getLocalPort();
            }
        }

        return -1;
    }

    /**
     * Creates a DefaultHttpClient instance.
     *
     * @return instance of DefaultHttpClient
     */
    public static CloseableHttpClient buildHttpClient(boolean isProxied, int port, ContentEncoding contentEncoding) throws Exception {
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
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
                NoopHostnameVerifier.INSTANCE);

        Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("https", sslsf)
                        .register("http", new PlainConnectionSocketFactory())
                        .build();

        BasicHttpClientConnectionManager connectionManager =
                new BasicHttpClientConnectionManager(socketFactoryRegistry);

        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .setConnectionManager(connectionManager)
                .disableContentCompression();

        if (isProxied) {
            HttpHost proxy = new HttpHost("127.0.0.1", port);
            httpClientBuilder.setProxy(proxy);
        }

        //set accepted content encodings
        Header header = new BasicHeader(HttpHeaders.ACCEPT_ENCODING, contentEncoding.getValue());
        List<Header> headers = Arrays.asList(header);
        httpClientBuilder.setDefaultHeaders(headers);

        CloseableHttpClient httpClient = httpClientBuilder.build();
        return httpClient;
    }

}
