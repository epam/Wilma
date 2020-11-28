package com.epam.wilma.mitmProxy.proxy.helper;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.util.Objects;

public class TestUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);
    /**
     * Creates and starts an embedded web server on JVM-assigned HTTP and HTTPS ports.
     * Each response has a body that contains the specified contents.
     *
     * @param enableHttps if true, an HTTPS connector will be added to the web server
     * @param content The response the server will return
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
                LOGGER.info("Done reading # of bytes: {}", numberOfBytesRead);
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);

                response.addHeader("Content-Length", Integer.toString(content.length));
                response.getOutputStream().write(content);
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
    public static DefaultHttpClient buildHttpClient() throws Exception {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        SSLSocketFactory sf = new SSLSocketFactory(
                new TrustSelfSignedStrategy(), new X509HostnameVerifier() {
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }

            public void verify(String host, String[] cns,
                               String[] subjectAlts) {
            }

            public void verify(String host, X509Certificate cert) {
            }

            public void verify(String host, SSLSocket ssl) {
            }
        });
        Scheme scheme = new Scheme("https", 443, sf);
        httpClient.getConnectionManager().getSchemeRegistry().register(scheme);
        return httpClient;
    }

}
