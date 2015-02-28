package net.lightbody.bmp.proxy;

import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;

public class DummyServerTest {
    private static final int DUMMY_SERVER_PORT = 4510;
    public static final String BASE_URL = "http://127.0.0.1:" + DUMMY_SERVER_PORT;

    protected DummyServer dummy = new DummyServer(DUMMY_SERVER_PORT);
    protected ProxyServer proxy = new ProxyServer(8081);
    protected DefaultHttpClient client = new DefaultHttpClient();

    @Before
    public void startServer() throws Exception {
        dummy.start();
        proxy.start(60000);

        HttpHost proxyHost = new HttpHost("127.0.0.1", 8081, "http");
        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyHost);
    }

    @After
    public void stopServer() throws Exception {
        proxy.stop();
        dummy.stop();
    }

}
