package com.epam.wilma.mitmProxy.proxy.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.X509Certificate;

/**
 * The {@link SSLContext} uses
 * self-signed certificates that are generated lazily if the given key store
 * file doesn't yet exist.
 */
public class SelfSignedSslEngineSource {
    private final Logger logger = LoggerFactory.getLogger(SelfSignedSslEngineSource.class);

    private static final String PROTOCOL = "TLSv1.2";

    private final String alias;
    private final String password;
    private final String keyStoreFile;
    private final boolean trustAllServers;
    private final boolean sendCerts;

    private SSLContext sslContext;

    public SelfSignedSslEngineSource(String keyStorePath, boolean trustAllServers, boolean sendCerts,
                                     String alias, String password) {
        this.trustAllServers = trustAllServers;
        this.sendCerts = sendCerts;
        this.keyStoreFile = keyStorePath;
        this.alias = alias;
        this.password = password;
        initializeSSLContext();
    }

    public SelfSignedSslEngineSource(String keyStorePath, boolean trustAllServers, boolean sendCerts) {
        this(keyStorePath, trustAllServers, sendCerts, "mitmproxy", "vvilma");
    }

    public SelfSignedSslEngineSource(String keyStorePath) {
        this(keyStorePath, false, true);
    }

    public SelfSignedSslEngineSource(boolean trustAllServers) {
        this(trustAllServers, true);
    }

    public SelfSignedSslEngineSource(boolean trustAllServers, boolean sendCerts) {
        this("sslSupport/mitmProxy_keystore.jks", trustAllServers, sendCerts);
    }

    public SelfSignedSslEngineSource() {
        this(false);
    }

    public SSLEngine newSslEngine() {
        return sslContext.createSSLEngine();
    }

    public SSLEngine newSslEngine(String peerHost, int peerPort) {
        return sslContext.createSSLEngine(peerHost, peerPort);
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    private void initializeKeyStore(String filename) {
        new IOException("Had to try to create JKS - forbidden in Wilma");
    }

    private void initializeSSLContext() {
        String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = "SunX509";
        }

        try {
            final KeyStore ks = loadKeyStore();

            // Set up key manager factory to use our key store
            final KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
            kmf.init(ks, password.toCharArray());

            // Set up a trust manager factory to use our key store
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
            tmf.init(ks);

            TrustManager[] trustManagers;
            if (!trustAllServers) {
                trustManagers = tmf.getTrustManagers();
            } else {
                trustManagers = new TrustManager[]{new X509TrustManager() {
                    // TrustManager that trusts all servers
                    @Override
                    public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }};
            }

            KeyManager[] keyManagers;
            if (sendCerts) {
                keyManagers = kmf.getKeyManagers();
            } else {
                keyManagers = new KeyManager[0];
            }

            // Initialize the SSLContext to work with our key managers.
            sslContext = SSLContext.getInstance(PROTOCOL);
            sslContext.init(keyManagers, trustManagers, null);
        } catch (final Exception e) {
            throw new Error(
                    "Failed to initialize the server-side SSLContext", e);
        }
    }

    private KeyStore loadKeyStore() throws IOException, GeneralSecurityException {

        InputStream fis = SelfSignedSslEngineSource.class.getResourceAsStream(keyStoreFile);
        final KeyStore keyStore = KeyStore.getInstance("jks");
        keyStore.load(fis, password.toCharArray());

        return keyStore;
    }

}
