package net.lightbody.bmp.proxy.http;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLSocket;

import net.lightbody.bmp.proxy.util.Log;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.HostNameResolver;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.params.HttpParams;
import org.java_bandwidthlimiter.StreamManager;

public class TrustingSSLSocketFactory extends SSLSocketFactory {

    public enum SSLAlgorithm {
        SSLv3,
        TLSv1
    }

    private static final Log LOG = new Log();
    private static TrustStrategy trustStrategy;
    private static KeyStore keyStore;
    private final StreamManager streamManager;
    private static String keyStorePassword;

    static {
        try {
            keyStorePassword = System.getProperty("javax.net.ssl.keyStorePassword");
            String keyStorePath = System.getProperty("javax.net.ssl.keyStore");
            if (keyStorePath != null) {
                FileInputStream fis = new FileInputStream(keyStorePath);
                keyStore = KeyStore.getInstance("jks");
                keyStore.load(fis, keyStorePassword.toCharArray());
            }
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            // JKS file not found, continue with keyStore == null
            LOG.warn("JKS file not found continue without keyStore", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        trustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
                return true;
            }
        };
    }

    public TrustingSSLSocketFactory(final HostNameResolver nameResolver, final StreamManager streamManager) throws KeyManagementException,
        UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        super(SSL, keyStore, keyStorePassword, null, null, trustStrategy, ALLOW_ALL_HOSTNAME_VERIFIER);
        assert nameResolver != null;
        assert streamManager != null;
        this.streamManager = streamManager;
    }

    //just an helper function to wrap a normal sslSocket into a simulated one so we can do throttling
    private Socket createSimulatedSocket(final SSLSocket socket) {
        SimulatedSocketFactory.configure(socket);
        socket.setEnabledProtocols(new String[]{SSLAlgorithm.SSLv3.name(), SSLAlgorithm.TLSv1.name()});
        //socket.setEnabledCipherSuites(new String[] { "SSL_RSA_WITH_RC4_128_MD5" });
        return new SimulatedSSLSocket(socket, streamManager);
    }

    @SuppressWarnings("deprecation")
    @Override
    public Socket createSocket() throws java.io.IOException {
        SSLSocket sslSocket = (SSLSocket) super.createSocket();
        return createSimulatedSocket(sslSocket);
    }

    @SuppressWarnings("deprecation")
    @Override
    public Socket connectSocket(final Socket socket, final String host, final int port, final InetAddress localAddress, final int localPort,
            final HttpParams params) throws java.io.IOException, java.net.UnknownHostException, org.apache.http.conn.ConnectTimeoutException {
        SSLSocket sslSocket = (SSLSocket) super.connectSocket(socket, host, port, localAddress, localPort, params);
        if (sslSocket instanceof SimulatedSSLSocket) {
            return sslSocket;
        }
        return createSimulatedSocket(sslSocket);
    }

    @Override
    public Socket createSocket(final org.apache.http.params.HttpParams params) throws java.io.IOException {
        SSLSocket sslSocket = (SSLSocket) super.createSocket(params);
        return createSimulatedSocket(sslSocket);
    }

    @Override
    public Socket connectSocket(final Socket socket, final InetSocketAddress remoteAddress, final InetSocketAddress localAddress,
            final HttpParams params) throws IOException, ConnectTimeoutException {
        SSLSocket sslSocket = (SSLSocket) super.connectSocket(socket, remoteAddress, localAddress, params);
        if (sslSocket instanceof SimulatedSSLSocket) {
            return sslSocket;
        }
        //not sure this is needed
        return createSimulatedSocket(sslSocket);
    }
}
